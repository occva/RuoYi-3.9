package com.ruoyi.user.service;

import java.util.List;
import com.ruoyi.user.domain.ClubCreateApplication;

/**
 * 新社团申请 Service
 */
public interface IClubCreateApplicationService {
    ClubCreateApplication selectClubCreateApplicationById(Long applyId);

    List<ClubCreateApplication> selectClubCreateApplicationList(ClubCreateApplication query);

    int insertClubCreateApplication(ClubCreateApplication application);

    int updateClubCreateApplication(ClubCreateApplication application);

    int reviewClubCreateApplication(ClubCreateApplication application);

    int deleteClubCreateApplicationByIds(Long[] applyIds);

    int deleteClubCreateApplicationById(Long applyId);
}
