package com.ruoyi.user.service;

import java.util.List;
import com.ruoyi.user.domain.ClubFavorite;

/**
 * 社团收藏Service接口
 * 
 * @author ruoyi
 */
public interface IClubFavoriteService {
    /**
     * 查询社团收藏
     * 
     * @param favoriteId 社团收藏主键
     * @return 社团收藏
     */
    public ClubFavorite selectClubFavoriteById(Long favoriteId);

    /**
     * 查询社团收藏列表
     * 
     * @param clubFavorite 社团收藏
     * @return 社团收藏集合
     */
    public List<ClubFavorite> selectClubFavoriteList(ClubFavorite clubFavorite);

    /**
     * 新增社团收藏
     * 
     * @param clubFavorite 社团收藏
     * @return 结果
     */
    public int insertClubFavorite(ClubFavorite clubFavorite);

    /**
     * 修改社团收藏
     * 
     * @param clubFavorite 社团收藏
     * @return 结果
     */
    public int updateClubFavorite(ClubFavorite clubFavorite);

    /**
     * 批量删除社团收藏
     * 
     * @param favoriteIds 需要删除的社团收藏主键集合
     * @return 结果
     */
    public int deleteClubFavoriteByIds(Long[] favoriteIds);

    /**
     * 删除社团收藏信息
     * 
     * @param favoriteId 社团收藏主键
     * @return 结果
     */
    public int deleteClubFavoriteById(Long favoriteId);

    /**
     * 切换收藏状态
     */
    public boolean toggleFavorite(Long userId, Long clubId);

    /**
     * 检查是否收藏
     */
    public boolean isFavorite(Long userId, Long clubId);
}
