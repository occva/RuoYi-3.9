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
}
