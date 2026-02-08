package com.ruoyi.user.service.impl;

import java.util.List;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * 查询入社申请
     * 
     * @param applicationId 入社申请ID
     * @return 入社申请
     */
    @Override
    public ClubApplication selectClubApplicationById(Long applicationId) {
        return clubApplicationMapper.selectClubApplicationById(applicationId);
    }

    /**
     * 查询入社申请列表
     * 
     * @param clubApplication 入社申请
     * @return 入社申请
     */
    @Override
    public List<ClubApplication> selectClubApplicationList(ClubApplication clubApplication) {
        return clubApplicationMapper.selectClubApplicationList(clubApplication);
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
        clubApplication.setUpdateTime(DateUtils.getNowDate());
        return clubApplicationMapper.updateClubApplication(clubApplication);
    }

    /**
     * 审核并处理入社申请
     */
    @Override
    @Transactional
    public int reviewApplication(ClubApplication clubApplication) {
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
            ClubMember query = new ClubMember();
            query.setClubId(fullApp.getClubId());
            query.setUserId(fullApp.getUserId());
            List<ClubMember> existingMembers = clubMemberMapper.selectClubMemberList(query);
            if (existingMembers == null || existingMembers.isEmpty()) {
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
        return clubApplicationMapper.deleteClubApplicationByIds(applicationIds);
    }

    /**
     * 删除入社申请信息
     * 
     * @param applicationId 入社申请ID
     * @return 结果
     */
    @Override
    public int deleteClubApplicationById(Long applicationId) {
        return clubApplicationMapper.deleteClubApplicationById(applicationId);
    }

    /**
     * 获取申请统计数据
     */
    @Override
    public java.util.Map<String, Object> getStatData() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();

        // 今日数据
        map.put("statusStat", clubApplicationMapper.selectStatusStat());

        // 昨日数据（用于计算环比）
        map.put("yesterdayStatusStat", clubApplicationMapper.selectYesterdayStatusStat());

        // 趋势统计（含社团信息）
        map.put("trendStat", clubApplicationMapper.selectTrendStat());
        map.put("trendStatByClub", clubApplicationMapper.selectTrendStatByClub());

        // 状态分布（含社团信息）
        map.put("statusStatByClub", clubApplicationMapper.selectStatusStatByClub());

        // 社团排名
        map.put("clubRankingStat", clubApplicationMapper.selectClubRankingStat());

        return map;
    }
}
