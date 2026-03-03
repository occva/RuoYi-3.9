package com.ruoyi.user.mapper;

import java.util.List;
import com.ruoyi.user.domain.ClubApplication;

/**
 * 入社申请Mapper接口
 * 
 * @author ruoyi
 */
public interface ClubApplicationMapper {
    /**
     * 查询入社申请
     * 
     * @param applicationId 入社申请ID
     * @return 入社申请
     */
    public ClubApplication selectClubApplicationById(Long applicationId);

    /**
     * 查询入社申请列表
     * 
     * @param clubApplication 入社申请
     * @return 入社申请集合
     */
    public List<ClubApplication> selectClubApplicationList(ClubApplication clubApplication);

    /**
     * 判断用户是否存在待审核入社申请（review_status=0 且 del_flag=0）
     */
    public int existsPendingApplication(
            @org.apache.ibatis.annotations.Param("clubId") Long clubId,
            @org.apache.ibatis.annotations.Param("userId") Long userId);

    /**
     * 新增入社申请
     * 
     * @param clubApplication 入社申请
     * @return 结果
     */
    public int insertClubApplication(ClubApplication clubApplication);

    /**
     * 修改入社申请
     * 
     * @param clubApplication 入社申请
     * @return 结果
     */
    public int updateClubApplication(ClubApplication clubApplication);

    /**
     * 删除入社申请
     * 
     * @param applicationId 入社申请ID
     * @return 结果
     */
    public int deleteClubApplicationById(Long applicationId);

    /**
     * 批量删除入社申请
     * 
     * @param applicationIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteClubApplicationByIds(Long[] applicationIds);

    /**
     * 统计各状态申请数量
     */
    public List<java.util.Map<String, Object>> selectStatusStat(
            @org.apache.ibatis.annotations.Param("clubIds") java.util.List<Long> clubIds);

    /**
     * 统计近30天申请趋势
     */
    public List<java.util.Map<String, Object>> selectTrendStat(
            @org.apache.ibatis.annotations.Param("clubIds") java.util.List<Long> clubIds);

    /**
     * 统计社团申请排名
     */
    public List<java.util.Map<String, Object>> selectClubRankingStat(
            @org.apache.ibatis.annotations.Param("clubIds") java.util.List<Long> clubIds);

    /**
     * 统计昨日各状态申请数量
     */
    public List<java.util.Map<String, Object>> selectYesterdayStatusStat(
            @org.apache.ibatis.annotations.Param("clubIds") java.util.List<Long> clubIds);

    /**
     * 按社团分组统计趋势数据
     */
    public List<java.util.Map<String, Object>> selectTrendStatByClub(
            @org.apache.ibatis.annotations.Param("clubIds") java.util.List<Long> clubIds);

    /**
     * 按社团分组统计状态分布
     */
    public List<java.util.Map<String, Object>> selectStatusStatByClub(
            @org.apache.ibatis.annotations.Param("clubIds") java.util.List<Long> clubIds);

    /**
     * 按日期范围统计各状态申请数量
     */
    public List<java.util.Map<String, Object>> selectStatusStatByDateRange(
            @org.apache.ibatis.annotations.Param("beginTime") String beginTime,
            @org.apache.ibatis.annotations.Param("endTime") String endTime,
            @org.apache.ibatis.annotations.Param("clubIds") java.util.List<Long> clubIds);

    /**
     * 统计今日各状态申请数量
     */
    public java.util.Map<String, Object> selectTodayStats(
            @org.apache.ibatis.annotations.Param("clubIds") java.util.List<Long> clubIds);
}
