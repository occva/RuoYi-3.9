package com.ruoyi.user.domain;

import java.io.Serial;
import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 通知已读请求
 */
public class AppNotificationReadRequest implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "通知来源不能为空")
    private String noticeSource;

    @NotNull(message = "通知ID不能为空")
    private Long noticeId;

    public String getNoticeSource()
    {
        return noticeSource;
    }

    public void setNoticeSource(String noticeSource)
    {
        this.noticeSource = noticeSource;
    }

    public Long getNoticeId()
    {
        return noticeId;
    }

    public void setNoticeId(Long noticeId)
    {
        this.noticeId = noticeId;
    }
}
