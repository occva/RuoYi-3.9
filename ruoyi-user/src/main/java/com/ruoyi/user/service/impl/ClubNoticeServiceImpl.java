package com.ruoyi.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
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
        ClubNotice notice = clubNoticeMapper.selectClubNoticeById(noticeId);
        if (notice == null) {
            return null;
        }
        if (!dataScopeHelper.isManagedClub(notice.getClubId())) {
            throw new ServiceException("无权查看该社团公告");
        }
        return notice;
    }

    @Override
    public List<ClubNotice> selectNoticeByClubId(Long clubId) {
        return clubNoticeMapper.selectNoticeByClubId(clubId);
    }

    @Override
    public int insertClubNotice(ClubNotice notice) {
        validateCreateClubId(notice.getClubId());
        return clubNoticeMapper.insertClubNotice(notice);
    }

    @Override
    public int updateClubNotice(ClubNotice notice) {
        ClubNotice existing = requireNotice(notice.getNoticeId());
        if (!dataScopeHelper.isManagedClub(existing.getClubId())) {
            throw new ServiceException("无权操作该社团公告");
        }
        if (dataScopeHelper.getManagedClubIds() != null
                && notice.getClubId() != null
                && !notice.getClubId().equals(existing.getClubId())) {
            throw new ServiceException("不允许修改公告所属社团");
        }
        return clubNoticeMapper.updateClubNotice(notice);
    }

    @Override
    public int deleteClubNoticeById(Long noticeId) {
        ClubNotice existing = requireNotice(noticeId);
        if (!dataScopeHelper.isManagedClub(existing.getClubId())) {
            throw new ServiceException("无权操作该社团公告");
        }
        return clubNoticeMapper.deleteClubNoticeById(noticeId);
    }

    @Override
    public int deleteClubNoticeByIds(Long[] noticeIds) {
        List<Long> managedClubIds = dataScopeHelper.getManagedClubIds();
        if (managedClubIds == null) {
            return clubNoticeMapper.deleteClubNoticeByIds(noticeIds);
        }

        List<Long> authorizedIds = new ArrayList<>();
        for (Long noticeId : noticeIds) {
            ClubNotice notice = clubNoticeMapper.selectClubNoticeById(noticeId);
            if (notice != null && managedClubIds.contains(notice.getClubId())) {
                authorizedIds.add(noticeId);
            }
        }
        if (authorizedIds.size() != noticeIds.length) {
            throw new ServiceException("包含不存在或无权限操作的公告");
        }
        return clubNoticeMapper.deleteClubNoticeByIds(authorizedIds.toArray(new Long[0]));
    }

    private ClubNotice requireNotice(Long noticeId) {
        ClubNotice notice = clubNoticeMapper.selectClubNoticeById(noticeId);
        if (notice == null) {
            throw new ServiceException("公告不存在");
        }
        return notice;
    }

    private void validateCreateClubId(Long clubId) {
        if (clubId == null) {
            throw new ServiceException("所属社团不能为空");
        }
        if (!dataScopeHelper.isManagedClub(clubId)) {
            throw new ServiceException("无权为其他社团创建公告");
        }
    }
}
