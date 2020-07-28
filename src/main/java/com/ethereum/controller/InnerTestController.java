package com.ethereum.controller;

import com.ethereum.bean.HomePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: Malakh
 * @Date: 2020/2/15
 * @Description: 内部测试Controller
 */
@Controller
@RequestMapping("/inner")
public class InnerTestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InnerTestController.class);

    /**
     * 测试 @PathVariable 从url中获取参数
     *
     * @param name
     * @return
     */
    @RequestMapping("url/{name}/test")
    @ResponseBody
    public HomePage testParamUrl(@PathVariable("name") String name) {
        LOGGER.info("[testParamUrl] ==== name:{}", name);

        HomePage homePage = HomePage.buildAtSuccess(name);
        LOGGER.info("[testParamUrl] ==== homePage:{}", homePage);

        return homePage;
    }
}
