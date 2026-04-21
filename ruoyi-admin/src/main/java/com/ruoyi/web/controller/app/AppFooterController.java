package com.ruoyi.web.controller.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.service.FooterConfigService;

/**
 * 用户端页脚配置接口
 */
@Anonymous
@RestController
@RequestMapping("/api/app/footer")
public class AppFooterController extends BaseController {

    private final FooterConfigService footerConfigService;

    public AppFooterController(FooterConfigService footerConfigService) {
        this.footerConfigService = footerConfigService;
    }

    @GetMapping
    public AjaxResult getInfo() {
        return success(footerConfigService.getFooterConfig());
    }
}
