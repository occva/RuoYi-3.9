package com.ruoyi.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.exception.ServiceException;
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

    @Autowired
    private ClubDataScopeHelper dataScopeHelper;

    @Override
    public ClubActivityRegistration selectClubActivityRegistrationById(Long registrationId) {
        ClubActivityRegistration record = clubActivityRegistrationMapper.selectClubActivityRegistrationById(registrationId);
        if (record == null) {
            return null;
        }
        List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds != null && !managedClubIds.contains(record.getClubId())) {
            return null;
        }
        return record;
    }

    @Override
    public List<ClubActivityRegistration> selectClubActivityRegistrationList(ClubActivityRegistration registration) {
        // 数据隔离：社长/副社长在管理端只能查看自己管理社团的报名记录
        List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds != null) {
            registration.getParams().put("clubIds", managedClubIds);
        }
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
        ClubActivityRegistration old = clubActivityRegistrationMapper
                .selectClubActivityRegistrationById(registration.getRegistrationId());
        if (old == null) {
            throw new ServiceException("报名记录不存在");
        }
        List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds != null && !managedClubIds.contains(old.getClubId())) {
            throw new ServiceException("无权操作该报名记录");
        }
        registration.setUpdateTime(DateUtils.getNowDate());
        return clubActivityRegistrationMapper.updateClubActivityRegistration(registration);
    }

    @Override
    public int cancelActiveRegistration(Long activityId, Long userId) {
        return clubActivityRegistrationMapper.cancelActiveRegistration(activityId, userId);
    }

    @Override
    public int deleteClubActivityRegistrationByIds(Long[] registrationIds) {
        List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds == null) {
            return clubActivityRegistrationMapper.deleteClubActivityRegistrationByIds(registrationIds);
        }

        List<Long> authorizedIds = new ArrayList<>();
        for (Long registrationId : registrationIds) {
            ClubActivityRegistration record = clubActivityRegistrationMapper.selectClubActivityRegistrationById(registrationId);
            if (record != null && managedClubIds.contains(record.getClubId())) {
                authorizedIds.add(registrationId);
            }
        }
        if (authorizedIds.isEmpty()) {
            throw new ServiceException("无可操作的报名记录");
        }
        if (authorizedIds.size() != registrationIds.length) {
            throw new ServiceException("包含无权限操作的报名记录");
        }
        return clubActivityRegistrationMapper.deleteClubActivityRegistrationByIds(authorizedIds.toArray(new Long[0]));
    }

    @Override
    public int deleteClubActivityRegistrationById(Long registrationId) {
        ClubActivityRegistration record = clubActivityRegistrationMapper.selectClubActivityRegistrationById(registrationId);
        if (record == null) {
            throw new ServiceException("报名记录不存在");
        }
        List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds != null && !managedClubIds.contains(record.getClubId())) {
            throw new ServiceException("无权操作该报名记录");
        }
        return clubActivityRegistrationMapper.deleteClubActivityRegistrationById(registrationId);
    }
}
