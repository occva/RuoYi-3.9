package com.ruoyi.user.service;

import java.util.List;
import com.ruoyi.user.domain.Club;

/**
 * 社团信息Service接口
 */
public interface IClubService {
    /**
     * 查询社团列表
     */
    List<Club> selectClubList(Club club);

    /**
     * 根据ID查询社团
     */
    Club selectClubById(Long clubId);

    /**
     * 浏览量 +1
     */
    int incrementViewCount(Long clubId);

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
    boolean checkClubCodeUnique(Club club);

    /**
     * 校验社团名称是否唯一
     */
    boolean checkClubNameUnique(Club club);

    /**
     * 批量更新社团状态
     */
    int updateClubStatus(Long[] clubIds, String status, String updateBy);

    /**
     * 批量设置热门
     */
    int updateClubPopular(Long[] clubIds, String isPopular, String updateBy);

    /**
     * 查询用户加入的社团列表
     */
    List<Club> selectClubListByUserId(Long userId);

    /**
     * 查询用户管理的社团列表
     */
    List<Club> selectClubListByPresidentId(Long userId);

    /**
     * 获取社团统计数据
     */
    java.util.Map<String, Object> getStatData(String beginTime, String endTime);
}
