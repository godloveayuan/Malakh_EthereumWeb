package com.ethereum.bean;

import java.util.Date;

/**
 * @Author: Malakh
 * @Date: 2020/2/18
 * @Description: 访问日志
 */

public class AccessLog {
    private Integer id;             // 主键id
    private String subjectUid;      // 请求主体
    private String subjectName;     // 请求主体
    private String objectUid;       // 请求客体
    private String objectName;      // 请求客体
    private String operateType;     // 请求操作
    private String operateData;     // 操作数据
    private Boolean accessAllow;    // 是否允许访问
    private String accessAllowName; // 是否允许访问：true-允许访问 false-拒绝访问
    private Integer failReason;     // 访问失败原因:1-频繁访问 2-身份认证不通过 3-越权访问
    private String failReasonName;  // 访问失败原因:1-频繁访问 2-身份认证不通过 3-越权访问
    private String resultData;      // 访问的结果数据
    private Date requestTime;       // 请求发送时间
    private String requestTimeStr;  // 请求时间
    private Date responseTime;      // 响应时间
    private String responseTimeStr; // 响应时间
    private Integer status;         // 访问日志状态，是否可用作为惩罚依据

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubjectUid() {
        return subjectUid;
    }

    public void setSubjectUid(String subjectUid) {
        this.subjectUid = subjectUid;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getObjectUid() {
        return objectUid;
    }

    public void setObjectUid(String objectUid) {
        this.objectUid = objectUid;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateData() {
        return operateData;
    }

    public void setOperateData(String operateData) {
        this.operateData = operateData;
    }

    public Boolean getAccessAllow() {
        return accessAllow;
    }

    public void setAccessAllow(Boolean accessAllow) {
        this.accessAllow = accessAllow;
    }

    public String getAccessAllowName() {
        return accessAllowName;
    }

    public void setAccessAllowName(String accessAllowName) {
        this.accessAllowName = accessAllowName;
    }

    public Integer getFailReason() {
        return failReason;
    }

    public void setFailReason(Integer failReason) {
        this.failReason = failReason;
    }

    public String getFailReasonName() {
        return failReasonName;
    }

    public void setFailReasonName(String failReasonName) {
        this.failReasonName = failReasonName;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getRequestTimeStr() {
        return requestTimeStr;
    }

    public void setRequestTimeStr(String requestTimeStr) {
        this.requestTimeStr = requestTimeStr;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public String getResponseTimeStr() {
        return responseTimeStr;
    }

    public void setResponseTimeStr(String responseTimeStr) {
        this.responseTimeStr = responseTimeStr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
