package com.ethereum.controller;

import com.ethereum.bean.ConstantValue;
import com.ethereum.bean.DeviceInfo;
import com.ethereum.bean.HomePage;
import com.ethereum.service.DeviceInfoService;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping("/device")
public class DeviceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    private static final String valueReplace = "###";     // 字符串占位符
    private static final String messageModel = "<div style=\"font-family:'楷体';color:#624638; margin-left:40%;margin-top:10%;\"><font size=7>" + valueReplace + "</font>";
    private static final Integer refreshTime = 1;         // 刷新重定向之前的停留时间,单位：秒

    @Resource
    private DeviceInfoService deviceInfoService;

    @RequestMapping("dataQueryFilter")
    public void dataQueryFilter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            // 获取 session
            HttpSession session = request.getSession(false);
            response.setContentType("text/html;charset=UTF-8");
            String rootDir = request.getContextPath();
            String consoleShowDir = rootDir + "/jsp/showDevice.jsp";

            String queryId = request.getParameter("queryId");
            session.setAttribute("queryId", queryId);

            String queryDeviceName = request.getParameter("queryDeviceName");
            session.setAttribute("queryDeviceName", queryDeviceName);

            String queryDeviceMac = request.getParameter("queryDeviceMac");
            session.setAttribute("queryDeviceMac", queryDeviceMac);

            String queryDeviceUid = request.getParameter("queryDeviceUid");
            session.setAttribute("queryDeviceUid", queryDeviceUid);

            String queryAttributeType = request.getParameter("queryAttributeType");
            queryAttributeType = StringUtils.isNotEmpty(queryAttributeType) ? queryAttributeType : ConstantValue.QUERY_ALL_STR;
            session.setAttribute("queryAttributeType", queryAttributeType);
            session.setAttribute("queryAttributeTypeName", ConstantValue.DEVICE_ATTR_TYPE_MAP.get(queryAttributeType));

            String queryAttributePosition = request.getParameter("queryAttributePosition");
            queryAttributePosition = StringUtils.isNotEmpty(queryAttributePosition) ? queryAttributePosition : ConstantValue.QUERY_ALL_STR;
            session.setAttribute("queryAttributePosition", queryAttributePosition);
            session.setAttribute("queryAttributePositionName", ConstantValue.DEVICE_ATTR_POSITION_MAP.get(queryAttributePosition));

            String queryAttributeSystem = request.getParameter("queryAttributeSystem");
            queryAttributeSystem = StringUtils.isNotEmpty(queryAttributeSystem) ? queryAttributeSystem : ConstantValue.QUERY_ALL_STR;
            session.setAttribute("queryAttributeSystem", queryAttributeSystem);
            session.setAttribute("queryAttributeSystemName", ConstantValue.DEVICE_ATTR_SYSTEM_MAP.get(queryAttributeSystem));

            String queryDeviceType = request.getParameter("queryDeviceType");
            queryDeviceType = StringUtils.isNotEmpty(queryDeviceType) ? queryDeviceType : ConstantValue.QUERY_ALL_STR;
            session.setAttribute("queryDeviceType", queryDeviceType);
            session.setAttribute("queryDeviceTypeName", ConstantValue.DEVICE_TYPE_MAP.get(queryDeviceType));

            String queryStatus = request.getParameter("queryStatus");
            queryStatus = StringUtils.isNotEmpty(queryStatus) ? queryStatus : ConstantValue.QUERY_ALL_INT.toString();
            session.setAttribute("queryStatus", queryStatus);
            session.setAttribute("queryStatusName", ConstantValue.STATUS_MAP.get(Integer.valueOf(queryStatus)));

            DeviceInfo queryParam = new DeviceInfo();
            queryParam.setId(StringUtils.isEmpty(queryId) ? null : Integer.valueOf(queryId));
            queryParam.setDeviceName(StringUtils.isEmpty(queryDeviceName) ? null : queryDeviceName);
            queryParam.setDeviceMac(StringUtils.isEmpty(queryDeviceMac) ? null : queryDeviceMac);
            queryParam.setDeviceUid(StringUtils.isEmpty(queryDeviceUid) ? null : queryDeviceUid);
            queryParam.setDeviceType((StringUtils.isEmpty(queryDeviceType) || StringUtils.equalsIgnoreCase(ConstantValue.QUERY_ALL_STR, queryDeviceType)) ? null : queryDeviceType);
            queryParam.setAttributeType((StringUtils.isEmpty(queryAttributeType) || StringUtils.equalsIgnoreCase(ConstantValue.QUERY_ALL_STR, queryAttributeType)) ? null : queryAttributeType);
            queryParam.setAttributePosition((StringUtils.isEmpty(queryAttributePosition) || StringUtils.equalsIgnoreCase(ConstantValue.QUERY_ALL_STR, queryAttributePosition)) ? null : queryAttributePosition);
            queryParam.setAttributeSystem((StringUtils.isEmpty(queryAttributeSystem) || StringUtils.equalsIgnoreCase(ConstantValue.QUERY_ALL_STR, queryAttributeSystem)) ? null : queryAttributeSystem);
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
            PageInfo<DeviceInfo> pageInfo = deviceInfoService.queryFilterByPage(queryParam, pageIndex, pageSize);
            int currentPageNum = pageInfo.getPageNum();        // 当前页
            int currentPageSize = pageInfo.getPageSize();      // 页面大小
            int totalPageNum = pageInfo.getPages();            // 总页数
            long totalNumSize = pageInfo.getTotal();           // 总条数
            List<DeviceInfo> deviceInfoList = pageInfo.getList();

            session.setAttribute("deviceInfoList", deviceInfoList);
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
            String updateInfoDir = rootDir + "/jsp/updateDevice.jsp";

            // 获取 session
            HttpSession session = request.getSession(false);
            String idStr = request.getParameter("id");

            if (StringUtils.isNotEmpty(idStr)) {
                DeviceInfo queryInfo = deviceInfoService.queryById(Integer.valueOf(idStr));
                LOGGER.info("[queryById] updateDeviceInfo:{}", queryInfo);
                session.setAttribute("updateDeviceInfo", queryInfo);
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
            String dataQueryDir = rootDir + "/device/dataQueryFilter";

            String deviceName = request.getParameter("deviceName");
            String deviceMac = request.getParameter("deviceMac");
            String manufacture = request.getParameter("manufacture");
            String agentDevice = request.getParameter("agentDevice");
            String attributeType = request.getParameter("attributeType");
            String attributePosition = request.getParameter("attributePosition");
            String attributeSystem = request.getParameter("attributeSystem");
            String deviceType = request.getParameter("deviceType");
            String status = request.getParameter("status");
            DeviceInfo insertInfo = new DeviceInfo();
            insertInfo.setDeviceName(deviceName);
            insertInfo.setDeviceMac(deviceMac);
            insertInfo.setManufacture(manufacture);
            insertInfo.setAgentDevice(agentDevice);
            insertInfo.setAttributeType(attributeType);
            insertInfo.setAttributePosition(attributePosition);
            insertInfo.setAttributeSystem(attributeSystem);
            insertInfo.setDeviceType(deviceType);
            insertInfo.setCreateTime(new Date());
            insertInfo.setUpdateTime(new Date());
            insertInfo.setStatus(StringUtils.isNotEmpty(status) ? Integer.valueOf(status) : 1);

            LOGGER.info("[dataInsert] insertInfo:{}", insertInfo);
            deviceInfoService.insert(insertInfo);

            responseWriter.write(messageModel.replaceAll(valueReplace, "注册成功!"));
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
            String dataQueryDir = rootDir + "/device/dataQueryFilter";

            String id = request.getParameter("id");
            String deviceName = request.getParameter("deviceName");
            String deviceMac = request.getParameter("deviceMac");
            String manufacture = request.getParameter("manufacture");
            String agentDevice = request.getParameter("agentDevice");
            String attributeType = request.getParameter("attributeType");
            String attributePosition = request.getParameter("attributePosition");
            String attributeSystem = request.getParameter("attributeSystem");
            String deviceType = request.getParameter("deviceType");
            String status = request.getParameter("status");

            DeviceInfo updateInfo = new DeviceInfo();
            updateInfo.setId(Integer.valueOf(id));
            updateInfo.setDeviceName(deviceName);
            updateInfo.setDeviceMac(deviceMac);
            updateInfo.setManufacture(manufacture);
            updateInfo.setAgentDevice(agentDevice);
            updateInfo.setAttributeType(attributeType);
            updateInfo.setAttributePosition(attributePosition);
            updateInfo.setAttributeSystem(attributeSystem);
            updateInfo.setDeviceType(deviceType);
            updateInfo.setUpdateTime(new Date());
            updateInfo.setStatus(StringUtils.isNotEmpty(status) ? Integer.valueOf(status) : 1);

            LOGGER.info("[dataUpdate] updateInfo:{}", updateInfo);
            deviceInfoService.update(updateInfo);

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
            String dataQueryDir = rootDir + "/device/dataQueryFilter";

            String deleteId = request.getParameter("id");
            Preconditions.checkArgument(StringUtils.isNotEmpty(deleteId), "请提供设备ID");

            LOGGER.info("[dataDelete] deviceId:{}", deleteId);
            deviceInfoService.delete(Integer.valueOf(deleteId));

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
     * 提供给外部的查询接口
     *
     * @param deviceUid
     * @return
     */
    @RequestMapping("/outer/queryByUid")
    @ResponseBody
    public HomePage queryByUid(@RequestParam(required = true, value = "deviceUid") String deviceUid) {
        try {
            if (StringUtils.isEmpty(deviceUid)) {
                return HomePage.buildAtFailed("deviceUid为空");
            }

            DeviceInfo deviceInfo = deviceInfoService.queryByUid(deviceUid);
            LOGGER.info("[queryByUid] deviceUid:{} deviceInfo:{}", deviceUid, deviceInfo);
            HomePage homePage = HomePage.buildAtSuccess("成功");
            homePage.setData(deviceInfo);
            return homePage;
        } catch (Exception e) {
            LOGGER.error("[queryByUid] catch exception", e);
            return HomePage.buildAtFailed("服务器异常");
        }
    }

    /**
     * 提供给外部的查询接口
     *
     * @param deviceMac
     * @return
     */
    @RequestMapping("/outer/queryByMac")
    @ResponseBody
    public HomePage queryByMac(@RequestParam(required = true, value = "deviceMac") String deviceMac) {
        try {
            if (StringUtils.isEmpty(deviceMac)) {
                return HomePage.buildAtFailed("deviceMac为空");
            }
            DeviceInfo deviceInfo = deviceInfoService.queryByMac(deviceMac);
            LOGGER.info("[queryByMac] deviceMac:{} deviceInfo:{}", deviceMac, deviceInfo);
            HomePage homePage = HomePage.buildAtSuccess("成功");
            homePage.setData(deviceInfo);
            return homePage;
        } catch (Exception e) {
            LOGGER.error("[queryByMac] catch exception", e);
            return HomePage.buildAtFailed("服务器异常");
        }
    }

}
