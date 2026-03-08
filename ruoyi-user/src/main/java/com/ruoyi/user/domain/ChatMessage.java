package com.ruoyi.user.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * AI聊天消息 MongoDB Document
 */
@Document(collection = "chat_messages")
@CompoundIndex(name = "idx_session_time", def = "{'sessionId': 1, 'createTime': 1}")
public class ChatMessage {
    @Id
    private String id;

    /** 会话ID */
    @Indexed
    private String sessionId;

    /** 用户ID（可为空，支持匿名） */
    @Indexed
    private Long userId;

    @Indexed
    private String guestId;

    /** 角色：user / assistant / system */
    private String role;

    /** 消息内容 */
    private String content;

    /** AI响应耗时（毫秒） */
    private Long responseTime;

    /** 创建时间 */
    private Date createTime;

    public ChatMessage() {
    }

    public ChatMessage(String sessionId, Long userId, String role, String content) {
        this(sessionId, userId, null, role, content);
    }

    public ChatMessage(String sessionId, Long userId, String guestId, String role, String content) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.guestId = guestId;
        this.role = role;
        this.content = content;
        this.createTime = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
