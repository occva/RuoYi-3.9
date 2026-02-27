package com.ruoyi.user.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 新社团申请对象 club_create_application
 */
public class ClubCreateApplication extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    private Long applyId;

    /** 拟创建社团名称 */
    @Excel(name = "社团名称")
    private String clubName;

    /** 社团分类ID */
    @Excel(name = "分类ID")
    private Long categoryId;

    /** 社团分类名称（关联查询） */
    @Excel(name = "分类")
    private String categoryName;

    /** 社团Logo */
    private String logoUrl;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String contactPhone;

    /** 社团简介 */
    @Excel(name = "社团简介")
    private String description;

    /** 申请理由 */
    @Excel(name = "申请理由")
    private String applyReason;

    /** 活动规划 */
    @Excel(name = "活动规划")
    private String activityPlan;

    /** 核心成员说明 */
    @Excel(name = "核心成员说明")
    private String coreMembers;

    /** 指导老师姓名 */
    @Excel(name = "指导老师")
    private String advisorName;

    /** 指导老师联系方式 */
    @Excel(name = "指导老师联系方式")
    private String advisorContact;

    /** 申请人ID（用户端账号） */
    @Excel(name = "申请人ID")
    private Long applicantUserId;

    /** 申请人账号 */
    @Excel(name = "申请人账号")
    private String applicantUserName;

    /** 申请人昵称 */
    @Excel(name = "申请人昵称")
    private String applicantNickName;

    /** 申请人手机号 */
    private String applicantPhone;

    /** 申请人邮箱 */
    private String applicantEmail;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /** 审核状态（0待审核 1通过 2拒绝） */
    @Excel(name = "审核状态", readConverterExp = "0=待审核,1=通过,2=拒绝")
    private String status;

    /** 审核人ID */
    private Long reviewerId;

    /** 审核人名称 */
    @Excel(name = "审核人")
    private String reviewerName;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    /** 审核意见 */
    @Excel(name = "审核意见")
    private String reviewComment;

    /** 审核通过后创建的社团ID */
    private Long approvedClubId;

    /** 审核通过后创建的后台账号ID */
    private Long adminUserId;

    /** 审核通过后创建的后台账号 */
    @Excel(name = "社长后台账号")
    private String adminUserName;

    /** 初始密码（仅用于告知） */
    private String adminInitPassword;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getActivityPlan() {
        return activityPlan;
    }

    public void setActivityPlan(String activityPlan) {
        this.activityPlan = activityPlan;
    }

    public String getCoreMembers() {
        return coreMembers;
    }

    public void setCoreMembers(String coreMembers) {
        this.coreMembers = coreMembers;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public void setAdvisorName(String advisorName) {
        this.advisorName = advisorName;
    }

    public String getAdvisorContact() {
        return advisorContact;
    }

    public void setAdvisorContact(String advisorContact) {
        this.advisorContact = advisorContact;
    }

    public Long getApplicantUserId() {
        return applicantUserId;
    }

    public void setApplicantUserId(Long applicantUserId) {
        this.applicantUserId = applicantUserId;
    }

    public String getApplicantUserName() {
        return applicantUserName;
    }

    public void setApplicantUserName(String applicantUserName) {
        this.applicantUserName = applicantUserName;
    }

    public String getApplicantNickName() {
        return applicantNickName;
    }

    public void setApplicantNickName(String applicantNickName) {
        this.applicantNickName = applicantNickName;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public Long getApprovedClubId() {
        return approvedClubId;
    }

    public void setApprovedClubId(Long approvedClubId) {
        this.approvedClubId = approvedClubId;
    }

    public Long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getAdminInitPassword() {
        return adminInitPassword;
    }

    public void setAdminInitPassword(String adminInitPassword) {
        this.adminInitPassword = adminInitPassword;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("applyId", getApplyId())
                .append("clubName", getClubName())
                .append("categoryId", getCategoryId())
                .append("categoryName", getCategoryName())
                .append("logoUrl", getLogoUrl())
                .append("contactPhone", getContactPhone())
                .append("description", getDescription())
                .append("applyReason", getApplyReason())
                .append("activityPlan", getActivityPlan())
                .append("coreMembers", getCoreMembers())
                .append("advisorName", getAdvisorName())
                .append("advisorContact", getAdvisorContact())
                .append("applicantUserId", getApplicantUserId())
                .append("applicantUserName", getApplicantUserName())
                .append("applicantNickName", getApplicantNickName())
                .append("applicantPhone", getApplicantPhone())
                .append("applicantEmail", getApplicantEmail())
                .append("applyTime", getApplyTime())
                .append("status", getStatus())
                .append("reviewerId", getReviewerId())
                .append("reviewerName", getReviewerName())
                .append("reviewTime", getReviewTime())
                .append("reviewComment", getReviewComment())
                .append("approvedClubId", getApprovedClubId())
                .append("adminUserId", getAdminUserId())
                .append("adminUserName", getAdminUserName())
                .append("adminInitPassword", getAdminInitPassword())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
