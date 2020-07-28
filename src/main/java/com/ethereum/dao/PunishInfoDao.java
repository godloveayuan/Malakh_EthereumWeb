package com.ethereum.dao;

import com.ethereum.bean.PunishInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description:
 */
@Repository
public interface PunishInfoDao {

    List<PunishInfo> selectAll();

    List<PunishInfo> selectAllByStatus(@Param("status") Integer status);

    List<PunishInfo> selectFilter(PunishInfo queryParam);

    PunishInfo selectById(@Param("id") Integer id);

    List<PunishInfo> selectDevicePunishInfo(@Param("deviceUid") String deviceUid);

    int insert(PunishInfo securityRule);

    int update(PunishInfo securityRule);

    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    int delete(int id);
}
