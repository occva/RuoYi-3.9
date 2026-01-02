package com.ruoyi.user.mapper;

import java.util.List;
import com.ruoyi.user.domain.ClubActivity;

/**
 * 社团活动Mapper接口
 */
public interface ClubActivityMapper {
    List<ClubActivity> selectClubActivityList(ClubActivity activity);

    ClubActivity selectClubActivityById(Long activityId);

    List<ClubActivity> selectActivityByClubId(Long clubId);

    int insertClubActivity(ClubActivity activity);

    int updateClubActivity(ClubActivity activity);

    int deleteClubActivityById(Long activityId);

    int deleteClubActivityByIds(Long[] activityIds);

    int updateActivityStatusBasedOnTime();
}
