package com.ruoyi.user.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.user.domain.ClubActivityRegistration;
import com.ruoyi.user.mapper.ClubActivityRegistrationMapper;
import com.ruoyi.user.service.IClubActivityRegistrationService;

/**
 * 活动报名Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class ClubActivityRegistrationServiceImpl implements IClubActivityRegistrationService {
    @Autowired
    private ClubActivityRegistrationMapper clubActivityRegistrationMapper;

    @Override
    public ClubActivityRegistration selectClubActivityRegistrationById(Long registrationId) {
        return clubActivityRegistrationMapper.selectClubActivityRegistrationById(registrationId);
    }

    @Override
    public List<ClubActivityRegistration> selectClubActivityRegistrationList(ClubActivityRegistration registration) {
        return clubActivityRegistrationMapper.selectClubActivityRegistrationList(registration);
    }

    @Override
    public int insertClubActivityRegistration(ClubActivityRegistration registration) {
        registration.setCreateTime(DateUtils.getNowDate());
        registration.setRegistrationTime(DateUtils.getNowDate());
        return clubActivityRegistrationMapper.insertClubActivityRegistration(registration);
    }

    @Override
    public int updateClubActivityRegistration(ClubActivityRegistration registration) {
        registration.setUpdateTime(DateUtils.getNowDate());
        return clubActivityRegistrationMapper.updateClubActivityRegistration(registration);
    }

    @Override
    public int deleteClubActivityRegistrationByIds(Long[] registrationIds) {
        return clubActivityRegistrationMapper.deleteClubActivityRegistrationByIds(registrationIds);
    }

    @Override
    public int deleteClubActivityRegistrationById(Long registrationId) {
        return clubActivityRegistrationMapper.deleteClubActivityRegistrationById(registrationId);
    }
}
