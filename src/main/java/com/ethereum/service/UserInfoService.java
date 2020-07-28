package com.ethereum.service;

import com.ethereum.bean.UserInfo;
import com.ethereum.dao.UserInfoDao;
import com.ethereum.util.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description:
 */
@Service
public class UserInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoService.class);

    @Resource
    private UserInfoDao userInfoDao;


    /**
     * 根据设备名查询设备信息
     *
     * @param userName
     * @return
     */
    public UserInfo queryByName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return null;
        }

        return userInfoDao.selectByName(userName);
    }

    public boolean loginCheck(String userName, String userPwd) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(userPwd)) {
            return false;
        }
        UserInfo userInfo = queryByName(userName);
        if (userInfo != null && StringUtils.equalsIgnoreCase(userPwd, userInfo.getUserPwd())) {
            return true;
        }
        return false;
    }

    public boolean cookieCheck(String userName, String encryptPwd) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(encryptPwd)) {
            return false;
        }
        UserInfo userInfo = queryByName(userName);
        if (userInfo != null) {
            String userPwdEncrypt = MD5Utils.MD5Encode(userInfo.getUserPwd());
            if (StringUtils.equalsIgnoreCase(encryptPwd, userPwdEncrypt)) {
                return true;
            }
        }
        return false;
    }

}
