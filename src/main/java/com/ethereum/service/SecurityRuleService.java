package com.ethereum.service;

import com.ethereum.bean.ConstantValue;
import com.ethereum.bean.SecurityRule;
import com.ethereum.dao.SecurityRuleDao;
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
public class SecurityRuleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityRuleService.class);

    public static final int RULE_ACCESS_FREQUENCY = 1;          // 访问频率规则
    public static final int RULE_IDENTITY_FREQUENCY = 2;        // 身份认证频率规则
    public static final int RULE_AUTHORITY_FREQUENCY = 3;       // 越权访问频率规则

    @Resource
    private SecurityRuleDao securityRuleDao;

    /**
     * 查询所有信息
     *
     * @return
     */
    public List<SecurityRule> queryAll() {
        List<SecurityRule> infoList = securityRuleDao.selectAll();
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (SecurityRule info : infoList) {
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
    public PageInfo<SecurityRule> queryFilterByPage(SecurityRule queryParam, Integer pageIndex, Integer pageSize) {
        PageInfo<SecurityRule> pageInfo = new PageInfo<>();

        pageIndex = pageIndex != null ? pageIndex : ConstantValue.PAGE_INDEX_DEFAULT;
        pageSize = pageSize != null ? pageSize : ConstantValue.PAGE_SIZE_DEFAULT;

        // 模糊查询
        if (queryParam != null) {
            if (StringUtils.isNotEmpty(queryParam.getName())) {
                queryParam.setName("'%" + queryParam.getName() + "%'");
            }
        }

        // 分页器，紧挨执行sql的语句
        Page page = PageHelper.startPage(pageIndex, pageSize);
        List<SecurityRule> infoList = securityRuleDao.selectFilter(queryParam);
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (SecurityRule info : infoList) {
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
    public SecurityRule queryById(Integer id) {
        if (id == null) {
            return null;
        }

        return fillShowValue(securityRuleDao.selectById(id));
    }

    /**
     * 根据唯一约束查询重复的数据
     *
     * @param rule
     * @return
     */
    public List<SecurityRule> queryByUnique(SecurityRule rule) {
        if (rule == null) {
            return null;
        }
        List<SecurityRule> duplicateList = securityRuleDao.selectByUnique(rule);
        if (duplicateList == null) {
            duplicateList = Lists.newArrayList();
        }
        SecurityRule queryByNameData = queryByName(rule.getName());
        if (queryByNameData != null) {
            duplicateList.add(queryByNameData);
        }

        return (CollectionUtils.isNotEmpty(duplicateList)) ? duplicateList : null;
    }

    /**
     * 根据合约名称查询合约信息
     *
     * @param name
     * @return
     */
    public SecurityRule queryByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        return fillShowValue(securityRuleDao.selectByName(name));
    }

    public List<SecurityRule> queryByRuleType(Integer ruleType) {
        if (ruleType == null) {
            return null;
        }
        List<SecurityRule> securityRules = securityRuleDao.selectByRuleType(ruleType);
        if (CollectionUtils.isNotEmpty(securityRules)) {
            for (SecurityRule rule : securityRules) {
                fillShowValue(rule);
            }
        }

        return securityRules;
    }

    /**
     * 插入数据
     *
     * @param rule
     */
    public void insert(SecurityRule rule) {
        preCheckInsertData(rule);

        List<SecurityRule> queryList = queryByUnique(rule);
        Preconditions.checkArgument(CollectionUtils.isEmpty(queryList), "规则已经存在!");

        LOGGER.info("[insert] insertRule:{}", rule);
        securityRuleDao.insert(rule);
    }

    /**
     * 更新数据
     *
     * @param rule
     */
    public void update(SecurityRule rule) {
        Preconditions.checkArgument(rule != null, "未接收到更新数据");
        Preconditions.checkArgument(rule.getId() != null, "请提供更新数据的ID");
        preCheckUpdateData(rule);

        List<SecurityRule> queryList = queryByUnique(rule);
        if (CollectionUtils.isNotEmpty(queryList)) {
            for (SecurityRule data : queryList) {
                if (data != null && data.getId().compareTo(rule.getId()) != 0) {
                    throw new IllegalArgumentException("已存在相同数据的其他规则!");
                }
            }
        }

        LOGGER.info("[update] updateRule:{}", rule);
        securityRuleDao.update(rule);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public void delete(Integer id) {
        Preconditions.checkArgument(id != null, "请提供要删除数据的ID");

        securityRuleDao.delete(id);
    }

    /**
     * 填充展示字段
     *
     * @param info
     * @return
     */
    public SecurityRule fillShowValue(SecurityRule info) {
        if (info == null) {
            return null;
        }
        if (info.getType() != null) {
            info.setTypeName(ConstantValue.SECURITY_RULE_TYPE_MAP.get(info.getType()));
        }
        if (info.getStatus() != null) {
            info.setStatusName(ConstantValue.STATUS_MAP.get(info.getStatus()));
        }
        if (StringUtils.isNotEmpty(info.getCheckTimeUnit())) {
            info.setCheckTimeUnitName(ConstantValue.TIME_UNIT_MAP.get(info.getCheckTimeUnit()));
        }
        if (StringUtils.isNotEmpty(info.getPunishUnit())) {
            info.setPunishUnitName(ConstantValue.TIME_UNIT_MAP.get(info.getPunishUnit()));
        }
        if (StringUtils.isNotEmpty(info.getPunishUnit())) {
            info.setPunishUnitName(ConstantValue.TIME_UNIT_MAP.get(info.getPunishUnit()));
        }

        Boolean kickOut = info.getKickOut();
        kickOut = (kickOut == null) ? false : kickOut;
        info.setKickName((kickOut != null && kickOut) ? "驱逐" : "隔离");
        if (kickOut) {
            info.setPunishUnit("");
            info.setPunishUnitName("");
            info.setPunishNumber(0);
        }

        return info;
    }

    /**
     * 信息插入数据库前的检查
     *
     * @param info
     */
    public void preCheckInsertData(SecurityRule info) {
        int lengthLimit = 50;
        Preconditions.checkArgument(info != null, "未获取到信息");

        String name = info.getName();
        Preconditions.checkArgument(StringUtils.isNotEmpty(name), "名称不能为空");
        Preconditions.checkArgument(name.length() <= lengthLimit, "名称超过长度限制:%s", lengthLimit);

    }

    /**
     * 信息更新入数据库前的检查
     *
     * @param info
     */
    public void preCheckUpdateData(SecurityRule info) {
        int lengthLimit = 50;
        Preconditions.checkArgument(info != null, "未获取到信息");

        String name = info.getName();
        if (StringUtils.isNotEmpty(name)) {
            Preconditions.checkArgument(name.length() <= lengthLimit, "名称超过长度限制:%s", lengthLimit);
        }
    }

}
