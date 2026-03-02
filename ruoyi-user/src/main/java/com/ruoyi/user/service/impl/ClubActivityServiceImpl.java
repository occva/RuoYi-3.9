package com.ruoyi.user.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.user.domain.ClubActivity;
import com.ruoyi.user.mapper.ClubActivityMapper;
import com.ruoyi.user.service.IClubActivityService;

/**
 * 社团活动Service实现
 */
@Service
public class ClubActivityServiceImpl implements IClubActivityService {

    @Autowired
    private ClubActivityMapper clubActivityMapper;

    @Autowired
    private ClubDataScopeHelper dataScopeHelper;

    @Override
    public List<ClubActivity> selectClubActivityList(ClubActivity activity) {
        // 数据隔离：社长/副社长只能看自己管理社团的活动
        java.util.List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds != null) {
            activity.getParams().put("clubIds", managedClubIds);
        }
        return clubActivityMapper.selectClubActivityList(activity);
    }

    @Override
    public ClubActivity selectClubActivityById(Long activityId) {
        return clubActivityMapper.selectClubActivityById(activityId);
    }

    @Override
    public List<ClubActivity> selectActivityByClubId(Long clubId) {
        return clubActivityMapper.selectActivityByClubId(clubId);
    }

    @Override
    public List<ClubActivity> selectMyRegisteredActivities(Long userId) {
        return clubActivityMapper.selectMyRegisteredActivities(userId);
    }

    @Override
    public int insertClubActivity(ClubActivity activity) {
        return clubActivityMapper.insertClubActivity(activity);
    }

    @Override
    public int updateClubActivity(ClubActivity activity) {
        return clubActivityMapper.updateClubActivity(activity);
    }

    @Override
    public int deleteClubActivityById(Long activityId) {
        return clubActivityMapper.deleteClubActivityById(activityId);
    }

    @Override
    public int deleteClubActivityByIds(Long[] activityIds) {
        return clubActivityMapper.deleteClubActivityByIds(activityIds);
    }

    @Override
    public int incrementParticipantsIfAvailable(Long activityId) {
        return clubActivityMapper.incrementParticipantsIfAvailable(activityId);
    }

    @Override
    public int decrementParticipants(Long activityId) {
        return clubActivityMapper.decrementParticipants(activityId);
    }

    @Override
    public void updateActivityStatusBasedOnTime() {
        clubActivityMapper.updateActivityStatusBasedOnTime();
    }

    @Override
    public int countOngoingByClubId(Long clubId) {
        return clubActivityMapper.countOngoingByClubId(clubId);
    }

    @Override
    public java.util.Map<String, Object> getStatData(String beginTime, String endTime) {
        // 数据隔离：社长/副社长只能看自己管理社团的活动统计
        java.util.List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("statusStat", clubActivityMapper.selectActivityStatusStat(beginTime, endTime, managedClubIds));
        map.put("todayStats", clubActivityMapper.selectActivityTodayStats(managedClubIds));
        map.put("trendStat", clubActivityMapper.selectActivityTrendStat(managedClubIds));
        map.put("typeStat", clubActivityMapper.selectActivityTypeStat(managedClubIds));
        map.put("clubRanking", clubActivityMapper.selectActivityClubRanking(managedClubIds));
        return map;
    }
}
