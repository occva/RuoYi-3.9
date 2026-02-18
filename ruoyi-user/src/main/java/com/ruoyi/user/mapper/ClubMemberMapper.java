package com.ruoyi.user.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.user.domain.ClubMember;

/**
 * 社团成员Mapper接口
 * 
 * @author ruoyi
 */
public interface ClubMemberMapper {
    public ClubMember selectClubMemberById(Long memberId);

    public List<ClubMember> selectClubMemberList(ClubMember clubMember);

    public int insertClubMember(ClubMember clubMember);

    public int updateClubMember(ClubMember clubMember);

    public int deleteClubMemberById(Long memberId);

    public int deleteClubMemberByIds(Long[] memberIds);

    /** 成员统计 - 按状态统计 */
    List<Map<String, Object>> selectMemberStatusStat(@Param("beginTime") String beginTime,
            @Param("endTime") String endTime);

    /** 成员统计 - 今日数据 */
    Map<String, Object> selectMemberTodayStats();

    /** 成员统计 - 入社趋势 */
    List<Map<String, Object>> selectMemberTrendStat();

    /** 成员统计 - 角色分布 */
    List<Map<String, Object>> selectMemberRoleStat();

    /** 成员统计 - 各社团成员排名 */
    List<Map<String, Object>> selectMemberClubRanking();
}
