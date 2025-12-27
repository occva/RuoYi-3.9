package com.ruoyi.user.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.user.domain.ClubActivity;
import com.ruoyi.user.service.IClubActivityService;

import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 社团活动Controller (用户端)
 */
@Anonymous
@RestController
@RequestMapping("/api/user/activity")
public class ClubActivityController extends BaseController {

    @Autowired
    private IClubActivityService clubActivityService;

    /**
     * 获取活动列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ClubActivity activity) {
        startPage();
        List<ClubActivity> list = clubActivityService.selectClubActivityList(activity);
        return getDataTable(list);
    }

    /**
     * 获取社团的活动列表
     */
    @GetMapping("/club/{clubId}")
    public AjaxResult listByClub(@PathVariable Long clubId) {
        return success(clubActivityService.selectActivityByClubId(clubId));
    }

    /**
     * 获取活动详情
     */
    @GetMapping("/{activityId}")
    public AjaxResult getInfo(@PathVariable Long activityId) {
        return success(clubActivityService.selectClubActivityById(activityId));
    }
}
