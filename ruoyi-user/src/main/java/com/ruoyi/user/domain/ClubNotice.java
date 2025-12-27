package com.ruoyi.user.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 社团公告对象 club_notice
 */
public class ClubNotice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long noticeId;
    private Long clubId;

    @Excel(name = "公告标题")
    private String noticeTitle;

    private String noticeType;
    private String noticeContent;
    private String coverUrl;
    private String isTop;
    private String isImportant;
    private Integer viewCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    private Long publisherId;
    private String publisherName;
    private String status;
    private String delFlag;

    /** 关联社团名称 */
    private String clubName;

    public Long getNoticeId() { return noticeId; }
    public void setNoticeId(Long noticeId) { this.noticeId = noticeId; }
    public Long getClubId() { return clubId; }
    public void setClubId(Long clubId) { this.clubId = clubId; }
    public String getNoticeTitle() { return noticeTitle; }
    public void setNoticeTitle(String noticeTitle) { this.noticeTitle = noticeTitle; }
    public String getNoticeType() { return noticeType; }
    public void setNoticeType(String noticeType) { this.noticeType = noticeType; }
    public String getNoticeContent() { return noticeContent; }
    public void setNoticeContent(String noticeContent) { this.noticeContent = noticeContent; }
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
    public String getIsTop() { return isTop; }
    public void setIsTop(String isTop) { this.isTop = isTop; }
    public String getIsImportant() { return isImportant; }
    public void setIsImportant(String isImportant) { this.isImportant = isImportant; }
    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public Date getPublishTime() { return publishTime; }
    public void setPublishTime(Date publishTime) { this.publishTime = publishTime; }
    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }
    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
    public String getClubName() { return clubName; }
    public void setClubName(String clubName) { this.clubName = clubName; }
}
