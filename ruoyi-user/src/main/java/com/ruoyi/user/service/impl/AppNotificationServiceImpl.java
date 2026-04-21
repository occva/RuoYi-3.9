package com.ruoyi.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.user.domain.AppNotificationItem;
import com.ruoyi.user.mapper.AppNotificationMapper;
import com.ruoyi.user.service.IAppNotificationService;

/**
 * 用户端通知中心Service实现
 */
@Service
public class AppNotificationServiceImpl implements IAppNotificationService
{
    private static final String SOURCE_SYSTEM = "system";

    private static final String SOURCE_CLUB = "club";

    @Autowired
    private AppNotificationMapper appNotificationMapper;

    @Override
    public List<AppNotificationItem> selectNotificationList(Long userId)
    {
        return appNotificationMapper.selectNotificationList(userId);
    }

    @Override
    public long countUnread(Long userId)
    {
        return appNotificationMapper.selectUnreadCount(userId);
    }

    @Override
    public Map<String, Object> getSummary(Long userId)
    {
        Map<String, Object> summary = new HashMap<>();
        summary.put("unreadCount", countUnread(userId));
        return summary;
    }

    @Override
    public void markRead(Long userId, String noticeSource, Long noticeId)
    {
        String normalizedSource = normalizeSource(noticeSource);
        int accessibleCount = appNotificationMapper.countAccessibleNotification(userId, normalizedSource, noticeId);
        if (accessibleCount <= 0)
        {
            throw new ServiceException("通知不存在或不可访问");
        }
        appNotificationMapper.insertReadRecord(userId, normalizedSource, noticeId);
    }

    @Override
    public int markAllRead(Long userId)
    {
        int affected = 0;
        affected += appNotificationMapper.insertAllSystemReadRecords(userId);
        affected += appNotificationMapper.insertAllClubReadRecords(userId);
        return affected;
    }

    private String normalizeSource(String noticeSource)
    {
        String normalized = noticeSource == null ? "" : noticeSource.trim().toLowerCase(Locale.ROOT);
        if (SOURCE_SYSTEM.equals(normalized) || SOURCE_CLUB.equals(normalized))
        {
            return normalized;
        }
        throw new ServiceException("不支持的通知来源");
    }
}
