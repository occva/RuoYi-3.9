package com.ruoyi.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.user.domain.Club;
import com.ruoyi.user.mapper.ClubMapper;
import com.ruoyi.user.service.IClubService;

/**
 * 社团信息Service实现
 */
@Service
public class ClubServiceImpl implements IClubService {

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private ClubDataScopeHelper dataScopeHelper;

    @Override
    public List<Club> selectClubList(Club club) {
        // 数据隔离：社长/副社长只能看自己管理的社团
        List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds != null) {
            club.getParams().put("clubIds", managedClubIds);
        }
        return clubMapper.selectClubList(club);
    }

    @Override
    public Club selectClubById(Long clubId) {
        Club club = clubMapper.selectClubById(clubId);
        if (club == null) {
            return null;
        }
        ensureClubInScope(club.getClubId(), "无权查看该社团");
        return club;
    }

    @Override
    public int incrementViewCount(Long clubId) {
        return clubMapper.incrementViewCount(clubId);
    }

    @Override
    public List<Club> selectPopularClubs(int limit) {
        return clubMapper.selectPopularClubs(limit);
    }

    @Override
    public int insertClub(Club club) {
        if (dataScopeHelper.getManagedClubIds() != null) {
            throw new ServiceException("无权新增社团");
        }
        return clubMapper.insertClub(club);
    }

    @Override
    public int updateClub(Club club) {
        Club existing = requireClub(club.getClubId());
        ensureClubInScope(existing.getClubId(), "无权操作该社团");
        return clubMapper.updateClub(club);
    }

    @Override
    public int deleteClubById(Long clubId) {
        Club existing = requireClub(clubId);
        ensureClubInScope(existing.getClubId(), "无权删除该社团");
        return clubMapper.deleteClubById(clubId);
    }

    @Override
    public int deleteClubByIds(Long[] clubIds) {
        authorizeClubIds(clubIds, "包含不存在或无权限操作的社团");
        return clubMapper.deleteClubByIds(clubIds);
    }

    @Override
    public boolean checkClubCodeUnique(Club club) {
        Long clubId = StringUtils.isNull(club.getClubId()) ? -1L : club.getClubId();
        Club info = clubMapper.checkClubCodeUnique(club.getClubCode());
        if (StringUtils.isNotNull(info) && info.getClubId().longValue() != clubId.longValue()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkClubNameUnique(Club club) {
        Long clubId = StringUtils.isNull(club.getClubId()) ? -1L : club.getClubId();
        Club info = clubMapper.checkClubNameUnique(club.getClubName());
        if (StringUtils.isNotNull(info) && info.getClubId().longValue() != clubId.longValue()) {
            return false;
        }
        return true;
    }

    @Override
    public int updateClubStatus(Long[] clubIds, String status, String updateBy) {
        authorizeClubIds(clubIds, "包含不存在或无权限操作的社团");
        return clubMapper.updateClubStatus(clubIds, status, updateBy);
    }

    @Override
    public int updateClubPopular(Long[] clubIds, String isPopular, String updateBy) {
        authorizeClubIds(clubIds, "包含不存在或无权限操作的社团");
        return clubMapper.updateClubPopular(clubIds, isPopular, updateBy);
    }

    @Override
    public List<Club> selectClubListByUserId(Long userId) {
        return clubMapper.selectClubListByUserId(userId);
    }

    @Override
    public List<Club> selectClubListByPresidentId(Long userId) {
        return clubMapper.selectClubListByPresidentId(userId);
    }

    @Override
    public java.util.Map<String, Object> getStatData(String beginTime, String endTime) {
        List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("statusStat", clubMapper.selectClubStatusStat(beginTime, endTime, managedClubIds));
        map.put("recruitingCount", clubMapper.selectClubRecruitingCount(beginTime, endTime, managedClubIds));
        map.put("todayStats", clubMapper.selectClubTodayStats(managedClubIds));
        map.put("trendStat", clubMapper.selectClubTrendStat(managedClubIds));
        map.put("categoryStat", clubMapper.selectClubCategoryStat(managedClubIds));
        map.put("memberRanking", clubMapper.selectClubMemberRanking(managedClubIds));
        return map;
    }

    private Club requireClub(Long clubId) {
        Club club = clubMapper.selectClubById(clubId);
        if (club == null) {
            throw new ServiceException("社团不存在");
        }
        return club;
    }

    private void authorizeClubIds(Long[] clubIds, String message) {
        List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds == null || clubIds == null || clubIds.length == 0) {
            return;
        }

        List<Long> authorizedIds = new ArrayList<>();
        for (Long clubId : clubIds) {
            Club club = clubMapper.selectClubById(clubId);
            if (club != null && managedClubIds.contains(club.getClubId())) {
                authorizedIds.add(clubId);
            }
        }
        if (authorizedIds.size() != clubIds.length) {
            throw new ServiceException(message);
        }
    }

    private void ensureClubInScope(Long clubId, String message) {
        if (!dataScopeHelper.isManagedClub(clubId)) {
            throw new ServiceException(message);
        }
    }
}
