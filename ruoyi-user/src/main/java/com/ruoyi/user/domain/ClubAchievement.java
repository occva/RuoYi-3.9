package com.ruoyi.user.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 社团荣誉/成就对象 club_achievement
 * 
 * @author ruoyi
 */
public class ClubAchievement extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 成就ID */
    private Long achievementId;

    /** 社团ID */
    @Excel(name = "社团ID")
    private Long clubId;

    /** 社团名称 */
    @Excel(name = "社团名称")
    private String clubName;

    /** 成就标题 */
    @Excel(name = "成就标题")
    private String achievementTitle;

    /** 成就类型（award/competition/activity） */
    @Excel(name = "成就类型")
    private String achievementType;

    /** 详细描述 */
    @Excel(name = "详细描述")
    private String description;

    /** 图片地址 */
    @Excel(name = "图片地址")
    private String imageUrl;

    /** 获得日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "获得日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date achieveDate;

    /** 级别（校级/市级/省级/国家级） */
    @Excel(name = "级别")
    private String level;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Integer sortOrder;

    /** 状态（0正常 1隐藏） */
    @Excel(name = "状态")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setAchievementId(Long achievementId) {
        this.achievementId = achievementId;
    }

    public Long getAchievementId() {
        return achievementId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubName() {
        return clubName;
    }

    public void setAchievementTitle(String achievementTitle) {
        this.achievementTitle = achievementTitle;
    }

    public String getAchievementTitle() {
        return achievementTitle;
    }

    public void setAchievementType(String achievementType) {
        this.achievementType = achievementType;
    }

    public String getAchievementType() {
        return achievementType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setAchieveDate(Date achieveDate) {
        this.achieveDate = achieveDate;
    }

    public Date getAchieveDate() {
        return achieveDate;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return "ClubAchievement{" +
                "achievementId=" + achievementId +
                ", clubId=" + clubId +
                ", clubName='" + clubName + '\'' +
                ", achievementTitle='" + achievementTitle + '\'' +
                ", achievementType='" + achievementType + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", achieveDate=" + achieveDate +
                ", level='" + level + '\'' +
                ", sortOrder=" + sortOrder +
                ", status='" + status + '\'' +
                ", delFlag='" + delFlag + '\'' +
                '}';
    }
}
