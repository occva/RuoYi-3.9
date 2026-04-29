package com.ruoyi.user.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.user.domain.AiToolTrace;
import com.ruoyi.user.domain.Club;
import com.ruoyi.user.domain.ClubActivity;
import com.ruoyi.user.domain.ClubApplication;
import com.ruoyi.user.domain.ClubCreateApplication;
import com.ruoyi.user.domain.ClubFavorite;
import com.ruoyi.user.service.IClubActivityService;
import com.ruoyi.user.service.IClubApplicationService;
import com.ruoyi.user.service.IClubCreateApplicationService;
import com.ruoyi.user.service.IClubFavoriteService;
import com.ruoyi.user.service.IClubService;

/**
 * AI 只读工具白名单。不要在这里实现任意 URL / Controller 代理。
 */
@Component
public class AiToolRegistry {
    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 20;

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

    private final Map<String, ToolSpec> tools = new LinkedHashMap<>();

    public AiToolRegistry() {
        register("list_clubs", "查询社团列表",
                "按关键词、分类、招新状态查询可访问社团列表。keyword 只用于明确的社团名称或主题词，不要把“有哪些社团/全部社团/推荐社团”等查询意图当关键词。",
                this::listClubs,
                objectSchema(Map.of(
                        "keyword", stringProp("社团名称关键词；只有用户明确给出社团名称或主题词时使用"),
                        "categoryId", numberProp("社团分类ID；只有明确知道分类ID时使用"),
                        "recruiting", booleanProp("是否招新中；用户询问招新/可加入/纳新时为 true"),
                        "limit", numberProp("返回数量，默认10，最多20"))));
        register("get_club_detail", "查询社团详情", "根据社团ID查询可访问社团详情。", this::getClubDetail,
                objectSchema(Map.of("clubId", numberProp("社团ID"))));
        register("list_activities", "查询活动列表",
                "按状态、社团、关键词查询活动列表。status 只能使用 0未开始、1进行中、2已结束、3已取消；用户问全部/所有活动时不要传 clubId，只有明确指定某个社团时才传 clubId。",
                this::listActivities,
                objectSchema(Map.of(
                        "status", enumStringProp("活动状态：0未开始，1进行中，2已结束，3已取消", "0", "1", "2", "3"),
                        "clubId", numberProp("社团ID；仅当用户明确询问某个社团的活动时使用"),
                        "keyword", stringProp("活动标题关键词；只有用户明确给出活动名称或主题词时使用，不要把“近期/所有/结束活动”等查询意图当关键词"),
                        "limit", numberProp("返回数量，默认10，最多20"))));
        register("get_activity_detail", "查询活动详情", "根据活动ID查询活动详情。", this::getActivityDetail,
                objectSchema(Map.of("activityId", numberProp("活动ID"))));
        register("list_my_clubs", "查询我的社团", "查询当前登录用户加入和管理的社团。", this::listMyClubs,
                objectSchema(Map.of()));
        register("list_my_favorites", "查询我的收藏", "查询当前登录用户收藏的社团。", this::listMyFavorites,
                objectSchema(Map.of()));
        register("list_my_applications", "查询我的申请", "查询当前登录用户入社申请和建社申请。", this::listMyApplications,
                objectSchema(Map.of()));
        register("list_my_registered_activities", "查询我的报名活动", "查询当前登录用户已报名活动。", this::listMyRegisteredActivities,
                objectSchema(Map.of()));
    }

    public JSONArray buildToolDefinitions() {
        JSONArray result = new JSONArray();
        for (ToolSpec spec : tools.values()) {
            JSONObject function = new JSONObject();
            function.put("name", spec.name);
            function.put("description", spec.description);
            function.put("parameters", spec.parameters);

            JSONObject tool = new JSONObject();
            tool.put("type", "function");
            tool.put("function", function);
            result.add(tool);
        }
        return result;
    }

    public ToolExecution execute(String toolName, String argumentsJson, Long userId) {
        return execute(toolName, argumentsJson, userId, null);
    }

    public ToolExecution execute(String toolName, String argumentsJson, Long userId, String userMessage) {
        ToolSpec spec = tools.get(toolName);
        if (spec == null) {
            AiToolTrace trace = new AiToolTrace(toolName, toolName);
            trace.setStatus("error");
            trace.setErrorMessage("工具不存在或不允许调用");
            trace.setResultSummary("工具不存在");
            return new ToolExecution(toolError("工具不存在或不允许调用"), trace);
        }

        JSONObject args = parseArguments(argumentsJson);
        normalizeArguments(toolName, args, userMessage);
        AiToolTrace trace = new AiToolTrace(spec.name, spec.label);
        trace.setArgsSummary(summarizeArgs(toolName, args));
        long start = System.currentTimeMillis();
        try {
            JSONObject result = spec.executor.apply(args, userId);
            trace.setStatus(result.getBooleanValue("ok") ? "success" : "error");
            trace.setResultSummary(result.getString("summary"));
            trace.setErrorMessage(result.getBooleanValue("ok") ? null : result.getString("message"));
            trace.setDurationMs(System.currentTimeMillis() - start);
            return new ToolExecution(result, trace);
        } catch (Exception e) {
            trace.setStatus("error");
            trace.setErrorMessage("工具执行失败");
            trace.setResultSummary("工具执行失败");
            trace.setDurationMs(System.currentTimeMillis() - start);
            return new ToolExecution(toolError("工具执行失败"), trace);
        }
    }

    private void normalizeArguments(String toolName, JSONObject args, String userMessage) {
        if (args == null) {
            return;
        }
        if ("list_clubs".equals(toolName)) {
            normalizeClubArguments(args, userMessage);
            return;
        }
        if (!"list_activities".equals(toolName)) {
            return;
        }
        String normalizedStatus = normalizeActivityStatus(args.getString("status"), userMessage);
        if (StringUtils.isNotEmpty(normalizedStatus)) {
            args.put("status", normalizedStatus);
        } else {
            args.remove("status");
        }
        if (shouldIgnoreActivityClubId(args.getLong("clubId"), userMessage)) {
            args.remove("clubId");
        }
        if (shouldIgnoreActivityKeyword(args.getString("keyword"), userMessage)) {
            args.remove("keyword");
        }
    }

    private void normalizeClubArguments(JSONObject args, String userMessage) {
        if (shouldIgnoreClubKeyword(args.getString("keyword"), userMessage)) {
            args.remove("keyword");
        }
        if (shouldIgnoreClubCategoryId(args.getLong("categoryId"), userMessage)) {
            args.remove("categoryId");
        }
        String message = userMessage == null ? "" : userMessage.trim().toLowerCase();
        if (!args.containsKey("recruiting")
                && containsAny(message, "招新", "纳新", "可加入", "能加入", "招人", "收人", "能进", "还能进")) {
            args.put("recruiting", true);
        }
    }

    private boolean shouldIgnoreClubKeyword(String keyword, String userMessage) {
        if (StringUtils.isEmpty(keyword)) {
            return false;
        }
        String kw = keyword.trim();
        String message = userMessage == null ? "" : userMessage.trim();
        if (kw.equals(message) && isClubFilterOnlyText(message)) {
            return true;
        }
        return isClubFilterOnlyText(kw);
    }

    private boolean shouldIgnoreClubCategoryId(Long categoryId, String userMessage) {
        if (categoryId != null && categoryId <= 0) {
            return true;
        }
        String message = userMessage == null ? "" : userMessage.trim().toLowerCase();
        return containsAny(message, "全部", "所有", "全部社团", "所有社团", "有哪些社团", "推荐社团");
    }

    private boolean isClubFilterOnlyText(String text) {
        if (StringUtils.isEmpty(text)) {
            return false;
        }
        String cleaned = text.trim()
                .replace("全部", "")
                .replace("所有", "")
                .replace("社团列表", "")
                .replace("社团", "")
                .replace("协会", "")
                .replace("俱乐部", "")
                .replace("组织", "")
                .replace("团队", "")
                .replace("学校里", "")
                .replace("现在", "")
                .replace("都", "")
                .replace("把", "")
                .replace("能看的", "")
                .replace("列", "")
                .replace("推荐", "")
                .replace("热门", "")
                .replace("正在", "")
                .replace("招新中", "")
                .replace("招新", "")
                .replace("纳新", "")
                .replace("可加入", "")
                .replace("能加入", "")
                .replace("还收人", "")
                .replace("收人", "")
                .replace("还能进", "")
                .replace("能进", "")
                .replace("有哪些", "")
                .replace("有啥", "")
                .replace("有没有", "")
                .replace("有什么", "")
                .replace("什么", "")
                .replace("几个", "")
                .replace("给新生", "")
                .replace("随便", "")
                .replace("挑", "")
                .replace("靠谱", "")
                .replace("看看", "")
                .replace("我想找", "")
                .replace("帮我", "")
                .replace("一下", "")
                .replace("查询", "")
                .replace("查看", "")
                .replace("的", "")
                .replace("？", "")
                .replace("?", "")
                .trim();
        return StringUtils.isEmpty(cleaned);
    }

    private String normalizeActivityStatus(String status, String userMessage) {
        String statusFromMessage = inferActivityStatusFromMessage(userMessage);
        if (StringUtils.isNotEmpty(statusFromMessage)) {
            return statusFromMessage;
        }

        String text = StringUtils.isNotEmpty(status) ? status.trim().toLowerCase() : "";
        if ("0".equals(text) || containsAny(text, "upcoming", "pending", "not_started", "notstarted", "未开始", "待开始", "即将")) {
            return "0";
        }
        if ("1".equals(text) || containsAny(text, "ongoing", "active", "running", "进行中", "正在")) {
            return "1";
        }
        if ("2".equals(text) || containsAny(text, "ended", "finished", "completed", "complete", "success", "closed", "结束", "已结束", "完成")) {
            return "2";
        }
        if ("3".equals(text) || containsAny(text, "cancelled", "canceled", "cancel", "取消", "已取消")) {
            return "3";
        }

        return null;
    }

    private String inferActivityStatusFromMessage(String userMessage) {
        String message = userMessage == null ? "" : userMessage.trim().toLowerCase();
        if (containsAny(message, "未开始", "待开始", "即将", "可报名", "报名时间", "能报名", "报名参加")) {
            return "0";
        }
        if (containsAny(message, "进行中", "正在", "进行")) {
            return "1";
        }
        if (containsAny(message, "结束", "已结束", "完成", "历史活动", "办完")) {
            return "2";
        }
        if (containsAny(message, "取消", "已取消", "黄了", "撤掉")) {
            return "3";
        }
        return null;
    }

    private boolean shouldIgnoreActivityClubId(Long clubId, String userMessage) {
        if (clubId == null) {
            return false;
        }
        if (clubId <= 0) {
            return true;
        }
        return !mentionsKnownClubName(userMessage);
    }

    private boolean shouldIgnoreActivityKeyword(String keyword, String userMessage) {
        if (StringUtils.isEmpty(keyword)) {
            return false;
        }
        String kw = keyword.trim();
        String message = userMessage == null ? "" : userMessage.trim();
        if (kw.equals(message) && isActivityFilterOnlyText(message)) {
            return true;
        }
        String compact = kw.replace("活动", "").replace("社团", "").trim();
        if (StringUtils.isEmpty(compact)) {
            return true;
        }
        return isActivityFilterOnlyText(kw);
    }

    private boolean isActivityFilterOnlyText(String text) {
        if (StringUtils.isEmpty(text)) {
            return false;
        }
        String cleaned = text.trim()
                .replace("近期", "")
                .replace("最近", "")
                .replace("上个月", "")
                .replace("当前", "")
                .replace("现在", "")
                .replace("所有", "")
                .replace("全部", "")
                .replace("活动列表", "")
                .replace("活动", "")
                .replace("项目", "")
                .replace("已结束", "")
                .replace("结束", "")
                .replace("已完成", "")
                .replace("完成", "")
                .replace("办完", "")
                .replace("进行中", "")
                .replace("正在", "")
                .replace("还在进行", "")
                .replace("还在", "")
                .replace("进行", "")
                .replace("未开始", "")
                .replace("待开始", "")
                .replace("可报名", "")
                .replace("能报名", "")
                .replace("报名参加", "")
                .replace("报名时间", "")
                .replace("已取消", "")
                .replace("取消", "")
                .replace("黄了", "")
                .replace("撤掉", "")
                .replace("已经", "")
                .replace("或者", "")
                .replace("有哪些", "")
                .replace("有啥", "")
                .replace("有没有", "")
                .replace("有什么", "")
                .replace("哪几个", "")
                .replace("几个", "")
                .replace("有", "")
                .replace("给我看看", "")
                .replace("我想看看", "")
                .replace("接下来", "")
                .replace("参加", "")
                .replace("东西", "")
                .replace("帮我", "")
                .replace("一下", "")
                .replace("查询", "")
                .replace("查看", "")
                .replace("查", "")
                .replace("的", "")
                .replace("？", "")
                .replace("?", "")
                .trim();
        return StringUtils.isEmpty(cleaned);
    }

    private boolean mentionsKnownClubName(String userMessage) {
        if (StringUtils.isEmpty(userMessage)) {
            return false;
        }
        try {
            Club query = new Club();
            query.setStatus("0");
            query.setDelFlag("0");
            List<Club> clubs = clubService.selectClubList(query);
            if (clubs == null) {
                return false;
            }
            for (Club club : clubs) {
                if (StringUtils.isNotEmpty(club.getClubName()) && userMessage.contains(club.getClubName())) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private void register(String name, String label, String description, BiFunction<JSONObject, Long, JSONObject> executor,
            JSONObject parameters) {
        tools.put(name, new ToolSpec(name, label, description, parameters, executor));
    }

    private JSONObject listClubs(JSONObject args, Long userId) {
        Club query = new Club();
        query.setStatus("0");
        query.setDelFlag("0");
        if (StringUtils.isNotEmpty(args.getString("keyword"))) {
            query.setClubName(args.getString("keyword").trim());
        }
        if (args.getLong("categoryId") != null) {
            query.setCategoryId(args.getLong("categoryId"));
        }
        if (args.containsKey("recruiting")) {
            query.setIsRecruiting(args.getBooleanValue("recruiting") ? "1" : "0");
        }
        List<Club> clubs = limit(clubService.selectClubList(query), args);
        List<Map<String, Object>> data = clubs.stream().map(this::clubRow).collect(Collectors.toList());
        return toolOk(data, "返回 " + data.size() + " 个社团：" + joinNames(data, "clubName"));
    }

    private JSONObject getClubDetail(JSONObject args, Long userId) {
        Long clubId = args.getLong("clubId");
        if (clubId == null) {
            return toolError("clubId 不能为空");
        }
        Club club = clubService.selectClubById(clubId);
        if (club == null || !"0".equals(club.getStatus()) || "2".equals(club.getDelFlag())) {
            return toolError("社团不存在或不可访问");
        }
        return toolOk(clubRow(club), "返回社团详情：" + nvl(club.getClubName(), "未知社团"));
    }

    private JSONObject listActivities(JSONObject args, Long userId) {
        ClubActivity query = new ClubActivity();
        query.setDelFlag("0");
        if (StringUtils.isNotEmpty(args.getString("status"))) {
            query.setStatus(args.getString("status").trim());
        }
        if (args.getLong("clubId") != null) {
            query.setClubId(args.getLong("clubId"));
        }
        if (StringUtils.isNotEmpty(args.getString("keyword"))) {
            query.setActivityTitle(args.getString("keyword").trim());
        }
        List<ClubActivity> activities = limit(activityService.selectClubActivityList(query), args);
        List<Map<String, Object>> data = activities.stream().map(this::activityRow).collect(Collectors.toList());
        return toolOk(data, "返回 " + data.size() + " 个活动：" + joinNames(data, "activityTitle"));
    }

    private JSONObject getActivityDetail(JSONObject args, Long userId) {
        Long activityId = args.getLong("activityId");
        if (activityId == null) {
            return toolError("activityId 不能为空");
        }
        ClubActivity activity = activityService.selectClubActivityById(activityId);
        if (activity == null || "2".equals(activity.getDelFlag())) {
            return toolError("活动不存在或已删除");
        }
        return toolOk(activityRow(activity), "返回活动详情：" + nvl(activity.getActivityTitle(), "未知活动"));
    }

    private JSONObject listMyClubs(JSONObject args, Long userId) {
        if (userId == null) {
            return toolError("请先登录后查询我的社团");
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("joined", clubService.selectClubListByUserId(userId).stream().limit(MAX_LIMIT).map(this::clubRow)
                .collect(Collectors.toList()));
        data.put("managed", clubService.selectClubListByPresidentId(userId).stream().limit(MAX_LIMIT).map(this::clubRow)
                .collect(Collectors.toList()));
        int total = ((List<?>) data.get("joined")).size() + ((List<?>) data.get("managed")).size();
        return toolOk(data, "返回我的社团 " + total + " 条");
    }

    private JSONObject listMyFavorites(JSONObject args, Long userId) {
        if (userId == null) {
            return toolError("请先登录后查询我的收藏");
        }
        ClubFavorite query = new ClubFavorite();
        query.setUserId(userId);
        List<ClubFavorite> favorites = limit(favoriteService.selectClubFavoriteList(query), args);
        List<Map<String, Object>> data = favorites.stream().map(item -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("clubId", item.getClubId());
            row.put("clubName", item.getClubName());
            row.put("createTime", formatDate(item.getCreateTime()));
            return row;
        }).collect(Collectors.toList());
        return toolOk(data, "返回收藏社团 " + data.size() + " 条：" + joinNames(data, "clubName"));
    }

    private JSONObject listMyApplications(JSONObject args, Long userId) {
        if (userId == null) {
            return toolError("请先登录后查询我的申请");
        }
        ClubApplication appQuery = new ClubApplication();
        appQuery.setUserId(userId);
        appQuery.setDelFlag("0");
        ClubCreateApplication createQuery = new ClubCreateApplication();
        createQuery.setApplicantUserId(userId);
        createQuery.setDelFlag("0");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("joinApplications", limit(applicationService.selectClubApplicationList(appQuery), args).stream()
                .map(this::applicationRow).collect(Collectors.toList()));
        data.put("createApplications", limit(clubCreateApplicationService.selectClubCreateApplicationList(createQuery), args)
                .stream().map(this::createApplicationRow).collect(Collectors.toList()));
        int total = ((List<?>) data.get("joinApplications")).size() + ((List<?>) data.get("createApplications")).size();
        return toolOk(data, "返回申请记录 " + total + " 条");
    }

    private JSONObject listMyRegisteredActivities(JSONObject args, Long userId) {
        if (userId == null) {
            return toolError("请先登录后查询我的报名活动");
        }
        List<ClubActivity> activities = limit(activityService.selectMyRegisteredActivities(userId), args);
        List<Map<String, Object>> data = activities.stream().map(this::activityRow).collect(Collectors.toList());
        return toolOk(data, "返回已报名活动 " + data.size() + " 条：" + joinNames(data, "activityTitle"));
    }

    private Map<String, Object> clubRow(Club club) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("clubId", club.getClubId());
        row.put("clubName", club.getClubName());
        row.put("categoryName", club.getCategoryName());
        row.put("isRecruiting", "1".equals(club.getIsRecruiting()));
        row.put("description", truncate(club.getDescription(), 80));
        row.put("memberCount", club.getMemberCount());
        return row;
    }

    private Map<String, Object> activityRow(ClubActivity activity) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("activityId", activity.getActivityId());
        row.put("activityTitle", activity.getActivityTitle());
        row.put("clubId", activity.getClubId());
        row.put("clubName", activity.getClubName());
        row.put("location", activity.getLocation());
        row.put("status", mapActivityStatus(activity.getStatus()));
        row.put("startTime", formatDate(activity.getStartTime()));
        row.put("endTime", formatDate(activity.getEndTime()));
        row.put("registrationStart", formatDate(activity.getRegistrationStart()));
        row.put("registrationEnd", formatDate(activity.getRegistrationEnd()));
        row.put("maxParticipants", activity.getMaxParticipants());
        row.put("currentParticipants", activity.getCurrentParticipants());
        return row;
    }

    private Map<String, Object> applicationRow(ClubApplication application) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("applicationId", application.getApplicationId());
        row.put("clubName", application.getClubName());
        row.put("status", mapApplicationStatus(application.getStatus()));
        row.put("applicationTime", formatDate(application.getApplicationTime()));
        return row;
    }

    private Map<String, Object> createApplicationRow(ClubCreateApplication application) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("applyId", application.getApplyId());
        row.put("clubName", application.getClubName());
        row.put("categoryName", application.getCategoryName());
        row.put("status", mapApplicationStatus(application.getStatus()));
        row.put("applyTime", formatDate(application.getApplyTime()));
        return row;
    }

    private JSONObject toolOk(Object data, String summary) {
        JSONObject result = new JSONObject();
        result.put("ok", true);
        result.put("data", data);
        result.put("summary", summary);
        return result;
    }

    private JSONObject toolError(String message) {
        JSONObject result = new JSONObject();
        result.put("ok", false);
        result.put("message", message);
        result.put("summary", message);
        return result;
    }

    private JSONObject parseArguments(String argumentsJson) {
        if (StringUtils.isEmpty(argumentsJson)) {
            return new JSONObject();
        }
        try {
            return JSON.parseObject(argumentsJson);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    private <T> List<T> limit(List<T> list, JSONObject args) {
        if (list == null) {
            return new ArrayList<>();
        }
        Integer requestedLimit = args.getInteger("limit");
        int limit = requestedLimit == null ? DEFAULT_LIMIT : requestedLimit;
        limit = Math.max(1, Math.min(MAX_LIMIT, limit));
        return list.stream().limit(limit).collect(Collectors.toList());
    }

    private String summarizeArgs(String toolName, JSONObject args) {
        if (args == null || args.isEmpty()) {
            return "无参数";
        }
        String summary = args.entrySet().stream()
                .filter(entry -> !isInternalUserIdentifier(entry.getKey()))
                .map(entry -> entry.getKey() + "=" + summarizeArgValue(toolName, entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("，"));
        return StringUtils.isEmpty(summary) ? "无参数" : summary;
    }

    private boolean isInternalUserIdentifier(String key) {
        if (key == null) {
            return false;
        }
        String normalized = key.replace("_", "").toLowerCase();
        return normalized.contains("userid") || normalized.contains("reviewerid") || normalized.contains("adminuserid");
    }

    private String summarizeArgValue(String toolName, String key, Object value) {
        if ("list_activities".equals(toolName) && "status".equals(key)) {
            return mapActivityStatus(String.valueOf(value));
        }
        return String.valueOf(value);
    }

    private String joinNames(List<Map<String, Object>> rows, String key) {
        String names = rows.stream()
                .map(row -> String.valueOf(row.getOrDefault(key, "")))
                .filter(StringUtils::isNotEmpty)
                .limit(5)
                .collect(Collectors.joining("、"));
        return StringUtils.isEmpty(names) ? "无记录" : names;
    }

    private String nvl(String text, String fallback) {
        return StringUtils.isEmpty(text) ? fallback : text;
    }

    private String truncate(String text, int maxLen) {
        if (text == null || text.length() <= maxLen) {
            return text;
        }
        return text.substring(0, maxLen) + "...";
    }

    private String formatDate(Date date) {
        return date == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
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

    private static JSONObject objectSchema(Map<String, JSONObject> properties) {
        JSONObject schema = new JSONObject();
        schema.put("type", "object");
        schema.put("properties", properties);
        return schema;
    }

    private static JSONObject stringProp(String description) {
        return prop("string", description);
    }

    private static JSONObject enumStringProp(String description, String... values) {
        JSONObject prop = stringProp(description);
        JSONArray enums = new JSONArray();
        for (String value : values) {
            enums.add(value);
        }
        prop.put("enum", enums);
        return prop;
    }

    private static JSONObject numberProp(String description) {
        return prop("number", description);
    }

    private static JSONObject booleanProp(String description) {
        return prop("boolean", description);
    }

    private static JSONObject prop(String type, String description) {
        JSONObject prop = new JSONObject();
        prop.put("type", type);
        prop.put("description", description);
        return prop;
    }

    private static boolean containsAny(String text, String... keywords) {
        if (text == null) {
            return false;
        }
        for (String keyword : keywords) {
            if (text.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static class ToolExecution {
        private final JSONObject result;
        private final AiToolTrace trace;

        public ToolExecution(JSONObject result, AiToolTrace trace) {
            this.result = result;
            this.trace = trace;
        }

        public JSONObject getResult() {
            return result;
        }

        public AiToolTrace getTrace() {
            return trace;
        }
    }

    private static class ToolSpec {
        private final String name;
        private final String label;
        private final String description;
        private final JSONObject parameters;
        private final BiFunction<JSONObject, Long, JSONObject> executor;

        private ToolSpec(String name, String label, String description, JSONObject parameters,
                BiFunction<JSONObject, Long, JSONObject> executor) {
            this.name = name;
            this.label = label;
            this.description = description;
            this.parameters = parameters;
            this.executor = executor;
        }
    }
}
