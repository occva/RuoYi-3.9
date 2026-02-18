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
import com.ruoyi.user.domain.ClubApplication;
import com.ruoyi.user.service.IClubApplicationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 入社申请Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/club/application")
public class ClubApplicationController extends BaseController {
    @Autowired
    private IClubApplicationService clubApplicationService;

    /**
     * 查询入社申请列表
     */
    @PreAuthorize("@ss.hasPermi('club:application:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClubApplication clubApplication) {
        startPage();
        List<ClubApplication> list = clubApplicationService.selectClubApplicationList(clubApplication);
        return getDataTable(list);
    }

    /**
     * 导出入社申请列表
     */
    @PreAuthorize("@ss.hasPermi('club:application:export')")
    @Log(title = "入社申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ClubApplication clubApplication) {
        List<ClubApplication> list = clubApplicationService.selectClubApplicationList(clubApplication);
        ExcelUtil<ClubApplication> util = new ExcelUtil<ClubApplication>(ClubApplication.class);
        util.exportExcel(response, list, "入社申请数据");
    }

    /**
     * 获取入社申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('club:application:query')")
    @GetMapping(value = "/{applicationId}")
    public AjaxResult getInfo(@PathVariable("applicationId") Long applicationId) {
        return success(clubApplicationService.selectClubApplicationById(applicationId));
    }

    /**
     * 审核入社申请
     */
    @PreAuthorize("@ss.hasPermi('club:application:review')")
    @Log(title = "审核申请", businessType = BusinessType.UPDATE)
    @PutMapping("/review")
    public AjaxResult review(@RequestBody ClubApplication clubApplication) {
        clubApplication.setUpdateBy(getUsername());
        clubApplication.setReviewerId(getUserId());
        clubApplication.setReviewerName(getLoginUser().getUser().getNickName());
        return toAjax(clubApplicationService.reviewApplication(clubApplication));
    }

    /**
     * 删除入社申请
     */
    @PreAuthorize("@ss.hasPermi('club:application:remove')")
    @Log(title = "入社申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{applicationIds}")
    public AjaxResult remove(@PathVariable Long[] applicationIds) {
        return toAjax(clubApplicationService.deleteClubApplicationByIds(applicationIds));
    }

    /**
     * 获取申请统计数据
     */
    @PreAuthorize("@ss.hasPermi('club:application:list')")
    @GetMapping("/stat")
    public AjaxResult stat(@org.springframework.web.bind.annotation.RequestParam(required = false) String beginTime,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String endTime) {
        return success(clubApplicationService.getStatData(beginTime, endTime));
    }
}
