package com.ruoyi.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.user.domain.AppNotificationItem;

/**
 * 用户端通知中心Mapper
 */
public interface AppNotificationMapper
{
    /**
     * 查询用户可见通知列表
     */
    List<AppNotificationItem> selectNotificationList(@Param("userId") Long userId);

    /**
     * 查询未读数量
     */
    long selectUnreadCount(@Param("userId") Long userId);

    /**
     * 校验通知是否对用户可见
     */
    int countAccessibleNotification(@Param("userId") Long userId, @Param("noticeSource") String noticeSource,
            @Param("noticeId") Long noticeId);

    /**
     * 标记单条通知已读
     */
    int insertReadRecord(@Param("userId") Long userId, @Param("noticeSource") String noticeSource,
            @Param("noticeId") Long noticeId);

    /**
     * 标记全部全站公告已读
     */
    int insertAllSystemReadRecords(@Param("userId") Long userId);

    /**
     * 标记全部社团公告已读
     */
    int insertAllClubReadRecords(@Param("userId") Long userId);
}
