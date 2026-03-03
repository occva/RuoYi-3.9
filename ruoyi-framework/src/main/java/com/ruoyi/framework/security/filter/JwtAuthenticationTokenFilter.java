package com.ruoyi.framework.security.filter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;

/**
 * token过滤器 验证token有效性
 * 
 * @author ruoyi
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter
{
    private static final String CLIENT_TYPE_ADMIN = "admin";
    private static final String CLIENT_TYPE_USER = "user";
    private static final String[] ADMIN_ONLY_PATH_PREFIXES = { "/system/", "/monitor/", "/tool/", "/gen/", "/club/" };

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication()))
        {
            tokenService.verifyToken(loginUser);
            ensureAdminClientAccess(request, loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }

    private void ensureAdminClientAccess(HttpServletRequest request, LoginUser loginUser)
    {
        if (!isAdminOnlyPath(request))
        {
            return;
        }
        String clientType = StringUtils.nvl(loginUser == null ? null : loginUser.getClientType(), CLIENT_TYPE_USER);
        if (!CLIENT_TYPE_ADMIN.equalsIgnoreCase(clientType))
        {
            throw new AccessDeniedException("Admin client access is required for this endpoint");
        }
    }

    private boolean isAdminOnlyPath(HttpServletRequest request)
    {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (StringUtils.isNotEmpty(contextPath) && uri.startsWith(contextPath))
        {
            uri = uri.substring(contextPath.length());
        }
        if ("/getRouters".equals(uri))
        {
            return true;
        }
        return StringUtils.startsWithAny(uri, ADMIN_ONLY_PATH_PREFIXES);
    }
}
