package com.ethereum.service;

import com.ethereum.bean.AgentInfo;
import com.ethereum.bean.ConstantValue;
import com.ethereum.dao.AgentInfoDao;
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
public class AgentInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentInfoService.class);


    @Resource
    private AgentInfoDao agentInfoDao;

    /**
     * 查询所有代理设备
     *
     * @return
     */
    public List<AgentInfo> queryAll() {
        List<AgentInfo> agentInfoList = agentInfoDao.selectAll();
        if (CollectionUtils.isNotEmpty(agentInfoList)) {
            for (AgentInfo agent : agentInfoList) {
                fillShowValue(agent);
            }
        }
        return agentInfoList;
    }

    /**
     * 分页查询
     *
     * @return
     */
    public PageInfo<AgentInfo> queryFilterByPage(AgentInfo queryParam, Integer pageIndex, Integer pageSize) {
        PageInfo<AgentInfo> pageInfo = new PageInfo<>();

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
        List<AgentInfo> deviceInfoList = agentInfoDao.selectFilter(queryParam);
        if (CollectionUtils.isNotEmpty(deviceInfoList)) {
            for (AgentInfo data : deviceInfoList) {
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
     * 根据id查询代理设备信息
     *
     * @param id
     * @return
     */
    public AgentInfo queryById(Integer id) {
        if (id == null) {
            return null;
        }

        return fillShowValue(agentInfoDao.selectById(id));
    }

    /**
     * 根据唯一约束查询重复的数据
     *
     * @param info
     * @return
     */
    public List<AgentInfo> queryByUnique(AgentInfo info) {
        if (info == null) {
            return null;
        }
        List<AgentInfo> duplicateList = Lists.newArrayList();
        AgentInfo queryByNameData = queryByName(info.getDeviceName());
        if (queryByNameData != null) {
            duplicateList.add(queryByNameData);
        }

        AgentInfo queryByMacData = queryByMac(info.getDeviceMac());
        if (queryByMacData != null) {
            duplicateList.add(queryByMacData);
        }

        return (CollectionUtils.isNotEmpty(duplicateList)) ? duplicateList : null;
    }

    /**
     * 根据设备名查询代理设备信息
     *
     * @param deviceName
     * @return
     */
    public AgentInfo queryByName(String deviceName) {
        if (StringUtils.isEmpty(deviceName)) {
            return null;
        }

        return fillShowValue(agentInfoDao.selectByName(deviceName));
    }

    /**
     * 根据设备mac查询设备信息
     *
     * @param deviceMac
     * @return
     */
    public AgentInfo queryByMac(String deviceMac) {
        if (StringUtils.isEmpty(deviceMac)) {
            return null;
        }

        return fillShowValue(agentInfoDao.selectByMac(deviceMac));
    }

    /**
     * 插入数据
     *
     * @param info
     */
    public void insert(AgentInfo info) {
        if (info != null) {
            info.setDeviceUid(MyStringUtils.buildUUID());
        }

        preCheckInsertData(info);
        List<AgentInfo> queryList = queryByUnique(info);
        Preconditions.checkArgument(CollectionUtils.isEmpty(queryList), "设备已经注册!");

        LOGGER.info("[insert] insertInfo:{}", info);
        agentInfoDao.insert(info);
    }

    /**
     * 更新数据
     *
     * @param info
     */
    public void update(AgentInfo info) {
        Preconditions.checkArgument(info != null, "未接到到更新数据");
        Preconditions.checkArgument(info.getId() != null, "请提供更新设备的ID");
        preCheckUpdateData(info);

        List<AgentInfo> queryList = queryByUnique(info);
        if (CollectionUtils.isNotEmpty(queryList)) {
            for (AgentInfo data : queryList) {
                if (data != null && data.getId().compareTo(info.getId()) != 0) {
                    throw new IllegalArgumentException("已存在相同数据的其他设备!");
                }
            }
        }

        info.setDeviceUid(null);  // 不能更改Uid
        LOGGER.info("[update] updateInfo:{}", info);
        agentInfoDao.update(info);
    }

    /**
     * 删除数据
     *
     * @param deleteId
     */
    public void delete(Integer deleteId) {
        Preconditions.checkArgument(deleteId != null, "请提供要删除设备的ID");
        agentInfoDao.delete(deleteId);
    }

    /**
     * 填充展示字段
     *
     * @param info
     * @return
     */
    public AgentInfo fillShowValue(AgentInfo info) {
        if (info == null) {
            return null;
        }
        if (info.getStatus() != null) {
            info.setStatusName(ConstantValue.STATUS_MAP.get(info.getStatus()));
        }
        if (info.getCreateTime() != null) {
            info.setCreateTimeStr(MyStringUtils.parseDateToStr(info.getCreateTime()));
        }

        return info;
    }

    /**
     * 信息插入数据库前的检查
     *
     * @param agentInfo
     */
    public void preCheckInsertData(AgentInfo agentInfo) {
        int lengthLimit = 50;
        Preconditions.checkArgument(agentInfo != null, "未获取到设备信息");

        String deviceName = agentInfo.getDeviceName();
        Preconditions.checkArgument(StringUtils.isNotEmpty(deviceName), "设备名称不能为空");
        Preconditions.checkArgument(deviceName.length() <= lengthLimit, "设备名称超过长度限制:%s", lengthLimit);

        String deviceMac = agentInfo.getDeviceMac();
        Preconditions.checkArgument(StringUtils.isNotEmpty(deviceMac), "出厂标识不能为空");
        Preconditions.checkArgument(deviceMac.length() <= lengthLimit, "出厂标识超过长度限制:%s", lengthLimit);
    }

    /**
     * 信息更新入数据库前的检查
     *
     * @param agentInfo
     */
    public void preCheckUpdateData(AgentInfo agentInfo) {
        int lengthLimit = 50;
        Preconditions.checkArgument(agentInfo != null, "未获取到设备信息");

        String deviceName = agentInfo.getDeviceName();
        if (StringUtils.isNotEmpty(deviceName)) {
            Preconditions.checkArgument(deviceName.length() <= lengthLimit, "设备名称超过长度限制:%s", lengthLimit);
        }

        String deviceMac = agentInfo.getDeviceMac();
        if (StringUtils.isNotEmpty(deviceMac)) {
            Preconditions.checkArgument(deviceMac.length() <= lengthLimit, "出厂标识超过长度限制:%s", lengthLimit);
        }
    }

}
