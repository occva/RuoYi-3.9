package com.ruoyi.user.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 活动报名对象 club_activity_registration
 * 
 * @author ruoyi
 */
public class ClubActivityRegistration extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 报名ID */
    private Long registrationId;

    /** 活动ID */
    @Excel(name = "活动ID")
    private Long activityId;

    /** 社团ID */
    private Long clubId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户账号 */
    @Excel(name = "用户账号")
    private String userName;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickName;

    /** 学号 */
    @Excel(name = "学号")
    private String studentId;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 报名时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报名时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date registrationTime;

    /** 签到状态（0未签到 1已签到） */
    @Excel(name = "签到状态", readConverterExp = "0=未签到,1=已签到")
    private String checkInStatus;

    /** 签到时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "签到时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date checkInTime;

    /** 状态（0待参加 1已参加 2已取消） */
    @Excel(name = "状态", readConverterExp = "0=待参加,1=已参加,2=已取消")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    // 扩展字段
    private String activityTitle;
    private String clubName;

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setCheckInStatus(String checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public String getCheckInStatus() {
        return checkInStatus;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Date getCheckInTime() {
        return checkInTime;
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

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("registrationId", getRegistrationId())
                .append("activityId", getActivityId())
                .append("clubId", getClubId())
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("nickName", getNickName())
                .append("studentId", getStudentId())
                .append("phone", getPhone())
                .append("registrationTime", getRegistrationTime())
                .append("checkInStatus", getCheckInStatus())
                .append("checkInTime", getCheckInTime())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
