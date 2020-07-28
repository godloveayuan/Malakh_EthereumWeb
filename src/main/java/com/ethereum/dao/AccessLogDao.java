package com.ethereum.dao;

import com.ethereum.bean.AccessLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description:
 */
@Repository
public interface AccessLogDao {

    List<AccessLog> selectAll();

    List<AccessLog> selectIllegalAccess(
            @Param("deviceUid") String deviceUid,
            @Param("failReason") Integer failReason);

    List<AccessLog> selectAccessCount(
            @Param("deviceUid") String deviceUid);

    List<AccessLog> selectFilter(AccessLog queryParam);

    AccessLog selectById(@Param("id") Integer id);

    int insert(AccessLog accessLog);

    int update(AccessLog accessLog);

    int delete(int id);
    int updateStatus(@Param("deviceUid") String deviceUid, @Param("status") Integer status);
}
