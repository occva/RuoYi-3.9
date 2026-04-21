package com.ruoyi.user.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.user.domain.Club;

/**
 * 社团信息Mapper接口
 */
public interface ClubMapper {
        /**
         * 查询社团列表
         */
        List<Club> selectClubList(Club club);

        /**
         * 根据ID查询社团
         */
        Club selectClubById(Long clubId);

        /**
         * 根据ID查询社团并加锁（for update）
         */
        Club selectClubByIdForUpdate(Long clubId);

        /**
         * 浏览量 +1
         */
        int incrementViewCount(@Param("clubId") Long clubId);

        /**
         * 根据收藏关系表精确回刷收藏数
         */
        int refreshFavoriteCount(@Param("clubId") Long clubId);

        /**
         * 查询热门社团
         */
        List<Club> selectPopularClubs(int limit);

        /**
         * 新增社团
         */
        int insertClub(Club club);

        /**
         * 修改社团
         */
        int updateClub(Club club);

        /**
         * 删除社团（逻辑删除）
         */
        int deleteClubById(Long clubId);

        /**
         * 批量删除社团（逻辑删除）
         */
        int deleteClubByIds(Long[] clubIds);

        /**
         * 校验社团编码是否唯一
         */
        Club checkClubCodeUnique(String clubCode);

        /**
         * 校验社团名称是否唯一
         */
        Club checkClubNameUnique(String clubName);

        /**
         * 批量更新社团状态
         */
        int updateClubStatus(@Param("clubIds") Long[] clubIds, @Param("status") String status,
                        @Param("updateBy") String updateBy);

        /**
         * 批量设置热门
         */
        int updateClubPopular(@Param("clubIds") Long[] clubIds, @Param("isPopular") String isPopular,
                        @Param("updateBy") String updateBy);

        /**
         * 统计社团数量
         */
        int countClubByCategoryId(Long categoryId);

        /**
         * 查询用户加入的社团列表
         */
        List<Club> selectClubListByUserId(Long userId);

        /**
         * 查询用户管理的社团列表
         */
        List<Club> selectClubListByPresidentId(Long userId);

        /** 社团统计 - 按状态统计 */
        List<Map<String, Object>> selectClubStatusStat(@Param("beginTime") String beginTime,
                        @Param("endTime") String endTime,
                        @Param("clubIds") List<Long> clubIds);

        /** 社团统计 - 招新中数量 */
        Integer selectClubRecruitingCount(@Param("beginTime") String beginTime,
                        @Param("endTime") String endTime,
                        @Param("clubIds") List<Long> clubIds);

        /** 社团统计 - 今日数据 */
        Map<String, Object> selectClubTodayStats(@Param("clubIds") List<Long> clubIds);

        /** 社团统计 - 创建趋势 */
        List<Map<String, Object>> selectClubTrendStat(@Param("clubIds") List<Long> clubIds);

        /** 社团统计 - 分类分布 */
        List<Map<String, Object>> selectClubCategoryStat(@Param("clubIds") List<Long> clubIds);

        /** 社团统计 - 成员数排名Top10 */
        List<Map<String, Object>> selectClubMemberRanking(@Param("clubIds") List<Long> clubIds);
}
