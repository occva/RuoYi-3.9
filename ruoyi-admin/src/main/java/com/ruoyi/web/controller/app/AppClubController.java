package com.ruoyi.web.controller.app;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.user.domain.Club;
import com.ruoyi.user.domain.ClubApplication;
import com.ruoyi.user.domain.ClubMember;
import com.ruoyi.user.service.IClubService;
import com.ruoyi.user.service.IClubApplicationService;
import com.ruoyi.user.service.IClubMemberService;
import com.ruoyi.user.domain.ClubFavorite;
import com.ruoyi.user.service.IClubFavoriteService;

/**
 * 社团信息Controller（用户端）
 * 
 * @author ruoyi
 */
@Anonymous
@RestController
@RequestMapping("/api/app/club")
public class AppClubController extends BaseController {

    @Autowired
    private IClubService clubService;

    @Autowired
    private IClubApplicationService applicationService;

    @Autowired
    private IClubMemberService memberService;

    @Autowired
    private IClubFavoriteService favoriteService;

    /**
     * 获取社团列表
     */
    @GetMapping("/list")
    public AjaxResult list(Club club) {
        // 只查询正常状态的社团
        club.setStatus("0");
        club.setDelFlag("0");
        List<Club> list = clubService.selectClubList(club);
        return success(list);
    }

    /**
     * 获取社团列表(分页)
     */
    @GetMapping("/page")
    public TableDataInfo page(Club club) {
        // 只查询正常状态的社团
        club.setStatus("0");
        club.setDelFlag("0");
        startPage();
        List<Club> list = clubService.selectClubList(club);
        return getDataTable(list);
    }

    /**
     * 获取热门社团
     */
    @GetMapping("/popular")
    public AjaxResult popular(@RequestParam(defaultValue = "6") int limit) {
        return success(clubService.selectPopularClubs(limit));
    }

    /**
     * 获取我的社团 (已加入 + 管理的)
     */
    @GetMapping("/my")
    public AjaxResult my() {
        try {
            SysUser user = getLoginUser().getUser();
            Long userId = user.getUserId();

            AjaxResult ajax = AjaxResult.success();
            ajax.put("joined", clubService.selectClubListByUserId(userId));
            ajax.put("managed", clubService.selectClubListByPresidentId(userId));

            // 查询我的申请
            ClubApplication appQuery = new ClubApplication();
            appQuery.setUserId(userId);
            appQuery.setDelFlag("0");
            ajax.put("applications", applicationService.selectClubApplicationList(appQuery));

            // 查询我的收藏
            ClubFavorite favQuery = new ClubFavorite();
            favQuery.setUserId(userId);
            ajax.put("favorites", favoriteService.selectClubFavoriteList(favQuery));

            return ajax;
        } catch (Exception e) {
            return error("请先登录");
        }
    }

    /**
     * 获取社团详情
     */
    @GetMapping("/{clubId}")
    public AjaxResult getInfo(@PathVariable Long clubId) {
        Club club = clubService.selectClubById(clubId);
        if (club == null || !"0".equals(club.getStatus())) {
            return error("社团不存在或已停用");
        }

        // 检查用户状态 (是否成员/已申请)
        try {
            // getLoginUser() throws exception if not logged in in RuoYi
            SysUser user = getLoginUser().getUser();
            if (user != null) {
                Long userId = user.getUserId();

                // 1. 检查是否是成员
                ClubMember memberQuery = new ClubMember();
                memberQuery.setClubId(clubId);
                memberQuery.setUserId(userId);
                memberQuery.setDelFlag("0");
                List<ClubMember> members = memberService.selectClubMemberList(memberQuery);
                club.setMember(members != null && !members.isEmpty());

                // 2. 如果不是成员，检查是否已申请
                if (!club.isMember()) {
                    ClubApplication appQuery = new ClubApplication();
                    appQuery.setClubId(clubId);
                    appQuery.setUserId(userId);
                    appQuery.setStatus("0"); // 待审核
                    appQuery.setDelFlag("0");
                    List<ClubApplication> apps = applicationService.selectClubApplicationList(appQuery);
                    club.setHasApplied(apps != null && !apps.isEmpty());
                }

                // 3. 检查是否已收藏
                club.setFavorite(favoriteService.isFavorite(userId, clubId));
            }
        } catch (Exception e) {
            // 用户未登录，忽略
        }

        return success(club);
    }

    /**
     * 申请创建社团 (需要登录)
     * 实现说明：
     * 1. 插入 club_application 表作为创建申请
     * 2. 状态设置为待审核
     * 3. 这里的 apply 应该是管理员审核通过后才在 club 表生成正式记录
     */
    @PostMapping("/apply")
    public AjaxResult apply(@RequestBody Club club) {
        club.setCreateTime(new java.util.Date());
        club.setStatus("1"); // 待审核
        club.setDelFlag("0");
        club.setCreateBy(getUsername());
        return toAjax(clubService.insertClub(club));
    }

    /**
     * 申请加入社团 (需要登录)
     * 实现说明：
     * 1. 验证社团是否存在且开放加入
     * 2. 验证用户是否已经是成员
     * 3. 插入 club_application 表作为入社申请
     */
    @PostMapping("/join")
    public AjaxResult join(@RequestBody ClubApplication application) {
        // 1. 验证社团是否存在且开放加入
        Long clubId = application.getClubId();
        if (clubId == null) {
            return error("社团ID不能为空");
        }

        Club club = clubService.selectClubById(clubId);
        if (club == null || !"0".equals(club.getStatus())) {
            return error("社团不存在或已停用");
        }

        // 获取当前用户信息
        SysUser currentUser = getLoginUser().getUser();
        Long userId = currentUser.getUserId();

        // 2. 验证用户是否已经是成员
        ClubMember memberQuery = new ClubMember();
        memberQuery.setClubId(clubId);
        memberQuery.setUserId(userId);
        memberQuery.setDelFlag("0");
        List<ClubMember> existingMembers = memberService.selectClubMemberList(memberQuery);
        if (existingMembers != null && !existingMembers.isEmpty()) {
            return error("您已经是该社团的成员");
        }

        // 验证用户是否已有待审核的申请
        ClubApplication appQuery = new ClubApplication();
        appQuery.setClubId(clubId);
        appQuery.setUserId(userId);
        appQuery.setStatus("0"); // 待审核
        appQuery.setDelFlag("0");
        List<ClubApplication> existingApplications = applicationService.selectClubApplicationList(appQuery);
        if (existingApplications != null && !existingApplications.isEmpty()) {
            return error("您已提交过入社申请，请等待审核");
        }

        // 3. 插入入社申请记录
        application.setUserId(userId);
        application.setUserName(currentUser.getUserName());
        application.setNickName(currentUser.getNickName());
        application.setPhone(currentUser.getPhonenumber());
        application.setEmail(currentUser.getEmail());
        application.setStatus("0"); // 待审核
        application.setDelFlag("0");
        application.setApplicationTime(new Date());
        application.setCreateBy(getUsername());

        int result = applicationService.insertClubApplication(application);
        if (result <= 0) {
            return error("申请提交失败，请稍后重试");
        }

        return success("申请已提交成功，请耐心等待社长审核");
    }

    /**
     * 切换收藏状态
     */
    @PostMapping("/favorite/{clubId}")
    public AjaxResult toggleFavorite(@PathVariable Long clubId) {
        try {
            SysUser user = getLoginUser().getUser();
            boolean isFavorite = favoriteService.toggleFavorite(user.getUserId(), clubId);
            return success(isFavorite ? "已收藏" : "已取消收藏");
        } catch (Exception e) {
            return error("操作失败，请重试");
        }
    }

    /**
     * 退出社团（需要登录）
     */
    @PostMapping("/quit/{clubId}")
    public AjaxResult quit(@PathVariable Long clubId) {
        SysUser currentUser;
        try {
            currentUser = getLoginUser().getUser();
        } catch (Exception e) {
            return error("请先登录");
        }

        Long userId = currentUser.getUserId();
        Club club = clubService.selectClubById(clubId);
        if (club == null) {
            return error("社团不存在");
        }

        // 社长不可直接退出，避免社团失管
        if (club.getPresidentId() != null && club.getPresidentId().equals(userId)) {
            return error("您是社长，请先转让社长职位后再退出");
        }

        ClubMember memberQuery = new ClubMember();
        memberQuery.setClubId(clubId);
        memberQuery.setUserId(userId);
        memberQuery.setStatus("0");
        memberQuery.setDelFlag("0");
        List<ClubMember> members = memberService.selectClubMemberList(memberQuery);
        if (members == null || members.isEmpty()) {
            return error("您不是该社团有效成员，无法退出");
        }

        ClubMember member = members.get(0);
        if ("1".equals(member.getRoleType())) {
            return error("您是社长，请先转让社长职位后再退出");
        }

        return toAjax(memberService.deleteClubMemberById(member.getMemberId()));
    }
}
