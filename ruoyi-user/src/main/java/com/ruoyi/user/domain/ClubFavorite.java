package com.ruoyi.user.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 社团收藏对象 club_favorite
 * 
 * @author ruoyi
 */
public class ClubFavorite extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 收藏ID */
    private Long favoriteId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 社团ID */
    @Excel(name = "社团ID")
    private Long clubId;

    /** 社团名称 (非数据库字段) */
    private String clubName;

    /** 社团Logo (非数据库字段) */
    private String logoUrl;

    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public Long getClubId() {
        return clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("favoriteId", getFavoriteId())
                .append("userId", getUserId())
                .append("clubId", getClubId())
                .append("createTime", getCreateTime())
                .toString();
    }
}
