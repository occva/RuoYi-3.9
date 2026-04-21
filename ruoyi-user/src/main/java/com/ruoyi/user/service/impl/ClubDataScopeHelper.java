package com.ruoyi.user.service.impl;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.user.mapper.ClubMemberMapper;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 社团数据范围辅助类
 * <p>
 * 社长/副社长只能看到并操作自己所在社团的数据（仅限管理端接口）。
 * 用户端（/api/app/**、/api/user/**）不做数据隔离，社长也可以浏览所有社团。
 * 管理员(admin)和社团管理员(club_admin)可查看全部数据。
 * </p>
 */
@Component
public class ClubDataScopeHelper {

    @Autowired
    private ClubMemberMapper clubMemberMapper;

    /**
     * 判断当前请求是否来自用户端（app/user），用户端不做数据隔离
     */
    private boolean isAppRequest() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null)
                return false;
            HttpServletRequest request = attrs.getRequest();
            String uri = request.getRequestURI();
            return uri.startsWith("/api/app/") || uri.startsWith("/api/user/");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 是否需要限制数据范围（社长或副社长角色，且为管理端请求）
     */
    public boolean needScopeLimit() {
        if (isAppRequest()) {
            return false;
        }
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            if (loginUser == null) {
                return false;
            }
            SysUser user = loginUser.getUser();
            if (user == null || user.isAdmin()) {
                return false;
            }
            // 检查用户是否有 president 或 vice_president 角色
            boolean hasPresidentRole = user.getRoles() != null && user.getRoles().stream()
                    .anyMatch(r -> "president".equals(r.getRoleKey()) || "vice_president".equals(r.getRoleKey()));
            return hasPresidentRole;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取当前登录社长/副社长管理的社团ID列表（仅管理端有效）。
     * 若不需要限制（管理员、用户端请求等），返回 null。
     * 若需要限制但查不到任何社团，返回一个不存在的哨兵ID，确保查询结果为空。
     *
     * @return null=不限制, 非null=仅允许访问列表内的社团
     */
    public List<Long> getManagedClubIds() {
        if (!needScopeLimit()) {
            return null;
        }
        try {
            Long userId = SecurityUtils.getUserId();
            List<Long> clubIds = clubMemberMapper.selectManagedClubIdsByUserId(userId);
            if (clubIds == null || clubIds.isEmpty()) {
                // 使用不存在的ID兜底，确保下游 in (...) 过滤后返回空结果
                return Collections.singletonList(-1L);
            }
            return clubIds;
        } catch (Exception e) {
            // 异常时同样收敛为无数据，避免误放大数据范围
            return Collections.singletonList(-1L);
        }
    }

    /**
     * 判断当前用户是否可访问指定社团数据。
     *
     * @param clubId 社团ID
     * @return true=可访问，false=不可访问
     */
    public boolean isManagedClub(Long clubId) {
        List<Long> managedClubIds = getManagedClubIds();
        return managedClubIds == null || (clubId != null && managedClubIds.contains(clubId));
    }
}
