package com.ruoyi.user.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.user.domain.AiToolTrace;
import com.ruoyi.user.domain.ChatMessage;
import com.ruoyi.user.domain.ChatMessageView;
import com.ruoyi.user.domain.ChatSessionSummary;
import com.ruoyi.user.domain.ClubApplication;
import com.ruoyi.user.domain.ClubCreateApplication;
import com.ruoyi.user.domain.ClubFavorite;
import com.ruoyi.user.domain.Club;
import com.ruoyi.user.domain.ClubActivity;
import com.ruoyi.user.repository.ChatMessageRepository;
import com.ruoyi.user.service.IAiChatService;
import com.ruoyi.user.service.IClubActivityService;
import com.ruoyi.user.service.IClubApplicationService;
import com.ruoyi.user.service.IClubCreateApplicationService;
import com.ruoyi.user.service.IClubFavoriteService;
import com.ruoyi.user.service.IClubService;
import com.ruoyi.user.service.impl.AiToolRegistry.ToolExecution;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * AI chat service implementation.
 */
@Service
@ConditionalOnProperty(prefix = "ai.chat", name = "enabled", havingValue = "true")
public class AiChatServiceImpl implements IAiChatService {
    private static final Logger log = LoggerFactory.getLogger(AiChatServiceImpl.class);
    private static final int CONTEXT_MESSAGE_LIMIT = 10;
    private static final int TOOL_CALL_ROUND_LIMIT = 2;
    private static final int UPSTREAM_ERROR_BODY_MAX_LEN = 600;
    private static final AtomicInteger SSE_THREAD_INDEX = new AtomicInteger(1);

    private final ExecutorService sseExecutor = new ThreadPoolExecutor(
            8,
            8,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(200),
            runnable -> {
                Thread thread = new Thread(runnable, "ai-chat-sse-" + SSE_THREAD_INDEX.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            },
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Value("${ai.chat.enabled:false}")
    private boolean aiEnabled;

    @Value("${ai.chat.api-url:}")
    private String apiUrl;

    @Value("${ai.chat.api-key:}")
    private String apiKey;

    @Value("${ai.chat.model:Pro/zai-org/GLM-4.7}")
    private String model;

    @Value("${ai.chat.max-tokens:1024}")
    private int maxTokens;

    @Value("${ai.chat.temperature:0.7}")
    private double temperature;

    @Value("${ai.chat.reasoning-enabled:false}")
    private boolean reasoningEnabled;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private AiToolRegistry aiToolRegistry;

    @Autowired
    private IClubService clubService;

    @Autowired
    private IClubActivityService activityService;

    @Autowired
    private IClubFavoriteService favoriteService;

    @Autowired
    private IClubApplicationService applicationService;

    @Autowired
    private IClubCreateApplicationService clubCreateApplicationService;

    @Autowired
    private ISysUserService sysUserService;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    @PostConstruct
    public void logAiConfigSummary() {
        String keyStatus;
        if (StringUtils.isEmpty(apiKey)) {
            keyStatus = "empty";
        } else {
            String trimmed = apiKey.trim();
            keyStatus = "len=" + trimmed.length() + ", placeholder=" + "YOUR_API_KEY".equalsIgnoreCase(trimmed);
        }
        log.info("AI config loaded: enabled={}, apiUrlSet={}, apiKey={}",
                aiEnabled, StringUtils.isNotEmpty(apiUrl), keyStatus);
    }

    @Override
    public String chat(String sessionId, Long userId, String guestId, String message) {
        chatMessageRepository.save(new ChatMessage(sessionId, userId, guestId, "user", message));

        if (!aiEnabled || !isAiApiConfigured()) {
            String reply = mockResponse(message);
            chatMessageRepository.save(new ChatMessage(sessionId, userId, guestId, "assistant", reply));
            return reply;
        }

        long startTime = System.currentTimeMillis();
        try {
            AiChatResult result = callLlmApiWithTools(buildContextMessages(sessionId, userId, guestId), userId);
            ChatMessage aiMsg = new ChatMessage(sessionId, userId, guestId, "assistant", result.getContent());
            aiMsg.setResponseTime(System.currentTimeMillis() - startTime);
            aiMsg.setTools(result.getTraces());
            chatMessageRepository.save(aiMsg);
            return result.getContent();
        } catch (Exception e) {
            log.error("AI chat failed", e);
            String fallback = "AI 服务暂时不可用，请稍后重试。";
            chatMessageRepository.save(new ChatMessage(sessionId, userId, guestId, "assistant", fallback));
            return fallback;
        }
    }

    @Override
    public SseEmitter chatStream(String sessionId, Long userId, String guestId, String message) {
        SseEmitter emitter = new SseEmitter(120_000L);
        chatMessageRepository.save(new ChatMessage(sessionId, userId, guestId, "user", message));

        sseExecutor.submit(() -> {
            try {
                if (!aiEnabled || !isAiApiConfigured()) {
                    String reply = mockResponse(message);
                    for (int i = 0; i < reply.length(); i++) {
                        emitter.send(SseEmitter.event().data(String.valueOf(reply.charAt(i))));
                        Thread.sleep(20L);
                    }
                    emitter.send(SseEmitter.event().data("[DONE]"));
                    emitter.complete();
                    chatMessageRepository.save(new ChatMessage(sessionId, userId, guestId, "assistant", reply));
                    return;
                }

                long startTime = System.currentTimeMillis();
                StringBuilder fullReply = new StringBuilder();
                AiToolPreparedMessages prepared = prepareToolAugmentedMessages(
                        buildContextMessages(sessionId, userId, guestId), userId, false);
                for (AiToolTrace trace : prepared.getTraces()) {
                    emitter.send(SseEmitter.event().data(buildToolEvent(trace)));
                }
                if (StringUtils.isNotEmpty(prepared.getFinalContent())) {
                    fullReply.append(prepared.getFinalContent());
                    emitTextChunks(emitter, prepared.getFinalContent());
                } else {
                    streamFinalReply(prepared.getMessages(), emitter, fullReply);
                }

                emitter.send(SseEmitter.event().data("[DONE]"));
                emitter.complete();

                ChatMessage aiMsg = new ChatMessage(sessionId, userId, guestId, "assistant", fullReply.toString());
                aiMsg.setResponseTime(System.currentTimeMillis() - startTime);
                aiMsg.setTools(prepared.getTraces());
                chatMessageRepository.save(aiMsg);
            } catch (Exception e) {
                log.error("SSE chat failed", e);
                try {
                    emitter.send(SseEmitter.event().data("AI 服务暂时不可用，请稍后重试。"));
                    emitter.send(SseEmitter.event().data("[DONE]"));
                    emitter.complete();
                } catch (Exception ignored) {
                }
            }
        });

        emitter.onTimeout(emitter::complete);
        emitter.onError(e -> emitter.complete());
        return emitter;
    }

    @Override
    public List<ChatMessageView> getHistory(String sessionId, Long userId, String guestId, String userName) {
        List<ChatMessage> messages;
        if (userId != null) {
            messages = chatMessageRepository.findBySessionIdAndUserIdOrderByCreateTimeAsc(sessionId, userId);
        } else if (StringUtils.isNotEmpty(guestId)) {
            messages = chatMessageRepository.findBySessionIdAndGuestIdAndUserIdIsNullOrderByCreateTimeAsc(sessionId, guestId);
        } else {
            messages = Collections.emptyList();
        }
        String displayName = StringUtils.isNotEmpty(userName) ? userName : null;
        return messages.stream()
                .map(message -> new ChatMessageView(message, displayName))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<ChatSessionSummary> getSessions(Long userId, String guestId) {
        List<ChatMessage> messages;
        if (userId != null) {
            messages = chatMessageRepository.findByUserIdOrderByCreateTimeDesc(userId);
        } else if (StringUtils.isNotEmpty(guestId)) {
            messages = chatMessageRepository.findByGuestIdAndUserIdIsNullOrderByCreateTimeDesc(guestId);
        } else {
            return Collections.emptyList();
        }

        Map<String, ChatSessionSummary> sessions = new LinkedHashMap<>();
        for (ChatMessage message : messages) {
            if (StringUtils.isEmpty(message.getSessionId())) {
                continue;
            }
            ChatSessionSummary summary = sessions.computeIfAbsent(message.getSessionId(), ChatSessionSummary::new);
            summary.setMessageCount(summary.getMessageCount() + 1);
            if (summary.getLatestTime() == null) {
                summary.setLatestTime(message.getCreateTime());
                summary.setLatestContent(truncateText(message.getContent(), 80));
            }
            if ("user".equals(message.getRole()) && StringUtils.isNotEmpty(message.getContent())) {
                summary.setTitle(truncateText(message.getContent(), 24));
            }
        }

        List<ChatSessionSummary> result = new ArrayList<>(sessions.values());
        for (ChatSessionSummary summary : result) {
            if (StringUtils.isEmpty(summary.getTitle())) {
                summary.setTitle("新对话");
            }
        }
        return result.size() > 30 ? result.subList(0, 30) : result;
    }

    @Override
    public void deleteSession(String sessionId, Long userId, String guestId) {
        if (StringUtils.isEmpty(sessionId)) {
            return;
        }
        if (userId != null) {
            chatMessageRepository.deleteBySessionIdAndUserId(sessionId, userId);
            return;
        }
        if (StringUtils.isNotEmpty(guestId)) {
            chatMessageRepository.deleteBySessionIdAndGuestIdAndUserIdIsNull(sessionId, guestId);
        }
    }

    @Override
    public String getActiveSessionId(Long userId) {
        if (userId == null) {
            return UUID.randomUUID().toString().replace("-", "");
        }
        return chatMessageRepository.findFirstByUserIdOrderByCreateTimeDesc(userId)
                .map(ChatMessage::getSessionId)
                .filter(StringUtils::isNotEmpty)
                .map(String::trim)
                .orElseGet(() -> UUID.randomUUID().toString().replace("-", ""));
    }

    @Override
    public long countGuestChatTurns(String guestId, String sessionId) {
        if (StringUtils.isNotEmpty(guestId)) {
            return chatMessageRepository.countByGuestIdAndRole(guestId, "user");
        }
        return chatMessageRepository.countBySessionIdAndUserIdIsNullAndRole(sessionId, "user");
    }

    @PreDestroy
    public void shutdownExecutor() {
        sseExecutor.shutdown();
    }

    private List<JSONObject> buildContextMessages(String sessionId, Long userId, String guestId) {
        List<JSONObject> messages = new ArrayList<>();
        messages.add(message("system", buildSystemPrompt(userId)));

        List<ChatMessage> history = queryContextHistory(sessionId, userId, guestId);
        if (history == null || history.isEmpty()) {
            return messages;
        }
        Collections.reverse(history);
        int start = Math.max(0, history.size() - CONTEXT_MESSAGE_LIMIT);
        for (int i = start; i < history.size(); i++) {
            ChatMessage msg = history.get(i);
            if ("system".equals(msg.getRole())) {
                continue;
            }
            messages.add(message(msg.getRole(), msg.getContent()));
        }
        return messages;
    }

    private JSONObject message(String role, String content) {
        JSONObject msg = new JSONObject();
        msg.put("role", role);
        msg.put("content", content == null ? "" : content);
        return msg;
    }

    private List<ChatMessage> queryContextHistory(String sessionId, Long userId, String guestId) {
        if (userId != null) {
            return chatMessageRepository.findTop20BySessionIdAndUserIdOrderByCreateTimeDesc(sessionId, userId);
        }
        if (StringUtils.isNotEmpty(guestId)) {
            return chatMessageRepository.findTop20BySessionIdAndGuestIdAndUserIdIsNullOrderByCreateTimeDesc(sessionId, guestId);
        }
        return chatMessageRepository.findTop20BySessionIdAndUserIdIsNullOrderByCreateTimeDesc(sessionId);
    }

    private String buildSystemPrompt(Long userId) {
        StringBuilder sb = new StringBuilder();
        appendCurrentUserDataPrompt(sb, userId);

        sb.append("你是社团管理助手。\n");
        sb.append("只能基于提供的实时数据库信息回答，不得虚构社团、活动、时间或地点。\n");
        sb.append("需要查询社团、活动或当前用户个人数据时，优先调用可用工具获取精确数据，再基于工具结果回答。\n");
        sb.append("不要要求用户提供内部账号标识，也不要输出内部账号标识；当前登录账号由系统自动注入工具，展示身份时只使用 username。\n");
        sb.append("回答必须使用 Markdown 格式，直接输出可渲染的 Markdown 内容。\n");
        sb.append("请输出标准 Markdown：标题、列表、表格、代码块都必须使用规范语法；块级元素前后保留空行。\n");
        sb.append("Markdown 标题必须独占一行，# 后必须有空格，例如：### 社团名称建议。\n");
        sb.append("Markdown 表格必须独立成块，表头、分隔行、数据行各占一行，例如：| 字段 | 内容 |、| --- | --- |。\n");
        sb.append("涉及社团、活动、流程说明时，可以使用编号列表、字段子列表或 Markdown 表格，按内容选择最清晰的格式。\n");
        sb.append("每个编号条目必须独立换行；条目内的社团、地点、时间、状态等字段必须另起一行，使用“- 字段：值”，不要挤在同一行。\n");
        sb.append("建议结构：先给一行结论，再给编号列表；每个条目下用子列表展示字段。\n");
        sb.append("用户询问“进行中活动”时，优先使用 [REALTIME_ONGOING_ACTIVITIES]。\n");
        sb.append("用户询问“可报名活动/报名时间区间”时，优先使用 [REALTIME_UPCOMING_ACTIVITIES] 并明确报名时间区间。\n");
        sb.append("用户询问“近期结束活动”时，优先使用 [REALTIME_RECENT_ENDED_ACTIVITIES]。\n");
        sb.append("如无匹配数据，回复：").append(getNoRecordMessage()).append("\n\n");

        try {
            Club clubQuery = new Club();
            clubQuery.setStatus("0");
            clubQuery.setDelFlag("0");
            List<Club> clubs = clubService.selectClubList(clubQuery);
            sb.append("[REALTIME_CLUBS]\n");
            if (clubs == null || clubs.isEmpty()) {
                sb.append("无记录\n");
            } else {
                int limit = Math.min(20, clubs.size());
                for (int i = 0; i < limit; i++) {
                    Club club = clubs.get(i);
                    sb.append(i + 1)
                            .append(". ")
                            .append(nvl(club.getClubName(), "未知"))
                            .append("，分类：").append(nvl(club.getCategoryName(), "未知"))
                            .append("，招新中：").append("1".equals(club.getIsRecruiting()) ? "是" : "否")
                            .append("，简介：").append(truncateText(nvl(club.getDescription(), "无"), 60))
                            .append("\n");
                }
            }
        } catch (Exception e) {
            log.warn("Load club list for AI prompt failed", e);
            sb.append("[REALTIME_CLUBS]\n加载失败\n");
        }

        try {
            ClubActivity ongoingQuery = new ClubActivity();
            ongoingQuery.setStatus("1");
            ongoingQuery.setDelFlag("0");
            List<ClubActivity> ongoingActivities = activityService.selectClubActivityList(ongoingQuery);
            sb.append("\n[REALTIME_ONGOING_ACTIVITIES]\n");
            if (ongoingActivities == null || ongoingActivities.isEmpty()) {
                sb.append("无记录\n");
            } else {
                int limit = Math.min(10, ongoingActivities.size());
                for (int i = 0; i < limit; i++) {
                    ClubActivity act = ongoingActivities.get(i);
                    sb.append(i + 1)
                            .append(". ")
                            .append(nvl(act.getActivityTitle(), "未知"))
                            .append("，社团：").append(nvl(act.getClubName(), "未知"))
                            .append("，地点：").append(nvl(act.getLocation(), "未知"))
                            .append("，开始时间：").append(formatDateTime(act.getStartTime()))
                            .append("，报名时间区间：")
                            .append(formatRegistrationWindow(act.getRegistrationStart(), act.getRegistrationEnd()))
                            .append("，状态：").append(mapActivityStatus(act.getStatus()))
                            .append("\n");
                }
            }
        } catch (Exception e) {
            log.warn("Load ongoing activities for AI prompt failed", e);
            sb.append("\n[REALTIME_ONGOING_ACTIVITIES]\n加载失败\n");
        }

        try {
            ClubActivity upcomingQuery = new ClubActivity();
            upcomingQuery.setStatus("0");
            upcomingQuery.setDelFlag("0");
            List<ClubActivity> upcomingActivities = activityService.selectClubActivityList(upcomingQuery);
            sb.append("\n[REALTIME_UPCOMING_ACTIVITIES]\n");
            if (upcomingActivities == null || upcomingActivities.isEmpty()) {
                sb.append("无记录\n");
            } else {
                int limit = Math.min(10, upcomingActivities.size());
                for (int i = 0; i < limit; i++) {
                    ClubActivity act = upcomingActivities.get(i);
                    sb.append(i + 1)
                            .append(". ")
                            .append(nvl(act.getActivityTitle(), "未知"))
                            .append("，社团：").append(nvl(act.getClubName(), "未知"))
                            .append("，地点：").append(nvl(act.getLocation(), "未知"))
                            .append("，开始时间：").append(formatDateTime(act.getStartTime()))
                            .append("，报名时间区间：")
                            .append(formatRegistrationWindow(act.getRegistrationStart(), act.getRegistrationEnd()))
                            .append("，状态：").append(mapActivityStatus(act.getStatus()))
                            .append("\n");
                }
            }
        } catch (Exception e) {
            log.warn("Load upcoming activities for AI prompt failed", e);
            sb.append("\n[REALTIME_UPCOMING_ACTIVITIES]\n加载失败\n");
        }

        try {
            ClubActivity endedQuery = new ClubActivity();
            endedQuery.setStatus("2");
            endedQuery.setDelFlag("0");
            List<ClubActivity> endedActivities = activityService.selectClubActivityList(endedQuery);
            sb.append("\n[REALTIME_RECENT_ENDED_ACTIVITIES]\n");
            if (endedActivities == null || endedActivities.isEmpty()) {
                sb.append("无记录\n");
            } else {
                int limit = Math.min(10, endedActivities.size());
                for (int i = 0; i < limit; i++) {
                    ClubActivity act = endedActivities.get(i);
                    sb.append(i + 1)
                            .append(". ")
                            .append(nvl(act.getActivityTitle(), "未知"))
                            .append("，社团：").append(nvl(act.getClubName(), "未知"))
                            .append("，地点：").append(nvl(act.getLocation(), "未知"))
                            .append("，报名时间区间：")
                            .append(formatRegistrationWindow(act.getRegistrationStart(), act.getRegistrationEnd()))
                            .append("，结束时间：").append(formatDateTime(act.getEndTime()))
                            .append("，状态：").append(mapActivityStatus(act.getStatus()))
                            .append("\n");
                }
            }
        } catch (Exception e) {
            log.warn("Load ended activities for AI prompt failed", e);
            sb.append("\n[REALTIME_RECENT_ENDED_ACTIVITIES]\n加载失败\n");
        }

        sb.append("\n[DATA_SOURCE]\n");
        sb.append("1. 社团信息：club（status='0' and del_flag='0'）\n");
        sb.append("2. 进行中活动：club_activity（status='1' and del_flag='0'，按 start_time 倒序）\n");
        sb.append("3. 未开始活动：club_activity（status='0' and del_flag='0'，按 start_time 倒序）\n");
        sb.append("4. 近期结束活动：club_activity（status='2' and del_flag='0'，按 start_time 倒序）\n");
        sb.append("5. 我加入的社团：按当前用户 member 关系查询 club\n");
        sb.append("6. 我管理的社团：club（president_id=当前用户 and del_flag='0'）\n");
        sb.append("7. 我的收藏：当前登录账号收藏的社团\n");
        sb.append("8. 我的入社申请：当前登录账号提交的入社申请\n");
        sb.append("9. 我的建社申请：当前登录账号提交的建社申请\n");
        sb.append("10. 我报名的活动：当前登录账号报名且未取消的活动，包含报名时间区间与我的报名时间\n");
        sb.append("11. 活动报名时间区间字段：registration_start 至 registration_end\n");
        sb.append("12. 查询时间：").append(formatDateTime(new Date())).append("\n");
        return sb.toString();
    }

    private void appendCurrentUserDataPrompt(StringBuilder sb, Long userId) {
        sb.append("[CURRENT_USER_RULE]\n");
        sb.append("当用户询问“我加入/管理/收藏的社团、我的入社申请、我的建社申请、我报名的活动”时，");
        sb.append("必须优先从 CURRENT_USER_DATA 回答。");
        sb.append("如果对应类别在 CURRENT_USER_DATA 中没有记录，必须精确回复：'")
                .append(getNoRecordMessage())
                .append("'.\n");

        if (userId == null) {
            sb.append("[CURRENT_USER_DATA]\n");
            sb.append("未登录\n\n");
            return;
        }

        sb.append("[CURRENT_USER_DATA]\n");
        sb.append("username=").append(resolveUserName(userId)).append("\n");

        try {
            List<Club> myClubs = clubService.selectClubListByUserId(userId);
            sb.append("joined_clubs:\n");
            if (myClubs == null || myClubs.isEmpty()) {
                sb.append("无记录\n");
            } else {
                int limit = Math.min(20, myClubs.size());
                for (int i = 0; i < limit; i++) {
                    Club club = myClubs.get(i);
                    sb.append(i + 1)
                            .append(". ")
                            .append(nvl(club.getClubName(), "未知"))
                            .append("，分类：").append(nvl(club.getCategoryName(), "未知"))
                            .append("，招新中：").append("1".equals(club.getIsRecruiting()) ? "是" : "否")
                            .append("\n");
                }
            }
        } catch (Exception e) {
            log.warn("Load current user clubs for AI prompt failed, userId={}", userId, e);
            sb.append("joined_clubs:\n加载失败\n");
        }

        try {
            List<Club> managedClubs = clubService.selectClubListByPresidentId(userId);
            sb.append("managed_clubs:\n");
            if (managedClubs == null || managedClubs.isEmpty()) {
                sb.append("无记录\n");
            } else {
                int limit = Math.min(20, managedClubs.size());
                for (int i = 0; i < limit; i++) {
                    Club club = managedClubs.get(i);
                    sb.append(i + 1)
                            .append(". ")
                            .append(nvl(club.getClubName(), "未知"))
                            .append("，分类：").append(nvl(club.getCategoryName(), "未知"))
                            .append("，招新中：").append("1".equals(club.getIsRecruiting()) ? "是" : "否")
                            .append("\n");
                }
            }
        } catch (Exception e) {
            log.warn("Load current user managed clubs for AI prompt failed, userId={}", userId, e);
            sb.append("managed_clubs:\n加载失败\n");
        }

        try {
            ClubFavorite favoriteQuery = new ClubFavorite();
            favoriteQuery.setUserId(userId);
            List<ClubFavorite> favorites = favoriteService.selectClubFavoriteList(favoriteQuery);
            sb.append("favorite_clubs:\n");
            if (favorites == null || favorites.isEmpty()) {
                sb.append("无记录\n");
            } else {
                int limit = Math.min(20, favorites.size());
                for (int i = 0; i < limit; i++) {
                    ClubFavorite favorite = favorites.get(i);
                    sb.append(i + 1)
                            .append(". ")
                            .append(nvl(favorite.getClubName(), "未知"))
                            .append("，社团ID：").append(favorite.getClubId() == null ? "未知" : favorite.getClubId())
                            .append("，收藏时间：").append(formatDateTime(favorite.getCreateTime()))
                            .append("\n");
                }
            }
        } catch (Exception e) {
            log.warn("Load current user favorites for AI prompt failed, userId={}", userId, e);
            sb.append("favorite_clubs:\n加载失败\n");
        }

        try {
            ClubApplication applicationQuery = new ClubApplication();
            applicationQuery.setUserId(userId);
            applicationQuery.setDelFlag("0");
            List<ClubApplication> applications = applicationService.selectClubApplicationList(applicationQuery);
            sb.append("join_applications:\n");
            if (applications == null || applications.isEmpty()) {
                sb.append("无记录\n");
            } else {
                int limit = Math.min(20, applications.size());
                for (int i = 0; i < limit; i++) {
                    ClubApplication application = applications.get(i);
                    sb.append(i + 1)
                            .append(". ")
                            .append(nvl(application.getClubName(), "未知"))
                            .append("，申请时间：").append(formatDateTime(application.getApplicationTime()))
                            .append("，状态：").append(mapApplicationStatus(application.getStatus()))
                            .append("\n");
                }
            }
        } catch (Exception e) {
            log.warn("Load current user join applications for AI prompt failed, userId={}", userId, e);
            sb.append("join_applications:\n加载失败\n");
        }

        try {
            ClubCreateApplication createApplicationQuery = new ClubCreateApplication();
            createApplicationQuery.setApplicantUserId(userId);
            createApplicationQuery.setDelFlag("0");
            List<ClubCreateApplication> createApplications = clubCreateApplicationService
                    .selectClubCreateApplicationList(createApplicationQuery);
            sb.append("create_club_applications:\n");
            if (createApplications == null || createApplications.isEmpty()) {
                sb.append("无记录\n");
            } else {
                int limit = Math.min(20, createApplications.size());
                for (int i = 0; i < limit; i++) {
                    ClubCreateApplication createApplication = createApplications.get(i);
                    sb.append(i + 1)
                            .append(". ")
                            .append(nvl(createApplication.getClubName(), "未知"))
                            .append("，分类：").append(nvl(createApplication.getCategoryName(), "未知"))
                            .append("，申请时间：").append(formatDateTime(createApplication.getApplyTime()))
                            .append("，状态：").append(mapCreateApplicationStatus(createApplication.getStatus()))
                            .append("\n");
                }
            }
        } catch (Exception e) {
            log.warn("Load current user create-club applications for AI prompt failed, userId={}", userId, e);
            sb.append("create_club_applications:\n加载失败\n");
        }

        try {
            List<ClubActivity> myActivities = activityService.selectMyRegisteredActivities(userId);
            sb.append("registered_activities:\n");
            if (myActivities == null || myActivities.isEmpty()) {
                sb.append("无记录\n");
            } else {
                int limit = Math.min(10, myActivities.size());
                for (int i = 0; i < limit; i++) {
                    ClubActivity act = myActivities.get(i);
                    sb.append(i + 1)
                            .append(". ")
                            .append(nvl(act.getActivityTitle(), "未知"))
                            .append("，社团：").append(nvl(act.getClubName(), "未知"))
                            .append("，开始时间：").append(formatDateTime(act.getStartTime()))
                            .append("，报名时间区间：")
                            .append(formatRegistrationWindow(act.getRegistrationStart(), act.getRegistrationEnd()))
                            .append("，我的报名时间：").append(formatDateTime(act.getRegistrationTime()))
                            .append("，状态：").append(mapActivityStatus(act.getStatus()))
                            .append("\n");
                }
            }
        } catch (Exception e) {
            log.warn("Load current user activities for AI prompt failed, userId={}", userId, e);
            sb.append("registered_activities:\n加载失败\n");
        }
        sb.append("\n");
    }

    private String getNoRecordMessage() {
        return "当前数据源暂无记录。";
    }

    private String resolveUserName(Long userId) {
        if (userId == null) {
            return "未登录";
        }
        try {
            SysUser user = sysUserService.selectUserById(userId);
            if (user != null && StringUtils.isNotEmpty(user.getUserName())) {
                return user.getUserName();
            }
        } catch (Exception e) {
            log.warn("Resolve username for AI prompt failed, userId={}", userId, e);
        }
        return "已登录用户";
    }

    private String nvl(String value, String defaultValue) {
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    private String truncateText(String text, int maxLen) {
        if (text == null || text.length() <= maxLen) {
            return text == null ? "" : text;
        }
        return text.substring(0, maxLen) + "...";
    }

    private String formatDateTime(Date date) {
        if (date == null) {
            return "无";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    private String formatRegistrationWindow(Date start, Date end) {
        String startText = formatDateTime(start);
        String endText = formatDateTime(end);
        if ("无".equals(startText) && "无".equals(endText)) {
            return "无";
        }
        return startText + " 至 " + endText;
    }

    private String mapActivityStatus(String status) {
        if ("0".equals(status)) {
            return "未开始";
        }
        if ("1".equals(status)) {
            return "进行中";
        }
        if ("2".equals(status)) {
            return "已结束";
        }
        if ("3".equals(status)) {
            return "已取消";
        }
        return "未知";
    }

    private String mapApplicationStatus(String status) {
        if ("0".equals(status)) {
            return "待审核";
        }
        if ("1".equals(status)) {
            return "已通过";
        }
        if ("2".equals(status)) {
            return "已拒绝";
        }
        if ("3".equals(status)) {
            return "已撤回";
        }
        return "未知";
    }

    private String mapCreateApplicationStatus(String status) {
        return mapApplicationStatus(status);
    }

    private AiChatResult callLlmApiWithTools(List<JSONObject> messages, Long userId) {
        AiToolPreparedMessages prepared = prepareToolAugmentedMessages(messages, userId, true);
        if (StringUtils.isNotEmpty(prepared.getFinalContent())) {
            return new AiChatResult(prepared.getFinalContent(), prepared.getTraces());
        }
        String reply = callLlmApi(prepared.getMessages(), false, false);
        return new AiChatResult(reply, prepared.getTraces());
    }

    private AiToolPreparedMessages prepareToolAugmentedMessages(List<JSONObject> baseMessages, Long userId,
            boolean resolveFinalContent) {
        List<JSONObject> messages = new ArrayList<>(baseMessages);
        List<AiToolTrace> traces = new ArrayList<>();
        String preferredToolName = inferPreferredToolName(baseMessages);
        String lastUserContent = getLastUserContent(baseMessages);

        for (int round = 0; round < TOOL_CALL_ROUND_LIMIT; round++) {
            JSONObject response = callLlmRaw(messages, false, true, preferredToolName);
            preferredToolName = null;
            if (response == null) {
                break;
            }
            JSONObject assistantMessage = extractAssistantMessage(response);
            if (assistantMessage == null) {
                break;
            }
            assistantMessage.put("role", "assistant");
            JSONArray toolCalls = assistantMessage.getJSONArray("tool_calls");
            if (toolCalls == null || toolCalls.isEmpty()) {
                String content = assistantMessage.getString("content");
                if (resolveFinalContent && StringUtils.isNotEmpty(content)) {
                    return new AiToolPreparedMessages(messages, traces, content);
                }
                break;
            }

            messages.add(assistantMessage);
            for (int i = 0; i < toolCalls.size(); i++) {
                JSONObject toolCall = toolCalls.getJSONObject(i);
                JSONObject function = toolCall.getJSONObject("function");
                String toolName = function == null ? null : function.getString("name");
                String arguments = function == null ? null : function.getString("arguments");
                ToolExecution execution = aiToolRegistry.execute(toolName, arguments, userId, lastUserContent);
                traces.add(execution.getTrace());

                JSONObject toolMessage = new JSONObject();
                toolMessage.put("role", "tool");
                toolMessage.put("tool_call_id", toolCall.getString("id"));
                toolMessage.put("name", toolName);
                toolMessage.put("content", execution.getResult().toJSONString());
                messages.add(toolMessage);
            }
        }

        return new AiToolPreparedMessages(messages, traces, null);
    }

    private String callLlmApi(List<JSONObject> messages, boolean stream, boolean includeTools) {
        JSONObject response = callLlmRaw(messages, stream, includeTools);
        if (response == null) {
            return "AI 服务暂时不可用，请稍后重试。";
        }
        JSONObject assistantMessage = extractAssistantMessage(response);
        if (assistantMessage != null) {
            String content = assistantMessage.getString("content");
            if (StringUtils.isNotEmpty(content)) {
                return content;
            }
        }
        return "AI 返回内容为空，请稍后重试。";
    }

    private JSONObject callLlmRaw(List<JSONObject> messages, boolean stream, boolean includeTools) {
        return callLlmRaw(messages, stream, includeTools, null);
    }

    private JSONObject callLlmRaw(List<JSONObject> messages, boolean stream, boolean includeTools, String preferredToolName) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        requestBody.put("messages", messages);
        requestBody.put("temperature", temperature);
        requestBody.put("stream", stream);
        if (includeTools) {
            requestBody.put("tools", aiToolRegistry.buildToolDefinitions());
            if (StringUtils.isNotEmpty(preferredToolName)) {
                JSONObject function = new JSONObject();
                function.put("name", preferredToolName);
                JSONObject toolChoice = new JSONObject();
                toolChoice.put("type", "function");
                toolChoice.put("function", function);
                requestBody.put("tool_choice", toolChoice);
            } else {
                requestBody.put("tool_choice", "auto");
            }
            requestBody.put("parallel_tool_calls", false);
        }
        appendReasoningOption(requestBody);

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toJSONString(), MediaType.parse("application/json")))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                log.error("LLM API failed: code={}, body={}", response.code(), truncate(errorBody));
                return syntheticAssistantResponse(resolveUpstreamErrorMessage(response.code()));
            }
            if (response.body() == null) {
                log.error("LLM API failed: code={}, empty body", response.code());
                return syntheticAssistantResponse("AI 服务响应异常，请稍后重试。");
            }

            String body = response.body().string();
            return JSON.parseObject(body);
        } catch (Exception e) {
            log.error("Call LLM API failed", e);
            return null;
        }
    }

    private String inferPreferredToolName(List<JSONObject> messages) {
        String content = getLastUserContent(messages);
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        String normalized = content.toLowerCase();
        if (containsAny(normalized, "我报名", "我的报名", "报名了哪些活动", "已报名活动")) {
            return "list_my_registered_activities";
        }
        if (containsAny(normalized, "我的入社申请", "入社申请状态", "加入申请状态")) {
            return "list_my_applications";
        }
        if (containsAny(normalized, "我的收藏", "收藏的社团")) {
            return "list_my_favorites";
        }
        if (containsAny(normalized, "我的社团", "我加入的社团", "我管理的社团")) {
            return "list_my_clubs";
        }
        if (containsAny(normalized, "活动", "讲座", "工作坊", "报名时间", "近期有什么")) {
            return "list_activities";
        }
        if (containsAny(normalized, "社团", "协会", "俱乐部", "推荐")) {
            return "list_clubs";
        }
        return null;
    }

    private String getLastUserContent(List<JSONObject> messages) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            JSONObject message = messages.get(i);
            if ("user".equals(message.getString("role"))) {
                return message.getString("content");
            }
        }
        return null;
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void streamFinalReply(List<JSONObject> messages, SseEmitter emitter, StringBuilder fullReply) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        requestBody.put("messages", messages);
        requestBody.put("temperature", temperature);
        requestBody.put("stream", true);
        appendReasoningOption(requestBody);

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toJSONString(), MediaType.parse("application/json")))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                log.error("AI stream API failed: code={}, body={}", response.code(), truncate(errorBody));
                String errorMessage = resolveUpstreamErrorMessage(response.code());
                fullReply.append(errorMessage);
                emitter.send(SseEmitter.event().data(errorMessage));
                return;
            }
            if (response.body() == null) {
                log.error("AI stream API failed: code={}, empty body", response.code());
                String errorMessage = "AI 服务响应异常，请稍后重试。";
                fullReply.append(errorMessage);
                emitter.send(SseEmitter.event().data(errorMessage));
                return;
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("data: ")) {
                    continue;
                }
                String data = line.substring(6).trim();
                if ("[DONE]".equals(data)) {
                    break;
                }
                try {
                    JSONObject chunkObj = JSON.parseObject(data);
                    JSONArray choices = chunkObj.getJSONArray("choices");
                    if (choices == null || choices.isEmpty()) {
                        continue;
                    }
                    JSONObject firstChoice = choices.getJSONObject(0);
                    JSONObject delta = firstChoice.getJSONObject("delta");
                    if (delta == null || !delta.containsKey("content")) {
                        continue;
                    }
                    String content = delta.getString("content");
                    if (content == null) {
                        continue;
                    }
                    fullReply.append(content);
                    emitter.send(SseEmitter.event().data(buildChunkEvent(content)));
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void emitTextChunks(SseEmitter emitter, String text) throws Exception {
        if (text == null) {
            return;
        }
        int offset = 0;
        int chunkSize = 12;
        while (offset < text.length()) {
            int end = Math.min(text.length(), offset + chunkSize);
            emitter.send(SseEmitter.event().data(buildChunkEvent(text.substring(offset, end))));
            offset = end;
        }
    }

    private JSONObject extractAssistantMessage(JSONObject response) {
        JSONArray choices = response.getJSONArray("choices");
        if (choices == null || choices.isEmpty()) {
            return null;
        }
        JSONObject firstChoice = choices.getJSONObject(0);
        return firstChoice == null ? null : firstChoice.getJSONObject("message");
    }

    private String buildToolEvent(AiToolTrace trace) {
        JSONObject event = new JSONObject();
        event.put("type", "tool");
        event.put("tool", trace);
        return event.toJSONString();
    }

    private String buildChunkEvent(String content) {
        JSONObject event = new JSONObject();
        event.put("type", "chunk");
        event.put("content", content == null ? "" : content);
        return event.toJSONString();
    }

    private JSONObject syntheticAssistantResponse(String content) {
        JSONObject message = new JSONObject();
        message.put("role", "assistant");
        message.put("content", content);

        JSONObject choice = new JSONObject();
        choice.put("message", message);

        JSONArray choices = new JSONArray();
        choices.add(choice);

        JSONObject response = new JSONObject();
        response.put("choices", choices);
        return response;
    }

    private boolean isAiApiConfigured() {
        if (StringUtils.isEmpty(apiUrl) || StringUtils.isEmpty(apiKey)) {
            log.warn("AI chat enabled but api-url/api-key is empty, fallback to mock.");
            return false;
        }
        if ("YOUR_API_KEY".equalsIgnoreCase(apiKey.trim())) {
            log.warn("AI chat api-key is placeholder, fallback to mock.");
            return false;
        }
        return true;
    }

    private void appendReasoningOption(JSONObject requestBody) {
        if (!reasoningEnabled) {
            return;
        }
        JSONObject reasoning = new JSONObject();
        reasoning.put("enabled", true);
        requestBody.put("reasoning", reasoning);
    }

    private String resolveUpstreamErrorMessage(int statusCode) {
        if (statusCode == 401 || statusCode == 403) {
            return "AI 鉴权失败，请检查 API Key 配置。";
        }
        if (statusCode == 429) {
            return "AI 请求过于频繁，请稍后重试。";
        }
        if (statusCode >= 500) {
            return "AI 服务暂时不可用，请稍后重试。";
        }
        return "AI 服务响应失败，请稍后重试。";
    }

    private String truncate(String text) {
        if (text == null) {
            return "";
        }
        if (text.length() <= UPSTREAM_ERROR_BODY_MAX_LEN) {
            return text;
        }
        return text.substring(0, UPSTREAM_ERROR_BODY_MAX_LEN) + "...";
    }

    private String mockResponse(String query) {
        String lower = query == null ? "" : query.toLowerCase();
        if (lower.contains("加入") || lower.contains("报名")) {
            return "加入社团流程：浏览社团详情，提交加入申请，等待审核结果。";
        }
        if (lower.contains("创建") || lower.contains("成立")) {
            return "创建社团流程：提交社团申请资料，等待管理员审核。";
        }
        if (lower.contains("活动")) {
            return "你可以在活动页面查看近期活动并完成报名。";
        }
        return "你好，我是社团助手。你可以问我社团信息、活动安排、加入与创建流程。";
    }

    private static class AiChatResult {
        private final String content;
        private final List<AiToolTrace> traces;

        private AiChatResult(String content, List<AiToolTrace> traces) {
            this.content = content;
            this.traces = traces;
        }

        private String getContent() {
            return content;
        }

        @SuppressWarnings("unused")
        private List<AiToolTrace> getTraces() {
            return traces;
        }
    }

    private static class AiToolPreparedMessages {
        private final List<JSONObject> messages;
        private final List<AiToolTrace> traces;
        private final String finalContent;

        private AiToolPreparedMessages(List<JSONObject> messages, List<AiToolTrace> traces, String finalContent) {
            this.messages = messages;
            this.traces = traces;
            this.finalContent = finalContent;
        }

        private List<JSONObject> getMessages() {
            return messages;
        }

        private List<AiToolTrace> getTraces() {
            return traces;
        }

        private String getFinalContent() {
            return finalContent;
        }
    }
}
