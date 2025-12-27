package com.ruoyi.user.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.user.domain.Club;
import com.ruoyi.user.service.IClubService;

/**
 * 社团信息Controller (用户端)
 */
@Anonymous
@RestController
@RequestMapping("/api/user/club")
public class ClubController extends BaseController {

    @Autowired
    private IClubService clubService;

    /**
     * 获取社团列表
     */
    @GetMapping("/list")
    public AjaxResult list(Club club) {
        List<Club> list = clubService.selectClubList(club);
        return success(list);
    }

    /**
     * 获取社团列表(分页)
     */
    @GetMapping("/page")
    public TableDataInfo page(Club club) {
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
        return success(clubService.selectClubById(clubId));
    }

    /**
     * 申请创建社团
     */
    @PostMapping("/apply")
    public AjaxResult apply(@RequestBody Club club) {
        club.setCreateTime(new java.util.Date());
        club.setStatus("1"); // 1=Pending
        club.setDelFlag("0");
        // Assume logged in user is the creator. In real app, get user from
        // SecurityUtils.
        // For now, allow submit.
        return toAjax(clubService.insertClub(club));
    }

    /**
     * 申请加入社团
     */
    @PostMapping("/join")
    public AjaxResult join(@RequestBody Club club) {
        // In a real app, this would insert into club_member table
        // For now, we just return success
        return success("申请提交成功，请等待社长审核");
    }
}
