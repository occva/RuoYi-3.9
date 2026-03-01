package com.ruoyi.user.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.user.domain.ClubActivity;

/**
 * 社团活动Mapper接口
 */
public interface ClubActivityMapper {
    List<ClubActivity> selectClubActivityList(ClubActivity activity);

    ClubActivity selectClubActivityById(Long activityId);

    List<ClubActivity> selectActivityByClubId(Long clubId);

    List<ClubActivity> selectMyRegisteredActivities(@Param("userId") Long userId);

    int insertClubActivity(ClubActivity activity);

    int updateClubActivity(ClubActivity activity);

    int deleteClubActivityById(Long activityId);

    int deleteClubActivityByIds(Long[] activityIds);

    int incrementParticipantsIfAvailable(@Param("activityId") Long activityId);

    int updateActivityStatusBasedOnTime();

    int countOngoingByClubId(Long clubId);

    /** 活动统计 - 按状态统计 */
    List<Map<String, Object>> selectActivityStatusStat(@Param("beginTime") String beginTime,
            @Param("endTime") String endTime,
            @Param("clubIds") List<Long> clubIds);

    /** 活动统计 - 今日数据 */
    Map<String, Object> selectActivityTodayStats(@Param("clubIds") List<Long> clubIds);

    /** 活动统计 - 创建趋势 */
    List<Map<String, Object>> selectActivityTrendStat(@Param("clubIds") List<Long> clubIds);

    /** 活动统计 - 类型分布 */
    List<Map<String, Object>> selectActivityTypeStat(@Param("clubIds") List<Long> clubIds);

    /** 活动统计 - 各社团活动排名 */
    List<Map<String, Object>> selectActivityClubRanking(@Param("clubIds") List<Long> clubIds);
}
