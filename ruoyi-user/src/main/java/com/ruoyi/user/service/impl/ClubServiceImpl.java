package com.ruoyi.user.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
