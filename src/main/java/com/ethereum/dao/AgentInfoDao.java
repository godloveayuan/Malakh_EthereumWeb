package com.ethereum.dao;

import com.ethereum.bean.AgentInfo;
import com.ethereum.bean.DeviceInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description:
 */
@Repository
public interface AgentInfoDao {

    List<AgentInfo> selectAll();

    List<AgentInfo> selectFilter(AgentInfo queryParam);

    AgentInfo selectById(@Param("id") Integer id);

    AgentInfo selectByName(@Param("deviceName") String deviceName);

    AgentInfo selectByMac(@Param("deviceMac") String deviceMac);

    int insert(AgentInfo agentInfo);

    int update(AgentInfo agentInfo);

    int delete(int id);
}
