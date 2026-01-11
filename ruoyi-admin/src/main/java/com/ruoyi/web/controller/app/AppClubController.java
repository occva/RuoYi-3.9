package com.ruoyi.web.controller.app;

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
     * TODO: 待实现业务逻辑：
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
     * TODO: 待实现业务逻辑：
     * 1. 验证社团是否存在且开放加入
     * 2. 验证用户是否已经是成员
     * 3. 插入 club_application 表作为入社申请
     */
    @PostMapping("/join")
    public AjaxResult join(@RequestBody Club club) {
        // 在实际应用中，这里应该插入到 club_application 表
        return success("申请提交成功，请等待社长审核");
    }
}
