package com.ruoyi.user.service;

import java.util.List;
import com.ruoyi.user.domain.ClubCategory;

/**
 * 社团分类Service接口
 */
public interface IClubCategoryService {
    List<ClubCategory> selectClubCategoryList(ClubCategory category);
    ClubCategory selectClubCategoryById(Long categoryId);
}
