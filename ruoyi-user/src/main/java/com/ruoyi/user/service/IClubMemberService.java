package com.ruoyi.user.service;

import java.util.List;
import com.ruoyi.user.domain.ClubMember;

/**
 * 社团成员Service接口
 * 
 * @author ruoyi
 */
public interface IClubMemberService {
    /**
     * 查询社团成员
     * 
     * @param memberId 社团成员ID
     * @return 社团成员
     */
    public ClubMember selectClubMemberById(Long memberId);

    /**
     * 查询社团成员列表
     * 
     * @param clubMember 社团成员
     * @return 社团成员集合
     */
    public List<ClubMember> selectClubMemberList(ClubMember clubMember);

    /**
     * 新增社团成员
     * 
     * @param clubMember 社团成员
     * @return 结果
     */
    public int insertClubMember(ClubMember clubMember);

    /**
     * 修改社团成员
     * 
     * @param clubMember 社团成员
     * @return 结果
     */
    public int updateClubMember(ClubMember clubMember);

    /**
     * 批量删除社团成员
     * 
     * @param memberIds 需要删除的社团成员ID
     * @return 结果
     */
    public int deleteClubMemberByIds(Long[] memberIds);

    /**
     * 删除社团成员信息
     * 
     * @param memberId 社团成员ID
     * @return 结果
     */
    public int deleteClubMemberById(Long memberId);

    /**
     * 获取成员统计数据
     */
    public java.util.Map<String, Object> getStatData(String beginTime, String endTime);

    /**
     * 转让社长（目标成员升级为社长，原社长降级为副社长或普通成员）
     *
     * @param clubId                  社团ID
     * @param fromMemberId            原社长成员ID（可为空，后端按当前社长判定）
     * @param toMemberId              新社长成员ID
     * @param fromPresidentNewRoleType 原社长转让后的角色（2副社长 / 3普通成员）
     * @return 结果
     */
    public int transferPresident(Long clubId, Long fromMemberId, Long toMemberId, String fromPresidentNewRoleType);

    /**
     * 任命副社长（专用流程）
     *
     * @param clubId   社团ID
     * @param memberId 目标成员ID
     * @return 结果
     */
    public int appointVicePresident(Long clubId, Long memberId);

    /**
     * 撤销副社长（专用流程）
     *
     * @param clubId     社团ID
     * @param memberId   目标成员ID
     * @param toRoleType 调整后的成员角色（默认普通成员=3）
     * @return 结果
     */
    public int revokeVicePresident(Long clubId, Long memberId, String toRoleType);
}
