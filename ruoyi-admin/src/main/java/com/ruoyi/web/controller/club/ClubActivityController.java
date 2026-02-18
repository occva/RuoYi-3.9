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
import com.ruoyi.user.domain.ClubActivity;
import com.ruoyi.user.service.IClubActivityService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 社团活动管理Controller
 * 
 * @author ruoyi
 */
@RestController("sysClubActivityController")
@RequestMapping("/club/activity")
public class ClubActivityController extends BaseController {
    @Autowired
    private IClubActivityService clubActivityService;

    /**
     * 查询社团活动列表
     */
    @PreAuthorize("@ss.hasPermi('system:activity:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClubActivity activity) {
        startPage();
        List<ClubActivity> list = clubActivityService.selectClubActivityList(activity);
        return getDataTable(list);
    }

    /**
     * 获取社团活动详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:activity:query')")
    @GetMapping(value = "/{activityId}")
    public AjaxResult getInfo(@PathVariable("activityId") Long activityId) {
        return success(clubActivityService.selectClubActivityById(activityId));
    }

    /**
     * 新增社团活动
     */
    @PreAuthorize("@ss.hasPermi('system:activity:add')")
    @Log(title = "社团活动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ClubActivity activity) {
        activity.setCreateBy(getUsername());
        return toAjax(clubActivityService.insertClubActivity(activity));
    }

    /**
     * 修改社团活动
     */
    @PreAuthorize("@ss.hasPermi('system:activity:edit')")
    @Log(title = "社团活动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClubActivity activity) {
        activity.setUpdateBy(getUsername());
        return toAjax(clubActivityService.updateClubActivity(activity));
    }

    /**
     * 删除社团活动
     */
    @PreAuthorize("@ss.hasPermi('system:activity:remove')")
    @Log(title = "社团活动", businessType = BusinessType.DELETE)
    @DeleteMapping("/{activityIds}")
    public AjaxResult remove(@PathVariable Long[] activityIds) {
        return toAjax(clubActivityService.deleteClubActivityByIds(activityIds));
    }

    /**
     * 获取活动统计数据
     */
    @PreAuthorize("@ss.hasPermi('system:activity:list')")
    @GetMapping("/stat")
    public AjaxResult stat(@org.springframework.web.bind.annotation.RequestParam(required = false) String beginTime,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String endTime) {
        return success(clubActivityService.getStatData(beginTime, endTime));
    }
}
