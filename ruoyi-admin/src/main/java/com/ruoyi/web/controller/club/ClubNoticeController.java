package com.ruoyi.web.controller.club;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.user.domain.ClubNotice;
import com.ruoyi.user.service.IClubNoticeService;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DateUtils;

/**
 * 社团公告管理Controller
 * 
 * @author ruoyi
 */
@RestController("sysClubNoticeController")
@RequestMapping("/club/notice")
public class ClubNoticeController extends BaseController {
    @Autowired
    private IClubNoticeService clubNoticeService;

    /**
     * 查询社团公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClubNotice notice) {
        startPage();
        List<ClubNotice> list = clubNoticeService.selectClubNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 获取社团公告详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable("noticeId") Long noticeId) {
        return success(clubNoticeService.selectClubNoticeById(noticeId));
    }

    /**
     * 新增社团公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "社团公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ClubNotice notice) {
        notice.setCreateBy(getUsername());
        notice.setPublisherId(getUserId());
        notice.setPublisherName(getLoginUser().getUser().getNickName());
        if ("1".equals(notice.getStatus())) {
            notice.setPublishTime(DateUtils.getNowDate());
        }
        return toAjax(clubNoticeService.insertClubNotice(notice));
    }

    /**
     * 修改社团公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "社团公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClubNotice notice) {
        notice.setUpdateBy(getUsername());
        if ("1".equals(notice.getStatus()) && notice.getPublishTime() == null) {
            notice.setPublishTime(DateUtils.getNowDate());
        }
        return toAjax(clubNoticeService.updateClubNotice(notice));
    }

    /**
     * 删除社团公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "社团公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds) {
        return toAjax(clubNoticeService.deleteClubNoticeByIds(noticeIds));
    }
}
