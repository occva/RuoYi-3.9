package com.ruoyi.user.mapper;

import java.util.List;
import com.ruoyi.user.domain.ClubCategory;

/**
 * 社团分类Mapper接口
 */
public interface ClubCategoryMapper {
    List<ClubCategory> selectClubCategoryList(ClubCategory category);
    ClubCategory selectClubCategoryById(Long categoryId);
}
