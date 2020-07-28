package com.ethereum.dao;

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
public interface DeviceInfoDao {

    List<DeviceInfo> selectAll();

    List<DeviceInfo> selectFilter(DeviceInfo queryParam);

    DeviceInfo selectById(@Param("id") Integer id);

    DeviceInfo selectByName(@Param("deviceName") String deviceName);

    DeviceInfo selectByMac(@Param("deviceMac") String deviceMac);

    DeviceInfo selectByUid(@Param("deviceUid") String deviceUid);

    int insert(DeviceInfo deviceInfo);

    int update(DeviceInfo deviceInfo);
    int updateStatus(@Param("deviceUid")String deviceUid,@Param("status")Integer status);

    int delete(int id);
}
