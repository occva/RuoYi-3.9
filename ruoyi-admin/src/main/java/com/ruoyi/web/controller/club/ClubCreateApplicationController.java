package com.ruoyi.web.controller.club;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.user.domain.ClubCreateApplication;
import com.ruoyi.user.service.IClubCreateApplicationService;

/**
 * 新社团申请管理 Controller
 */
@RestController
@RequestMapping("/system/clubApply")
public class ClubCreateApplicationController extends BaseController {

    @Autowired
    private IClubCreateApplicationService clubCreateApplicationService;

    @PreAuthorize("@ss.hasPermi('club:createApply:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClubCreateApplication query) {
        startPage();
        List<ClubCreateApplication> list = clubCreateApplicationService.selectClubCreateApplicationList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('club:createApply:export')")
    @Log(title = "新社团申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ClubCreateApplication query) {
        List<ClubCreateApplication> list = clubCreateApplicationService.selectClubCreateApplicationList(query);
        ExcelUtil<ClubCreateApplication> util = new ExcelUtil<>(ClubCreateApplication.class);
        util.exportExcel(response, list, "新社团申请数据");
    }

    @PreAuthorize("@ss.hasPermi('club:createApply:query')")
    @GetMapping("/{applyId}")
    public AjaxResult getInfo(@PathVariable Long applyId) {
        return success(clubCreateApplicationService.selectClubCreateApplicationById(applyId));
    }

    @PreAuthorize("@ss.hasPermi('club:createApply:review')")
    @Log(title = "新社团申请审核", businessType = BusinessType.UPDATE)
    @PutMapping("/review")
    public AjaxResult review(@RequestBody ClubCreateApplication application) {
        application.setUpdateBy(getUsername());
        application.setReviewerId(getUserId());
        application.setReviewerName(getLoginUser().getUser().getNickName());
        return toAjax(clubCreateApplicationService.reviewClubCreateApplication(application));
    }

    @PreAuthorize("@ss.hasPermi('club:createApply:remove')")
    @Log(title = "新社团申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{applyIds}")
    public AjaxResult remove(@PathVariable Long[] applyIds) {
        return toAjax(clubCreateApplicationService.deleteClubCreateApplicationByIds(applyIds));
    }
}
