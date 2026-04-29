package com.ruoyi.web.controller.app;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.user.domain.ChatMessageView;
import com.ruoyi.user.domain.ChatSessionSummary;
import com.ruoyi.user.service.IAiChatService;

/**
 * 用户端 AI 聊天接口
 */
@Anonymous
@RestController
@RequestMapping("/api/app/ai")
@ConditionalOnProperty(prefix = "ai.chat", name = "enabled", havingValue = "true")
public class AppAiChatController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AppAiChatController.class);
    private static final int GUEST_CHAT_LIMIT = 5;
    private static final String GUEST_LIMIT_MESSAGE = "未登录用户最多可对话5次，请登录后继续";
    private static final String GUEST_ID_HEADER = "X-AI-Guest-Id";
    private static final Pattern GUEST_ID_PATTERN = Pattern.compile("^[A-Za-z0-9_\\-:.]{8,128}$");

    @Autowired
    private IAiChatService aiChatService;

    /**
     * 普通对话（一次性返回）
     */
    @PostMapping("/chat")
    public AjaxResult chat(
            @RequestBody ChatRequest request,
            HttpServletRequest servletRequest) {
        if (request == null || request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return error("消息内容不能为空");
        }

        String sessionId = resolveSessionId(request.getSessionId());
        Long userId = resolveCurrentUserIdSafely();
        String normalizedGuestId = resolveGuestId(servletRequest, sessionId);

        if (isGuestOverLimit(userId, normalizedGuestId, sessionId)) {
            AjaxResult limited = success();
            limited.put("reply", GUEST_LIMIT_MESSAGE);
            limited.put("sessionId", sessionId);
            limited.put("limited", true);
            limited.put("remaining", 0);
            return limited;
        }

        String safeMessage = HtmlUtils.htmlEscape(request.getMessage().trim());
        String reply = aiChatService.chat(sessionId, userId, normalizedGuestId, safeMessage);

        AjaxResult result = success();
        result.put("reply", reply);
        result.put("sessionId", sessionId);
        if (userId == null) {
            long used = aiChatService.countGuestChatTurns(normalizedGuestId, sessionId);
            result.put("remaining", Math.max(0, GUEST_CHAT_LIMIT - used));
        }
        return result;
    }

    /**
     * 流式对话（SSE）
     */
    @PostMapping(value = "/chat/stream", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> chatStream(
            @RequestBody ChatRequest request,
            HttpServletRequest servletRequest) {
        if (request == null || request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return sseError(HttpStatus.BAD_REQUEST, "消息内容不能为空", null);
        }

        String sessionId = resolveSessionId(request.getSessionId());
        Long userId = resolveCurrentUserIdSafely();
        String normalizedGuestId = resolveGuestId(servletRequest, sessionId);
        if (isGuestOverLimit(userId, normalizedGuestId, sessionId)) {
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .header("X-AI-Session-Id", sessionId)
                    .header("X-AI-Limited", "true")
                    .body(buildTerminalEmitter(GUEST_LIMIT_MESSAGE));
        }

        String safeMessage = HtmlUtils.htmlEscape(request.getMessage().trim());
        SseEmitter emitter = aiChatService.chatStream(sessionId, userId, normalizedGuestId, safeMessage);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .header("X-AI-Session-Id", sessionId)
                .body(emitter);
    }

    private ResponseEntity<SseEmitter> sseError(HttpStatus status, String message, String sessionId) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(status)
                .contentType(MediaType.TEXT_EVENT_STREAM);
        if (sessionId != null && !sessionId.isBlank()) {
            builder.header("X-AI-Session-Id", sessionId);
        }
        return builder.body(buildTerminalEmitter(message));
    }

    private SseEmitter buildTerminalEmitter(String message) {
        SseEmitter emitter = new SseEmitter(10_000L);
        CompletableFuture.runAsync(() -> {
            try {
                emitter.send(SseEmitter.event().data(message));
                emitter.send(SseEmitter.event().data("[DONE]"));
            } catch (Exception ignored) {
            } finally {
                emitter.complete();
            }
        });
        return emitter;
    }

    /**
     * 获取会话历史
     */
    @GetMapping("/chat/history")
    public AjaxResult history(@RequestParam String sessionId, HttpServletRequest servletRequest) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return error("会话ID不能为空");
        }

        Long userId = resolveCurrentUserIdSafely();
        String normalizedSessionId = sessionId.trim();
        String normalizedGuestId = userId == null ? resolveGuestId(servletRequest, normalizedSessionId) : null;

        String userName = userId == null ? null : getUsername();
        List<ChatMessageView> messages = aiChatService.getHistory(normalizedSessionId, userId, normalizedGuestId, userName);
        return success(messages);
    }

    /**
     * 获取会话列表
     */
    @GetMapping("/chat/sessions")
    public AjaxResult sessions(HttpServletRequest servletRequest) {
        Long userId = resolveCurrentUserIdSafely();
        String normalizedGuestId = userId == null ? resolveGuestId(servletRequest, null) : null;
        List<ChatSessionSummary> sessions = aiChatService.getSessions(userId, normalizedGuestId);
        return success(sessions);
    }

    /**
     * 删除会话
     */
    @DeleteMapping("/chat/session")
    public AjaxResult deleteSession(@RequestParam String sessionId, HttpServletRequest servletRequest) {
        return doDeleteSession(sessionId, servletRequest);
    }

    /**
     * 删除会话（兼容不方便发送 DELETE 的客户端）
     */
    @PostMapping("/chat/session/delete")
    public AjaxResult deleteSessionByPost(@RequestParam String sessionId, HttpServletRequest servletRequest) {
        return doDeleteSession(sessionId, servletRequest);
    }

    private AjaxResult doDeleteSession(String sessionId, HttpServletRequest servletRequest) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return error("会话ID不能为空");
        }
        Long userId = resolveCurrentUserIdSafely();
        String normalizedSessionId = sessionId.trim();
        String normalizedGuestId = userId == null ? resolveGuestId(servletRequest, normalizedSessionId) : null;
        aiChatService.deleteSession(normalizedSessionId, userId, normalizedGuestId);
        return success();
    }

    /**
     * 获取当前登录用户活跃会话ID（支持跨设备续聊）
     */
    @GetMapping("/chat/session")
    public AjaxResult activeSession() {
        Long userId = resolveCurrentUserIdSafely();
        if (userId == null) {
            return AjaxResult.error(403, "请先登录后再获取会话");
        }
        String sessionId = aiChatService.getActiveSessionId(userId);
        AjaxResult result = success();
        result.put("sessionId", sessionId);
        return result;
    }

    private boolean isGuestOverLimit(Long userId, String guestId, String sessionId) {
        if (userId != null) {
            return false;
        }
        long usedTurns = aiChatService.countGuestChatTurns(guestId, sessionId);
        boolean overLimit = usedTurns >= GUEST_CHAT_LIMIT;
        log.info("AI guest chat check: guestId={}, usedTurns={}, limit={}, overLimit={}",
                guestId, usedTurns, GUEST_CHAT_LIMIT, overLimit);
        return overLimit;
    }

    private String resolveSessionId(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return UUID.randomUUID().toString().replace("-", "");
        }
        return sessionId.trim();
    }

    private String resolveGuestId(HttpServletRequest request, String sessionId) {
        if (request != null) {
            String guestIdHeader = request.getHeader(GUEST_ID_HEADER);
            if (isValidGuestId(guestIdHeader)) {
                return "guest:" + guestIdHeader.trim();
            }
        }
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return "anon:" + sessionId;
        }
        return "anon:" + sessionId.trim();
    }

    private boolean isValidGuestId(String guestId) {
        if (guestId == null) {
            return false;
        }
        String normalized = guestId.trim();
        if (normalized.isEmpty()) {
            return false;
        }
        return GUEST_ID_PATTERN.matcher(normalized).matches();
    }

    private Long resolveCurrentUserIdSafely() {
        try {
            return getUserId();
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * 聊天请求数据
     */
    public static class ChatRequest {
        private String sessionId;
        private String message;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
