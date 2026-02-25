package com.ruoyi.user.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.user.domain.ClubNotice;
import com.ruoyi.user.mapper.ClubNoticeMapper;
import com.ruoyi.user.service.IClubNoticeService;

/**
 * 社团公告Service实现
 */
@Service
public class ClubNoticeServiceImpl implements IClubNoticeService {

    @Autowired
    private ClubNoticeMapper clubNoticeMapper;

    @Autowired
    private ClubDataScopeHelper dataScopeHelper;

    @Override
    public List<ClubNotice> selectClubNoticeList(ClubNotice notice) {
        // 数据隔离：社长/副社长只能看自己管理社团的公告
        java.util.List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds != null) {
            notice.getParams().put("clubIds", managedClubIds);
        }
        return clubNoticeMapper.selectClubNoticeList(notice);
    }

    @Override
    public ClubNotice selectClubNoticeById(Long noticeId) {
        return clubNoticeMapper.selectClubNoticeById(noticeId);
    }

    @Override
    public List<ClubNotice> selectNoticeByClubId(Long clubId) {
        return clubNoticeMapper.selectNoticeByClubId(clubId);
    }

    @Override
    public int insertClubNotice(ClubNotice notice) {
        return clubNoticeMapper.insertClubNotice(notice);
    }

    @Override
    public int updateClubNotice(ClubNotice notice) {
        return clubNoticeMapper.updateClubNotice(notice);
    }

    @Override
    public int deleteClubNoticeById(Long noticeId) {
        return clubNoticeMapper.deleteClubNoticeById(noticeId);
    }

    @Override
    public int deleteClubNoticeByIds(Long[] noticeIds) {
        return clubNoticeMapper.deleteClubNoticeByIds(noticeIds);
    }
}
