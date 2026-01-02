package com.ruoyi.web.controller.club;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
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
import com.ruoyi.user.domain.ClubAchievement;
import com.ruoyi.user.service.IClubAchievementService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 社团荣誉/成就Controller
 * 
 * @author ruoyi
 */
@RestController("sysClubAchievementController")
@RequestMapping("/system/achievement")
public class ClubAchievementController extends BaseController {
    @Autowired
    private IClubAchievementService clubAchievementService;

    /**
     * 查询社团荣誉/成就列表
     */
    @PreAuthorize("@ss.hasPermi('system:achievement:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClubAchievement clubAchievement) {
        startPage();
        List<ClubAchievement> list = clubAchievementService.selectClubAchievementList(clubAchievement);
        return getDataTable(list);
    }

    /**
     * 导出社团荣誉/成就列表
     */
    @PreAuthorize("@ss.hasPermi('system:achievement:export')")
    @Log(title = "社团荣誉/成就", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ClubAchievement clubAchievement) {
        List<ClubAchievement> list = clubAchievementService.selectClubAchievementList(clubAchievement);
        ExcelUtil<ClubAchievement> util = new ExcelUtil<ClubAchievement>(ClubAchievement.class);
        util.exportExcel(response, list, "社团荣誉/成就数据");
    }

    /**
     * 获取社团荣誉/成就详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:achievement:query')")
    @GetMapping(value = "/{achievementId}")
    public AjaxResult getInfo(@PathVariable("achievementId") Long achievementId) {
        return success(clubAchievementService.selectClubAchievementById(achievementId));
    }

    /**
     * 新增社团荣誉/成就
     */
    @PreAuthorize("@ss.hasPermi('system:achievement:add')")
    @Log(title = "社团荣誉/成就", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ClubAchievement clubAchievement) {
        clubAchievement.setCreateBy(getUsername());
        return toAjax(clubAchievementService.insertClubAchievement(clubAchievement));
    }

    /**
     * 修改社团荣誉/成就
     */
    @PreAuthorize("@ss.hasPermi('system:achievement:edit')")
    @Log(title = "社团荣誉/成就", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClubAchievement clubAchievement) {
        clubAchievement.setUpdateBy(getUsername());
        return toAjax(clubAchievementService.updateClubAchievement(clubAchievement));
    }

    /**
     * 删除社团荣誉/成就
     */
    @PreAuthorize("@ss.hasPermi('system:achievement:remove')")
    @Log(title = "社团荣誉/成就", businessType = BusinessType.DELETE)
    @DeleteMapping("/{achievementIds}")
    public AjaxResult remove(@PathVariable Long[] achievementIds) {
        return toAjax(clubAchievementService.deleteClubAchievementByIds(achievementIds));
    }
}
