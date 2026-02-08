package com.ruoyi.user.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 入社申请对象 club_application
 * 
 * @author ruoyi
 */
public class ClubApplication extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    private Long applicationId;

    /** 社团ID */
    @Excel(name = "社团ID")
    private Long clubId;

    /** 社团名称 */
    @Excel(name = "社团名称")
    private String clubName;

    /** 社团Logo (关联查询) */
    private String logoUrl;

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

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 性别（0男 1女 2未知） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女,2=未知")
    private String gender;

    /** 专业 */
    @Excel(name = "专业")
    private String major;

    /** 年级 */
    @Excel(name = "年级")
    private String grade;

    /** 班级 */
    @Excel(name = "班级")
    private String className;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 自我介绍 */
    @Excel(name = "自我介绍")
    private String selfIntroduction;

    /** 申请理由 */
    @Excel(name = "申请理由")
    private String applyReason;

    /** 特长 */
    @Excel(name = "特长")
    private String specialSkills;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date applicationTime;

    /** 审核状态（0待审核 1已通过 2已拒绝 3已撤回） */
    @Excel(name = "审核状态", readConverterExp = "0=待审核,1=已通过,2=已拒绝,3=已撤回")
    private String status;

    /** 审核人ID */
    @Excel(name = "审核人ID")
    private Long reviewerId;

    /** 审核人姓名 */
    @Excel(name = "审核人姓名")
    private String reviewerName;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    /** 审核意见 */
    @Excel(name = "审核意见")
    private String reviewComment;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getApplicationId() {
        return applicationId;
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

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
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

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMajor() {
        return major;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setSpecialSkills(String specialSkills) {
        this.specialSkills = specialSkills;
    }

    public String getSpecialSkills() {
        return specialSkills;
    }

    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }

    public Date getApplicationTime() {
        return applicationTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("applicationId", getApplicationId())
                .append("clubId", getClubId())
                .append("clubName", getClubName())
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("nickName", getNickName())
                .append("studentId", getStudentId())
                .append("realName", getRealName())
                .append("gender", getGender())
                .append("major", getMajor())
                .append("grade", getGrade())
                .append("className", getClassName())
                .append("phone", getPhone())
                .append("email", getEmail())
                .append("selfIntroduction", getSelfIntroduction())
                .append("applyReason", getApplyReason())
                .append("specialSkills", getSpecialSkills())
                .append("applicationTime", getApplicationTime())
                .append("status", getStatus())
                .append("reviewerId", getReviewerId())
                .append("reviewerName", getReviewerName())
                .append("reviewTime", getReviewTime())
                .append("reviewComment", getReviewComment())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
