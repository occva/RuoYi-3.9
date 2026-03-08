package com.ruoyi.web.controller.club;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.ruoyi.user.domain.ClubActivityRegistration;
import com.ruoyi.user.service.IClubActivityRegistrationService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 活动报名Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/club/registration")
public class ClubActivityRegistrationController extends BaseController {
    @Autowired
    private IClubActivityRegistrationService clubActivityRegistrationService;

    /**
     * 查询活动报名列表
     */
    @PreAuthorize("@ss.hasPermi('club:registration:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClubActivityRegistration registration) {
        // 签到管理列表按签到时间排序：已签到在前，且最新签到优先
        registration.getParams().put("sortMode", "CHECKIN_LATEST");
        startPage();
        List<ClubActivityRegistration> list = clubActivityRegistrationService
                .selectClubActivityRegistrationList(registration);
        return getDataTable(list);
    }

    /**
     * 获取活动报名详细信息
     */
    @PreAuthorize("@ss.hasPermi('club:registration:query')")
    @GetMapping(value = "/{registrationId}")
    public AjaxResult getInfo(@PathVariable("registrationId") Long registrationId) {
        return success(clubActivityRegistrationService.selectClubActivityRegistrationById(registrationId));
    }

    /**
     * 修改活动报名
     */
    @PreAuthorize("@ss.hasPermi('club:registration:edit')")
    @Log(title = "活动报名", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClubActivityRegistration registration) {
        return toAjax(clubActivityRegistrationService.updateClubActivityRegistration(registration));
    }

    /**
     * 签到
     */
    @PreAuthorize("@ss.hasPermi('club:registration:edit')")
    @Log(title = "活动签到", businessType = BusinessType.UPDATE)
    @PutMapping("/checkin/{registrationId}")
    public AjaxResult checkin(@PathVariable Long registrationId) {
        ClubActivityRegistration registration = clubActivityRegistrationService
                .selectClubActivityRegistrationById(registrationId);
        if (registration == null) {
            return error("报名记录不存在");
        }
        if ("2".equals(registration.getStatus())) {
            return error("该报名记录已取消，无法签到");
        }
        if ("1".equals(registration.getCheckInStatus())) {
            return success("该成员已签到");
        }
        registration.setCheckInStatus("1"); // 已签到
        registration.setCheckInTime(new java.util.Date());
        registration.setStatus("1"); // 已参加
        return toAjax(clubActivityRegistrationService.updateClubActivityRegistration(registration));
    }

    /**
     * 删除活动报名
     */
    @PreAuthorize("@ss.hasPermi('club:registration:remove')")
    @Log(title = "活动报名", businessType = BusinessType.DELETE)
    @DeleteMapping("/{registrationIds}")
    public AjaxResult remove(@PathVariable Long[] registrationIds) {
        return toAjax(clubActivityRegistrationService.deleteClubActivityRegistrationByIds(registrationIds));
    }
}
