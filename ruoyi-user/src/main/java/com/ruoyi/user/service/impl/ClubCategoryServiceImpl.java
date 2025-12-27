package com.ruoyi.user.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.user.domain.ClubCategory;
import com.ruoyi.user.mapper.ClubCategoryMapper;
import com.ruoyi.user.mapper.ClubMapper;
import com.ruoyi.user.service.IClubCategoryService;

/**
 * 社团分类Service实现
 */
@Service
public class ClubCategoryServiceImpl implements IClubCategoryService {

    @Autowired
    private ClubCategoryMapper clubCategoryMapper;

    @Autowired
    private ClubMapper clubMapper;

    @Override
    public List<ClubCategory> selectClubCategoryList(ClubCategory category) {
        return clubCategoryMapper.selectClubCategoryList(category);
    }

    @Override
    public ClubCategory selectClubCategoryById(Long categoryId) {
        return clubCategoryMapper.selectClubCategoryById(categoryId);
    }

    @Override
    public int insertClubCategory(ClubCategory category) {
        return clubCategoryMapper.insertClubCategory(category);
    }

    @Override
    public int updateClubCategory(ClubCategory category) {
        return clubCategoryMapper.updateClubCategory(category);
    }

    @Override
    public int deleteClubCategoryById(Long categoryId) {
        return clubCategoryMapper.deleteClubCategoryById(categoryId);
    }

    @Override
    public int deleteClubCategoryByIds(Long[] categoryIds) {
        return clubCategoryMapper.deleteClubCategoryByIds(categoryIds);
    }

    @Override
    public boolean checkCategoryCodeUnique(ClubCategory category) {
        Long categoryId = StringUtils.isNull(category.getCategoryId()) ? -1L : category.getCategoryId();
        ClubCategory info = clubCategoryMapper.checkCategoryCodeUnique(category.getCategoryCode());
        if (StringUtils.isNotNull(info) && info.getCategoryId().longValue() != categoryId.longValue()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkCategoryNameUnique(ClubCategory category) {
        Long categoryId = StringUtils.isNull(category.getCategoryId()) ? -1L : category.getCategoryId();
        ClubCategory info = clubCategoryMapper.checkCategoryNameUnique(category.getCategoryName());
        if (StringUtils.isNotNull(info) && info.getCategoryId().longValue() != categoryId.longValue()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean hasClubByCategory(Long categoryId) {
        int count = clubMapper.countClubByCategoryId(categoryId);
        return count > 0;
    }
}
