package com.ruoyi.user.service;

import java.util.List;
import com.ruoyi.user.domain.ClubCategory;

/**
 * 社团分类Service接口
 */
public interface IClubCategoryService {
    /**
     * 查询分类列表
     */
    List<ClubCategory> selectClubCategoryList(ClubCategory category);

    /**
     * 根据ID查询分类
     */
    ClubCategory selectClubCategoryById(Long categoryId);

    /**
     * 新增分类
     */
    int insertClubCategory(ClubCategory category);

    /**
     * 修改分类
     */
    int updateClubCategory(ClubCategory category);

    /**
     * 删除分类
     */
    int deleteClubCategoryById(Long categoryId);

    /**
     * 批量删除分类
     */
    int deleteClubCategoryByIds(Long[] categoryIds);

    /**
     * 校验分类编码是否唯一
     */
    boolean checkCategoryCodeUnique(ClubCategory category);

    /**
     * 校验分类名称是否唯一
     */
    boolean checkCategoryNameUnique(ClubCategory category);

    /**
     * 检查分类下是否有社团
     */
    boolean hasClubByCategory(Long categoryId);
}
