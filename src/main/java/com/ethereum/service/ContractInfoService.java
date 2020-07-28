package com.ethereum.service;

import com.ethereum.bean.ConstantValue;
import com.ethereum.bean.ContractInfo;
import com.ethereum.dao.ContractInfoDao;
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
public class ContractInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractInfoService.class);


    @Resource
    private ContractInfoDao contractInfoDao;

    /**
     * 查询所有信息
     *
     * @return
     */
    public List<ContractInfo> queryAll() {
        List<ContractInfo> infoList = contractInfoDao.selectAll();
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (ContractInfo info : infoList) {
                fillShowValue(info);
            }
        }
        return infoList;
    }

    /**
     * 查询所有公共合约
     *
     * @return
     */
    public List<ContractInfo> queryPublicContract() {
        List<ContractInfo> infoList = contractInfoDao.selectPublicContract();
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (ContractInfo info : infoList) {
                fillShowValue(info);
            }
        }
        return infoList;
    }

    /**
     * 查询所有私有合约
     *
     * @return
     */
    public List<ContractInfo> queryPrivateContract(String deviceUid) {
        if(StringUtils.isEmpty(deviceUid)){
            return null;
        }

        List<ContractInfo> infoList = contractInfoDao.selectPrivateContract(deviceUid);
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (ContractInfo info : infoList) {
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
    public PageInfo<ContractInfo> queryFilterByPage(ContractInfo queryParam, Integer pageIndex, Integer pageSize) {
        PageInfo<ContractInfo> pageInfo = new PageInfo<>();

        pageIndex = pageIndex != null ? pageIndex : ConstantValue.PAGE_INDEX_DEFAULT;
        pageSize = pageSize != null ? pageSize : ConstantValue.PAGE_SIZE_DEFAULT;

        // 模糊查询
        if (queryParam != null) {
            if (StringUtils.isNotEmpty(queryParam.getName())) {
                queryParam.setName("'%" + queryParam.getName() + "%'");
            }
            if (StringUtils.isNotEmpty(queryParam.getAddress())) {
                queryParam.setAddress("'%" + queryParam.getAddress() + "%'");
            }
            if (StringUtils.isNotEmpty(queryParam.getOwner())) {
                queryParam.setOwner("'%" + queryParam.getOwner() + "%'");
            }
            if (StringUtils.isNotEmpty(queryParam.getOwnerName())) {
                queryParam.setOwnerName("'%" + queryParam.getOwnerName() + "%'");
            }
        }

        // 分页器，紧挨执行sql的语句
        Page page = PageHelper.startPage(pageIndex, pageSize);
        List<ContractInfo> infoList = contractInfoDao.selectFilter(queryParam);
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (ContractInfo info : infoList) {
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
    public ContractInfo queryById(Integer id) {
        if (id == null) {
            return null;
        }

        return fillShowValue(contractInfoDao.selectById(id));
    }

    /**
     * 根据唯一约束查询重复的数据
     *
     * @param info
     * @return
     */
    public List<ContractInfo> queryByUnique(ContractInfo info) {
        if (info == null) {
            return null;
        }
        List<ContractInfo> duplicateList = Lists.newArrayList();
        ContractInfo queryByNameData = queryByName(info.getName());
        if (queryByNameData != null) {
            duplicateList.add(queryByNameData);
        }

        ContractInfo queryByAddress = queryByAddress(info.getAddress());
        if (queryByAddress != null) {
            duplicateList.add(queryByAddress);
        }

        return (CollectionUtils.isNotEmpty(duplicateList)) ? duplicateList : null;
    }

    /**
     * 根据合约名称查询合约信息
     *
     * @param name
     * @return
     */
    public ContractInfo queryByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        return fillShowValue(contractInfoDao.selectByName(name));
    }

    /**
     * 根据合约地址查询合约信息
     *
     * @param address
     * @return
     */
    public ContractInfo queryByAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            return null;
        }

        return fillShowValue(contractInfoDao.selectByAddress(address));
    }

    /**
     * 插入数据
     *
     * @param info
     */
    public void insert(ContractInfo info) {
        preCheckInsertData(info);

        List<ContractInfo> queryList = queryByUnique(info);
        Preconditions.checkArgument(CollectionUtils.isEmpty(queryList), "合约已经存在!");

        LOGGER.info("[insert] insertInfo:{}", info);
        contractInfoDao.insert(info);
    }

    /**
     * 更新数据
     *
     * @param info
     */
    public void update(ContractInfo info) {
        Preconditions.checkArgument(info != null, "未接收到更新数据");
        Preconditions.checkArgument(info.getId() != null, "请提供更新数据的ID");
        preCheckUpdateData(info);

        List<ContractInfo> queryList = queryByUnique(info);
        if (CollectionUtils.isNotEmpty(queryList)) {
            for (ContractInfo data : queryList) {
                if (data != null && data.getId().compareTo(info.getId()) != 0) {
                    throw new IllegalArgumentException("已存在相同数据的其他合约!");
                }
            }
        }

        LOGGER.info("[update] updateInfo:{}", info);
        contractInfoDao.update(info);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public void delete(Integer id) {
        Preconditions.checkArgument(id != null, "请提供要删除数据的ID");

        contractInfoDao.delete(id);
    }

    /**
     * 填充展示字段
     *
     * @param info
     * @return
     */
    public ContractInfo fillShowValue(ContractInfo info) {
        if (info == null) {
            return null;
        }
        if (StringUtils.isNotEmpty(info.getType())) {
            info.setTypeName(ConstantValue.CONTRACT_TYPE_MAP.get(info.getType()));
        }
        if (info.getStatus() != null) {
            info.setStatusName(ConstantValue.STATUS_MAP.get(info.getStatus()));
        }

        String type = info.getType();
        if (StringUtils.isNotEmpty(type) && StringUtils.equalsIgnoreCase("public", type)) {
            info.setOwner("all");
            info.setOwnerName("公共");
        }

        return info;
    }

    /**
     * 信息插入数据库前的检查
     *
     * @param info
     */
    public void preCheckInsertData(ContractInfo info) {
        int lengthLimit = 50;
        Preconditions.checkArgument(info != null, "未获取到信息");

        String name = info.getName();
        Preconditions.checkArgument(StringUtils.isNotEmpty(name), "合约名称不能为空！");
        Preconditions.checkArgument(name.length() <= lengthLimit, "合约名称超过长度限制:%s", lengthLimit);

        String address = info.getAddress();
        Preconditions.checkArgument(StringUtils.isNotEmpty(address), "合约地址不能为空！");
        Preconditions.checkArgument(address.length() <= lengthLimit, "地址超过长度限制:%s", lengthLimit);

        Preconditions.checkArgument(StringUtils.isNotEmpty(info.getOwner()), "合约拥有者不能为空！");
    }

    /**
     * 信息更新入数据库前的检查
     *
     * @param info
     */
    public void preCheckUpdateData(ContractInfo info) {
        int lengthLimit = 50;
        Preconditions.checkArgument(info != null, "未获取到信息");

        String name = info.getName();
        if (StringUtils.isNotEmpty(name)) {
            Preconditions.checkArgument(name.length() <= lengthLimit, "名称超过长度限制:%s", lengthLimit);
        }

        String address = info.getAddress();
        if (StringUtils.isNotEmpty(address)) {
            Preconditions.checkArgument(address.length() <= lengthLimit, "地址超过长度限制:%s", lengthLimit);
        }
    }

}
