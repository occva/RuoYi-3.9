package com.ruoyi.web.controller.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 用户端功能开关接口
 */
@Anonymous
@RestController
@RequestMapping("/api/app/features")
public class AppFeatureController extends BaseController {

    @Value("${ai.chat.enabled:false}")
    private boolean aiChatEnabled;

    /**
     * 查询用户端可用功能
     */
    @GetMapping
    public AjaxResult features() {
        AjaxResult ajax = success();
        ajax.put("aiChatEnabled", aiChatEnabled);
        return ajax;
    }
}
