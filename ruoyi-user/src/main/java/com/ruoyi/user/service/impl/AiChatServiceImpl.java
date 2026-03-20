package com.ruoyi.user.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import com.ruoyi.user.domain.ChatMessage;
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
    private IClubService clubService;

    @Autowired
    private IClubActivityService activityService;

    @Autowired
    private IClubFavoriteService favoriteService;

    @Autowired
    private IClubApplicationService applicationService;

    @Autowired
    private IClubCreateApplicationService clubCreateApplicationService;

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
            String reply = callLlmApi(buildContextMessages(sessionId, userId, guestId));
            ChatMessage aiMsg = new ChatMessage(sessionId, userId, guestId, "assistant", reply);
            aiMsg.setResponseTime(System.currentTimeMillis() - startTime);
            chatMessageRepository.save(aiMsg);
            return reply;
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

                JSONObject requestBody = new JSONObject();
                requestBody.put("model", model);
                requestBody.put("messages", buildContextMessages(sessionId, userId, guestId));
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
                        emitter.send(SseEmitter.event().data(resolveUpstreamErrorMessage(response.code())));
                        emitter.send(SseEmitter.event().data("[DONE]"));
                        emitter.complete();
                        return;
                    }
                    if (response.body() == null) {
                        log.error("AI stream API failed: code={}, empty body", response.code());
                        emitter.send(SseEmitter.event().data("AI 服务响应异常，请稍后重试。"));
                        emitter.send(SseEmitter.event().data("[DONE]"));
                        emitter.complete();
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
                            emitter.send(SseEmitter.event().data(content));
                        } catch (Exception ignored) {
                        }
                    }
                }

                emitter.send(SseEmitter.event().data("[DONE]"));
                emitter.complete();

                ChatMessage aiMsg = new ChatMessage(sessionId, userId, guestId, "assistant", fullReply.toString());
                aiMsg.setResponseTime(System.currentTimeMillis() - startTime);
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
    public List<ChatMessage> getHistory(String sessionId, Long userId) {
        return chatMessageRepository.findBySessionIdAndUserIdOrderByCreateTimeAsc(sessionId, userId);
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

    private List<Map<String, String>> buildContextMessages(String sessionId, Long userId, String guestId) {
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", buildSystemPrompt(userId)));

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
            messages.add(Map.of("role", msg.getRole(), "content", msg.getContent()));
        }
        return messages;
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
        sb.append("回答必须使用 Markdown 格式，直接输出可渲染的 Markdown 内容。\n");
        sb.append("涉及社团、活动、流程说明时，使用编号列表（1. 2. 3.），字段使用子列表（- 字段：值）。\n");
        sb.append("禁止使用“|”作为分隔符，统一使用中文标点和换行。\n");
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
        sb.append("7. 我的收藏：club_favorite（user_id=当前用户）\n");
        sb.append("8. 我的入社申请：club_application（user_id=当前用户 and del_flag='0'）\n");
        sb.append("9. 我的建社申请：club_create_application（applicant_user_id=当前用户 and del_flag='0'）\n");
        sb.append("10. 我报名的活动：club_activity + club_activity_registration（user_id=当前用户 and registration.status!='2'），包含报名时间区间与我的报名时间\n");
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
        sb.append("user_id=").append(userId).append("\n");

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

    private String callLlmApi(List<Map<String, String>> messages) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        requestBody.put("messages", messages);
                requestBody.put("temperature", temperature);
        requestBody.put("stream", false);
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
                return resolveUpstreamErrorMessage(response.code());
            }
            if (response.body() == null) {
                log.error("LLM API failed: code={}, empty body", response.code());
                return "AI 服务响应异常，请稍后重试。";
            }

            String body = response.body().string();
            JSONObject json = JSON.parseObject(body);
            JSONArray choices = json.getJSONArray("choices");
            if (choices != null && !choices.isEmpty()) {
                JSONObject firstChoice = choices.getJSONObject(0);
                JSONObject msgObj = firstChoice.getJSONObject("message");
                if (msgObj != null) {
                    String content = msgObj.getString("content");
                    if (StringUtils.isNotEmpty(content)) {
                        return content;
                    }
                }
            }
            return "AI 返回内容为空，请稍后重试。";
        } catch (Exception e) {
            log.error("Call LLM API failed", e);
            return "AI 服务暂时不可用，请稍后重试。";
        }
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
}
