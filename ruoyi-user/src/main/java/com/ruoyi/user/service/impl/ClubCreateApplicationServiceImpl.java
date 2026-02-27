package com.ruoyi.user.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.user.domain.Club;
import com.ruoyi.user.domain.ClubCreateApplication;
import com.ruoyi.user.domain.ClubMember;
import com.ruoyi.user.mapper.ClubCreateApplicationMapper;
import com.ruoyi.user.mapper.ClubMapper;
import com.ruoyi.user.mapper.ClubMemberMapper;
import com.ruoyi.user.service.IClubCreateApplicationService;

/**
 * 新社团申请 Service 实现
 */
@Service
public class ClubCreateApplicationServiceImpl implements IClubCreateApplicationService {
    private static final Long DEFAULT_DEPT_ID = 100L;

    @Autowired
    private ClubCreateApplicationMapper clubCreateApplicationMapper;

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private ClubMemberMapper clubMemberMapper;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SysRoleMapper roleMapper;

    @Override
    public ClubCreateApplication selectClubCreateApplicationById(Long applyId) {
        return clubCreateApplicationMapper.selectClubCreateApplicationById(applyId);
    }

    @Override
    public List<ClubCreateApplication> selectClubCreateApplicationList(ClubCreateApplication query) {
        return clubCreateApplicationMapper.selectClubCreateApplicationList(query);
    }

    @Override
    public int insertClubCreateApplication(ClubCreateApplication application) {
        normalizeOptionalFields(application);
        application.setCreateTime(DateUtils.getNowDate());
        application.setApplyTime(DateUtils.getNowDate());
        if (StringUtils.isBlank(application.getStatus())) {
            application.setStatus("0");
        }
        if (StringUtils.isBlank(application.getDelFlag())) {
            application.setDelFlag("0");
        }
        return clubCreateApplicationMapper.insertClubCreateApplication(application);
    }

    @Override
    public int updateClubCreateApplication(ClubCreateApplication application) {
        application.setUpdateTime(DateUtils.getNowDate());
        return clubCreateApplicationMapper.updateClubCreateApplication(application);
    }

    @Override
    @Transactional
    public int reviewClubCreateApplication(ClubCreateApplication application) {
        ClubCreateApplication full = clubCreateApplicationMapper.selectClubCreateApplicationById(application.getApplyId());
        if (full == null || !"0".equals(full.getDelFlag())) {
            throw new ServiceException("申请记录不存在");
        }
        if (!"0".equals(full.getStatus())) {
            throw new ServiceException("该申请已审核，请勿重复操作");
        }
        if (!"1".equals(application.getStatus()) && !"2".equals(application.getStatus())) {
            throw new ServiceException("审核状态无效");
        }

        application.setReviewTime(DateUtils.getNowDate());
        application.setUpdateTime(DateUtils.getNowDate());
        int row = clubCreateApplicationMapper.updateClubCreateApplication(application);
        if (row <= 0 || !"1".equals(application.getStatus())) {
            return row;
        }

        if (clubMapper.checkClubNameUnique(full.getClubName()) != null) {
            throw new ServiceException("社团名称已存在，无法通过该申请");
        }

        String initPassword = configService.selectConfigByKey("sys.user.initPassword");
        if (StringUtils.isBlank(initPassword)) {
            initPassword = "123456";
        }

        SysUser adminUser = buildPresidentAdminUser(full, initPassword, application.getUpdateBy());
        userService.insertUser(adminUser);

        Club club = buildClub(full, adminUser, application.getUpdateBy());
        clubMapper.insertClub(club);

        ClubMember president = new ClubMember();
        president.setClubId(club.getClubId());
        president.setUserId(adminUser.getUserId());
        president.setUserName(adminUser.getUserName());
        president.setNickName(adminUser.getNickName());
        president.setRoleType("1");
        president.setJoinDate(new Date());
        president.setStatus("0");
        president.setDelFlag("0");
        president.setCreateBy(application.getUpdateBy());
        president.setCreateTime(new Date());
        clubMemberMapper.insertClubMember(president);

        ClubCreateApplication afterApprove = new ClubCreateApplication();
        afterApprove.setApplyId(full.getApplyId());
        afterApprove.setApprovedClubId(club.getClubId());
        afterApprove.setAdminUserId(adminUser.getUserId());
        afterApprove.setAdminUserName(adminUser.getUserName());
        afterApprove.setAdminInitPassword(initPassword);
        afterApprove.setReviewComment(buildReviewComment(application.getReviewComment(), adminUser.getUserName(), initPassword));
        afterApprove.setUpdateBy(application.getUpdateBy());
        afterApprove.setUpdateTime(DateUtils.getNowDate());
        clubCreateApplicationMapper.updateClubCreateApplication(afterApprove);

        return row;
    }

    @Override
    public int deleteClubCreateApplicationByIds(Long[] applyIds) {
        return clubCreateApplicationMapper.deleteClubCreateApplicationByIds(applyIds);
    }

    @Override
    public int deleteClubCreateApplicationById(Long applyId) {
        return clubCreateApplicationMapper.deleteClubCreateApplicationById(applyId);
    }

    private void normalizeOptionalFields(ClubCreateApplication application) {
        application.setActivityPlan(StringUtils.trimToNull(application.getActivityPlan()));
        application.setCoreMembers(StringUtils.trimToNull(application.getCoreMembers()));
        application.setAdvisorName(StringUtils.trimToNull(application.getAdvisorName()));
    }

    private SysUser buildPresidentAdminUser(ClubCreateApplication application, String initPassword, String createBy) {
        Long presidentRoleId = resolvePresidentRoleId();
        String userName = generateAdminUserName(application.getApplyId());
        String nickName = StringUtils.isNotBlank(application.getApplicantNickName())
                ? application.getApplicantNickName()
                : application.getClubName() + "社长";

        SysUser user = new SysUser();
        user.setDeptId(DEFAULT_DEPT_ID);
        user.setUserName(userName);
        user.setNickName(nickName);
        user.setStatus("0");
        user.setPassword(SecurityUtils.encryptPassword(initPassword));
        user.setRoleIds(new Long[] { presidentRoleId });
        user.setCreateBy(createBy);
        user.setRemark("Created from club create application #" + application.getApplyId());
        return user;
    }

    private Club buildClub(ClubCreateApplication application, SysUser adminUser, String createBy) {
        Club club = new Club();
        club.setClubName(application.getClubName());
        club.setCategoryId(application.getCategoryId());
        club.setDescription(application.getDescription());
        club.setLogoUrl(application.getLogoUrl());
        club.setPresidentId(adminUser.getUserId());
        club.setPresidentName(adminUser.getNickName());
        club.setContactPhone(application.getContactPhone());
        club.setMemberCount(1);
        club.setStatus("0");
        club.setIsRecruiting("1");
        club.setIsPopular("0");
        club.setCreateBy(createBy);
        club.setCreateTime(DateUtils.getNowDate());
        club.setRemark("Created from application #" + application.getApplyId());
        return club;
    }

    private Long resolvePresidentRoleId() {
        SysRole role = roleMapper.checkRoleKeyUnique("president");
        if (role == null || role.getRoleId() == null) {
            throw new ServiceException("未找到社长角色，请先初始化 role_key=president");
        }
        return role.getRoleId();
    }

    private String generateAdminUserName(Long applyId) {
        String base = "clubp" + applyId;
        for (int i = 0; i < 20; i++) {
            String candidate = i == 0 ? base : (base + "_" + i);
            SysUser check = new SysUser();
            check.setUserName(candidate);
            if (userService.checkUserNameUnique(check)) {
                return candidate;
            }
        }
        throw new ServiceException("自动生成后台账号失败，请重试");
    }

    private String buildReviewComment(String original, String userName, String password) {
        String credentialMessage = "审核通过，已创建社长后台账号：" + userName + "，初始密码：" + password;
        if (StringUtils.isBlank(original)) {
            return credentialMessage;
        }
        return original + "；" + credentialMessage;
    }
}
