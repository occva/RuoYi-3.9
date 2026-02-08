package com.ruoyi.user.mapper;

import java.util.List;
import com.ruoyi.user.domain.ClubFavorite;

/**
 * 社团收藏Mapper接口
 * 
 * @author ruoyi
 */
public interface ClubFavoriteMapper {
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
     * 删除社团收藏
     * 
     * @param favoriteId 社团收藏主键
     * @return 结果
     */
    public int deleteClubFavoriteById(Long favoriteId);

    /**
     * 批量删除社团收藏
     * 
     * @param favoriteIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteClubFavoriteByIds(Long[] favoriteIds);

    /**
     * 检查是否收藏
     * 
     * @param userId 用户ID
     * @param clubId 社团ID
     * @return 结果
     */
    public ClubFavorite checkFavorite(Long userId, Long clubId);

    /**
     * 删除收藏 by user and club
     */
    public int deleteClubFavoriteByUserAndClub(Long userId, Long clubId);
}
