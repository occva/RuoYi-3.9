package com.ruoyi.user.service;

import java.util.List;
import com.ruoyi.user.domain.ClubAchievement;

/**
 * 社团荣誉/成就Service接口
 * 
 * @author ruoyi
 */
public interface IClubAchievementService {
    /**
     * 查询社团荣誉/成就
     * 
     * @param achievementId 社团荣誉/成就ID
     * @return 社团荣誉/成就
     */
    public ClubAchievement selectClubAchievementById(Long achievementId);

    /**
     * 查询社团荣誉/成就列表
     * 
     * @param clubAchievement 社团荣誉/成就
     * @return 社团荣誉/成就集合
     */
    public List<ClubAchievement> selectClubAchievementList(ClubAchievement clubAchievement);

    /**
     * 新增社团荣誉/成就
     * 
     * @param clubAchievement 社团荣誉/成就
     * @return 结果
     */
    public int insertClubAchievement(ClubAchievement clubAchievement);

    /**
     * 修改社团荣誉/成就
     * 
     * @param clubAchievement 社团荣誉/成就
     * @return 结果
     */
    public int updateClubAchievement(ClubAchievement clubAchievement);

    /**
     * 批量删除社团荣誉/成就
     * 
     * @param achievementIds 需要删除的社团荣誉/成就ID
     * @return 结果
     */
    public int deleteClubAchievementByIds(Long[] achievementIds);

    /**
     * 删除社团荣誉/成就信息
     * 
     * @param achievementId 社团荣誉/成就ID
     * @return 结果
     */
    public int deleteClubAchievementById(Long achievementId);
}
