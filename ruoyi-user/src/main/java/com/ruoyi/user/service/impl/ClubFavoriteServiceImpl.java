package com.ruoyi.user.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.user.mapper.ClubFavoriteMapper;
import com.ruoyi.user.domain.ClubFavorite;
import com.ruoyi.user.service.IClubFavoriteService;
import com.ruoyi.common.utils.DateUtils;

/**
 * 社团收藏Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class ClubFavoriteServiceImpl implements IClubFavoriteService {
    @Autowired
    private ClubFavoriteMapper clubFavoriteMapper;

    /**
     * 查询社团收藏
     * 
     * @param favoriteId 社团收藏主键
     * @return 社团收藏
     */
    @Override
    public ClubFavorite selectClubFavoriteById(Long favoriteId) {
        return clubFavoriteMapper.selectClubFavoriteById(favoriteId);
    }

    /**
     * 查询社团收藏列表
     * 
     * @param clubFavorite 社团收藏
     * @return 社团收藏
     */
    @Override
    public List<ClubFavorite> selectClubFavoriteList(ClubFavorite clubFavorite) {
        return clubFavoriteMapper.selectClubFavoriteList(clubFavorite);
    }

    /**
     * 新增社团收藏
     * 
     * @param clubFavorite 社团收藏
     * @return 结果
     */
    @Override
    public int insertClubFavorite(ClubFavorite clubFavorite) {
        clubFavorite.setCreateTime(DateUtils.getNowDate());
        return clubFavoriteMapper.insertClubFavorite(clubFavorite);
    }

    /**
     * 修改社团收藏
     * 
     * @param clubFavorite 社团收藏
     * @return 结果
     */
    @Override
    public int updateClubFavorite(ClubFavorite clubFavorite) {
        return clubFavoriteMapper.updateClubFavorite(clubFavorite);
    }

    /**
     * 批量删除社团收藏
     * 
     * @param favoriteIds 需要删除的社团收藏主键
     * @return 结果
     */
    @Override
    public int deleteClubFavoriteByIds(Long[] favoriteIds) {
        return clubFavoriteMapper.deleteClubFavoriteByIds(favoriteIds);
    }

    /**
     * 删除社团收藏信息
     * 
     * @param favoriteId 社团收藏主键
     * @return 结果
     */
    @Override
    public int deleteClubFavoriteById(Long favoriteId) {
        return clubFavoriteMapper.deleteClubFavoriteById(favoriteId);
    }

    @Override
    public boolean toggleFavorite(Long userId, Long clubId) {
        ClubFavorite existing = clubFavoriteMapper.checkFavorite(userId, clubId);
        if (existing != null) {
            // Already favorite, remove it
            clubFavoriteMapper.deleteClubFavoriteByUserAndClub(userId, clubId);
            return false; // Removed
        } else {
            // accessible add
            ClubFavorite fav = new ClubFavorite();
            fav.setUserId(userId);
            fav.setClubId(clubId);
            fav.setCreateTime(new Date());
            clubFavoriteMapper.insertClubFavorite(fav);
            return true; // Added
        }
    }

    @Override
    public boolean isFavorite(Long userId, Long clubId) {
        return clubFavoriteMapper.checkFavorite(userId, clubId) != null;
    }
}
