package com.ruoyi.web.controller.system;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.domain.FooterConfig;
import com.ruoyi.web.service.FooterConfigService;

/**
 * 后台页脚配置
 */
@RestController
@RequestMapping("/system/footer")
public class SysFooterConfigController extends BaseController {

    private final FooterConfigService footerConfigService;

    public SysFooterConfigController(FooterConfigService footerConfigService) {
        this.footerConfigService = footerConfigService;
    }

    @PreAuthorize("@ss.hasPermi('system:footer:query')")
    @GetMapping
    public AjaxResult getInfo() {
        return success(footerConfigService.getFooterConfig());
    }

    @PreAuthorize("@ss.hasPermi('system:footer:edit')")
    @Log(title = "页脚配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FooterConfig footerConfig) {
        return success(footerConfigService.saveFooterConfig(footerConfig, getUsername()));
    }
}
