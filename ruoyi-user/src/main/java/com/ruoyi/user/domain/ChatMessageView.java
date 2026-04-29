package com.ruoyi.user.domain;

import java.util.Date;
import java.util.List;

/**
 * AI聊天消息前端视图，不暴露内部 userId。
 */
public class ChatMessageView {
    private String id;
    private String sessionId;
    private String userName;
    private String role;
    private String content;
    private Long responseTime;
    private List<AiToolTrace> tools;
    private Date createTime;

    public ChatMessageView() {
    }

    public ChatMessageView(ChatMessage message, String userName) {
        this.id = message.getId();
        this.sessionId = message.getSessionId();
        this.userName = userName;
        this.role = message.getRole();
        this.content = message.getContent();
        this.responseTime = message.getResponseTime();
        this.tools = message.getTools();
        this.createTime = message.getCreateTime();
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public List<AiToolTrace> getTools() {
        return tools;
    }

    public void setTools(List<AiToolTrace> tools) {
        this.tools = tools;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
