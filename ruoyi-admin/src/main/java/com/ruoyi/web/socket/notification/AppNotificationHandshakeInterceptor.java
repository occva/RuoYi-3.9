package com.ruoyi.web.socket.notification;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;

/**
 * 用户端通知中心WebSocket握手拦截器
 */
@Component
public class AppNotificationHandshakeInterceptor implements HandshakeInterceptor
{
    public static final String ATTR_USER_ID = "userId";

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes)
    {
        if (!(request instanceof ServletServerHttpRequest servletRequest))
        {
            return false;
        }

        String token = servletRequest.getServletRequest().getParameter("token");
        if (StringUtils.isEmpty(token))
        {
            token = servletRequest.getServletRequest().getHeader("Authorization");
        }

        LoginUser loginUser = tokenService.getLoginUser(token);
        if (loginUser == null || loginUser.getUserId() == null)
        {
            return false;
        }

        tokenService.verifyToken(loginUser);
        attributes.put(ATTR_USER_ID, loginUser.getUserId());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception exception)
    {
    }
}
