package com.ruoyi.web.controller.club;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
import com.ruoyi.user.domain.Club;
import com.ruoyi.user.service.IClubService;

/**
 * 社团管理Controller
 * 
 * @author ruoyi
 */
@RestController("sysClubController")
@RequestMapping("/system/club")
public class ClubController extends BaseController {

    @Autowired
    private IClubService clubService;

    /**
     * 获取社团列表
     */
    @PreAuthorize("@ss.hasPermi('system:club:list')")
    @GetMapping("/list")
    public TableDataInfo list(Club club) {
        startPage();
        List<Club> list = clubService.selectClubList(club);
        return getDataTable(list);
    }

    /**
     * 导出社团列表
     */
    @PreAuthorize("@ss.hasPermi('system:club:export')")
    @Log(title = "社团管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Club club) {
        List<Club> list = clubService.selectClubList(club);
        ExcelUtil<Club> util = new ExcelUtil<Club>(Club.class);
        util.exportExcel(response, list, "社团数据");
    }

    /**
     * 根据社团编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:club:query')")
    @GetMapping(value = "/{clubId}")
    public AjaxResult getInfo(@PathVariable Long clubId) {
        return success(clubService.selectClubById(clubId));
    }

    /**
     * 新增社团
     */
    @PreAuthorize("@ss.hasPermi('system:club:add')")
    @Log(title = "社团管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Club club) {
        if (!clubService.checkClubNameUnique(club)) {
            return error("新增社团'" + club.getClubName() + "'失败，社团名称已存在");
        }
        if (club.getClubCode() != null && !club.getClubCode().isEmpty() && !clubService.checkClubCodeUnique(club)) {
            return error("新增社团'" + club.getClubName() + "'失败，社团编码已存在");
        }
        club.setCreateBy(getUsername());
        return toAjax(clubService.insertClub(club));
    }

    /**
     * 修改社团
     */
    @PreAuthorize("@ss.hasPermi('system:club:edit')")
    @Log(title = "社团管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Club club) {
        if (!clubService.checkClubNameUnique(club)) {
            return error("修改社团'" + club.getClubName() + "'失败，社团名称已存在");
        }
        if (club.getClubCode() != null && !club.getClubCode().isEmpty() && !clubService.checkClubCodeUnique(club)) {
            return error("修改社团'" + club.getClubName() + "'失败，社团编码已存在");
        }
        club.setUpdateBy(getUsername());
        return toAjax(clubService.updateClub(club));
    }

    /**
     * 删除社团
     */
    @PreAuthorize("@ss.hasPermi('system:club:remove')")
    @Log(title = "社团管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{clubIds}")
    public AjaxResult remove(@PathVariable Long[] clubIds) {
        return toAjax(clubService.deleteClubByIds(clubIds));
    }

    /**
     * 修改社团状态
     */
    @PreAuthorize("@ss.hasPermi('system:club:edit')")
    @Log(title = "社团管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody Club club) {
        return toAjax(clubService.updateClubStatus(new Long[] { club.getClubId() }, club.getStatus(), getUsername()));
    }

    /**
     * 批量修改社团状态
     */
    @PreAuthorize("@ss.hasPermi('system:club:edit')")
    @Log(title = "社团管理", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult changeStatusBatch(@RequestBody ClubStatusRequest request) {
        return toAjax(clubService.updateClubStatus(request.getClubIds(), request.getStatus(), getUsername()));
    }

    /**
     * 批量设置热门社团
     */
    @PreAuthorize("@ss.hasPermi('system:club:edit')")
    @Log(title = "社团管理", businessType = BusinessType.UPDATE)
    @PutMapping("/popular")
    public AjaxResult changePopular(@RequestBody ClubPopularRequest request) {
        return toAjax(clubService.updateClubPopular(request.getClubIds(), request.getIsPopular(), getUsername()));
    }

    /**
     * 获取社团统计数据
     */
    @PreAuthorize("@ss.hasPermi('system:club:list')")
    @GetMapping("/stat")
    public AjaxResult stat(@org.springframework.web.bind.annotation.RequestParam(required = false) String beginTime,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String endTime) {
        return success(clubService.getStatData(beginTime, endTime));
    }

    /**
     * 状态修改请求体
     */
    public static class ClubStatusRequest {
        private Long[] clubIds;
        private String status;

        public Long[] getClubIds() {
            return clubIds;
        }

        public void setClubIds(Long[] clubIds) {
            this.clubIds = clubIds;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    /**
     * 热门设置请求体
     */
    public static class ClubPopularRequest {
        private Long[] clubIds;
        private String isPopular;

        public Long[] getClubIds() {
            return clubIds;
        }

        public void setClubIds(Long[] clubIds) {
            this.clubIds = clubIds;
        }

        public String getIsPopular() {
            return isPopular;
        }

        public void setIsPopular(String isPopular) {
            this.isPopular = isPopular;
        }
    }
}
