package com.ruoyi.user.service;

import java.util.List;
import com.ruoyi.user.domain.ClubActivityRegistration;

/**
 * 活动报名Service接口
 * 
 * @author ruoyi
 */
public interface IClubActivityRegistrationService {
    public ClubActivityRegistration selectClubActivityRegistrationById(Long registrationId);

    public List<ClubActivityRegistration> selectClubActivityRegistrationList(ClubActivityRegistration registration);

    public int insertClubActivityRegistration(ClubActivityRegistration registration);

    public int updateClubActivityRegistration(ClubActivityRegistration registration);

    public int cancelActiveRegistration(Long activityId, Long userId);

    public int deleteClubActivityRegistrationByIds(Long[] registrationIds);

    public int deleteClubActivityRegistrationById(Long registrationId);
}
