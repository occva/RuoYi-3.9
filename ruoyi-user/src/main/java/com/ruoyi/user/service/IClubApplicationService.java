package com.ruoyi.user.service;

import java.util.List;
import com.ruoyi.user.domain.ClubApplication;

/**
 * 入社申请Service接口
 * 
 * @author ruoyi
 */
public interface IClubApplicationService {
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
     * 审核入社申请
     * 
     * @param clubApplication 入社申请
     * @return 结果
     */
    public int reviewApplication(ClubApplication clubApplication);

    /**
     * 批量删除入社申请
     * 
     * @param applicationIds 需要删除的入社申请ID
     * @return 结果
     */
    public int deleteClubApplicationByIds(Long[] applicationIds);

    /**
     * 删除入社申请信息
     * 
     * @param applicationId 入社申请ID
     * @return 结果
     */
    public int deleteClubApplicationById(Long applicationId);
}
