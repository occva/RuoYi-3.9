package com.ruoyi.user.mapper;

import java.util.List;
import com.ruoyi.user.domain.Club;

/**
 * 社团信息Mapper接口
 */
public interface ClubMapper {
    List<Club> selectClubList(Club club);

    Club selectClubById(Long clubId);

    List<Club> selectPopularClubs(int limit);

    int insertClub(Club club);
}
