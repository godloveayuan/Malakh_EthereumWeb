package com.ethereum.bean;

/**
 * @Author: Malakh
 * @Date: 2020/2/19
 * @Description:
 */

public class UserInfo {
    private Integer id;             // 主键id
    private String userName;        // 用户名
    private String userPwd;         // 密码

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
}
