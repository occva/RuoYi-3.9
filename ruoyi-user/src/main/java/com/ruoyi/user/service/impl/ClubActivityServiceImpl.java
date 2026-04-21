package com.ruoyi.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
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
        ClubActivity activity = clubActivityMapper.selectClubActivityById(activityId);
        if (activity == null) {
            return null;
        }
        if (!dataScopeHelper.isManagedClub(activity.getClubId())) {
            throw new ServiceException("无权查看该社团活动");
        }
        return activity;
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
        validateCreateClubId(activity.getClubId());
        return clubActivityMapper.insertClubActivity(activity);
    }

    @Override
    public int updateClubActivity(ClubActivity activity) {
        ClubActivity existing = requireActivity(activity.getActivityId());
        if (!dataScopeHelper.isManagedClub(existing.getClubId())) {
            throw new ServiceException("无权操作该社团活动");
        }
        if (dataScopeHelper.getManagedClubIds() != null
                && activity.getClubId() != null
                && !activity.getClubId().equals(existing.getClubId())) {
            throw new ServiceException("不允许修改活动所属社团");
        }
        return clubActivityMapper.updateClubActivity(activity);
    }

    @Override
    public int deleteClubActivityById(Long activityId) {
        ClubActivity existing = requireActivity(activityId);
        if (!dataScopeHelper.isManagedClub(existing.getClubId())) {
            throw new ServiceException("无权操作该社团活动");
        }
        return clubActivityMapper.deleteClubActivityById(activityId);
    }

    @Override
    public int deleteClubActivityByIds(Long[] activityIds) {
        List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds == null) {
            return clubActivityMapper.deleteClubActivityByIds(activityIds);
        }

        List<Long> authorizedIds = new ArrayList<>();
        for (Long activityId : activityIds) {
            ClubActivity activity = clubActivityMapper.selectClubActivityById(activityId);
            if (activity != null && managedClubIds.contains(activity.getClubId())) {
                authorizedIds.add(activityId);
            }
        }
        if (authorizedIds.size() != activityIds.length) {
            throw new ServiceException("包含不存在或无权限操作的活动");
        }
        return clubActivityMapper.deleteClubActivityByIds(authorizedIds.toArray(new Long[0]));
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

    private ClubActivity requireActivity(Long activityId) {
        ClubActivity activity = clubActivityMapper.selectClubActivityById(activityId);
        if (activity == null) {
            throw new ServiceException("活动不存在");
        }
        return activity;
    }

    private void validateCreateClubId(Long clubId) {
        if (clubId == null) {
            throw new ServiceException("所属社团不能为空");
        }
        if (!dataScopeHelper.isManagedClub(clubId)) {
            throw new ServiceException("无权为其他社团创建活动");
        }
    }
}
