package com.ruoyi.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.user.domain.ClubActivityRegistration;

/**
 * 活动报名Mapper接口
 * 
 * @author ruoyi
 */
public interface ClubActivityRegistrationMapper {
    public ClubActivityRegistration selectClubActivityRegistrationById(Long registrationId);

    public List<ClubActivityRegistration> selectClubActivityRegistrationList(ClubActivityRegistration registration);

    public int insertClubActivityRegistration(ClubActivityRegistration registration);

    public int updateClubActivityRegistration(ClubActivityRegistration registration);

    public int cancelActiveRegistration(@Param("activityId") Long activityId, @Param("userId") Long userId);

    public int deleteClubActivityRegistrationById(Long registrationId);

    public int deleteClubActivityRegistrationByIds(Long[] registrationIds);
}
