package com.ethereum.interceptor;

import com.ethereum.bean.ConstantValue;
import com.ethereum.service.UserInfoService;
import com.ethereum.util.CookiesUtil;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Malakh
 * @createDate 2018/9/3 18:55
 * @describe 登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);  // 登录拦截器日志单独打印

    @Resource
    private UserInfoService userInfoService;

    private List<String> urlWhiteList;          // 拦截白名单,在xml里配置

    public List<String> getUrlWhiteList() {
        return urlWhiteList;
    }

    public void setUrlWhiteList(List<String> urlWhiteList) {
        this.urlWhiteList = urlWhiteList;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String rootDir = request.getContextPath();
        String loginDir = rootDir + "/jsp/login.jsp";

        // 请求的url
        String requestUrl = request.getRequestURI();
        // 拦截白名单
        if (checkInWhiteList(requestUrl)) {
//            LOGGER.info("[LoginInterceptor] url:{} in white list.", requestUrl);
            return true;
        }

        String loginUser = CookiesUtil.getCookieValue(request, ConstantValue.COOKIE_KEY_LOGIN_USER);        // Cookie 信息
        String loginCheck = CookiesUtil.getCookieValue(request, ConstantValue.COOKIE_KEY_LOGIN_CHECK);        // Cookie 信息
        if (userInfoService.cookieCheck(loginUser, loginCheck)) {
//            LOGGER.info("[LoginInterceptor] user:{} has login.", loginUser);
            return true;
        } else {
            LOGGER.info("[LoginInterceptor] user:{} not login.", loginUser);
//            response.sendRedirect(loginDir);  // 这种方式会在main的子frame里显示登录页面
            response.getWriter().write("<script>this.top.location.href = '" + loginDir + "'</script>");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 校验请求的url是否在拦截白名单里
     *
     * @param requestUrl
     * @return true: 在白名单，不拦截   false: 不在白名单拦截
     */
    public boolean checkInWhiteList(String requestUrl) {
        Preconditions.checkNotNull(requestUrl);
        if (StringUtils.equals("/", requestUrl)) {
            return true;
        }
        for (String url : urlWhiteList) {
            if (url.endsWith("/**")) {
                // 如果白名单以 /** 结尾，则进行前缀判断
                if (requestUrl.startsWith(url.substring(0, url.length() - 3))) {
                    return true;
                }
            } else if (url.endsWith("/*")) {
                // 如果白名单以 /* 结尾，则进行前缀判断
                if (requestUrl.startsWith(url.substring(0, url.length() - 2))) {
                    return true;
                }
            } else if (requestUrl.startsWith(url)) {
                return true;
            }
        }
        return false;
    }

}
