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

/**
 * 社团信息Controller (用户端 - 只读)
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
     * 获取社团详情
     */
    @GetMapping("/{clubId}")
    public AjaxResult getInfo(@PathVariable Long clubId) {
        Club club = clubService.selectClubById(clubId);
        if (club == null || !"0".equals(club.getStatus())) {
            return error("社团不存在或已停用");
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
}
