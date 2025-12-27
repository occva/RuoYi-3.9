package com.ruoyi.user.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Override
    public List<Club> selectClubList(Club club) {
        return clubMapper.selectClubList(club);
    }

    @Override
    public Club selectClubById(Long clubId) {
        return clubMapper.selectClubById(clubId);
    }

    @Override
    public List<Club> selectPopularClubs(int limit) {
        return clubMapper.selectPopularClubs(limit);
    }

    @Override
    public int insertClub(Club club) {
        return clubMapper.insertClub(club);
    }

    @Override
    public int updateClub(Club club) {
        return clubMapper.updateClub(club);
    }

    @Override
    public int deleteClubById(Long clubId) {
        return clubMapper.deleteClubById(clubId);
    }

    @Override
    public int deleteClubByIds(Long[] clubIds) {
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
        return clubMapper.updateClubStatus(clubIds, status, updateBy);
    }

    @Override
    public int updateClubPopular(Long[] clubIds, String isPopular, String updateBy) {
        return clubMapper.updateClubPopular(clubIds, isPopular, updateBy);
    }
}
