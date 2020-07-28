package com.ethereum.dao;

import com.ethereum.bean.ContractInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description:
 */
@Repository
public interface ContractInfoDao {

    List<ContractInfo> selectAll();

    List<ContractInfo> selectFilter(ContractInfo queryParam);

    ContractInfo selectById(@Param("id") Integer id);

    ContractInfo selectByName(@Param("name") String name);

    ContractInfo selectByAddress(@Param("address") String address);

    int insert(ContractInfo contractInfo);

    int update(ContractInfo contractInfo);

    int delete(int id);

    List<ContractInfo> selectPublicContract();

    List<ContractInfo> selectPrivateContract(@Param("deviceUid") String deviceUid);
}
