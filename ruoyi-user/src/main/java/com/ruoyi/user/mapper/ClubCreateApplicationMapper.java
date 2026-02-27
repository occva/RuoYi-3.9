package com.ruoyi.user.mapper;

import java.util.List;
import com.ruoyi.user.domain.ClubCreateApplication;

/**
 * 新社团申请 Mapper
 */
public interface ClubCreateApplicationMapper {
    ClubCreateApplication selectClubCreateApplicationById(Long applyId);

    List<ClubCreateApplication> selectClubCreateApplicationList(ClubCreateApplication query);

    int insertClubCreateApplication(ClubCreateApplication application);

    int updateClubCreateApplication(ClubCreateApplication application);

    int deleteClubCreateApplicationById(Long applyId);

    int deleteClubCreateApplicationByIds(Long[] applyIds);
}
