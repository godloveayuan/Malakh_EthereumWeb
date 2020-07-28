package com.ethereum.controller;

import com.ethereum.bean.ConstantValue;
import com.ethereum.bean.SecurityRule;
import com.ethereum.service.SecurityRuleService;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description:
 */
@Controller
@RequestMapping("/securityRule")
public class SecurityRuleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityRuleController.class);

    private static final String valueReplace = "###";     // 字符串占位符
    private static final String messageModel = "<div style=\"font-family:'楷体';color:#624638; margin-left:40%;margin-top:10%;\"><font size=7>" + valueReplace + "</font>";
    private static final Integer refreshTime = 1;         // 刷新重定向之前的停留时间,单位：秒

    @Resource
    private SecurityRuleService securityRuleService;

    @RequestMapping("dataQuery")
    public void dataQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            // 获取 session
            HttpSession session = request.getSession(false);
            response.setContentType("text/html;charset=UTF-8");
            String rootDir = request.getContextPath();
            String consoleShowDir = rootDir + "/jsp/showSecurityRule.jsp";

            List<SecurityRule> queryInfoList = securityRuleService.queryAll();
            session.setAttribute("securityRuleList", queryInfoList);
            response.sendRedirect(consoleShowDir);
        } catch (IllegalArgumentException e) {
            LOGGER.error("argument illegal", e);
            response.getWriter().write("数据输入错误:" + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Server Exception", e);
            response.getWriter().write("服务器错误!");
        }
    }

    @RequestMapping("dataQueryFilter")
    public void dataQueryFilter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            // 获取 session
            HttpSession session = request.getSession(false);
            response.setContentType("text/html;charset=UTF-8");
            String rootDir = request.getContextPath();
            String consoleShowDir = rootDir + "/jsp/showSecurityRule.jsp";

            String queryId = request.getParameter("queryId");
            session.setAttribute("queryId", queryId);

            String queryName = request.getParameter("queryName");
            session.setAttribute("queryName", queryName);

            String queryKickOut = request.getParameter("queryKickOut");
            session.setAttribute("queryKickOut", queryKickOut);
            String queryKickName = "不限";
            if (StringUtils.isEmpty(queryKickOut) || StringUtils.equalsIgnoreCase("all", queryKickOut)) {
                queryKickName = "不限";
            } else if (StringUtils.equalsIgnoreCase("true", queryKickOut)) {
                queryKickName = "驱逐";
            } else if (StringUtils.equalsIgnoreCase("false", queryKickOut)) {
                queryKickName = "隔离";
            }
            session.setAttribute("queryKickName", queryKickName);

            String queryType = request.getParameter("queryType");
            queryType = StringUtils.isNotEmpty(queryType) ? queryType : ConstantValue.QUERY_ALL_INT.toString();
            session.setAttribute("queryType", queryType);
            session.setAttribute("queryTypeName", ConstantValue.SECURITY_RULE_TYPE_MAP.get(Integer.valueOf(queryType)));

            String queryStatus = request.getParameter("queryStatus");
            queryStatus = StringUtils.isNotEmpty(queryStatus) ? queryStatus : ConstantValue.QUERY_ALL_INT.toString();
            session.setAttribute("queryStatus", queryStatus);
            session.setAttribute("queryStatusName", ConstantValue.STATUS_MAP.get(Integer.valueOf(queryStatus)));

            SecurityRule queryParam = new SecurityRule();
            queryParam.setId(StringUtils.isEmpty(queryId) ? null : Integer.valueOf(queryId));
            queryParam.setName(StringUtils.isEmpty(queryName) ? null : queryName);
            queryParam.setType((StringUtils.isEmpty(queryType) || StringUtils.equalsIgnoreCase(ConstantValue.QUERY_ALL_INT.toString(), queryType)) ? null : Integer.valueOf(queryType));
            queryParam.setStatus((StringUtils.isEmpty(queryStatus) || StringUtils.equalsIgnoreCase(ConstantValue.QUERY_ALL_INT.toString(), queryStatus)) ? null : Integer.valueOf(queryStatus));

            String pageIndexStr = request.getParameter("pageIndex");
            String pageSizeStr = request.getParameter("pageSize");
            Integer pageIndex = ConstantValue.PAGE_INDEX_DEFAULT;
            Integer pageSize = ConstantValue.PAGE_SIZE_DEFAULT;
            if (StringUtils.isNotEmpty(pageIndexStr) && pageIndexStr.matches("^[0-9]*$")) {
                pageIndex = Integer.valueOf(pageIndexStr);
            }
            if (StringUtils.isNotEmpty(pageSizeStr) && pageSizeStr.matches("^[0-9]*$")) {
                pageSize = Integer.valueOf(pageSizeStr);
            }

//            LOGGER.info("[dataQueryFilter] queryParam:{}", queryParam);
            PageInfo<SecurityRule> pageInfo = securityRuleService.queryFilterByPage(queryParam, pageIndex, pageSize);
            int currentPageNum = pageInfo.getPageNum();        // 当前页
            int currentPageSize = pageInfo.getPageSize();      // 页面大小
            int totalPageNum = pageInfo.getPages();            // 总页数
            long totalNumSize = pageInfo.getTotal();           // 总条数
            List<SecurityRule> queryInfoList = pageInfo.getList();

            session.setAttribute("securityRuleList", queryInfoList);
            session.setAttribute("currentPageNum", currentPageNum);
            session.setAttribute("currentPageSize", currentPageSize);
            session.setAttribute("totalPageNum", totalPageNum);
            session.setAttribute("totalNumSize", totalNumSize);
            response.sendRedirect(consoleShowDir);
        } catch (IllegalArgumentException e) {
            LOGGER.error("argument illegal", e);
            response.getWriter().write("数据输入错误:" + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Server Exception", e);
            response.getWriter().write("服务器错误!");
        }
    }

    @RequestMapping("queryById")
    public void queryById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            String rootDir = request.getContextPath();
            String updateInfoDir = rootDir + "/jsp/updateSecurityRule.jsp";

            // 获取 session
            HttpSession session = request.getSession(false);
            String idStr = request.getParameter("id");

            if (StringUtils.isNotEmpty(idStr)) {
                SecurityRule queryInfo = securityRuleService.queryById(Integer.valueOf(idStr));
                LOGGER.info("[queryById] updateSecurityRule:{}", queryInfo);
                session.setAttribute("updateSecurityRule", queryInfo);
            }

            response.sendRedirect(updateInfoDir);
        } catch (IllegalArgumentException e) {
            LOGGER.error("argument illegal", e);
            response.getWriter().write("数据输入错误:" + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Server Exception", e);
            response.getWriter().write("服务器错误!");
        }
    }

    /**
     * 添加合约
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("dataInsert")
    public void dataInsert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter responseWriter = response.getWriter();
            String rootDir = request.getContextPath();
            String dataQueryDir = rootDir + "/securityRule/dataQueryFilter";

            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String checkCount = request.getParameter("checkCount");
            String checkTimeNumber = request.getParameter("checkTimeNumber");
            String checkTimeUnit = request.getParameter("checkTimeUnit");
            String punishNumber = request.getParameter("punishNumber");
            String punishUnit = request.getParameter("punishUnit");
            String kickOut = request.getParameter("kickOut");
            String status = request.getParameter("status");

            SecurityRule insertRule = new SecurityRule();
            insertRule.setName(name);
            insertRule.setType(StringUtils.isNotEmpty(type) ? Integer.valueOf(type) : 0);
            insertRule.setCheckCount(StringUtils.isNotEmpty(checkCount) ? Integer.valueOf(checkCount) : 0);
            insertRule.setCheckTimeNumber(StringUtils.isNotEmpty(checkTimeNumber) ? Integer.valueOf(checkTimeNumber) : 1);
            insertRule.setCheckTimeUnit(checkTimeUnit);
            insertRule.setPunishNumber(StringUtils.isNotEmpty(punishNumber) ? Integer.valueOf(punishNumber) : 1);
            insertRule.setPunishUnit(punishUnit);
            insertRule.setKickOut(StringUtils.isNotEmpty(kickOut) && StringUtils.equalsIgnoreCase("true", kickOut) ? true : false);
            insertRule.setStatus(StringUtils.isNotEmpty(status) ? Integer.valueOf(status) : 1);
            insertRule.setCreateTime(new Date());
            insertRule.setUpdateTime(new Date());

            LOGGER.info("[dataInsert] insertRule:{}", insertRule);
            securityRuleService.insert(insertRule);

            responseWriter.write(messageModel.replaceAll(valueReplace, "添加成功!"));
            response.setHeader("refresh", refreshTime + ";url=" + dataQueryDir);
        } catch (IllegalArgumentException e) {
            LOGGER.error("argument illegal", e);
            response.getWriter().write("数据输入错误:" + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Server Exception", e);
            response.getWriter().write("服务器错误!");
        }
    }

    /**
     * 更新合约
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("dataUpdate")
    public void dataUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter responseWriter = response.getWriter();
            String rootDir = request.getContextPath();
            String dataQueryDir = rootDir + "/securityRule/dataQueryFilter";

            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String checkCount = request.getParameter("checkCount");
            String checkTimeNumber = request.getParameter("checkTimeNumber");
            String checkTimeUnit = request.getParameter("checkTimeUnit");
            String punishNumber = request.getParameter("punishNumber");
            String punishUnit = request.getParameter("punishUnit");
            String kickOut = request.getParameter("kickOut");
            String status = request.getParameter("status");

            SecurityRule updateRule = new SecurityRule();
            updateRule.setId(Integer.valueOf(id));
            updateRule.setName(name);
            updateRule.setType(StringUtils.isNotEmpty(type) ? Integer.valueOf(type) : 0);
            updateRule.setCheckCount(StringUtils.isNotEmpty(checkCount) ? Integer.valueOf(checkCount) : 0);
            updateRule.setCheckTimeNumber(StringUtils.isNotEmpty(checkTimeNumber) ? Integer.valueOf(checkTimeNumber) : 1);
            updateRule.setCheckTimeUnit(checkTimeUnit);
            updateRule.setPunishNumber(StringUtils.isNotEmpty(punishNumber) ? Integer.valueOf(punishNumber) : 1);
            updateRule.setPunishUnit(punishUnit);
            updateRule.setKickOut(StringUtils.isNotEmpty(kickOut) && StringUtils.equalsIgnoreCase("true", kickOut) ? true : false);
            updateRule.setStatus(StringUtils.isNotEmpty(status) ? Integer.valueOf(status) : 1);
            updateRule.setUpdateTime(new Date());

            LOGGER.info("[dataUpdate] updateRule:{}", updateRule);
            securityRuleService.update(updateRule);

            responseWriter.write(messageModel.replaceAll(valueReplace, "修改成功!"));
            response.setHeader("refresh", refreshTime + ";url=" + dataQueryDir);
        } catch (IllegalArgumentException e) {
            LOGGER.error("argument illegal", e);
            response.getWriter().write("数据输入错误:" + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Server Exception", e);
            response.getWriter().write("服务器错误!");
        }
    }

    /**
     * 删除合约
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("dataDelete")
    public void dataDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter responseWriter = response.getWriter();
            String rootDir = request.getContextPath();
            String dataQueryDir = rootDir + "/securityRule/dataQueryFilter";

            String deleteId = request.getParameter("id");
            Preconditions.checkArgument(StringUtils.isNotEmpty(deleteId), "请提供ID");

            LOGGER.info("[dataDelete] deleteId:{}", deleteId);
            securityRuleService.delete(Integer.valueOf(deleteId));

            responseWriter.write(messageModel.replaceAll(valueReplace, "删除成功!"));
            response.setHeader("refresh", refreshTime + ";url=" + dataQueryDir);
        } catch (IllegalArgumentException e) {
            LOGGER.error("argument illegal", e);
            response.getWriter().write("数据输入错误:" + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Server Exception", e);
            response.getWriter().write("服务器错误!");
        }
    }
}
