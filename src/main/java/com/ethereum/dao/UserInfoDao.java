package com.ethereum.dao;

import com.ethereum.bean.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description:
 */
@Repository
public interface UserInfoDao {
    UserInfo selectByName(@Param("userName") String userName);
}
