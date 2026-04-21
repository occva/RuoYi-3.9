package com.ruoyi.web.socket.notification;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用户端通知中心WebSocket处理器
 */
@Component
public class AppNotificationWebSocketHandler extends TextWebSocketHandler
{
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<Long, Set<WebSocketSession>> sessionsByUser = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception
    {
        Long userId = getUserId(session);
        if (userId == null)
        {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("用户未登录"));
            return;
        }

        sessionsByUser.computeIfAbsent(userId, key -> ConcurrentHashMap.newKeySet()).add(session);
        sendPayload(session, Map.of("type", "notification_connected", "timestamp", System.currentTimeMillis()));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception
    {
        removeSession(session);
        if (session.isOpen())
        {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception
    {
        removeSession(session);
    }

    public void sendRefreshToAll()
    {
        sessionsByUser.keySet().forEach(this::sendRefreshToUser);
    }

    public void sendRefreshToUsers(Collection<Long> userIds)
    {
        if (userIds == null)
        {
            return;
        }

        userIds.stream()
                .filter(userId -> userId != null)
                .distinct()
                .forEach(this::sendRefreshToUser);
    }

    public void sendRefreshToUser(Long userId)
    {
        if (userId == null)
        {
            return;
        }

        Set<WebSocketSession> sessions = sessionsByUser.get(userId);
        if (sessions == null || sessions.isEmpty())
        {
            return;
        }

        TextMessage message = buildRefreshMessage();
        sessions.removeIf(session -> !sendMessage(session, message));
        if (sessions.isEmpty())
        {
            sessionsByUser.remove(userId);
        }
    }

    private TextMessage buildRefreshMessage()
    {
        try
        {
            String payload = objectMapper.writeValueAsString(
                    Map.of("type", "notification_refresh", "timestamp", System.currentTimeMillis()));
            return new TextMessage(payload);
        }
        catch (Exception e)
        {
            throw new IllegalStateException("构建通知消息失败", e);
        }
    }

    private void sendPayload(WebSocketSession session, Map<String, Object> payload) throws IOException
    {
        if (!session.isOpen())
        {
            return;
        }
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(payload)));
    }

    private boolean sendMessage(WebSocketSession session, TextMessage message)
    {
        if (!session.isOpen())
        {
            return false;
        }

        try
        {
            session.sendMessage(message);
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    private void removeSession(WebSocketSession session)
    {
        Long userId = getUserId(session);
        if (userId == null)
        {
            return;
        }

        Set<WebSocketSession> sessions = sessionsByUser.get(userId);
        if (sessions == null)
        {
            return;
        }

        sessions.remove(session);
        if (sessions.isEmpty())
        {
            sessionsByUser.remove(userId);
        }
    }

    private Long getUserId(WebSocketSession session)
    {
        Object userId = session.getAttributes().get(AppNotificationHandshakeInterceptor.ATTR_USER_ID);
        if (userId instanceof Long value)
        {
            return value;
        }
        if (userId instanceof Number number)
        {
            return number.longValue();
        }
        return null;
    }
}
