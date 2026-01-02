package com.ruoyi.user.mapper;

import java.util.List;
import com.ruoyi.user.domain.ClubMember;

/**
 * 社团成员Mapper接口
 * 
 * @author ruoyi
 */
public interface ClubMemberMapper {
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
     * 删除社团成员
     * 
     * @param memberId 社团成员ID
     * @return 结果
     */
    public int deleteClubMemberById(Long memberId);

    /**
     * 批量删除社团成员
     * 
     * @param memberIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteClubMemberByIds(Long[] memberIds);
}
