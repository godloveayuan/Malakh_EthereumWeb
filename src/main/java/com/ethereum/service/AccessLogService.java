package com.ethereum.service;

import com.ethereum.bean.AccessLog;
import com.ethereum.bean.ConstantValue;
import com.ethereum.bean.DeviceInfo;
import com.ethereum.dao.AccessLogDao;
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
import java.util.Date;
import java.util.List;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description:
 */
@Service
public class AccessLogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessLogService.class);

    public static final int FAIL_REASON_ACCESS_FREQUENCY = 1;          // 失败原因：违反访问频率规则
    public static final int FAIL_REASON_FREQUENCY = 2;                 // 失败原因：违反身份认证频率规则
    public static final int FAIL_REASON_AUTHORITY_FREQUENCY = 3;       // 失败原因：违反越权访问频率规则

    @Resource
    private AccessLogDao accessLogDao;
    @Resource
    private DeviceInfoService deviceInfoService;

    /**
     * 查询所有信息
     *
     * @return
     */
    public List<AccessLog> queryAll() {
        List<AccessLog> infoList = accessLogDao.selectAll();
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (AccessLog info : infoList) {
                fillShowValue(info);
            }
        }
        return infoList;
    }

    /**
     * 查询设备在监测时间内违反规则的次数
     *
     * @return
     */
    public List<AccessLog> queryIllegalAccess(String deviceUid, Integer checkTimeNumber, String checkTimeUnit, Integer failReason) {
        if (StringUtils.isEmpty(deviceUid)) {
            return null;
        }

        List<AccessLog> accessLogs = accessLogDao.selectIllegalAccess(deviceUid, failReason);
        List<AccessLog> resultLogs = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(accessLogs)) {
            for (AccessLog accessLog : accessLogs) {
                fillShowValue(accessLog);
                String requestTimeStr = accessLog.getRequestTimeStr();
                if (MyStringUtils.checkDateIsBetweenNow(requestTimeStr, checkTimeNumber, checkTimeUnit)) {
                    resultLogs.add(accessLog);
                }
            }
        }
        return resultLogs;
    }

    /**
     * 查询设备在监测时间内访问次数
     *
     * @return
     */
    public List<AccessLog> queryAccessCount(String deviceUid, Integer checkTimeNumber, String checkTimeUnit) {
        if (StringUtils.isEmpty(deviceUid)) {
            return null;
        }

        List<AccessLog> accessLogs = accessLogDao.selectAccessCount(deviceUid);
        List<AccessLog> resultLogs = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(accessLogs)) {
            for (AccessLog accessLog : accessLogs) {
                fillShowValue(accessLog);
                String requestTimeStr = accessLog.getRequestTimeStr();
                if (MyStringUtils.checkDateIsBetweenNow(requestTimeStr, checkTimeNumber, checkTimeUnit)) {
                    resultLogs.add(accessLog);
                }
            }
        }
        return resultLogs;
    }

    /**
     * 分页查询
     *
     * @return
     */
    public PageInfo<AccessLog> queryFilterByPage(AccessLog queryParam, Integer pageIndex, Integer pageSize) {
        PageInfo<AccessLog> pageInfo = new PageInfo<>();

        pageIndex = pageIndex != null ? pageIndex : ConstantValue.PAGE_INDEX_DEFAULT;
        pageSize = pageSize != null ? pageSize : ConstantValue.PAGE_SIZE_DEFAULT;

        // 模糊查询
        if (queryParam != null) {
            if (StringUtils.isNotEmpty(queryParam.getSubjectUid())) {
                queryParam.setSubjectUid("'%" + queryParam.getSubjectUid() + "%'");
            }
            if (StringUtils.isNotEmpty(queryParam.getSubjectName())) {
                queryParam.setSubjectName("'%" + queryParam.getSubjectName() + "%'");
            }
            if (StringUtils.isNotEmpty(queryParam.getObjectUid())) {
                queryParam.setObjectUid("'%" + queryParam.getObjectUid() + "%'");
            }
        }

        // 分页器，紧挨执行sql的语句
        Page page = PageHelper.startPage(pageIndex, pageSize);
        List<AccessLog> infoList = accessLogDao.selectFilter(queryParam);
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (AccessLog info : infoList) {
                fillShowValue(info);
            }
        }

        pageInfo.setPageNum(page.getPageNum());
        pageInfo.setPageSize(page.getPageSize());
        pageInfo.setTotal(page.getTotal());
        pageInfo.setPages(page.getPages());
        pageInfo.setList(infoList);

        return pageInfo;
    }

    /**
     * 根据id查询信息
     *
     * @param id
     * @return
     */
    public AccessLog queryById(Integer id) {
        if (id == null) {
            return null;
        }

        return fillShowValue(accessLogDao.selectById(id));
    }

    /**
     * 插入数据
     *
     * @param insertInfo
     */
    public void insert(AccessLog insertInfo) {
        preCheckInsertData(insertInfo);
        fillInsertValue(insertInfo);

        if (insertInfo.getRequestTime() == null) {
            insertInfo.setResponseTime(new Date());
        }

        LOGGER.info("[insert] insertLog:{}", insertInfo);
        // 会把自增的id封装到插入的对象里
        accessLogDao.insert(insertInfo);
    }

    /**
     * 更新数据
     *
     * @param updateInfo
     */
    public void update(AccessLog updateInfo) {
        Preconditions.checkArgument(updateInfo != null, "未接收到更新数据");
        Preconditions.checkArgument(updateInfo.getId() != null, "请提供更新数据的ID");
        preCheckUpdateData(updateInfo);

        fillInsertValue(updateInfo);

        LOGGER.info("[update] updateLog:{}", updateInfo);
        accessLogDao.update(updateInfo);
    }
    /**
     * 更新日志状态
     *
     * @param deviceUid
     */
    public void updateStatusDisable(String deviceUid) {
        LOGGER.info("[updateStatusDisable] deviceUid:{}", deviceUid);
        accessLogDao.updateStatus(deviceUid,0);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public void delete(Integer id) {
        Preconditions.checkArgument(id != null, "请提供要删除数据的ID");

        accessLogDao.delete(id);
    }

    /**
     * 填充展示字段
     *
     * @param info
     * @return
     */
    public AccessLog fillShowValue(AccessLog info) {
        if (info == null) {
            return null;
        }

        String objectUid = info.getObjectUid();
        if (StringUtils.isNotEmpty(objectUid)) {
            DeviceInfo deviceInfo = deviceInfoService.queryByUid(objectUid);
            if (deviceInfo != null) {
                info.setObjectName(deviceInfo.getDeviceName());
            }
        }
        if (info.getRequestTime() != null) {
            info.setRequestTimeStr(MyStringUtils.parseDateToStr(info.getRequestTime()));
        }
        if (info.getResponseTime() != null) {
            info.setResponseTimeStr(MyStringUtils.parseDateToStr(info.getResponseTime()));
        }
        if (info.getAccessAllow() != null) {
            info.setAccessAllowName(ConstantValue.ACCESS_Allow_MAP.get(info.getAccessAllow().toString()));
        }
        if (info.getFailReason() != null) {
            info.setFailReasonName(ConstantValue.ACCESS_FAIL_REASON_MAP.get(info.getFailReason()));
        }
        if (StringUtils.isEmpty(info.getOperateData()) || StringUtils.equalsIgnoreCase("null", info.getOperateData())) {
            info.setOperateData("");
        }
        if (StringUtils.isEmpty(info.getResultData())|| StringUtils.equalsIgnoreCase("null", info.getResultData())) {
            info.setResultData("");
        }

        return info;
    }

    /**
     * 填充插入字段
     *
     * @param info
     * @return
     */
    public AccessLog fillInsertValue(AccessLog info) {
        if (info == null) {
            return null;
        }
        if (StringUtils.isNotEmpty(info.getRequestTimeStr())) {
            info.setRequestTime(MyStringUtils.parseStrToDate(info.getRequestTimeStr()));
        }
        if (StringUtils.isNotEmpty(info.getResponseTimeStr())) {
            info.setResponseTime(MyStringUtils.parseStrToDate(info.getResponseTimeStr()));
        }

        return info;
    }

    /**
     * 信息插入数据库前的检查
     *
     * @param info
     */
    public void preCheckInsertData(AccessLog info) {
        Preconditions.checkArgument(info != null, "未获取到信息");
    }

    /**
     * 信息更新入数据库前的检查
     *
     * @param info
     */
    public void preCheckUpdateData(AccessLog info) {
        Preconditions.checkArgument(info != null, "未获取到信息");
    }

}
