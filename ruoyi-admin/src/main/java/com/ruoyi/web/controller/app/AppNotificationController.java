package com.ruoyi.web.controller.app;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.user.domain.AppNotificationItem;
import com.ruoyi.user.domain.AppNotificationReadRequest;
import com.ruoyi.user.service.IAppNotificationService;

/**
 * 用户端通知中心
 */
@RestController
@RequestMapping("/api/app/notification")
public class AppNotificationController extends BaseController
{
    @Autowired
    private IAppNotificationService appNotificationService;

    /**
     * 获取通知摘要
     */
    @GetMapping("/summary")
    public AjaxResult summary()
    {
        return success(appNotificationService.getSummary(getUserId()));
    }

    /**
     * 获取通知列表
     */
    @GetMapping("/list")
    public AjaxResult list()
    {
        startPage();
        Long userId = getUserId();
        List<AppNotificationItem> list = appNotificationService.selectNotificationList(userId);
        AjaxResult ajax = success();
        ajax.put("rows", list);
        ajax.put("total", new PageInfo<>(list).getTotal());
        ajax.put("unreadCount", appNotificationService.countUnread(userId));
        return ajax;
    }

    /**
     * 标记单条已读
     */
    @PostMapping("/read")
    public AjaxResult markRead(@Validated @RequestBody AppNotificationReadRequest request)
    {
        appNotificationService.markRead(getUserId(), request.getNoticeSource(), request.getNoticeId());
        return success();
    }

    /**
     * 全部标记已读
     */
    @PostMapping("/read-all")
    public AjaxResult markAllRead()
    {
        int affected = appNotificationService.markAllRead(getUserId());
        return success().put("affected", affected);
    }
}
