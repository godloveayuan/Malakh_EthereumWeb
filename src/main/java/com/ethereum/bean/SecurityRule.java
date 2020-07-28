package com.ethereum.bean;

import java.util.Date;

/**
 * @Author: Malakh
 * @Date: 2020/2/13
 * @Description: 安全规则
 */

public class SecurityRule {
    private Integer id;             // 主键ID
    private String name;            // 规则名称
    private Integer type;           // 规则类型
    private String typeName;        // 规则类型
    private Integer checkCount;     // 检查次数
    private Integer checkTimeNumber;        // 监测时间长度
    private String checkTimeUnit;           // 监测时间单位 second/minute/hour/day
    private String checkTimeUnitName;
    private Integer punishNumber;           // 惩罚时间长度
    private String punishUnit;              // 惩罚时间单位
    private String punishUnitName;          // 惩罚时间单位
    private Boolean kickOut;                // 违反规则后是否直接驱逐出网络
    private String kickName;                // 惩罚措施：隔离/驱逐
    private Integer status;                 // 设备状态 0-不可用 1-可用
    private String statusName;              // 设备状态 0-不可用 1-可用
    private Date createTime;                // 创建时间
    private Date updateTime;                // 修改时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(Integer checkCount) {
        this.checkCount = checkCount;
    }

    public Integer getCheckTimeNumber() {
        return checkTimeNumber;
    }

    public void setCheckTimeNumber(Integer checkTimeNumber) {
        this.checkTimeNumber = checkTimeNumber;
    }

    public String getCheckTimeUnit() {
        return checkTimeUnit;
    }

    public void setCheckTimeUnit(String checkTimeUnit) {
        this.checkTimeUnit = checkTimeUnit;
    }

    public String getCheckTimeUnitName() {
        return checkTimeUnitName;
    }

    public void setCheckTimeUnitName(String checkTimeUnitName) {
        this.checkTimeUnitName = checkTimeUnitName;
    }

    public Integer getPunishNumber() {
        return punishNumber;
    }

    public void setPunishNumber(Integer punishNumber) {
        this.punishNumber = punishNumber;
    }

    public String getPunishUnit() {
        return punishUnit;
    }

    public void setPunishUnit(String punishUnit) {
        this.punishUnit = punishUnit;
    }

    public String getPunishUnitName() {
        return punishUnitName;
    }

    public void setPunishUnitName(String punishUnitName) {
        this.punishUnitName = punishUnitName;
    }

    public Boolean getKickOut() {
        return kickOut;
    }

    public void setKickOut(Boolean kickOut) {
        this.kickOut = kickOut;
    }

    public String getKickName() {
        return kickName;
    }

    public void setKickName(String kickName) {
        this.kickName = kickName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
