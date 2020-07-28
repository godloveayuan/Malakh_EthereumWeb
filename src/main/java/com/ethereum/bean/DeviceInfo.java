package com.ethereum.bean;

import java.util.Date;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description: 设备信息
 */

public class DeviceInfo {
    private Integer id;
    private String deviceName;              // 设备名称
    private String deviceMac;               // 设备出厂标识
    private String deviceUid;               // 服务器下发的设备标识
    private String deviceType;              // 设备类型
    private String deviceTypeName;          // 设备类型
    private String manufacture;             // 生产厂商
    private String attributeType;           // 设备类型属性
    private String attributeTypeName;       // 设备类型属性名称
    private String attributePosition;       // 设备位置属性
    private String attributePositionName;   // 设备位置属性名称
    private String attributeSystem;         // 设备系统属性
    private String attributeSystemName;     // 设备系统属性名称
    private String agentDevice;             // 代理设备
    private String agentAddress;            // 代理地址
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

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getAttributeTypeName() {
        return attributeTypeName;
    }

    public void setAttributeTypeName(String attributeTypeName) {
        this.attributeTypeName = attributeTypeName;
    }

    public String getAttributePosition() {
        return attributePosition;
    }

    public void setAttributePosition(String attributePosition) {
        this.attributePosition = attributePosition;
    }

    public String getAttributePositionName() {
        return attributePositionName;
    }

    public void setAttributePositionName(String attributePositionName) {
        this.attributePositionName = attributePositionName;
    }

    public String getAttributeSystem() {
        return attributeSystem;
    }

    public void setAttributeSystem(String attributeSystem) {
        this.attributeSystem = attributeSystem;
    }

    public String getAttributeSystemName() {
        return attributeSystemName;
    }

    public void setAttributeSystemName(String attributeSystemName) {
        this.attributeSystemName = attributeSystemName;
    }

    public String getAgentDevice() {
        return agentDevice;
    }

    public void setAgentDevice(String agentDevice) {
        this.agentDevice = agentDevice;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
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
