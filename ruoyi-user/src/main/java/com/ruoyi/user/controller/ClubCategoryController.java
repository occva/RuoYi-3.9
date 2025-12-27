package com.ruoyi.user.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.user.domain.ClubCategory;
import com.ruoyi.user.service.IClubCategoryService;

/**
 * 社团分类Controller (用户端)
 */
@Anonymous
@RestController
@RequestMapping("/api/user/category")
public class ClubCategoryController extends BaseController {
    
    @Autowired
    private IClubCategoryService clubCategoryService;

    /**
     * 获取分类列表
     */
    @GetMapping("/list")
    public AjaxResult list(ClubCategory category) {
        List<ClubCategory> list = clubCategoryService.selectClubCategoryList(category);
        return success(list);
    }

    /**
     * 获取分类详情
     */
    @GetMapping("/{categoryId}")
    public AjaxResult getInfo(@PathVariable Long categoryId) {
        return success(clubCategoryService.selectClubCategoryById(categoryId));
    }
}
