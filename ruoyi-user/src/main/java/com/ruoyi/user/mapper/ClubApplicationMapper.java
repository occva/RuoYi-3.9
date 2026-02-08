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
    public List<java.util.Map<String, Object>> selectStatusStat();

    /**
     * 统计近30天申请趋势
     */
    public List<java.util.Map<String, Object>> selectTrendStat();

    /**
     * 统计社团申请排名
     */
    public List<java.util.Map<String, Object>> selectClubRankingStat();

    /**
     * 统计昨日各状态申请数量
     */
    public List<java.util.Map<String, Object>> selectYesterdayStatusStat();

    /**
     * 按社团分组统计趋势数据
     */
    public List<java.util.Map<String, Object>> selectTrendStatByClub();

    /**
     * 按社团分组统计状态分布
     */
    public List<java.util.Map<String, Object>> selectStatusStatByClub();
}
