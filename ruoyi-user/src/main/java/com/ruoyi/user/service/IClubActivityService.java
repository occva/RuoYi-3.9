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
}
