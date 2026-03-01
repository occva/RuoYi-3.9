package com.ruoyi.user.service;

import java.util.List;
import com.ruoyi.user.domain.ClubActivity;

/**
 * 社团活动Service接口
 */
public interface IClubActivityService {
    List<ClubActivity> selectClubActivityList(ClubActivity activity);

    ClubActivity selectClubActivityById(Long activityId);

    List<ClubActivity> selectActivityByClubId(Long clubId);

    List<ClubActivity> selectMyRegisteredActivities(Long userId);

    int insertClubActivity(ClubActivity activity);

    int updateClubActivity(ClubActivity activity);

    int deleteClubActivityById(Long activityId);

    int deleteClubActivityByIds(Long[] activityIds);

    int incrementParticipantsIfAvailable(Long activityId);

    void updateActivityStatusBasedOnTime();

    /**
     * 统计社团进行中的活动数量
     */
    int countOngoingByClubId(Long clubId);

    /**
     * 获取活动统计数据
     */
    java.util.Map<String, Object> getStatData(String beginTime, String endTime);
}
