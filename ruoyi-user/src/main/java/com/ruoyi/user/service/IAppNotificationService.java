package com.ruoyi.user.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.user.domain.AppNotificationItem;

/**
 * 用户端通知中心Service
 */
public interface IAppNotificationService
{
    /**
     * 查询通知列表
     */
    List<AppNotificationItem> selectNotificationList(Long userId);

    /**
     * 查询未读数量
     */
    long countUnread(Long userId);

    /**
     * 获取摘要
     */
    Map<String, Object> getSummary(Long userId);

    /**
     * 标记单条已读
     */
    void markRead(Long userId, String noticeSource, Long noticeId);

    /**
     * 全部标记已读
     */
    int markAllRead(Long userId);
}
