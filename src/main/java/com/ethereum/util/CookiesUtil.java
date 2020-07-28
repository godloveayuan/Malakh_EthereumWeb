package com.ethereum.util;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class CookiesUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CookiesUtil.class);
    public static final int COOKIE_TIME = 60 * 60 * 24; // 单位：秒


    /**
     * cookie值放入response
     *
     * @param response servlet请求
     * @param value    保存值
     */
    public static HttpServletResponse setResponseCookie(HttpServletResponse response, String key, String value, Integer time) {
        //传入一个键值对
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(time);
        response.addCookie(cookie);

        return response;
    }

    /**
     * cookie值放入response
     *
     * @param response servlet请求
     */
    public static HttpServletResponse removeCookie(HttpServletResponse response, String key) {
        //传入一个键值对
        Cookie cookie = new Cookie(key, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return response;
    }

    public static HttpServletResponse setResponseCookie(HttpServletResponse response, String key, String value) {
        return setResponseCookie(response, key, value, COOKIE_TIME);
    }



    /**
     * 读取request 里的cookie内容
     *
     * @param request
     * @return
     */
    private static Map<String, Cookie> readRequestCookies(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = Maps.newHashMap();
        Cookie[] cookies = request.getCookies();
        if (!Objects.equal(null, cookies)) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

    /**
     * 读取request的cookie里的key值
     *
     * @param request
     * @param key
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        key = key.trim();
        Map<String, Cookie> cookieMap = readRequestCookies(request);
        if (cookieMap != null && cookieMap.containsKey(key)) {
            return cookieMap.get(key).getValue();
        }
        return null;
    }
}
