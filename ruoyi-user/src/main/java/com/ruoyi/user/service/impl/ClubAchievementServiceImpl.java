package com.ruoyi.user.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.user.mapper.ClubAchievementMapper;
import com.ruoyi.user.domain.ClubAchievement;
import com.ruoyi.user.service.IClubAchievementService;

/**
 * 社团荣誉/成就Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class ClubAchievementServiceImpl implements IClubAchievementService {
    @Autowired
    private ClubAchievementMapper clubAchievementMapper;

    @Autowired
    private ClubDataScopeHelper dataScopeHelper;

    /**
     * 查询社团荣誉/成就
     * 
     * @param achievementId 社团荣誉/成就ID
     * @return 社团荣誉/成就
     */
    @Override
    public ClubAchievement selectClubAchievementById(Long achievementId) {
        return clubAchievementMapper.selectClubAchievementById(achievementId);
    }

    /**
     * 查询社团荣誉/成就列表
     * 
     * @param clubAchievement 社团荣誉/成就
     * @return 社团荣誉/成就
     */
    @Override
    public List<ClubAchievement> selectClubAchievementList(ClubAchievement clubAchievement) {
        // 数据隔离：社长/副社长只能看自己管理社团的荣誉
        java.util.List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds != null) {
            clubAchievement.getParams().put("clubIds", managedClubIds);
        }
        return clubAchievementMapper.selectClubAchievementList(clubAchievement);
    }

    /**
     * 新增社团荣誉/成就
     * 
     * @param clubAchievement 社团荣誉/成就
     * @return 结果
     */
    @Override
    public int insertClubAchievement(ClubAchievement clubAchievement) {
        clubAchievement.setCreateTime(DateUtils.getNowDate());
        return clubAchievementMapper.insertClubAchievement(clubAchievement);
    }

    /**
     * 修改社团荣誉/成就
     * 
     * @param clubAchievement 社团荣誉/成就
     * @return 结果
     */
    @Override
    public int updateClubAchievement(ClubAchievement clubAchievement) {
        clubAchievement.setUpdateTime(DateUtils.getNowDate());
        return clubAchievementMapper.updateClubAchievement(clubAchievement);
    }

    /**
     * 批量删除社团荣誉/成就
     * 
     * @param achievementIds 需要删除的社团荣誉/成就ID
     * @return 结果
     */
    @Override
    public int deleteClubAchievementByIds(Long[] achievementIds) {
        return clubAchievementMapper.deleteClubAchievementByIds(achievementIds);
    }

    /**
     * 删除社团荣誉/成就信息
     * 
     * @param achievementId 社团荣誉/成就ID
     * @return 结果
     */
    @Override
    public int deleteClubAchievementById(Long achievementId) {
        return clubAchievementMapper.deleteClubAchievementById(achievementId);
    }
}
