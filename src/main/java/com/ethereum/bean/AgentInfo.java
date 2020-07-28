package com.ethereum.bean;

import java.util.Date;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description: 代理设备信息
 */

public class AgentInfo {
    private Integer id;
    private String deviceName;              // 设备名称
    private String deviceMac;               // 设备出厂标识
    private String deviceUid;               // 服务器下发的设备标识
    private String manufacture;             // 生产厂商
    private String address;                 // 代理地址
    private Integer status;                 // 设备状态 0-不可用 1-可用
    private String statusName;              // 设备状态 0-不可用 1-可用
    private Date createTime;                // 创建时间
    private String createTimeStr;           // 创建时间
    private Date updateTime;                // 修改时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public String getDeviceUid() {
        return deviceUid;
    }

    public void setDeviceUid(String deviceUid) {
        this.deviceUid = deviceUid;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
