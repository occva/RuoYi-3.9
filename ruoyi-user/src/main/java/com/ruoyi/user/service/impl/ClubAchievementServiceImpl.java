package com.ruoyi.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
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
        ClubAchievement achievement = clubAchievementMapper.selectClubAchievementById(achievementId);
        if (achievement == null) {
            return null;
        }
        if (!dataScopeHelper.isManagedClub(achievement.getClubId())) {
            throw new ServiceException("无权查看该社团荣誉");
        }
        return achievement;
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
        validateCreateClubId(clubAchievement.getClubId());
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
        ClubAchievement existing = requireAchievement(clubAchievement.getAchievementId());
        if (!dataScopeHelper.isManagedClub(existing.getClubId())) {
            throw new ServiceException("无权操作该社团荣誉");
        }
        if (dataScopeHelper.getManagedClubIds() != null
                && clubAchievement.getClubId() != null
                && !clubAchievement.getClubId().equals(existing.getClubId())) {
            throw new ServiceException("不允许修改荣誉所属社团");
        }
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
        List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds == null) {
            return clubAchievementMapper.deleteClubAchievementByIds(achievementIds);
        }

        List<Long> authorizedIds = new ArrayList<>();
        for (Long achievementId : achievementIds) {
            ClubAchievement achievement = clubAchievementMapper.selectClubAchievementById(achievementId);
            if (achievement != null && managedClubIds.contains(achievement.getClubId())) {
                authorizedIds.add(achievementId);
            }
        }
        if (authorizedIds.size() != achievementIds.length) {
            throw new ServiceException("包含不存在或无权限操作的荣誉记录");
        }
        return clubAchievementMapper.deleteClubAchievementByIds(authorizedIds.toArray(new Long[0]));
    }

    /**
     * 删除社团荣誉/成就信息
     * 
     * @param achievementId 社团荣誉/成就ID
     * @return 结果
     */
    @Override
    public int deleteClubAchievementById(Long achievementId) {
        ClubAchievement existing = requireAchievement(achievementId);
        if (!dataScopeHelper.isManagedClub(existing.getClubId())) {
            throw new ServiceException("无权操作该社团荣誉");
        }
        return clubAchievementMapper.deleteClubAchievementById(achievementId);
    }

    private ClubAchievement requireAchievement(Long achievementId) {
        ClubAchievement achievement = clubAchievementMapper.selectClubAchievementById(achievementId);
        if (achievement == null) {
            throw new ServiceException("荣誉记录不存在");
        }
        return achievement;
    }

    private void validateCreateClubId(Long clubId) {
        if (clubId == null) {
            throw new ServiceException("所属社团不能为空");
        }
        if (!dataScopeHelper.isManagedClub(clubId)) {
            throw new ServiceException("无权为其他社团创建荣誉");
        }
    }
}
