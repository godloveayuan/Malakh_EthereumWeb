package com.ethereum.controller;

import com.ethereum.bean.AgentInfo;
import com.ethereum.bean.ConstantValue;
import com.ethereum.service.AgentInfoService;
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
@RequestMapping("/agent")
public class AgentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentController.class);

    private static final String valueReplace = "###";     // 字符串占位符
    private static final String messageModel = "<div style=\"font-family:'楷体';color:#624638; margin-left:40%;margin-top:10%;\"><font size=7>" + valueReplace + "</font>";
    private static final Integer refreshTime = 1;         // 刷新重定向之前的停留时间,单位：秒

    @Resource
    private AgentInfoService agentInfoService;

    @RequestMapping("dataQuery")
    public void dataQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            // 获取 session
            HttpSession session = request.getSession(false);
            response.setContentType("text/html;charset=UTF-8");
            String rootDir = request.getContextPath();
            String consoleShowDir = rootDir + "/jsp/showAgent.jsp";

            List<AgentInfo> agentInfoList = agentInfoService.queryAll();
            session.setAttribute("agentInfoList", agentInfoList);
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
            String consoleShowDir = rootDir + "/jsp/showAgent.jsp";

            String queryId = request.getParameter("queryId");
            session.setAttribute("queryId", queryId);

            String queryDeviceName = request.getParameter("queryDeviceName");
            session.setAttribute("queryDeviceName", queryDeviceName);

            String queryDeviceMac = request.getParameter("queryDeviceMac");
            session.setAttribute("queryDeviceMac", queryDeviceMac);

            String queryDeviceUid = request.getParameter("queryDeviceUid");
            session.setAttribute("queryDeviceUid", queryDeviceUid);


            String queryStatus = request.getParameter("queryStatus");
            queryStatus = StringUtils.isNotEmpty(queryStatus) ? queryStatus : ConstantValue.QUERY_ALL_INT.toString();
            session.setAttribute("queryStatus", queryStatus);
            session.setAttribute("queryStatusName", ConstantValue.STATUS_MAP.get(Integer.valueOf(queryStatus)));

            AgentInfo queryParam = new AgentInfo();
            queryParam.setId(StringUtils.isEmpty(queryId) ? null : Integer.valueOf(queryId));
            queryParam.setDeviceName(StringUtils.isEmpty(queryDeviceName) ? null : queryDeviceName);
            queryParam.setDeviceMac(StringUtils.isEmpty(queryDeviceMac) ? null : queryDeviceMac);
            queryParam.setDeviceUid(StringUtils.isEmpty(queryDeviceUid) ? null : queryDeviceUid);
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
            PageInfo<AgentInfo> pageInfo = agentInfoService.queryFilterByPage(queryParam, pageIndex, pageSize);
            int currentPageNum = pageInfo.getPageNum();        // 当前页
            int currentPageSize = pageInfo.getPageSize();      // 页面大小
            int totalPageNum = pageInfo.getPages();            // 总页数
            long totalNumSize = pageInfo.getTotal();           // 总条数
            List<AgentInfo> agentInfoList = pageInfo.getList();

            session.setAttribute("agentInfoList", agentInfoList);
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
            String updateInfoDir = rootDir + "/jsp/updateAgent.jsp";

            // 获取 session
            HttpSession session = request.getSession(false);
            String idStr = request.getParameter("id");

            if (StringUtils.isNotEmpty(idStr)) {
                AgentInfo queryInfo = agentInfoService.queryById(Integer.valueOf(idStr));
                LOGGER.info("[queryById] updateAgentInfo:{}", queryInfo);
                session.setAttribute("updateAgentInfo", queryInfo);
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
     * 注册设备
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
            String dataQueryDir = rootDir + "/agent/dataQueryFilter";

            String deviceName = request.getParameter("deviceName");
            String deviceMac = request.getParameter("deviceMac");
            String manufacture = request.getParameter("manufacture");
            String address = request.getParameter("address");
            String status = request.getParameter("status");

            AgentInfo insertInfo = new AgentInfo();
            insertInfo.setDeviceName(deviceName);
            insertInfo.setDeviceMac(deviceMac);
            insertInfo.setManufacture(manufacture);
            insertInfo.setAddress(address);
            insertInfo.setCreateTime(new Date());
            insertInfo.setUpdateTime(new Date());
            insertInfo.setStatus(StringUtils.isNotEmpty(status) ? Integer.valueOf(status) : 1);

            LOGGER.info("[dataInsert] insertInfo:{}", insertInfo);
            agentInfoService.insert(insertInfo);

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
     * 更新设备
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
            String dataQueryDir = rootDir + "/agent/dataQueryFilter";

            String id = request.getParameter("id");
            String deviceName = request.getParameter("deviceName");
            String deviceMac = request.getParameter("deviceMac");
            String manufacture = request.getParameter("manufacture");
            String address = request.getParameter("address");
            String status = request.getParameter("status");

            AgentInfo updateInfo = new AgentInfo();
            updateInfo.setId(Integer.valueOf(id));
            updateInfo.setDeviceName(deviceName);
            updateInfo.setDeviceMac(deviceMac);
            updateInfo.setManufacture(manufacture);
            updateInfo.setAddress(address);
            updateInfo.setUpdateTime(new Date());
            updateInfo.setStatus(StringUtils.isNotEmpty(status) ? Integer.valueOf(status) : 1);

            LOGGER.info("[dataUpdate] updateInfo:{}", updateInfo);
            agentInfoService.update(updateInfo);

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
     * 删除设备
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
            String dataQueryDir = rootDir + "/agent/dataQueryFilter";

            String deleteId = request.getParameter("id");
            Preconditions.checkArgument(StringUtils.isNotEmpty(deleteId), "请提供设备ID");

            LOGGER.info("[dataDelete] deleteId:{}", deleteId);
            agentInfoService.delete(Integer.valueOf(deleteId));

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
