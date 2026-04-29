package com.ruoyi.user.domain;

/**
 * AI 工具调用轨迹，用于前端展示本次回复调用了哪些只读工具。
 */
public class AiToolTrace {
    private String name;
    private String label;
    private String status;
    private String argsSummary;
    private String resultSummary;
    private Long durationMs;
    private String errorMessage;

    public AiToolTrace() {
    }

    public AiToolTrace(String name, String label) {
        this.name = name;
        this.label = label;
        this.status = "running";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getArgsSummary() {
        return argsSummary;
    }

    public void setArgsSummary(String argsSummary) {
        this.argsSummary = argsSummary;
    }

    public String getResultSummary() {
        return resultSummary;
    }

    public void setResultSummary(String resultSummary) {
        this.resultSummary = resultSummary;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
