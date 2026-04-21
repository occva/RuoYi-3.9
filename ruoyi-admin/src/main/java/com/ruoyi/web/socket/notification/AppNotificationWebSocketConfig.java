package com.ruoyi.web.socket.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 用户端通知中心WebSocket配置
 */
@Configuration
@EnableWebSocket
public class AppNotificationWebSocketConfig implements WebSocketConfigurer
{
    @Autowired
    private AppNotificationWebSocketHandler appNotificationWebSocketHandler;

    @Autowired
    private AppNotificationHandshakeInterceptor appNotificationHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        registry.addHandler(appNotificationWebSocketHandler, "/api/app/ws/notification")
                .addInterceptors(appNotificationHandshakeInterceptor)
                .setAllowedOriginPatterns("*");
    }
}
