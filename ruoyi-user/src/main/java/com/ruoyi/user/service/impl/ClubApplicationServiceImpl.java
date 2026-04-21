package com.ruoyi.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.user.domain.ClubApplication;
import com.ruoyi.user.domain.ClubMember;
import com.ruoyi.user.mapper.ClubApplicationMapper;
import com.ruoyi.user.mapper.ClubMemberMapper;
import com.ruoyi.user.service.IClubApplicationService;

/**
 * 入社申请Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class ClubApplicationServiceImpl implements IClubApplicationService {
    @Autowired
    private ClubApplicationMapper clubApplicationMapper;

    @Autowired
    private ClubMemberMapper clubMemberMapper;

    @Autowired
    private ClubDataScopeHelper dataScopeHelper;

    /**
     * 查询入社申请
     * 
     * @param applicationId 入社申请ID
     * @return 入社申请
     */
    @Override
    public ClubApplication selectClubApplicationById(Long applicationId) {
        ClubApplication application = clubApplicationMapper.selectClubApplicationById(applicationId);
        if (application == null) {
            return null;
        }
        ensureApplicationInScope(application, "无权查看该入社申请");
        return application;
    }

    /**
     * 查询入社申请列表
     * 
     * @param clubApplication 入社申请
     * @return 入社申请
     */
    @Override
    public List<ClubApplication> selectClubApplicationList(ClubApplication clubApplication) {
        // 数据隔离：社长/副社长只能看自己管理社团的申请
        java.util.List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds != null) {
            clubApplication.getParams().put("clubIds", managedClubIds);
        }
        return clubApplicationMapper.selectClubApplicationList(clubApplication);
    }

    @Override
    public boolean hasPendingApplication(Long clubId, Long userId) {
        if (clubId == null || userId == null) {
            return false;
        }
        return clubApplicationMapper.existsPendingApplication(clubId, userId) > 0;
    }

    /**
     * 新增入社申请
     * 
     * @param clubApplication 入社申请
     * @return 结果
     */
    @Override
    public int insertClubApplication(ClubApplication clubApplication) {
        clubApplication.setCreateTime(DateUtils.getNowDate());
        clubApplication.setApplicationTime(DateUtils.getNowDate());
        clubApplication.setStatus("0"); // 待审核
        return clubApplicationMapper.insertClubApplication(clubApplication);
    }

    /**
     * 修改入社申请
     * 
     * @param clubApplication 入社申请
     * @return 结果
     */
    @Override
    public int updateClubApplication(ClubApplication clubApplication) {
        ClubApplication existing = requireApplication(clubApplication.getApplicationId());
        ensureApplicationInScope(existing, "无权操作该入社申请");
        if (dataScopeHelper.getManagedClubIds() != null
                && clubApplication.getClubId() != null
                && !clubApplication.getClubId().equals(existing.getClubId())) {
            throw new ServiceException("不允许修改申请所属社团");
        }
        clubApplication.setUpdateTime(DateUtils.getNowDate());
        return clubApplicationMapper.updateClubApplication(clubApplication);
    }

    /**
     * 审核并处理入社申请
     */
    @Override
    @Transactional
    public int reviewApplication(ClubApplication clubApplication) {
        ClubApplication existing = requireApplication(clubApplication.getApplicationId());
        ensureApplicationInScope(existing, "无权审核该入社申请");
        if (dataScopeHelper.getManagedClubIds() != null
                && clubApplication.getClubId() != null
                && !clubApplication.getClubId().equals(existing.getClubId())) {
            throw new ServiceException("不允许修改申请所属社团");
        }
        clubApplication.setReviewTime(DateUtils.getNowDate());
        clubApplication.setUpdateTime(DateUtils.getNowDate());

        int row = clubApplicationMapper.updateClubApplication(clubApplication);

        // 如果审核状态为通过 (1)，则自动加入社团成员表
        if (row > 0 && "1".equals(clubApplication.getStatus())) {
            ClubApplication fullApp = clubApplicationMapper
                    .selectClubApplicationById(clubApplication.getApplicationId());

            ClubMember member = new ClubMember();
            member.setClubId(fullApp.getClubId());
            member.setUserId(fullApp.getUserId());
            member.setUserName(fullApp.getUserName());
            member.setNickName(fullApp.getNickName());
            member.setStudentId(fullApp.getStudentId());
            member.setJoinDate(new Date());
            member.setRoleType("3"); // 默认为普通成员
            member.setStatus("0"); // 正常
            member.setCreateBy(clubApplication.getUpdateBy());
            member.setCreateTime(new Date());

            // 检查是否已经是成员，避免重复插入
            if (clubMemberMapper.existsActiveMember(fullApp.getClubId(), fullApp.getUserId()) <= 0) {
                clubMemberMapper.insertClubMember(member);
            }
        }

        return row;
    }

    /**
     * 批量删除入社申请
     * 
     * @param applicationIds 需要删除的入社申请ID
     * @return 结果
     */
    @Override
    public int deleteClubApplicationByIds(Long[] applicationIds) {
        List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds == null) {
            return clubApplicationMapper.deleteClubApplicationByIds(applicationIds);
        }

        List<Long> authorizedIds = new ArrayList<>();
        for (Long applicationId : applicationIds) {
            ClubApplication application = clubApplicationMapper.selectClubApplicationById(applicationId);
            if (application != null && managedClubIds.contains(application.getClubId())) {
                authorizedIds.add(applicationId);
            }
        }
        if (authorizedIds.size() != applicationIds.length) {
            throw new ServiceException("包含不存在或无权限操作的入社申请");
        }
        return clubApplicationMapper.deleteClubApplicationByIds(authorizedIds.toArray(new Long[0]));
    }

    /**
     * 删除入社申请信息
     * 
     * @param applicationId 入社申请ID
     * @return 结果
     */
    @Override
    public int deleteClubApplicationById(Long applicationId) {
        ClubApplication application = requireApplication(applicationId);
        ensureApplicationInScope(application, "无权删除该入社申请");
        return clubApplicationMapper.deleteClubApplicationById(applicationId);
    }

    /**
     * 获取申请统计数据（支持社长数据范围过滤）
     */
    @Override
    public java.util.Map<String, Object> getStatData(String beginTime, String endTime) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();

        // 数据隔离：社长只能看自己管理社团的申请统计
        java.util.List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();

        // 按日期范围统计（如未传参则查全部）
        map.put("statusStat", clubApplicationMapper.selectStatusStatByDateRange(beginTime, endTime, managedClubIds));

        // 今日数据（用于计算环比差值）
        map.put("todayStats", clubApplicationMapper.selectTodayStats(managedClubIds));

        // 昨日数据（用于计算环比差值）
        map.put("yesterdayStatusStat", clubApplicationMapper.selectYesterdayStatusStat(managedClubIds));

        // 趋势统计（含社团信息）
        map.put("trendStat", clubApplicationMapper.selectTrendStat(managedClubIds));
        map.put("trendStatByClub", clubApplicationMapper.selectTrendStatByClub(managedClubIds));

        // 状态分布（含社团信息）
        map.put("statusStatByClub", clubApplicationMapper.selectStatusStatByClub(managedClubIds));

        // 社团排名
        map.put("clubRankingStat", clubApplicationMapper.selectClubRankingStat(managedClubIds));

        return map;
    }

    private ClubApplication requireApplication(Long applicationId) {
        ClubApplication application = clubApplicationMapper.selectClubApplicationById(applicationId);
        if (application == null) {
            throw new ServiceException("入社申请不存在");
        }
        return application;
    }

    private void ensureApplicationInScope(ClubApplication application, String message) {
        if (application == null || !dataScopeHelper.isManagedClub(application.getClubId())) {
            throw new ServiceException(message);
        }
    }
}
