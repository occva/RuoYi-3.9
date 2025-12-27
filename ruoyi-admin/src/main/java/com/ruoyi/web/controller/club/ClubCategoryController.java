package com.ruoyi.web.controller.club;

import java.util.List;
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
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.user.domain.ClubCategory;
import com.ruoyi.user.service.IClubCategoryService;

/**
 * 社团分类管理Controller
 * 
 * @author ruoyi
 */
@RestController("sysClubCategoryController")
@RequestMapping("/system/category")
public class ClubCategoryController extends BaseController {

    @Autowired
    private IClubCategoryService clubCategoryService;

    /**
     * 获取分类列表
     */
    @PreAuthorize("@ss.hasPermi('system:category:list')")
    @GetMapping("/list")
    public AjaxResult list(ClubCategory category) {
        List<ClubCategory> list = clubCategoryService.selectClubCategoryList(category);
        return success(list);
    }

    /**
     * 根据分类编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:category:query')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable Long categoryId) {
        return success(clubCategoryService.selectClubCategoryById(categoryId));
    }

    /**
     * 新增分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:add')")
    @Log(title = "社团分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ClubCategory category) {
        if (!clubCategoryService.checkCategoryCodeUnique(category)) {
            return error("新增分类'" + category.getCategoryName() + "'失败，分类编码已存在");
        }
        if (!clubCategoryService.checkCategoryNameUnique(category)) {
            return error("新增分类'" + category.getCategoryName() + "'失败，分类名称已存在");
        }
        category.setCreateBy(getUsername());
        return toAjax(clubCategoryService.insertClubCategory(category));
    }

    /**
     * 修改分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:edit')")
    @Log(title = "社团分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ClubCategory category) {
        if (!clubCategoryService.checkCategoryCodeUnique(category)) {
            return error("修改分类'" + category.getCategoryName() + "'失败，分类编码已存在");
        }
        if (!clubCategoryService.checkCategoryNameUnique(category)) {
            return error("修改分类'" + category.getCategoryName() + "'失败，分类名称已存在");
        }
        category.setUpdateBy(getUsername());
        return toAjax(clubCategoryService.updateClubCategory(category));
    }

    /**
     * 删除分类
     */
    @PreAuthorize("@ss.hasPermi('system:category:remove')")
    @Log(title = "社团分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds) {
        for (Long categoryId : categoryIds) {
            if (clubCategoryService.hasClubByCategory(categoryId)) {
                ClubCategory category = clubCategoryService.selectClubCategoryById(categoryId);
                return error("分类'" + category.getCategoryName() + "'下有社团，无法删除");
            }
        }
        return toAjax(clubCategoryService.deleteClubCategoryByIds(categoryIds));
    }
}
