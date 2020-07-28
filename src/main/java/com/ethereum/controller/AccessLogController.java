package com.ethereum.controller;

import com.ethereum.bean.AccessLog;
import com.ethereum.bean.ConstantValue;
import com.ethereum.bean.HomePage;
import com.ethereum.service.AccessLogService;
import com.ethereum.service.PunishInfoService;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description:
 */
@Controller
@RequestMapping("/access")
public class AccessLogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessLogController.class);

    private static final String valueReplace = "###";     // 字符串占位符
    private static final String messageModel = "<div style=\"font-family:'楷体';color:#624638; margin-left:40%;margin-top:10%;\"><font size=7>" + valueReplace + "</font>";
    private static final Integer refreshTime = 1;         // 刷新重定向之前的停留时间,单位：秒

    @Resource
    private AccessLogService accessLogService;
    @Resource
    private PunishInfoService punishInfoService;

    @RequestMapping("dataQuery")
    public void dataQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            // 获取 session
            HttpSession session = request.getSession(false);
            response.setContentType("text/html;charset=UTF-8");
            String rootDir = request.getContextPath();
            String consoleShowDir = rootDir + "/jsp/showAccess.jsp";

            List<AccessLog> queryInfoList = accessLogService.queryAll();

            session.setAttribute("accessLogList", queryInfoList);
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
            String consoleShowDir = rootDir + "/jsp/showAccess.jsp";

            String queryId = request.getParameter("queryId");
            session.setAttribute("queryId", queryId);

            String querySubjectName = request.getParameter("querySubjectName");
            session.setAttribute("querySubjectName", querySubjectName);

            String querySubjectUid = request.getParameter("querySubjectUid");
            session.setAttribute("querySubjectUid", querySubjectUid);

            String queryObjectUid = request.getParameter("queryObjectUid");
            session.setAttribute("queryObjectUid", queryObjectUid);

            String queryOperateType = request.getParameter("queryOperateType");
            queryOperateType = StringUtils.isNotEmpty(queryOperateType) ? queryOperateType : ConstantValue.QUERY_ALL_STR;
            session.setAttribute("queryOperateType", queryOperateType);
            session.setAttribute("queryOperateTypeName", ConstantValue.OPERATE_TYPE_MAP.get(queryOperateType));

            String queryFailReason = request.getParameter("queryFailReason");
            queryFailReason = StringUtils.isNotEmpty(queryFailReason) ? queryFailReason : ConstantValue.QUERY_ALL_INT.toString();
            session.setAttribute("queryFailReason", queryFailReason);
            session.setAttribute("queryFailReasonName", ConstantValue.ACCESS_FAIL_REASON_MAP.get(Integer.valueOf(queryFailReason)));

            String queryAccessAllow = request.getParameter("queryAccessAllow");
            queryAccessAllow = StringUtils.isNotEmpty(queryAccessAllow) ? queryAccessAllow : ConstantValue.QUERY_ALL_STR;
            session.setAttribute("queryAccessAllow", queryAccessAllow);
            session.setAttribute("queryAccessAllowName", ConstantValue.ACCESS_Allow_MAP.get(queryAccessAllow));

            AccessLog queryParam = new AccessLog();
            queryParam.setId(StringUtils.isEmpty(queryId) ? null : Integer.valueOf(queryId));
            queryParam.setSubjectName(StringUtils.isEmpty(querySubjectName) ? null : querySubjectName);
            queryParam.setSubjectUid(StringUtils.isEmpty(querySubjectUid) ? null : querySubjectUid);
            queryParam.setObjectUid(StringUtils.isEmpty(queryObjectUid) ? null : queryObjectUid);
            queryParam.setOperateType((StringUtils.isEmpty(queryOperateType) || StringUtils.equalsIgnoreCase(ConstantValue.QUERY_ALL_STR, queryOperateType)) ? null : queryOperateType);
            queryParam.setAccessAllow((StringUtils.isEmpty(queryAccessAllow) || StringUtils.equalsIgnoreCase(ConstantValue.QUERY_ALL_STR, queryAccessAllow)) ? null : Boolean.valueOf(queryAccessAllow));
            queryParam.setFailReason((StringUtils.isEmpty(queryFailReason) || StringUtils.equalsIgnoreCase(ConstantValue.QUERY_ALL_INT.toString(), queryFailReason)) ? null : Integer.valueOf(queryFailReason));

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
            PageInfo<AccessLog> pageInfo = accessLogService.queryFilterByPage(queryParam, pageIndex, pageSize);
            int currentPageNum = pageInfo.getPageNum();        // 当前页
            int currentPageSize = pageInfo.getPageSize();      // 页面大小
            int totalPageNum = pageInfo.getPages();            // 总页数
            long totalNumSize = pageInfo.getTotal();           // 总条数
            List<AccessLog> queryInfoList = pageInfo.getList();

            session.setAttribute("accessLogList", queryInfoList);
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
            String updateInfoDir = rootDir + "/jsp/updateAccess.jsp";

            // 获取 session
            HttpSession session = request.getSession(false);
            String idStr = request.getParameter("id");

            if (StringUtils.isNotEmpty(idStr)) {
                AccessLog queryInfo = accessLogService.queryById(Integer.valueOf(idStr));
                LOGGER.info("[queryById] updateAccessLog:{}", queryInfo);
                session.setAttribute("updateAccessLog", queryInfo);
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
     * 添加访问
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
            String dataQueryDir = rootDir + "/access/dataQueryFilter";

            String subjectUid = request.getParameter("subjectUid");
            String objectUid = request.getParameter("objectUid");
            String operateType = request.getParameter("operateType");
            String operateData = request.getParameter("operateData");
            String accessAllow = request.getParameter("accessAllow");
            String failReason = request.getParameter("failReason");
            String resultData = request.getParameter("resultData");
            String requestTimeStr = request.getParameter("requestTimeStr");
            String responseTimeStr = request.getParameter("responseTimeStr");

            AccessLog insertLog = new AccessLog();
            insertLog.setSubjectUid(subjectUid);
            insertLog.setObjectUid(objectUid);
            insertLog.setOperateType(operateType);
            insertLog.setOperateData(operateData);
            insertLog.setAccessAllow(StringUtils.isNotEmpty(accessAllow) ? Boolean.valueOf(accessAllow) : null);
            insertLog.setFailReason(StringUtils.isNotEmpty(failReason) ? Integer.valueOf(failReason) : null);
            insertLog.setResultData(resultData);
            insertLog.setRequestTimeStr(requestTimeStr);
            insertLog.setResponseTimeStr(responseTimeStr);

            LOGGER.info("[dataInsert] insertLog:{}", insertLog);
            accessLogService.insert(insertLog);

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
     * 更新访问日志
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
            String dataQueryDir = rootDir + "/access/dataQueryFilter";

            String id = request.getParameter("id");
            String subjectUid = request.getParameter("subjectUid");
            String objectUid = request.getParameter("objectUid");
            String operateType = request.getParameter("operateType");
            String operateData = request.getParameter("operateData");
            String accessAllow = request.getParameter("accessAllow");
            String failReason = request.getParameter("failReason");
            String resultData = request.getParameter("resultData");
            String requestTimeStr = request.getParameter("requestTimeStr");
            String responseTimeStr = request.getParameter("responseTimeStr");

            AccessLog updateLog = new AccessLog();
            updateLog.setId(Integer.valueOf(id));
            updateLog.setSubjectUid(subjectUid);
            updateLog.setObjectUid(objectUid);
            updateLog.setOperateType(operateType);
            updateLog.setOperateData(operateData);
            updateLog.setAccessAllow(StringUtils.isNotEmpty(accessAllow) ? Boolean.valueOf(accessAllow) : null);
            updateLog.setFailReason(StringUtils.isNotEmpty(failReason) ? Integer.valueOf(failReason) : null);
            updateLog.setResultData(resultData);
            updateLog.setRequestTimeStr(requestTimeStr);
            updateLog.setResponseTimeStr(responseTimeStr);

            LOGGER.info("[dataUpdate] updateLog:{}", updateLog);
            accessLogService.update(updateLog);

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
     * 删除惩罚
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
            String dataQueryDir = rootDir + "/access/dataQueryFilter";

            String deleteId = request.getParameter("id");
            Preconditions.checkArgument(StringUtils.isNotEmpty(deleteId), "请提供ID");

            LOGGER.info("[dataDelete] deleteId:{}", deleteId);
            accessLogService.delete(Integer.valueOf(deleteId));

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

    /**
     * 添加访问日志
     * application/json 请求
     *
     * @throws IOException
     */
    @RequestMapping("/outer/addLog")
    @ResponseBody
    public HomePage addLog(@RequestBody(required = false) AccessLog accessLog) {
        try {

            LOGGER.info("[addLog] accessLog:{}", accessLog);
            accessLogService.insert(accessLog);
            punishInfoService.executePunish(accessLog.getSubjectUid());

            return HomePage.buildAtSuccess("成功", accessLog);
        } catch (Exception e) {
            LOGGER.error("[addLog] Server Exception", e);
            return HomePage.buildAtFailed("服务器异常");
        }
    }

    /**
     * 添加访问日志
     * application/json 请求
     *
     * @throws IOException
     */
    @RequestMapping("/outer/updateLog")
    @ResponseBody
    public HomePage updateLog(@RequestBody(required = false) AccessLog accessLog) {
        try {

            LOGGER.info("[updateLog] accessLog:{}", accessLog);
            accessLogService.update(accessLog);

            return HomePage.buildAtSuccess("成功", accessLog);
        } catch (Exception e) {
            LOGGER.error("[updateLog] Server Exception", e);
            return HomePage.buildAtFailed("服务器异常");
        }
    }
}
