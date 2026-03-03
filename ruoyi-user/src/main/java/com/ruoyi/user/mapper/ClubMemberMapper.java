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

    /**
     * 按成员ID加锁查询（for update）
     */
    ClubMember selectClubMemberByIdForUpdate(@Param("memberId") Long memberId);

    public List<ClubMember> selectClubMemberList(ClubMember clubMember);

    public int insertClubMember(ClubMember clubMember);

    public int updateClubMember(ClubMember clubMember);

    public int deleteClubMemberById(Long memberId);

    public int deleteClubMemberByIds(Long[] memberIds);

    /**
     * 查询用户作为社长/副社长管理的社团ID列表
     * role_type: 1=社长, 2=副社长
     */
    List<Long> selectManagedClubIdsByUserId(@Param("userId") Long userId);

    /**
     * 查询社团当前有效社长并加锁
     */
    ClubMember selectCurrentPresidentByClubIdForUpdate(@Param("clubId") Long clubId);

    /**
     * 查询用户在有效成员中的管理角色数量（status=0 且 del_flag=0）
     */
    int countActiveManagementMembership(@Param("userId") Long userId, @Param("roleType") String roleType);

    /** 成员统计 - 按状态统计 */
    List<Map<String, Object>> selectMemberStatusStat(@Param("beginTime") String beginTime,
            @Param("endTime") String endTime,
            @Param("clubIds") List<Long> clubIds);

    /** 成员统计 - 今日数据 */
    Map<String, Object> selectMemberTodayStats(@Param("clubIds") List<Long> clubIds);

    /** 成员统计 - 入社趋势 */
    List<Map<String, Object>> selectMemberTrendStat(@Param("clubIds") List<Long> clubIds);

    /** 成员统计 - 角色分布 */
    List<Map<String, Object>> selectMemberRoleStat(@Param("clubIds") List<Long> clubIds);

    /** 成员统计 - 各社团成员排名 */
    List<Map<String, Object>> selectMemberClubRanking(@Param("clubIds") List<Long> clubIds);
}
