package com.ruoyi.user.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 社团活动对象 club_activity
 */
public class ClubActivity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long activityId;
    private Long clubId;

    @Excel(name = "活动标题")
    private String activityTitle;

    private String activityType;
    private String description;
    private String coverUrl;
    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registrationStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registrationEnd;

    /** 当前用户报名时间（仅“我报名的活动”场景有值） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registrationTime;

    private Integer maxParticipants;
    private Integer currentParticipants;
    private String status;
    private String organizer;
    private String contactInfo;
    private String isPublic;
    private Integer viewCount;
    private String delFlag;

    /** 关联社团名称 */
    private String clubName;

    /** 当前登录用户是否已报名（非数据库字段） */
    private Boolean hasRegistered;

    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public Long getClubId() { return clubId; }
    public void setClubId(Long clubId) { this.clubId = clubId; }
    public String getActivityTitle() { return activityTitle; }
    public void setActivityTitle(String activityTitle) { this.activityTitle = activityTitle; }
    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }
    public Date getRegistrationStart() { return registrationStart; }
    public void setRegistrationStart(Date registrationStart) { this.registrationStart = registrationStart; }
    public Date getRegistrationEnd() { return registrationEnd; }
    public void setRegistrationEnd(Date registrationEnd) { this.registrationEnd = registrationEnd; }
    public Date getRegistrationTime() { return registrationTime; }
    public void setRegistrationTime(Date registrationTime) { this.registrationTime = registrationTime; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    public Integer getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(Integer currentParticipants) { this.currentParticipants = currentParticipants; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public String getIsPublic() { return isPublic; }
    public void setIsPublic(String isPublic) { this.isPublic = isPublic; }
    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
    public String getClubName() { return clubName; }
    public void setClubName(String clubName) { this.clubName = clubName; }
    public Boolean getHasRegistered() { return hasRegistered; }
    public void setHasRegistered(Boolean hasRegistered) { this.hasRegistered = hasRegistered; }
}
