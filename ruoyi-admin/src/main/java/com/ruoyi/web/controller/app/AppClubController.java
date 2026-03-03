package com.ruoyi.web.controller.app;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.user.domain.Club;
import com.ruoyi.user.domain.ClubApplication;
import com.ruoyi.user.domain.ClubCreateApplication;
import com.ruoyi.user.domain.ClubFavorite;
import com.ruoyi.user.domain.ClubMember;
import com.ruoyi.user.service.IClubApplicationService;
import com.ruoyi.user.service.IClubCreateApplicationService;
import com.ruoyi.user.service.IClubFavoriteService;
import com.ruoyi.user.service.IClubMemberService;
import com.ruoyi.user.service.IClubService;

/**
 * 用户端社团接口
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
    private IClubCreateApplicationService clubCreateApplicationService;

    @Autowired
    private IClubMemberService memberService;

    @Autowired
    private IClubFavoriteService favoriteService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 社团列表（非分页）
     */
    @GetMapping("/list")
    public AjaxResult list(Club club) {
        club.setStatus("0");
        club.setDelFlag("0");
        List<Club> list = clubService.selectClubList(club);
        return success(list);
    }

    /**
     * 社团列表（分页）
     */
    @GetMapping("/page")
    public TableDataInfo page(Club club) {
        club.setStatus("0");
        club.setDelFlag("0");
        startPage();
        List<Club> list = clubService.selectClubList(club);
        return getDataTable(list);
    }

    /**
     * 热门社团
     */
    @GetMapping("/popular")
    public AjaxResult popular(@RequestParam(defaultValue = "6") int limit) {
        return success(clubService.selectPopularClubs(limit));
    }

    /**
     * 我的社团数据（已加入、管理、入社申请、创建社团申请、收藏）
     */
    @GetMapping("/my")
    public AjaxResult my() {
        SysUser user;
        try {
            user = getLoginUser().getUser();
        } catch (Exception e) {
            return error("请先登录");
        }

        Long userId = user.getUserId();
        AjaxResult ajax = AjaxResult.success();
        fillMyBaseData(ajax, userId);
        fillMyApplicationsData(ajax, userId);
        fillMyFavoritesData(ajax, userId);
        return ajax;
    }

    @GetMapping("/my/base")
    public AjaxResult myBase() {
        SysUser user;
        try {
            user = getLoginUser().getUser();
        } catch (Exception e) {
            return error("请先登录");
        }
        AjaxResult ajax = AjaxResult.success();
        fillMyBaseData(ajax, user.getUserId());
        return ajax;
    }

    @GetMapping("/my/favorites")
    public AjaxResult myFavorites() {
        SysUser user;
        try {
            user = getLoginUser().getUser();
        } catch (Exception e) {
            return error("请先登录");
        }
        AjaxResult ajax = AjaxResult.success();
        fillMyFavoritesData(ajax, user.getUserId());
        return ajax;
    }

    @GetMapping("/my/applications")
    public AjaxResult myApplications() {
        SysUser user;
        try {
            user = getLoginUser().getUser();
        } catch (Exception e) {
            return error("请先登录");
        }
        AjaxResult ajax = AjaxResult.success();
        fillMyApplicationsData(ajax, user.getUserId());
        return ajax;
    }

    /**
     * 社团详情
     */
    @GetMapping("/{clubId}")
    public AjaxResult getInfo(@PathVariable Long clubId) {
        Club club = clubService.selectClubById(clubId);
        if (club == null || !"0".equals(club.getStatus())) {
            return error("社团不存在或不可访问");
        }

        SysUser currentUser = getCurrentUserIfLogin();
        boolean viewStatPaused = false;
        String viewStatTip = null;
        if (currentUser != null && currentUser.getUserId() != null) {
            String rateKey = "club:view:rate:" + clubId + ":u:" + currentUser.getUserId();
            Long requestTimes = redisTemplate.opsForValue().increment(rateKey);
            if (requestTimes != null && requestTimes == 1L) {
                redisTemplate.expire(rateKey, 5, TimeUnit.SECONDS);
            }
            if (requestTimes != null && requestTimes > 5L) {
                viewStatPaused = true;
                viewStatTip = "5秒内访问过于频繁，已暂停浏览热度统计";
            } else {
                int changed = clubService.incrementViewCount(clubId);
                if (changed > 0) {
                    int current = club.getViewCount() == null ? 0 : club.getViewCount();
                    club.setViewCount(current + 1);
                }
            }
        }

        if (currentUser != null) {
            Long userId = currentUser.getUserId();

            club.setMember(memberService.isActiveMember(clubId, userId));

            if (!club.isMember()) {
                club.setHasApplied(applicationService.hasPendingApplication(clubId, userId));
            }

            club.setFavorite(favoriteService.isFavorite(userId, clubId));
        }
        AjaxResult ajax = success(club);
        ajax.put("viewStatPaused", viewStatPaused);
        if (viewStatPaused) {
            ajax.put("viewStatTip", viewStatTip);
        }
        return ajax;
    }

    private SysUser getCurrentUserIfLogin() {
        try {
            Authentication authentication = SecurityUtils.getAuthentication();
            if (authentication == null) {
                return null;
            }
            Object principal = authentication.getPrincipal();
            if (principal instanceof LoginUser) {
                return ((LoginUser) principal).getUser();
            }
        } catch (Exception ignored) {
            // ignore
        }
        return null;
    }

    private void fillMyBaseData(AjaxResult ajax, Long userId) {
        ajax.put("joined", clubService.selectClubListByUserId(userId));
        ajax.put("managed", clubService.selectClubListByPresidentId(userId));
    }

    private void fillMyApplicationsData(AjaxResult ajax, Long userId) {
        ClubApplication appQuery = new ClubApplication();
        appQuery.setUserId(userId);
        appQuery.setDelFlag("0");
        ajax.put("applications", applicationService.selectClubApplicationList(appQuery));

        ClubCreateApplication createAppQuery = new ClubCreateApplication();
        createAppQuery.setApplicantUserId(userId);
        createAppQuery.setDelFlag("0");
        ajax.put("createApplications", clubCreateApplicationService.selectClubCreateApplicationList(createAppQuery));
    }

    private void fillMyFavoritesData(AjaxResult ajax, Long userId) {
        ClubFavorite favQuery = new ClubFavorite();
        favQuery.setUserId(userId);
        ajax.put("favorites", favoriteService.selectClubFavoriteList(favQuery));
    }

    /**
     * 获取单条创建社团申请（用于重新申请预填）
     */
    @GetMapping("/create-application/{applyId}")
    public AjaxResult getCreateApplication(@PathVariable Long applyId) {
        SysUser currentUser;
        try {
            currentUser = getLoginUser().getUser();
        } catch (Exception e) {
            return error("请先登录");
        }
        ClubCreateApplication app = clubCreateApplicationService.selectClubCreateApplicationById(applyId);
        if (app == null || "2".equals(app.getDelFlag())) {
            return error("申请记录不存在");
        }
        if (!app.getApplicantUserId().equals(currentUser.getUserId())) {
            return error("无权访问");
        }
        return success(app);
    }

    /**
     * 新社团申请（用户端）
     */
    @PostMapping("/apply")
    public AjaxResult apply(@RequestBody ClubCreateApplication application) {
        SysUser currentUser;
        try {
            currentUser = getLoginUser().getUser();
        } catch (Exception e) {
            return error("请先登录");
        }

        if (application == null || application.getCategoryId() == null
                || application.getClubName() == null || application.getClubName().trim().isEmpty()) {
            return error("社团名称和分类为必填项");
        }

        Club clubNameCheck = new Club();
        clubNameCheck.setClubName(application.getClubName().trim());
        if (!clubService.checkClubNameUnique(clubNameCheck)) {
            return error("社团名称已存在，请更换后重试");
        }

        ClubCreateApplication query = new ClubCreateApplication();
        query.setApplicantUserId(currentUser.getUserId());
        query.setClubName(application.getClubName().trim());
        query.setStatus("0");
        query.setDelFlag("0");
        List<ClubCreateApplication> pendingList = clubCreateApplicationService.selectClubCreateApplicationList(query);
        boolean hasExactPending = pendingList != null && pendingList.stream()
                .anyMatch(item -> application.getClubName().trim().equalsIgnoreCase(item.getClubName()));
        if (hasExactPending) {
            return error("您已提交过同名社团申请，请等待审核");
        }

        application.setClubName(application.getClubName().trim());
        application.setApplicantUserId(currentUser.getUserId());
        application.setApplicantUserName(currentUser.getUserName());
        application.setApplicantNickName(currentUser.getNickName());
        application.setApplicantPhone(currentUser.getPhonenumber());
        application.setApplicantEmail(currentUser.getEmail());
        application.setStatus("0");
        application.setDelFlag("0");
        application.setCreateBy(getUsername());

        int result = clubCreateApplicationService.insertClubCreateApplication(application);
        if (result <= 0) {
            return error("申请提交失败，请稍后重试");
        }
        return success("申请提交成功，请等待审核");
    }

    /**
     * 入社申请
     */
    @PostMapping("/join")
    public AjaxResult join(@RequestBody ClubApplication application) {
        Long clubId = application.getClubId();
        if (clubId == null) {
            return error("社团ID不能为空");
        }

        Club club = clubService.selectClubById(clubId);
        if (club == null || !"0".equals(club.getStatus())) {
            return error("社团不存在或不可加入");
        }

        SysUser currentUser = getLoginUser().getUser();
        Long userId = currentUser.getUserId();
        if (memberService.isActiveMember(clubId, userId)) {
            return error("您已经是该社团成员");
        }
        if (applicationService.hasPendingApplication(clubId, userId)) {
            return error("您已提交过入社申请，请等待审核");
        }

        application.setUserId(userId);
        application.setUserName(currentUser.getUserName());
        application.setNickName(currentUser.getNickName());
        application.setPhone(currentUser.getPhonenumber());
        application.setEmail(currentUser.getEmail());
        application.setStatus("0");
        application.setDelFlag("0");
        application.setApplicationTime(new Date());
        application.setCreateBy(getUsername());

        int result = applicationService.insertClubApplication(application);
        if (result <= 0) {
            return error("申请提交失败，请稍后重试");
        }
        return success("申请已提交，请等待审核");
    }

    /**
     * 切换收藏
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
     * 退出社团
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

        if (club.getPresidentId() != null && club.getPresidentId().equals(userId)) {
            return error("您是社长，请先完成职务交接");
        }

        ClubMember memberQuery = new ClubMember();
        memberQuery.setClubId(clubId);
        memberQuery.setUserId(userId);
        memberQuery.setStatus("0");
        memberQuery.setDelFlag("0");
        List<ClubMember> members = memberService.selectClubMemberList(memberQuery);
        if (members == null || members.isEmpty()) {
            return error("您不是该社团有效成员");
        }

        ClubMember member = members.get(0);
        if ("1".equals(member.getRoleType())) {
            return error("您是社长，请先完成职务交接");
        }

        return toAjax(memberService.deleteClubMemberById(member.getMemberId()));
    }

    /**
     * 撤回入社申请（仅待审核状态可撤回）
     */
    @PostMapping("/cancel-join/{applicationId}")
    public AjaxResult cancelJoinApplication(@PathVariable Long applicationId) {
        SysUser currentUser;
        try {
            currentUser = getLoginUser().getUser();
        } catch (Exception e) {
            return error("请先登录");
        }

        ClubApplication app = applicationService.selectClubApplicationById(applicationId);
        if (app == null || "2".equals(app.getDelFlag())) {
            return error("申请记录不存在");
        }
        if (!app.getUserId().equals(currentUser.getUserId())) {
            return error("无权操作他人申请");
        }
        if (!"0".equals(app.getStatus())) {
            return error("只有待审核状态的申请可以撤回");
        }

        ClubApplication update = new ClubApplication();
        update.setApplicationId(applicationId);
        update.setStatus("3"); // 已撤回
        update.setUpdateBy(getUsername());
        return toAjax(applicationService.updateClubApplication(update));
    }

    /**
     * 撤回创建社团申请（仅待审核状态可撤回）
     */
    @PostMapping("/cancel-create/{applyId}")
    public AjaxResult cancelCreateApplication(@PathVariable Long applyId) {
        SysUser currentUser;
        try {
            currentUser = getLoginUser().getUser();
        } catch (Exception e) {
            return error("请先登录");
        }

        ClubCreateApplication app = clubCreateApplicationService.selectClubCreateApplicationById(applyId);
        if (app == null || "2".equals(app.getDelFlag())) {
            return error("申请记录不存在");
        }
        if (!app.getApplicantUserId().equals(currentUser.getUserId())) {
            return error("无权操作他人申请");
        }
        if (!"0".equals(app.getStatus())) {
            return error("只有待审核状态的申请可以撤回");
        }

        ClubCreateApplication update = new ClubCreateApplication();
        update.setApplyId(applyId);
        update.setStatus("3"); // 已撤回
        update.setUpdateBy(getUsername());
        return toAjax(clubCreateApplicationService.updateClubCreateApplication(update));
    }
}
