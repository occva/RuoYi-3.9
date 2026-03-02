package com.ruoyi.web.controller.app;

import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.user.domain.ClubActivity;
import com.ruoyi.user.domain.ClubActivityRegistration;
import com.ruoyi.user.service.IClubActivityRegistrationService;
import com.ruoyi.user.service.IClubActivityService;
import com.ruoyi.user.service.IClubMemberService;
import com.ruoyi.user.domain.ClubMember;

/**
 * 用户端活动接口
 */
@RestController
@RequestMapping("/api/app/activity")
public class AppActivityController extends BaseController {

    @Autowired
    private IClubActivityService clubActivityService;

    @Autowired
    private IClubActivityRegistrationService registrationService;

    @Autowired
    private IClubMemberService clubMemberService;

    /**
     * 活动分页列表
     */
    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(ClubActivity activity) {
        activity.setDelFlag("0");
        startPage();
        List<ClubActivity> list = clubActivityService.selectClubActivityList(activity);
        return getDataTable(list);
    }

    /**
     * 社团活动列表
     */
    @Anonymous
    @GetMapping("/club/{clubId}")
    public AjaxResult listByClub(@PathVariable Long clubId) {
        List<ClubActivity> activities = clubActivityService.selectActivityByClubId(clubId);
        attachRegisteredState(activities, clubId, resolveCurrentUserIdSafely());
        return success(activities);
    }

    /**
     * 我的报名活动
     */
    @GetMapping("/my")
    public AjaxResult myActivities() {
        SysUser user = getLoginUser().getUser();
        return success(clubActivityService.selectMyRegisteredActivities(user.getUserId()));
    }

    /**
     * 活动详情
     */
    @Anonymous
    @GetMapping("/{activityId:\\d+}")
    public AjaxResult getInfo(@PathVariable Long activityId) {
        ClubActivity activity = clubActivityService.selectClubActivityById(activityId);
        if (activity == null || "2".equals(activity.getDelFlag())) {
            return error("活动不存在或已删除");
        }
        Long userId = resolveCurrentUserIdSafely();
        activity.setHasRegistered(isUserRegistered(activityId, userId));
        return success(activity);
    }

    /**
     * 活动报名名单（用户端）
     */
    @GetMapping("/{activityId:\\d+}/registrations")
    public AjaxResult listRegistrations(@PathVariable Long activityId) {
        ClubActivity activity = clubActivityService.selectClubActivityById(activityId);
        if (activity == null || "2".equals(activity.getDelFlag())) {
            return error("活动不存在或已删除");
        }
        Long userId = resolveCurrentUserIdSafely();
        if (userId == null) {
            return AjaxResult.error(403, "请先登录后查看报名名单")
                    .put("errorKey", "ACTIVITY_NEED_LOGIN");
        }
        if ("0".equals(activity.getIsPublic()) && !isUserActiveClubMember(activity.getClubId(), userId)) {
            return AjaxResult.error(403, "仅社团成员可查看报名名单")
                    .put("errorKey", "ACTIVITY_NEED_CLUB_MEMBER");
        }

        ClubActivityRegistration query = new ClubActivityRegistration();
        query.setActivityId(activityId);
        query.setDelFlag("0");
        query.setStatus("0");
        List<ClubActivityRegistration> records = registrationService.selectClubActivityRegistrationList(query);
        List<Map<String, Object>> result = records.stream().map(item -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("registrationId", item.getRegistrationId());
            row.put("userId", item.getUserId());
            row.put("userName", item.getUserName());
            row.put("nickName", item.getNickName());
            row.put("registrationTime", item.getRegistrationTime());
            row.put("avatar", item.getAvatar());
            return row;
        }).collect(Collectors.toList());
        return success(result);
    }

    /**
     * 报名活动
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/register/{activityId:\\d+}")
    public AjaxResult register(@PathVariable Long activityId) {
        ClubActivity activity = clubActivityService.selectClubActivityById(activityId);
        if (activity == null || "2".equals(activity.getDelFlag())) {
            return error("活动不存在或已删除");
        }

        String status = activity.getStatus();
        if ("2".equals(status)) {
            return error("活动已结束，无法报名");
        }
        if ("3".equals(status)) {
            return error("活动已取消，无法报名");
        }

        Date now = new Date();
        if (activity.getEndTime() != null && now.after(activity.getEndTime())) {
            return error("活动已结束，无法报名");
        }
        if (activity.getRegistrationEnd() != null && now.after(activity.getRegistrationEnd())) {
            return error("报名时间已截止");
        }
        if (activity.getRegistrationStart() != null && now.before(activity.getRegistrationStart())) {
            return error("报名尚未开始");
        }

        SysUser currentUser = getLoginUser().getUser();
        Long userId = currentUser.getUserId();

        // 校验是否是该社团成员
        ClubMember memberQuery = new ClubMember();
        memberQuery.setUserId(userId);
        memberQuery.setClubId(activity.getClubId());
        memberQuery.setStatus("0"); // 正常状态
        List<ClubMember> members = clubMemberService.selectClubMemberList(memberQuery);
        if (members == null || members.isEmpty()) {
            return AjaxResult.error(403, "请先加入社团后再报名")
                    .put("errorKey", "ACTIVITY_NEED_CLUB_MEMBER");
        }

        ClubActivityRegistration query = new ClubActivityRegistration();
        query.setActivityId(activityId);
        query.setUserId(userId);
        query.setDelFlag("0");
        List<ClubActivityRegistration> existingRegistrations = registrationService
                .selectClubActivityRegistrationList(query);

        ClubActivityRegistration existingCancelledReg = null;
        if (existingRegistrations != null && !existingRegistrations.isEmpty()) {
            for (ClubActivityRegistration reg : existingRegistrations) {
                if (!"2".equals(reg.getStatus())) {
                    return error("您已报名该活动");
                } else {
                    existingCancelledReg = reg;
                }
            }
        }

        Integer maxParticipants = activity.getMaxParticipants();
        Integer currentParticipants = activity.getCurrentParticipants() != null ? activity.getCurrentParticipants() : 0;
        if (maxParticipants != null && maxParticipants > 0 && currentParticipants >= maxParticipants) {
            return error("活动报名人数已满");
        }

        int result;
        try {
            if (existingCancelledReg != null) {
                existingCancelledReg.setStatus("0");
                existingCancelledReg.setRegistrationTime(new Date());
                existingCancelledReg.setUpdateTime(new Date());
                existingCancelledReg.setUpdateBy(getUsername());
                result = registrationService.updateClubActivityRegistration(existingCancelledReg);
            } else {
                ClubActivityRegistration registration = new ClubActivityRegistration();
                registration.setActivityId(activityId);
                registration.setClubId(activity.getClubId());
                registration.setUserId(userId);
                registration.setUserName(currentUser.getUserName());
                registration.setNickName(currentUser.getNickName());
                registration.setStudentId(currentUser.getUserName());
                registration.setPhone(currentUser.getPhonenumber());
                registration.setCheckInStatus("0");
                registration.setStatus("0");
                registration.setDelFlag("0");
                registration.setCreateBy(getUsername());
                result = registrationService.insertClubActivityRegistration(registration);
            }
        } catch (DuplicateKeyException e) {
            return error("您已报名该活动");
        }
        if (result <= 0) {
            return error("报名失败，请稍后重试");
        }

        int updated = clubActivityService.incrementParticipantsIfAvailable(activityId);
        if (updated <= 0) {
            throw new ServiceException("活动报名人数已满");
        }

        return success("报名成功");
    }

    /**
     * 取消报名
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/cancel/{activityId:\\d+}")
    public AjaxResult cancelRegistration(@PathVariable Long activityId) {
        SysUser currentUser;
        try {
            currentUser = getLoginUser().getUser();
        } catch (Exception e) {
            return error("请先登录");
        }
        Long userId = currentUser.getUserId();

        // 检查活动是否已结束，已结束不允许取消
        ClubActivity activity = clubActivityService.selectClubActivityById(activityId);
        if (activity != null && ("2".equals(activity.getStatus()) || "3".equals(activity.getStatus()))) {
            return error("活动已结束或取消，无法撤销报名");
        }

        int result = registrationService.cancelActiveRegistration(activityId, userId);
        if (result <= 0) {
            return error("您尚未报名该活动");
        }

        // 减少参与人数
        int updated = clubActivityService.decrementParticipants(activityId);
        if (updated <= 0) {
            throw new ServiceException("取消报名失败，请稍后重试");
        }

        return success("已取消报名");
    }

    private Long resolveCurrentUserIdSafely() {
        try {
            return getUserId();
        } catch (Exception ignored) {
            return null;
        }
    }

    private boolean isUserRegistered(Long activityId, Long userId) {
        if (activityId == null || userId == null) {
            return false;
        }
        ClubActivityRegistration query = new ClubActivityRegistration();
        query.setActivityId(activityId);
        query.setUserId(userId);
        query.setDelFlag("0");
        List<ClubActivityRegistration> records = registrationService.selectClubActivityRegistrationList(query);
        return records != null && records.stream().anyMatch(item -> !"2".equals(item.getStatus()));
    }

    private boolean isUserActiveClubMember(Long clubId, Long userId) {
        if (clubId == null || userId == null) {
            return false;
        }
        ClubMember memberQuery = new ClubMember();
        memberQuery.setClubId(clubId);
        memberQuery.setUserId(userId);
        memberQuery.setStatus("0");
        memberQuery.setDelFlag("0");
        List<ClubMember> members = clubMemberService.selectClubMemberList(memberQuery);
        return members != null && !members.isEmpty();
    }

    private void attachRegisteredState(List<ClubActivity> activities, Long clubId, Long userId) {
        if (activities == null || activities.isEmpty()) {
            return;
        }
        if (userId == null) {
            activities.forEach(item -> item.setHasRegistered(false));
            return;
        }

        ClubActivityRegistration query = new ClubActivityRegistration();
        query.setClubId(clubId);
        query.setUserId(userId);
        query.setDelFlag("0");
        List<ClubActivityRegistration> records = registrationService.selectClubActivityRegistrationList(query);
        if (records == null || records.isEmpty()) {
            activities.forEach(item -> item.setHasRegistered(false));
            return;
        }
        Set<Long> registeredActivityIds = records.stream()
                .filter(item -> !"2".equals(item.getStatus()))
                .map(ClubActivityRegistration::getActivityId)
                .collect(Collectors.toSet());

        activities.forEach(item -> item.setHasRegistered(registeredActivityIds.contains(item.getActivityId())));
    }
}
