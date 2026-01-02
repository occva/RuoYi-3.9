package com.ruoyi.user.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.user.domain.ClubMember;
import com.ruoyi.user.mapper.ClubMemberMapper;
import com.ruoyi.user.service.IClubMemberService;

/**
 * 社团成员Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class ClubMemberServiceImpl implements IClubMemberService {
    @Autowired
    private ClubMemberMapper clubMemberMapper;

    /**
     * 查询社团成员
     * 
     * @param memberId 社团成员ID
     * @return 社团成员
     */
    @Override
    public ClubMember selectClubMemberById(Long memberId) {
        return clubMemberMapper.selectClubMemberById(memberId);
    }

    /**
     * 查询社团成员列表
     * 
     * @param clubMember 社团成员
     * @return 社团成员
     */
    @Override
    public List<ClubMember> selectClubMemberList(ClubMember clubMember) {
        return clubMemberMapper.selectClubMemberList(clubMember);
    }

    /**
     * 新增社团成员
     * 
     * @param clubMember 社团成员
     * @return 结果
     */
    @Override
    public int insertClubMember(ClubMember clubMember) {
        clubMember.setCreateTime(DateUtils.getNowDate());
        return clubMemberMapper.insertClubMember(clubMember);
    }

    /**
     * 修改社团成员
     * 
     * @param clubMember 社团成员
     * @return 结果
     */
    @Override
    public int updateClubMember(ClubMember clubMember) {
        clubMember.setUpdateTime(DateUtils.getNowDate());
        return clubMemberMapper.updateClubMember(clubMember);
    }

    /**
     * 批量删除社团成员
     * 
     * @param memberIds 需要删除的社团成员ID
     * @return 结果
     */
    @Override
    public int deleteClubMemberByIds(Long[] memberIds) {
        return clubMemberMapper.deleteClubMemberByIds(memberIds);
    }

    /**
     * 删除社团成员信息
     * 
     * @param memberId 社团成员ID
     * @return 结果
     */
    @Override
    public int deleteClubMemberById(Long memberId) {
        return clubMemberMapper.deleteClubMemberById(memberId);
    }
}
