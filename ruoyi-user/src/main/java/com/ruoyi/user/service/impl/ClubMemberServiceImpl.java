package com.ruoyi.user.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.user.domain.Club;
import com.ruoyi.user.domain.ClubMember;
import com.ruoyi.user.mapper.ClubMapper;
import com.ruoyi.user.mapper.ClubMemberMapper;
import com.ruoyi.user.service.IClubMemberService;

/**
 * 社团成员Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class ClubMemberServiceImpl implements IClubMemberService {
    private static final String ROLE_PRESIDENT = "1";
    private static final String ROLE_VICE_PRESIDENT = "2";
    private static final String ROLE_MEMBER = "3";
    private static final String STATUS_NORMAL = "0";

    @Autowired
    private ClubMemberMapper clubMemberMapper;

    @Autowired
    private ClubDataScopeHelper dataScopeHelper;

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询社团成员
     * 
     * @param memberId 社团成员ID
     * @return 社团成员
     */
    @Override
    public ClubMember selectClubMemberById(Long memberId) {
        ClubMember member = clubMemberMapper.selectClubMemberById(memberId);
        if (!isActiveMember(member)) {
            return null;
        }
        ensureClubInScope(member.getClubId());
        return member;
    }

    /**
     * 查询社团成员列表
     * 
     * @param clubMember 社团成员
     * @return 社团成员
     */
    @Override
    public List<ClubMember> selectClubMemberList(ClubMember clubMember) {
        // 数据隔离：社长/副社长只能看自己管理社团的成员
        java.util.List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds != null) {
            clubMember.getParams().put("clubIds", managedClubIds);
        }
        return clubMemberMapper.selectClubMemberList(clubMember);
    }

    /**
     * 新增社团成员
     * 
     * @param clubMember 社团成员
     * @return 结果
     */
    @Override
    @Transactional
    public int insertClubMember(ClubMember clubMember) {
        if (clubMember == null || clubMember.getClubId() == null || clubMember.getUserId() == null) {
            throw new ServiceException("新增成员参数不完整");
        }
        ensureClubInScope(clubMember.getClubId());
        if (StringUtils.isBlank(clubMember.getRoleType())) {
            clubMember.setRoleType(ROLE_MEMBER);
        }
        if (!ROLE_MEMBER.equals(clubMember.getRoleType())) {
            throw new ServiceException("新增成员仅支持普通成员，管理岗位请使用任命/转让流程");
        }
        if (StringUtils.isBlank(clubMember.getStatus())) {
            clubMember.setStatus(STATUS_NORMAL);
        }
        clubMember.setCreateTime(DateUtils.getNowDate());
        int rows = clubMemberMapper.insertClubMember(clubMember);
        if (rows > 0) {
            syncManagedRolesByUserId(clubMember.getUserId());
            rebuildVicePresidentField(clubMember.getClubId());
        }
        return rows;
    }

    /**
     * 修改社团成员
     * 
     * @param clubMember 社团成员
     * @return 结果
     */
    @Override
    @Transactional
    public int updateClubMember(ClubMember clubMember) {
        if (clubMember.getMemberId() == null) {
            throw new ServiceException("成员ID不能为空");
        }
        ClubMember old = clubMemberMapper.selectClubMemberById(clubMember.getMemberId());
        if (!isActiveMember(old)) {
            throw new ServiceException("成员记录不存在");
        }
        ensureClubInScope(old.getClubId());

        String newRoleType = StringUtils.isBlank(clubMember.getRoleType()) ? old.getRoleType() : clubMember.getRoleType();
        String newStatus = StringUtils.isBlank(clubMember.getStatus()) ? old.getStatus() : clubMember.getStatus();

        if (!ROLE_PRESIDENT.equals(old.getRoleType()) && ROLE_PRESIDENT.equals(newRoleType)) {
            throw new ServiceException("设置社长属于职务转让，请使用“转让社长”操作");
        }
        if (ROLE_PRESIDENT.equals(old.getRoleType())
                && (!ROLE_PRESIDENT.equals(newRoleType) || !STATUS_NORMAL.equals(newStatus))) {
            throw new ServiceException("社长成员不可直接降级或退出，请先执行社长转让");
        }
        if (!ROLE_VICE_PRESIDENT.equals(old.getRoleType()) && ROLE_VICE_PRESIDENT.equals(newRoleType)) {
            throw new ServiceException("设置副社长属于任命操作，请使用“任命副社长”");
        }
        if (ROLE_VICE_PRESIDENT.equals(old.getRoleType())
                && (!ROLE_VICE_PRESIDENT.equals(newRoleType) || !STATUS_NORMAL.equals(newStatus))) {
            throw new ServiceException("副社长降级或退出请使用“撤销副社长”");
        }

        clubMember.setUpdateTime(DateUtils.getNowDate());
        int rows = clubMemberMapper.updateClubMember(clubMember);
        if (rows > 0) {
            syncManagedRolesByUserId(old.getUserId());
            rebuildVicePresidentField(old.getClubId());
        }
        return rows;
    }

    /**
     * 批量删除社团成员
     * 
     * @param memberIds 需要删除的社团成员ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteClubMemberByIds(Long[] memberIds) {
        if (memberIds == null || memberIds.length == 0) {
            return 0;
        }

        Set<Long> deleteIds = new LinkedHashSet<>();
        Set<Long> affectedUsers = new LinkedHashSet<>();
        Set<Long> affectedClubs = new LinkedHashSet<>();
        for (Long memberId : memberIds) {
            ClubMember member = clubMemberMapper.selectClubMemberByIdForUpdate(memberId);
            if (!isActiveMember(member)) {
                continue;
            }
            ensureClubInScope(member.getClubId());
            if (ROLE_PRESIDENT.equals(member.getRoleType())) {
                throw new ServiceException("社长不可移除，请先执行社长转让");
            }
            deleteIds.add(memberId);
            affectedUsers.add(member.getUserId());
            affectedClubs.add(member.getClubId());
        }
        if (deleteIds.isEmpty()) {
            return 0;
        }

        int rows = clubMemberMapper.deleteClubMemberByIds(deleteIds.toArray(new Long[0]));
        if (rows != deleteIds.size()) {
            throw new ServiceException("成员状态已变更，请刷新后重试");
        }
        if (rows > 0) {
            for (Long userId : affectedUsers) {
                syncManagedRolesByUserId(userId);
            }
            for (Long clubId : affectedClubs) {
                rebuildVicePresidentField(clubId);
            }
        }
        return rows;
    }

    /**
     * 删除社团成员信息
     * 
     * @param memberId 社团成员ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteClubMemberById(Long memberId) {
        return deleteClubMemberByIds(new Long[] { memberId });
    }

    @Override
    public java.util.Map<String, Object> getStatData(String beginTime, String endTime) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        // 数据隔离：社长/副社长只能看自己管理社团的成员统计
        java.util.List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        map.put("statusStat", clubMemberMapper.selectMemberStatusStat(beginTime, endTime, managedClubIds));
        map.put("todayStats", clubMemberMapper.selectMemberTodayStats(managedClubIds));
        map.put("trendStat", clubMemberMapper.selectMemberTrendStat(managedClubIds));
        map.put("roleStat", clubMemberMapper.selectMemberRoleStat(managedClubIds));
        map.put("clubRanking", clubMemberMapper.selectMemberClubRanking(managedClubIds));
        return map;
    }

    @Override
    @Transactional
    public int transferPresident(Long clubId, Long fromMemberId, Long toMemberId, String fromPresidentNewRoleType) {
        if (toMemberId == null) {
            throw new ServiceException("目标成员不能为空");
        }
        if (StringUtils.isBlank(fromPresidentNewRoleType)) {
            fromPresidentNewRoleType = ROLE_MEMBER;
        }
        if (!ROLE_VICE_PRESIDENT.equals(fromPresidentNewRoleType) && !ROLE_MEMBER.equals(fromPresidentNewRoleType)) {
            throw new ServiceException("原社长转让后角色仅支持副社长或普通成员");
        }

        ClubMember target = clubMemberMapper.selectClubMemberByIdForUpdate(toMemberId);
        if (!isActiveMember(target) || !STATUS_NORMAL.equals(target.getStatus())) {
            throw new ServiceException("目标成员不存在或状态异常");
        }
        if (clubId != null && !clubId.equals(target.getClubId())) {
            throw new ServiceException("目标成员不属于指定社团");
        }
        clubId = target.getClubId();
        ensureClubInScope(clubId);

        Club club = clubMapper.selectClubByIdForUpdate(clubId);
        if (club == null || !"0".equals(club.getDelFlag())) {
            throw new ServiceException("社团不存在");
        }

        ClubMember currentPresident = clubMemberMapper.selectCurrentPresidentByClubIdForUpdate(clubId);
        if (currentPresident == null) {
            throw new ServiceException("当前社团未找到有效社长");
        }
        if (fromMemberId != null && !fromMemberId.equals(currentPresident.getMemberId())) {
            throw new ServiceException("当前社长已变更，请刷新后重试");
        }
        ensureTransferPermission(currentPresident);

        if (currentPresident.getMemberId().equals(target.getMemberId())) {
            return 1;
        }

        String operator = getCurrentUsernameSafe();
        Date now = DateUtils.getNowDate();

        ClubMember demote = new ClubMember();
        demote.setMemberId(currentPresident.getMemberId());
        demote.setRoleType(fromPresidentNewRoleType);
        demote.setUpdateBy(operator);
        demote.setUpdateTime(now);
        clubMemberMapper.updateClubMember(demote);

        ClubMember promote = new ClubMember();
        promote.setMemberId(target.getMemberId());
        promote.setRoleType(ROLE_PRESIDENT);
        promote.setStatus(STATUS_NORMAL);
        promote.setUpdateBy(operator);
        promote.setUpdateTime(now);
        clubMemberMapper.updateClubMember(promote);

        Club updateClub = new Club();
        updateClub.setClubId(clubId);
        updateClub.setPresidentId(target.getUserId());
        updateClub.setPresidentName(StringUtils.isNotBlank(target.getNickName()) ? target.getNickName() : target.getUserName());
        updateClub.setVicePresident(buildVicePresidentNames(clubId));
        updateClub.setUpdateBy(operator);
        clubMapper.updateClub(updateClub);

        syncManagedRolesByUserId(currentPresident.getUserId());
        syncManagedRolesByUserId(target.getUserId());
        return 1;
    }

    @Override
    @Transactional
    public int appointVicePresident(Long clubId, Long memberId) {
        if (memberId == null) {
            throw new ServiceException("目标成员不能为空");
        }

        ClubMember target = clubMemberMapper.selectClubMemberByIdForUpdate(memberId);
        if (!isActiveMember(target) || !STATUS_NORMAL.equals(target.getStatus())) {
            throw new ServiceException("目标成员不存在或状态异常");
        }
        if (clubId != null && !clubId.equals(target.getClubId())) {
            throw new ServiceException("目标成员不属于指定社团");
        }
        clubId = target.getClubId();
        ensureClubInScope(clubId);

        Club club = clubMapper.selectClubByIdForUpdate(clubId);
        if (club == null || !"0".equals(club.getDelFlag())) {
            throw new ServiceException("社团不存在");
        }
        ensurePresidentOrAdminPermission(club);

        if (ROLE_PRESIDENT.equals(target.getRoleType())) {
            throw new ServiceException("社长无需再任命为副社长");
        }
        if (ROLE_VICE_PRESIDENT.equals(target.getRoleType())) {
            return 1;
        }

        ClubMember update = new ClubMember();
        update.setMemberId(target.getMemberId());
        update.setRoleType(ROLE_VICE_PRESIDENT);
        update.setStatus(STATUS_NORMAL);
        update.setUpdateBy(getCurrentUsernameSafe());
        update.setUpdateTime(DateUtils.getNowDate());
        clubMemberMapper.updateClubMember(update);

        rebuildVicePresidentField(clubId);
        syncManagedRolesByUserId(target.getUserId());
        return 1;
    }

    @Override
    @Transactional
    public int revokeVicePresident(Long clubId, Long memberId, String toRoleType) {
        if (memberId == null) {
            throw new ServiceException("目标成员不能为空");
        }
        if (StringUtils.isBlank(toRoleType)) {
            toRoleType = ROLE_MEMBER;
        }
        if (!ROLE_MEMBER.equals(toRoleType)) {
            throw new ServiceException("撤销副社长后仅支持降级为普通成员");
        }

        ClubMember target = clubMemberMapper.selectClubMemberByIdForUpdate(memberId);
        if (!isActiveMember(target)) {
            throw new ServiceException("目标成员不存在");
        }
        if (clubId != null && !clubId.equals(target.getClubId())) {
            throw new ServiceException("目标成员不属于指定社团");
        }
        clubId = target.getClubId();
        ensureClubInScope(clubId);

        Club club = clubMapper.selectClubByIdForUpdate(clubId);
        if (club == null || !"0".equals(club.getDelFlag())) {
            throw new ServiceException("社团不存在");
        }
        ensurePresidentOrAdminPermission(club);

        if (!ROLE_VICE_PRESIDENT.equals(target.getRoleType())) {
            throw new ServiceException("目标成员当前不是副社长");
        }

        ClubMember update = new ClubMember();
        update.setMemberId(target.getMemberId());
        update.setRoleType(toRoleType);
        update.setUpdateBy(getCurrentUsernameSafe());
        update.setUpdateTime(DateUtils.getNowDate());
        clubMemberMapper.updateClubMember(update);

        rebuildVicePresidentField(clubId);
        syncManagedRolesByUserId(target.getUserId());
        return 1;
    }

    private void ensureTransferPermission(ClubMember currentPresident) {
        LoginUser loginUser = null;
        try {
            loginUser = SecurityUtils.getLoginUser();
        } catch (Exception ignored) {
        }
        Long operatorId = loginUser == null ? null : loginUser.getUserId();
        boolean isCurrentPresident = operatorId != null && operatorId.equals(currentPresident.getUserId());
        boolean isAdminOperator = operatorId != null && (SecurityUtils.isAdmin(operatorId) || hasRoleKey(loginUser, "club_admin"));
        if (!isCurrentPresident && !isAdminOperator) {
            throw new ServiceException("仅当前社长或管理员可执行转让");
        }
    }

    private void ensurePresidentOrAdminPermission(Club club) {
        LoginUser loginUser = null;
        try {
            loginUser = SecurityUtils.getLoginUser();
        } catch (Exception ignored) {
        }
        Long operatorId = loginUser == null ? null : loginUser.getUserId();
        boolean isPresident = operatorId != null && club != null && operatorId.equals(club.getPresidentId());
        boolean isAdminOperator = operatorId != null && (SecurityUtils.isAdmin(operatorId) || hasRoleKey(loginUser, "club_admin"));
        if (!isPresident && !isAdminOperator) {
            throw new ServiceException("仅当前社长或管理员可执行该操作");
        }
    }

    private boolean hasRoleKey(LoginUser loginUser, String roleKey) {
        if (loginUser == null || loginUser.getUser() == null || loginUser.getUser().getRoles() == null) {
            return false;
        }
        return loginUser.getUser().getRoles().stream().anyMatch(r -> roleKey.equals(r.getRoleKey()));
    }

    private void ensureClubInScope(Long clubId) {
        List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds != null && (clubId == null || !managedClubIds.contains(clubId))) {
            throw new ServiceException("无权操作该社团成员");
        }
    }

    private boolean isActiveMember(ClubMember member) {
        return member != null && !"2".equals(member.getDelFlag());
    }

    private void syncManagedRolesByUserId(Long userId) {
        if (userId == null) {
            return;
        }
        Long presidentRoleId = resolveRoleId("president");
        Long viceRoleId = resolveRoleId("vice_president");

        boolean hasPresidentMembership = hasActiveManagementMembership(userId, ROLE_PRESIDENT);
        boolean hasViceMembership = hasActiveManagementMembership(userId, ROLE_VICE_PRESIDENT);

        boolean changed = false;
        changed |= syncManagedRole(userId, presidentRoleId, hasPresidentMembership);
        changed |= syncManagedRole(userId, viceRoleId, hasViceMembership);

        if (changed) {
            invalidateUserSessions(userId);
        }
    }

    private boolean syncManagedRole(Long userId, Long roleId, boolean shouldExist) {
        if (shouldExist) {
            return userRoleMapper.insertUserRoleIgnore(userId, roleId) > 0;
        }
        return userRoleMapper.deleteUserRoleByUserIdAndRoleId(userId, roleId) > 0;
    }

    private boolean hasActiveManagementMembership(Long userId, String roleType) {
        return clubMemberMapper.countActiveManagementMembership(userId, roleType) > 0;
    }

    private Long resolveRoleId(String roleKey) {
        SysRole role = roleMapper.checkRoleKeyUnique(roleKey);
        if (role == null || role.getRoleId() == null) {
            throw new ServiceException("未找到角色：" + roleKey);
        }
        return role.getRoleId();
    }

    private void rebuildVicePresidentField(Long clubId) {
        if (clubId == null) {
            return;
        }
        Club update = new Club();
        update.setClubId(clubId);
        update.setVicePresident(buildVicePresidentNames(clubId));
        update.setUpdateBy(getCurrentUsernameSafe());
        clubMapper.updateClub(update);
    }

    private String buildVicePresidentNames(Long clubId) {
        ClubMember query = new ClubMember();
        query.setClubId(clubId);
        query.setRoleType(ROLE_VICE_PRESIDENT);
        query.setStatus(STATUS_NORMAL);
        List<ClubMember> viceList = clubMemberMapper.selectClubMemberList(query);
        if (viceList == null || viceList.isEmpty()) {
            return "";
        }
        return viceList.stream()
                .map(m -> StringUtils.isNotBlank(m.getNickName()) ? m.getNickName() : m.getUserName())
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.joining(","));
    }

    private String getCurrentUsernameSafe() {
        try {
            return SecurityUtils.getUsername();
        } catch (Exception e) {
            return "system";
        }
    }

    private void invalidateUserSessions(Long userId) {
        if (userId == null) {
            return;
        }
        Collection<String> tokenKeys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        if (tokenKeys == null || tokenKeys.isEmpty()) {
            return;
        }
        for (String tokenKey : tokenKeys) {
            LoginUser loginUser = redisCache.getCacheObject(tokenKey);
            if (loginUser != null && userId.equals(loginUser.getUserId())) {
                redisCache.deleteObject(tokenKey);
            }
        }
    }
}
