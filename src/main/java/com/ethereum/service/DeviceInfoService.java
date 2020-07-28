package com.ethereum.service;

import com.ethereum.bean.ConstantValue;
import com.ethereum.bean.DeviceInfo;
import com.ethereum.dao.DeviceInfoDao;
import com.ethereum.util.MyStringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description:
 */
@Service
public class DeviceInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceInfoService.class);

    @Resource
    private DeviceInfoDao deviceInfoDao;

    /**
     * 查询所有设备
     *
     * @return
     */
    public List<DeviceInfo> queryAll() {
        List<DeviceInfo> deviceInfoList = deviceInfoDao.selectAll();
        if (CollectionUtils.isNotEmpty(deviceInfoList)) {
            for (DeviceInfo dev : deviceInfoList) {
                fillShowValue(dev);
            }
        }
        return deviceInfoList;
    }

    /**
     * 分页查询
     *
     * @return
     */
    public PageInfo<DeviceInfo> queryFilterByPage(DeviceInfo queryParam, Integer pageIndex, Integer pageSize) {
        PageInfo<DeviceInfo> pageInfo = new PageInfo<>();

        pageIndex = pageIndex != null ? pageIndex : ConstantValue.PAGE_INDEX_DEFAULT;
        pageSize = pageSize != null ? pageSize : ConstantValue.PAGE_SIZE_DEFAULT;

        // 模糊查询
        if (queryParam != null) {
            if (StringUtils.isNotEmpty(queryParam.getDeviceName())) {
                queryParam.setDeviceName("'%" + queryParam.getDeviceName() + "%'");
            }
            if (StringUtils.isNotEmpty(queryParam.getDeviceMac())) {
                queryParam.setDeviceMac("'%" + queryParam.getDeviceMac() + "%'");
            }
            if (StringUtils.isNotEmpty(queryParam.getDeviceUid())) {
                queryParam.setDeviceUid("'%" + queryParam.getDeviceUid() + "%'");
            }
            if (StringUtils.isNotEmpty(queryParam.getManufacture())) {
                queryParam.setManufacture("'%" + queryParam.getManufacture() + "%'");
            }
        }

        // 分页器，紧挨执行sql的语句
        Page page = PageHelper.startPage(pageIndex, pageSize);
        List<DeviceInfo> deviceInfoList = deviceInfoDao.selectFilter(queryParam);
        if (CollectionUtils.isNotEmpty(deviceInfoList)) {
            for (DeviceInfo data : deviceInfoList) {
                fillShowValue(data);
            }
        }

        pageInfo.setPageNum(page.getPageNum());
        pageInfo.setPageSize(page.getPageSize());
        pageInfo.setTotal(page.getTotal());
        pageInfo.setPages(page.getPages());
        pageInfo.setList(deviceInfoList);

        return pageInfo;
    }

    /**
     * 根据id查询设备信息
     *
     * @param id
     * @return
     */
    public DeviceInfo queryById(Integer id) {
        if (id == null) {
            return null;
        }

        return fillShowValue(deviceInfoDao.selectById(id));
    }

    /**
     * 根据唯一约束查询重复的数据
     *
     * @param info
     * @return
     */
    public List<DeviceInfo> queryByUnique(DeviceInfo info) {
        if (info == null) {
            return null;
        }
        List<DeviceInfo> duplicateList = Lists.newArrayList();
        DeviceInfo queryByNameData = queryByName(info.getDeviceName());
        if (queryByNameData != null) {
            duplicateList.add(queryByNameData);
        }

        DeviceInfo queryByMacData = queryByMac(info.getDeviceMac());
        if (queryByMacData != null) {
            duplicateList.add(queryByMacData);
        }

        DeviceInfo queryByUidData = queryByUid(info.getDeviceUid());
        if (queryByUidData != null) {
            duplicateList.add(queryByUidData);
        }

        return (CollectionUtils.isNotEmpty(duplicateList)) ? duplicateList : null;
    }

    /**
     * 根据设备名查询设备信息
     *
     * @param deviceName
     * @return
     */
    public DeviceInfo queryByName(String deviceName) {
        if (StringUtils.isEmpty(deviceName)) {
            return null;
        }

        return fillShowValue(deviceInfoDao.selectByName(deviceName));
    }

    /**
     * 根据设备mac查询设备信息
     *
     * @param deviceMac
     * @return
     */
    public DeviceInfo queryByMac(String deviceMac) {
        if (StringUtils.isEmpty(deviceMac)) {
            return null;
        }

        return fillShowValue(deviceInfoDao.selectByMac(deviceMac));
    }

    /**
     * 根据设备uid查询设备信息
     *
     * @param deviceUid
     * @return
     */
    public DeviceInfo queryByUid(String deviceUid) {
        if (StringUtils.isEmpty(deviceUid)) {
            return null;
        }

        return fillShowValue(deviceInfoDao.selectByUid(deviceUid));
    }

    /**
     * 插入数据
     *
     * @param info
     */
    public void insert(DeviceInfo info) {
        if (info != null) {
            info.setDeviceUid(MyStringUtils.buildUUID());
        }
        preCheckInsertData(info);
        List<DeviceInfo> queryList = queryByUnique(info);
        Preconditions.checkArgument(CollectionUtils.isEmpty(queryList), "设备已经注册!");

        LOGGER.info("[insert] insertInfo:{}", info);
        deviceInfoDao.insert(info);
    }

    /**
     * 更新数据
     *
     * @param info
     */
    public void update(DeviceInfo info) {
        Preconditions.checkArgument(info != null, "未接到到更新数据");
        Preconditions.checkArgument(info.getId() != null, "请提供更新设备的ID");
        preCheckUpdateData(info);

        preCheckInsertData(info);
        List<DeviceInfo> queryList = queryByUnique(info);
        if (CollectionUtils.isNotEmpty(queryList)) {
            for (DeviceInfo data : queryList) {
                if (data != null && data.getId().compareTo(info.getId()) != 0) {
                    throw new IllegalArgumentException("已存在相同数据的其他设备!");
                }
            }
        }

        info.setDeviceUid(null);  // 不能更改Uid
        LOGGER.info("[update] updateInfo:{}", info);
        deviceInfoDao.update(info);
    }
    /**
     * 更新设备状态为可用
     *
     * @param deviceUid
     */
    public void updateStatusEnable(String deviceUid) {
        LOGGER.info("[updateStatusEnable] deviceUid:{},", deviceUid);
        deviceInfoDao.updateStatus(deviceUid, 1);
    }
    /**
     * 更新设备状态为不可用
     *
     * @param deviceUid
     */
    public void updateStatusDisable(String deviceUid) {
        LOGGER.info("[updateStatusEnable] deviceUid:{},", deviceUid);
        deviceInfoDao.updateStatus(deviceUid, 0);
    }

    /**
     * 删除数据
     *
     * @param deviceId
     */
    public void delete(Integer deviceId) {
        Preconditions.checkArgument(deviceId != null, "请提供要删除设备的ID");

        deviceInfoDao.delete(deviceId);
    }

    /**
     * 填充展示字段
     *
     * @param deviceInfo
     * @return
     */
    public DeviceInfo fillShowValue(DeviceInfo deviceInfo) {
        if (deviceInfo == null) {
            return null;
        }
        if (StringUtils.isNotEmpty(deviceInfo.getDeviceType())) {
            deviceInfo.setDeviceTypeName(ConstantValue.DEVICE_TYPE_MAP.get(deviceInfo.getDeviceType()));
        }

        if (StringUtils.isNotEmpty(deviceInfo.getAttributeType())) {
            deviceInfo.setAttributeTypeName(ConstantValue.DEVICE_ATTR_TYPE_MAP.get(deviceInfo.getAttributeType()));
        }

        if (StringUtils.isNotEmpty(deviceInfo.getAttributePosition())) {
            deviceInfo.setAttributePositionName(ConstantValue.DEVICE_ATTR_POSITION_MAP.get(deviceInfo.getAttributePosition()));
        }

        if (StringUtils.isNotEmpty(deviceInfo.getAttributeSystem())) {
            deviceInfo.setAttributeSystemName(ConstantValue.DEVICE_ATTR_SYSTEM_MAP.get(deviceInfo.getAttributeSystem()));
        }

        if (deviceInfo.getStatus() != null) {
            deviceInfo.setStatusName(ConstantValue.STATUS_MAP.get(deviceInfo.getStatus()));
        }

        if (deviceInfo.getAgentAddress() == null) {
            deviceInfo.setAgentAddress("");
        }

        return deviceInfo;
    }

    /**
     * 设备信息插入数据库前的检查
     *
     * @param info
     */
    public void preCheckInsertData(DeviceInfo info) {
        int lengthLimit = 50;
        Preconditions.checkArgument(info != null, "未获取到设备信息");

        String deviceName = info.getDeviceName();
        Preconditions.checkArgument(StringUtils.isNotEmpty(deviceName), "设备名称不能为空");
        Preconditions.checkArgument(deviceName.length() <= lengthLimit, "设备名称超过长度限制:%s", lengthLimit);

        String deviceMac = info.getDeviceMac();
        Preconditions.checkArgument(StringUtils.isNotEmpty(deviceMac), "出厂标识不能为空");
        Preconditions.checkArgument(deviceMac.length() <= lengthLimit, "出厂标识超过长度限制:%s", lengthLimit);

        Preconditions.checkArgument(StringUtils.isNotEmpty(info.getAttributeType()), "设备类型属性不能为空!");
        Preconditions.checkArgument(StringUtils.isNotEmpty(info.getAttributePosition()), "设备位置属性不能为空!");
        Preconditions.checkArgument(StringUtils.isNotEmpty(info.getAttributeSystem()), "设备系统功能属性不能为空!");
        Preconditions.checkArgument(StringUtils.isNotEmpty(info.getDeviceType()), "设备类型不能为空!");
    }

    /**
     * 设备信息插入数据库前的检查
     *
     * @param deviceInfo
     */
    public void preCheckUpdateData(DeviceInfo deviceInfo) {
        int lengthLimit = 50;
        Preconditions.checkArgument(deviceInfo != null, "未获取到设备信息");

        String deviceName = deviceInfo.getDeviceName();
        if (StringUtils.isNotEmpty(deviceName)) {
            Preconditions.checkArgument(deviceName.length() <= lengthLimit, "设备名称超过长度限制:%s", lengthLimit);
        }

        String deviceMac = deviceInfo.getDeviceMac();
        if (StringUtils.isNotEmpty(deviceMac)) {
            Preconditions.checkArgument(deviceMac.length() <= lengthLimit, "出厂标识超过长度限制:%s", lengthLimit);
        }
    }

}
