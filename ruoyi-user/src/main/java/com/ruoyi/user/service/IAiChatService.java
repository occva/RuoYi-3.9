package com.ruoyi.user.service;

import com.ruoyi.user.domain.ChatMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * AI聊天服务接口
 */
public interface IAiChatService {
    /**
     * 普通对话（一次性返回）
     *
     * @param sessionId 会话ID
     * @param userId    用户ID（可为null）
     * @param message   用户消息
     * @return AI回复文本
     */
    String chat(String sessionId, Long userId, String guestId, String message);

    /**
     * 流式对话（SSE推送）
     *
     * @param sessionId 会话ID
     * @param userId    用户ID（可为null）
     * @param message   用户消息
     * @return SseEmitter
     */
    SseEmitter chatStream(String sessionId, Long userId, String guestId, String message);

    /**
     * 获取会话历史
     *
     * @param sessionId 会话ID
     * @return 消息列表
     */
    List<ChatMessage> getHistory(String sessionId, Long userId);

    /**
     * 获取登录用户当前活跃会话ID（用于跨设备续聊）
     *
     * @param userId 用户ID
     * @return 会话ID
     */
    String getActiveSessionId(Long userId);

    /**
     * 统计未登录访客已使用的对话次数（按用户消息计数）
     *
     * @param guestId   访客标识
     * @param sessionId 会话ID（guestId 缺失时兜底）
     * @return 已使用次数
     */
    long countGuestChatTurns(String guestId, String sessionId);
}
