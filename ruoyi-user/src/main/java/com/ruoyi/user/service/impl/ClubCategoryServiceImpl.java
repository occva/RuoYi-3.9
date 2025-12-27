package com.ruoyi.user.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.user.domain.ClubCategory;
import com.ruoyi.user.mapper.ClubCategoryMapper;
import com.ruoyi.user.service.IClubCategoryService;

/**
 * 社团分类Service实现
 */
@Service
public class ClubCategoryServiceImpl implements IClubCategoryService {
    
    @Autowired
    private ClubCategoryMapper clubCategoryMapper;

    @Override
    public List<ClubCategory> selectClubCategoryList(ClubCategory category) {
        return clubCategoryMapper.selectClubCategoryList(category);
    }

    @Override
    public ClubCategory selectClubCategoryById(Long categoryId) {
        return clubCategoryMapper.selectClubCategoryById(categoryId);
    }
}
