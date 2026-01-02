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

    @Override
    public List<ClubActivity> selectClubActivityList(ClubActivity activity) {
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
    public void updateActivityStatusBasedOnTime() {
        clubActivityMapper.updateActivityStatusBasedOnTime();
    }
}
