package com.ruoyi.user.service;

import java.util.List;
import com.ruoyi.user.domain.Club;

/**
 * 社团信息Service接口
 */
public interface IClubService {
    List<Club> selectClubList(Club club);

    Club selectClubById(Long clubId);

    List<Club> selectPopularClubs(int limit);

    int insertClub(Club club);
}
