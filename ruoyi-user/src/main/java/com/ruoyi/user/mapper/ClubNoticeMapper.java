package com.ruoyi.user.mapper;

import java.util.List;
import com.ruoyi.user.domain.ClubNotice;

/**
 * 社团公告Mapper接口
 */
public interface ClubNoticeMapper {
    List<ClubNotice> selectClubNoticeList(ClubNotice notice);

    ClubNotice selectClubNoticeById(Long noticeId);

    List<ClubNotice> selectNoticeByClubId(Long clubId);

    int insertClubNotice(ClubNotice notice);

    int updateClubNotice(ClubNotice notice);

    int deleteClubNoticeById(Long noticeId);

    int deleteClubNoticeByIds(Long[] noticeIds);
}
