package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;
import com.ruoyi.web.socket.notification.AppNotificationWebSocketHandler;

/**
 * 公告 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController
{
    @Autowired
    private ISysNoticeService noticeService;

    @Autowired
    private AppNotificationWebSocketHandler appNotificationWebSocketHandler;

    /**
     * 获取通知公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice)
    {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId)
    {
        return success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNotice notice)
    {
        notice.setCreateBy(getUsername());
        AjaxResult ajax = toAjax(noticeService.insertNotice(notice));
        if (ajax.isSuccess() && isSystemNoticeVisible(notice.getStatus()))
        {
            appNotificationWebSocketHandler.sendRefreshToAll();
        }
        return ajax;
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysNotice notice)
    {
        SysNotice existed = noticeService.selectNoticeById(notice.getNoticeId());
        notice.setUpdateBy(getUsername());
        AjaxResult ajax = toAjax(noticeService.updateNotice(notice));
        if (ajax.isSuccess() && shouldRefreshSystemNotice(existed, notice))
        {
            appNotificationWebSocketHandler.sendRefreshToAll();
        }
        return ajax;
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds)
    {
        List<SysNotice> existedNotices = new ArrayList<>();
        for (Long noticeId : noticeIds)
        {
            SysNotice notice = noticeService.selectNoticeById(noticeId);
            if (notice != null)
            {
                existedNotices.add(notice);
            }
        }
        AjaxResult ajax = toAjax(noticeService.deleteNoticeByIds(noticeIds));
        if (ajax.isSuccess() && hasVisibleSystemNotice(existedNotices))
        {
            appNotificationWebSocketHandler.sendRefreshToAll();
        }
        return ajax;
    }

    private boolean shouldRefreshSystemNotice(SysNotice existed, SysNotice current)
    {
        String currentStatus = current != null && current.getStatus() != null
                ? current.getStatus()
                : existed != null ? existed.getStatus() : null;
        return isSystemNoticeVisible(existed != null ? existed.getStatus() : null)
                || isSystemNoticeVisible(currentStatus);
    }

    private boolean hasVisibleSystemNotice(List<SysNotice> notices)
    {
        for (SysNotice notice : notices)
        {
            if (isSystemNoticeVisible(notice.getStatus()))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isSystemNoticeVisible(String status)
    {
        return !"1".equals(status);
    }
}
