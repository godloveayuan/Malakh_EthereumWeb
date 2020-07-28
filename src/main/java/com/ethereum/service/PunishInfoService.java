package com.ethereum.service;

import com.ethereum.bean.AccessLog;
import com.ethereum.bean.ConstantValue;
import com.ethereum.bean.PunishInfo;
import com.ethereum.bean.SecurityRule;
import com.ethereum.dao.PunishInfoDao;
import com.ethereum.util.MyStringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
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
public class PunishInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PunishInfoService.class);

    @Resource
    private PunishInfoDao punishInfoDao;
    @Resource
    private SecurityRuleService securityRuleService;
    @Resource
    private AccessLogService accessLogService;
    @Resource
    private DeviceInfoService deviceInfoService;

    /**
     * 查询所有信息
     *
     * @return
     */
    public List<PunishInfo> queryAll() {
        List<PunishInfo> infoList = punishInfoDao.selectAll();
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (PunishInfo info : infoList) {
                fillShowValue(info);
            }
        }
        return infoList;
    }

    /**
     * 查询所有信息
     *
     * @return
     */
    public List<PunishInfo> queryAllByStatus(Integer status) {
        List<PunishInfo> infoList = punishInfoDao.selectAllByStatus(status);
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (PunishInfo info : infoList) {
                fillShowValue(info);
            }
        }
        return infoList;
    }

    /**
     * 分页查询
     *
     * @return
     */
    public PageInfo<PunishInfo> queryFilterByPage(PunishInfo queryParam, Integer pageIndex, Integer pageSize) {
        // 先将惩罚时间过期的状态更新
        updateStatusByEndTime();

        PageInfo<PunishInfo> pageInfo = new PageInfo<>();

        pageIndex = pageIndex != null ? pageIndex : ConstantValue.PAGE_INDEX_DEFAULT;
        pageSize = pageSize != null ? pageSize : ConstantValue.PAGE_SIZE_DEFAULT;

        // 模糊查询
        if (queryParam != null) {
            if (StringUtils.isNotEmpty(queryParam.getDeviceUid())) {
                queryParam.setDeviceUid("'%" + queryParam.getDeviceUid() + "%'");
            }
            if (StringUtils.isNotEmpty(queryParam.getRuleName())) {
                queryParam.setRuleName("'%" + queryParam.getRuleName() + "%'");
            }
        }

        // 分页器，紧挨执行sql的语句
        Page page = PageHelper.startPage(pageIndex, pageSize);
        List<PunishInfo> infoList = punishInfoDao.selectFilter(queryParam);
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (PunishInfo info : infoList) {
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
    public PunishInfo queryById(Integer id) {
        if (id == null) {
            return null;
        }

        return fillShowValue(punishInfoDao.selectById(id));
    }

    /**
     * 根据设备名称查询已经处于惩罚状态的信息
     *
     * @param deviceUid
     * @return
     */
    public List<PunishInfo> queryDevicePunishInfo(String deviceUid) {
        if (StringUtils.isEmpty(deviceUid)) {
            return null;
        }
        // 先将惩罚时间过期的状态更新
        updateStatusByEndTime();

        List<PunishInfo> punishInfos = punishInfoDao.selectDevicePunishInfo(deviceUid);
        if (CollectionUtils.isNotEmpty(punishInfos)) {
            for (PunishInfo info : punishInfos) {
                fillShowValue(info);
            }
        }

        return punishInfos;
    }

    /**
     * 将惩罚结束时间已经到期的惩罚状态设置为不可用
     */
    public void updateStatusByEndTime() {
        List<PunishInfo> punishInfos = queryAllByStatus(1);
        if (CollectionUtils.isNotEmpty(punishInfos)) {
            for (PunishInfo punish : punishInfos) {
                if (punish.getPunish() == 2) {
                    continue;
                }
                String punishEndStr = punish.getPunishEndStr();
                if (MyStringUtils.checkAIsBeforeB(MyStringUtils.parseStrToDate(punishEndStr), new Date())) {
                    // 惩罚到期后将惩罚状态设置为不可用
                    updateStatusDisable(punish.getId());
                    // 该惩罚依据的访问日志已经使用过，设置为status=0
                    accessLogService.updateStatusDisable(punish.getDeviceUid());
                }
            }
        }
    }

    /**
     * 插入数据
     *
     * @param insertInfo
     */
    public void insert(PunishInfo insertInfo) {
        preCheckInsertData(insertInfo);

        Preconditions.checkArgument(CollectionUtils.isEmpty(queryDevicePunishInfo(insertInfo.getDeviceUid())), "该设备已处于惩罚状态!");
        fillInsertValue(insertInfo);

        LOGGER.info("[insert] insertInfo:{}", insertInfo);
        punishInfoDao.insert(insertInfo);
    }

    /**
     * 更新数据
     *
     * @param updateInfo
     */
    public void update(PunishInfo updateInfo) {
        Preconditions.checkArgument(updateInfo != null, "未接收到更新数据");
        Preconditions.checkArgument(updateInfo.getId() != null, "请提供更新数据的ID");
        preCheckUpdateData(updateInfo);

        List<PunishInfo> punishInfos = queryDevicePunishInfo(updateInfo.getDeviceUid());
        if (CollectionUtils.isNotEmpty(punishInfos)) {
            for (PunishInfo info : punishInfos) {
                if (info != null && info.getId().compareTo(updateInfo.getId()) != 0) {
                    throw new IllegalArgumentException("该设备已经处于惩罚状态!");
                }
            }
        }

        fillInsertValue(updateInfo);
        LOGGER.info("[dataUpdate] updateInfo:{}", updateInfo);
        punishInfoDao.update(updateInfo);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public void delete(Integer id) {
        Preconditions.checkArgument(id != null, "请提供要删除数据的ID");

        punishInfoDao.delete(id);
    }

    public void updateStatusDisable(Integer id) {
        Preconditions.checkArgument(id != null, "请提供要数据的ID");

        punishInfoDao.updateStatus(id, 0);
    }

    /**
     * 查看设备是否有违反的规则，如果有则执行惩罚
     *
     * @param deviceUid
     * @return true：设备有违反的规则，执行惩罚  false:未添加新的惩罚
     */
    public boolean executePunish(String deviceUid) {

        if (StringUtils.isEmpty(deviceUid)) {
            return false;
        }

        /**
         * 是否已经在惩罚中
         */
        List<PunishInfo> punishInfos = queryDevicePunishInfo(deviceUid);
        if (CollectionUtils.isNotEmpty(punishInfos)) {
            LOGGER.info("[executePunish] 设备{} 已经在惩罚中", deviceUid);
            return false;
        }

        /**
         * 访问频率规则
         */
        boolean breakAccessRule = punishIfBreakAccessRule(deviceUid, SecurityRuleService.RULE_ACCESS_FREQUENCY);
        if (breakAccessRule) {
            LOGGER.info("[executePunish] 设备:{} 违反访问频率规则，执行惩罚", deviceUid);
            return true;
        }

        /**
         * 身份认证规则
         */
        breakAccessRule = punishIfBreakAccessRule(deviceUid, SecurityRuleService.RULE_IDENTITY_FREQUENCY);
        if (breakAccessRule) {
            LOGGER.info("[executePunish] 设备:{} 违反身份认证规则，执行惩罚", deviceUid);
            return true;
        }

        /**
         * 越权访问规则
         */
        breakAccessRule = punishIfBreakAccessRule(deviceUid, SecurityRuleService.RULE_AUTHORITY_FREQUENCY);
        if (breakAccessRule) {
            LOGGER.info("[executePunish] 设备:{} 违反越权访问规则，执行惩罚", deviceUid);
            return true;
        }

        LOGGER.info("[executePunish] 设备:{} 未违反任何访问规则，可正常访问", deviceUid);

        return false;
    }

    /**
     * 查看设备是否违反了访问频率规则，如果违反了则进行惩罚
     *
     * @param deviceUid
     * @return
     */
    public boolean punishIfBreakAccessRule(String deviceUid, Integer ruleType) {
        // 找到规则
        List<SecurityRule> accessSecurityRules = securityRuleService.queryByRuleType(ruleType);
        if (CollectionUtils.isEmpty(accessSecurityRules)) {
            return false;
        }

        // 找到有效的规则
        SecurityRule accessRule = null;
        for (SecurityRule rule : accessSecurityRules) {
            if (rule != null && rule.getStatus() == 1) {
                accessRule = rule;
                break;
            }
        }
        LOGGER.info("[检查规则]:{}", accessRule);
        // 查看规则信息
        Integer checkTimeNumber = accessRule.getCheckTimeNumber();
        String checkTimeUnit = accessRule.getCheckTimeUnit();

        // 查询监测时间内设备违反访问次数
        List<AccessLog> accessLogs = null;
        if (ruleType == SecurityRuleService.RULE_ACCESS_FREQUENCY) {
            // 访问频率
            accessLogs = accessLogService.queryAccessCount(deviceUid, checkTimeNumber, checkTimeUnit);
            LOGGER.info("===accessLogs:{}", accessLogs);
        } else {
            // 违规次数
            accessLogs = accessLogService.queryIllegalAccess(deviceUid, checkTimeNumber, checkTimeUnit, ruleType);
            LOGGER.info("===accessLogs:{}", accessLogs);
        }

        int accessCount = CollectionUtils.isNotEmpty(accessLogs) ? accessLogs.size() : 0;
        LOGGER.info("===accessCount:{}", accessCount);
        // 超过访问次数，进行惩罚
        if (accessCount > accessRule.getCheckCount()) {
            // 根据规则进行惩罚
            punishDevice(deviceUid, accessRule);
            return true;
        }

        return false;
    }

    /**
     * 根据设备违反的规则进行惩罚
     *
     * @param deviceUid
     * @param securityRule
     */
    public void punishDevice(String deviceUid, SecurityRule securityRule) {
        PunishInfo punishInfo = new PunishInfo();
        punishInfo.setDeviceUid(deviceUid);
        punishInfo.setRuleName(securityRule.getName());
        punishInfo.setPunish(securityRule.getKickOut() ? 2 : 1);
        punishInfo.setPunishStart(new Date());
        if (securityRule.getKickOut()) {
            punishInfo.setPunishEnd(punishInfo.getPunishStart());
        } else {
            punishInfo.setPunishEnd(MyStringUtils.plusDate(punishInfo.getPunishStart(), securityRule.getPunishNumber(), securityRule.getPunishUnit()));
        }

        LOGGER.info("[添加惩罚] punishInfo:{}", punishInfo);
        insert(punishInfo);
        if (securityRule.getKickOut()) {
            deviceInfoService.updateStatusDisable(deviceUid);
        }
    }

    /**
     * 填充展示字段
     *
     * @param info
     * @return
     */
    public PunishInfo fillShowValue(PunishInfo info) {
        if (info == null) {
            return null;
        }
        if (info.getPunish() != null) {
            info.setPunishName(ConstantValue.PUNISH_TYPE_MAP.get(info.getPunish()));
        }
        if (info.getRuleType() != null) {
            info.setRuleTypeName(ConstantValue.SECURITY_RULE_TYPE_MAP.get(info.getRuleType()));
        }
        if (info.getStatus() != null) {
            if (info.getStatus() == 1) {
                info.setStatusName("惩罚中");
            } else {
                info.setStatusName("惩罚结束");
            }
        }
        if (info.getPunishStart() != null) {
            info.setPunishStartStr(MyStringUtils.parseDateToStr(info.getPunishStart()));
        }
        if (info.getPunishEnd() != null) {
            info.setPunishEndStr(MyStringUtils.parseDateToStr(info.getPunishEnd()));
        }

        return info;
    }

    /**
     * 填充插入字段
     *
     * @param info
     * @return
     */
    public PunishInfo fillInsertValue(PunishInfo info) {
        if (info == null) {
            return null;
        }
        if (StringUtils.isNotEmpty(info.getPunishStartStr())) {
            info.setPunishStart(MyStringUtils.parseStrToDate(info.getPunishStartStr()));
        }
        if (StringUtils.isNotEmpty(info.getPunishEndStr())) {
            info.setPunishEnd(MyStringUtils.parseStrToDate(info.getPunishEndStr()));
        }

        return info;
    }

    /**
     * 信息插入数据库前的检查
     *
     * @param info
     */
    public void preCheckInsertData(PunishInfo info) {
        int lengthLimit = 50;
        Preconditions.checkArgument(info != null, "未获取到信息");

        String deviceUid = info.getDeviceUid();
        Preconditions.checkArgument(StringUtils.isNotEmpty(deviceUid), "惩罚设备不能为空");
        Preconditions.checkArgument(deviceUid.length() <= lengthLimit, "惩罚设备名称超过长度限制:%s", lengthLimit);

    }

    /**
     * 信息更新入数据库前的检查
     *
     * @param info
     */
    public void preCheckUpdateData(PunishInfo info) {
        int lengthLimit = 50;
        Preconditions.checkArgument(info != null, "未获取到信息");

        String deviceUid = info.getDeviceUid();
        if (StringUtils.isNotEmpty(deviceUid)) {
            Preconditions.checkArgument(deviceUid.length() <= lengthLimit, "惩罚设备名称超过长度限制:%s", lengthLimit);
        }
    }

}
