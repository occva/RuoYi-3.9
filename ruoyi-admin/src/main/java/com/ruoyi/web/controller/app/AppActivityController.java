package com.ruoyi.web.controller.app;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.user.domain.ClubActivity;
import com.ruoyi.user.domain.ClubActivityRegistration;
import com.ruoyi.user.service.IClubActivityService;
import com.ruoyi.user.service.IClubActivityRegistrationService;

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

    @Autowired
    private IClubActivityRegistrationService registrationService;

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
     * 1. 验证活动是否存在且处于可报名状态
     * 2. 验证用户是否已报名
     * 3. 验证人数是否已满
     * 4. 插入 club_activity_registration 表
     * 5. 更新 club_activity 表的 current_participants 字段
     */
    @PostMapping("/register/{activityId}")
    public AjaxResult register(@PathVariable Long activityId) {
        // 1. 验证活动是否存在且处于可报名状态
        ClubActivity activity = clubActivityService.selectClubActivityById(activityId);
        if (activity == null || "2".equals(activity.getDelFlag())) {
            return error("活动不存在或已删除");
        }

        // 检查活动状态（0=即将开始，1=进行中，2=已结束，3=已取消）
        String status = activity.getStatus();
        if ("2".equals(status)) {
            return error("活动已结束，无法报名");
        }
        if ("3".equals(status)) {
            return error("活动已取消，无法报名");
        }

        // 检查报名时间
        Date now = new Date();
        if (activity.getRegistrationEnd() != null && now.after(activity.getRegistrationEnd())) {
            return error("报名时间已截止");
        }
        if (activity.getRegistrationStart() != null && now.before(activity.getRegistrationStart())) {
            return error("报名尚未开始");
        }

        // 获取当前用户信息
        SysUser currentUser = getLoginUser().getUser();
        Long userId = currentUser.getUserId();

        // 2. 验证用户是否已报名
        ClubActivityRegistration query = new ClubActivityRegistration();
        query.setActivityId(activityId);
        query.setUserId(userId);
        query.setDelFlag("0");
        List<ClubActivityRegistration> existingRegistrations = registrationService
                .selectClubActivityRegistrationList(query);
        if (existingRegistrations != null && !existingRegistrations.isEmpty()) {
            // 检查是否有未取消的报名
            for (ClubActivityRegistration reg : existingRegistrations) {
                if (!"2".equals(reg.getStatus())) { // status: 0=待参加, 1=已参加, 2=已取消
                    return error("您已报名该活动");
                }
            }
        }

        // 3. 验证人数是否已满
        Integer maxParticipants = activity.getMaxParticipants();
        Integer currentParticipants = activity.getCurrentParticipants() != null ? activity.getCurrentParticipants() : 0;
        if (maxParticipants != null && currentParticipants >= maxParticipants) {
            return error("活动报名人数已满");
        }

        // 4. 插入报名记录
        ClubActivityRegistration registration = new ClubActivityRegistration();
        registration.setActivityId(activityId);
        registration.setClubId(activity.getClubId());
        registration.setUserId(userId);
        registration.setUserName(currentUser.getUserName());
        registration.setNickName(currentUser.getNickName());
        registration.setStudentId(currentUser.getUserName()); // 假设用户名就是学号
        registration.setPhone(currentUser.getPhonenumber());
        registration.setCheckInStatus("0"); // 0=未签到
        registration.setStatus("0"); // 0=待参加
        registration.setDelFlag("0");
        registration.setCreateBy(getUsername());

        int result = registrationService.insertClubActivityRegistration(registration);
        if (result <= 0) {
            return error("报名失败，请稍后重试");
        }

        // 5. 更新活动的当前参与人数
        activity.setCurrentParticipants(currentParticipants + 1);
        clubActivityService.updateClubActivity(activity);

        return success("报名成功");
    }
}
