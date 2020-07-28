package com.ethereum.controller;

import com.ethereum.bean.ConstantValue;
import com.ethereum.service.UserInfoService;
import com.ethereum.util.CookiesUtil;
import com.ethereum.util.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description:
 */
@Controller
@RequestMapping("/user")
public class UserInfoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoController.class);

    private static final String valueReplace = "###";     // 字符串占位符
    private static final String messageModel = "<div style=\"font-family:'楷体';color:#624638; margin-left:40%;margin-top:10%;\"><font size=7>" + valueReplace + "</font>";
    private static final Integer refreshTime = 1;         // 刷新重定向之前的停留时间,单位：秒

    @Resource
    private UserInfoService userInfoService;


    /**
     * 注册设备
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            // 获取 session
            HttpSession session = request.getSession(false);

            String rootDir = request.getContextPath();
            String mainDir = rootDir + "/jsp/main.jsp";
            String loginDir = rootDir + "/jsp/login.jsp";

            String userName = request.getParameter("userName");
            String userPwd = request.getParameter("userPwd");
            boolean loginCheck = userInfoService.loginCheck(userName, userPwd);
            if (loginCheck) {
                // 登录成功
                CookiesUtil.setResponseCookie(response, ConstantValue.COOKIE_KEY_LOGIN_USER, userName);
                CookiesUtil.setResponseCookie(response, ConstantValue.COOKIE_KEY_LOGIN_CHECK, MD5Utils.MD5Encode(userPwd));
                session.setAttribute("loginUser", userName);
                response.sendRedirect(mainDir);
            } else {
                // 登录失败
                session.setAttribute("loginCheck", "false");
                response.sendRedirect(loginDir);
            }
        } catch (Exception e) {
            LOGGER.error("Server Exception", e);
            response.getWriter().write("服务器错误!");
        }
    }

    /**
     * 注册设备
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("loginOut")
    public void loginOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");

            String rootDir = request.getContextPath();
            String loginDir = rootDir + "/jsp/login.jsp";

            CookiesUtil.removeCookie(response, ConstantValue.COOKIE_KEY_LOGIN_USER);
            CookiesUtil.removeCookie(response, ConstantValue.COOKIE_KEY_LOGIN_CHECK);
            response.getWriter().write("<script>this.top.location.href = '" + loginDir + "'</script>");
        } catch (Exception e) {
            LOGGER.error("Server Exception", e);
            response.getWriter().write("服务器错误!");
        }
    }

}
