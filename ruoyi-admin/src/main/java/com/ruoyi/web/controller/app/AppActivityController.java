package com.ruoyi.web.controller.app;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.user.domain.ClubActivity;
import com.ruoyi.user.service.IClubActivityService;

/**
 * 社团活动Controller (用户端 - 只读)
 * 
 * @author ruoyi
 */
@Anonymous
@RestController
@RequestMapping("/api/app/activity")
public class AppActivityController extends BaseController {

    @Autowired
    private IClubActivityService clubActivityService;

    /**
     * 获取活动列表（分页）
     */
    @GetMapping("/list")
    public TableDataInfo list(ClubActivity activity) {
        // 只查询已发布且未删除的活动
        activity.setDelFlag("0");
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
        ClubActivity activity = clubActivityService.selectClubActivityById(activityId);
        if (activity == null || "2".equals(activity.getDelFlag())) {
            return error("活动不存在或已删除");
        }
        return success(activity);
    }

    /**
     * 报名参加活动 (需要登录)
     * TODO: 实际应该插入到 club_activity_registration 表
     */
    @PostMapping("/register/{activityId}")
    public AjaxResult register(@PathVariable Long activityId) {
        // 在实际应用中，这里应该插入到 club_activity_registration 表
        return success("报名成功");
    }
}
