package com.ethereum.controller;

import com.ethereum.bean.ConstantValue;
import com.ethereum.bean.DeviceInfo;
import com.ethereum.bean.HomePage;
import com.ethereum.bean.PunishInfo;
import com.ethereum.service.AccessLogService;
import com.ethereum.service.DeviceInfoService;
import com.ethereum.service.PunishInfoService;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.List;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description:
 */
@Controller
@RequestMapping("/punish")
public class PunishController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PunishController.class);

    private static final String valueReplace = "###";     // 字符串占位符
    private static final String messageModel = "<div style=\"font-family:'楷体';color:#624638; margin-left:40%;margin-top:10%;\"><font size=7>" + valueReplace + "</font>";
    private static final Integer refreshTime = 1;         // 刷新重定向之前的停留时间,单位：秒

    @Resource
    private PunishInfoService punishInfoService;
    @Resource
    private AccessLogService accessLogService;

    @Resource
    private DeviceInfoService deviceInfoService;

    @RequestMapping("dataQuery")
    public void dataQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            // 获取 session
            HttpSession session = request.getSession(false);
            response.setContentType("text/html;charset=UTF-8");
            String rootDir = request.getContextPath();
            String consoleShowDir = rootDir + "/jsp/showPunish.jsp";

            List<PunishInfo> queryInfoList = punishInfoService.queryAll();

            session.setAttribute("punishInfoList", queryInfoList);
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
            String consoleShowDir = rootDir + "/jsp/showPunish.jsp";

            String queryId = request.getParameter("queryId");
            session.setAttribute("queryId", queryId);

            String queryDeviceName = request.getParameter("queryDeviceName");
            session.setAttribute("queryDeviceName", queryDeviceName);

            String queryRuleName = request.getParameter("queryRuleName");
            session.setAttribute("queryRuleName", queryRuleName);

            String queryRuleType = request.getParameter("queryRuleType");
            queryRuleType = StringUtils.isNotEmpty(queryRuleType) ? queryRuleType : ConstantValue.QUERY_ALL_INT.toString();
            session.setAttribute("queryRuleType", queryRuleType);
            session.setAttribute("queryRuleTypeName", ConstantValue.SECURITY_RULE_TYPE_MAP.get(Integer.valueOf(queryRuleType)));

            String queryPunish = request.getParameter("queryPunish");
            queryPunish = StringUtils.isNotEmpty(queryPunish) ? queryPunish : ConstantValue.QUERY_ALL_INT.toString();
            session.setAttribute("queryPunish", queryPunish);
            session.setAttribute("queryPunishName", ConstantValue.PUNISH_TYPE_MAP.get(Integer.valueOf(queryPunish)));

            String queryStatus = request.getParameter("queryStatus");
            queryStatus = StringUtils.isNotEmpty(queryStatus) ? queryStatus : ConstantValue.QUERY_ALL_INT.toString();
            session.setAttribute("queryStatus", queryStatus);
            session.setAttribute("queryStatusName", ConstantValue.STATUS_MAP.get(Integer.valueOf(queryStatus)));

            PunishInfo queryParam = new PunishInfo();
            queryParam.setId(StringUtils.isEmpty(queryId) ? null : Integer.valueOf(queryId));
            queryParam.setDeviceName(StringUtils.isEmpty(queryDeviceName) ? null : queryDeviceName);
            queryParam.setRuleName((StringUtils.isEmpty(queryRuleName) || StringUtils.equalsIgnoreCase(ConstantValue.QUERY_ALL_INT.toString(), queryRuleName)) ? null : queryRuleName);
            queryParam.setPunish((StringUtils.isEmpty(queryPunish) || StringUtils.equalsIgnoreCase(ConstantValue.QUERY_ALL_INT.toString(), queryPunish)) ? null : Integer.valueOf(queryPunish));
            queryParam.setRuleType((StringUtils.isEmpty(queryRuleType) || StringUtils.equalsIgnoreCase(ConstantValue.QUERY_ALL_INT.toString(), queryRuleType)) ? null : Integer.valueOf(queryRuleType));
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
            PageInfo<PunishInfo> pageInfo = punishInfoService.queryFilterByPage(queryParam, pageIndex, pageSize);
            int currentPageNum = pageInfo.getPageNum();        // 当前页
            int currentPageSize = pageInfo.getPageSize();      // 页面大小
            int totalPageNum = pageInfo.getPages();            // 总页数
            long totalNumSize = pageInfo.getTotal();           // 总条数
            List<PunishInfo> queryInfoList = pageInfo.getList();

            session.setAttribute("punishInfoList", queryInfoList);
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
            String updateInfoDir = rootDir + "/jsp/updatePunish.jsp";

            // 获取 session
            HttpSession session = request.getSession(false);
            String idStr = request.getParameter("id");

            if (StringUtils.isNotEmpty(idStr)) {
                PunishInfo queryInfo = punishInfoService.queryById(Integer.valueOf(idStr));
                LOGGER.info("[queryById] updatePunishInfo:{}", queryInfo);
                session.setAttribute("updatePunishInfo", queryInfo);
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
     * 添加惩罚
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
            String dataQueryDir = rootDir + "/punish/dataQueryFilter";

            String deviceUid = request.getParameter("deviceUid");
            String ruleName = request.getParameter("ruleName");
            String punish = request.getParameter("punish");
            String punishStartStr = request.getParameter("punishStartStr");
            String punishEndStr = request.getParameter("punishEndStr");
            String status = request.getParameter("status");

            PunishInfo insertInfo = new PunishInfo();
            insertInfo.setDeviceUid(deviceUid);
            insertInfo.setRuleName(ruleName);
            insertInfo.setPunishStartStr(punishStartStr);
            insertInfo.setPunishEndStr(punishEndStr);
            insertInfo.setPunish(StringUtils.isNotEmpty(punish) ? Integer.valueOf(punish) : 0);
            insertInfo.setStatus(StringUtils.isNotEmpty(status) ? Integer.valueOf(status) : 1);

            LOGGER.info("[dataInsert] insertInfo:{}", insertInfo);
            punishInfoService.insert(insertInfo);

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
     * 更新惩罚信息
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
            String dataQueryDir = rootDir + "/punish/dataQueryFilter";

            String id = request.getParameter("id");
            String deviceUid = request.getParameter("deviceUid");
            String ruleName = request.getParameter("ruleName");
            String punish = request.getParameter("punish");
            String punishStartStr = request.getParameter("punishStartStr");
            String punishEndStr = request.getParameter("punishEndStr");
            String status = request.getParameter("status");

            PunishInfo updateInfo = new PunishInfo();
            updateInfo.setId(Integer.valueOf(id));
            updateInfo.setDeviceUid(deviceUid);
            updateInfo.setRuleName(ruleName);
            updateInfo.setPunishStartStr(punishStartStr);
            updateInfo.setPunishEndStr(punishEndStr);
            updateInfo.setPunish(StringUtils.isNotEmpty(punish) ? Integer.valueOf(punish) : 0);
            updateInfo.setStatus(StringUtils.isNotEmpty(status) ? Integer.valueOf(status) : 1);

            LOGGER.info("[dataUpdate] updateInfo:{}", updateInfo);
            punishInfoService.update(updateInfo);

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
            String dataQueryDir = rootDir + "/punish/dataQueryFilter";

            String deleteId = request.getParameter("id");
            Preconditions.checkArgument(StringUtils.isNotEmpty(deleteId), "请提供ID");

            LOGGER.info("[dataDelete] deleteId:{}", deleteId);
            punishInfoService.delete(Integer.valueOf(deleteId));

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
     * 惩罚状态设为不可用
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("dataDisable")
    public void dataDisable(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter responseWriter = response.getWriter();
            String rootDir = request.getContextPath();
            String dataQueryDir = rootDir + "/punish/dataQueryFilter";

            String disableId = request.getParameter("id");
            Preconditions.checkArgument(StringUtils.isNotEmpty(disableId), "请提供ID");

            LOGGER.info("[dataDisable] disableId:{}", disableId);
            PunishInfo punishInfo = punishInfoService.queryById(Integer.valueOf(disableId));
            LOGGER.info("[dataDisable] queryPunishInfo:{}", punishInfo);

            // 设置惩罚状态为不可用
            punishInfoService.updateStatusDisable(Integer.valueOf(disableId));
            // 将日志状态设置为不可用
            accessLogService.updateStatusDisable(punishInfo.getDeviceUid());
            // 将设备状态设置为可用
            DeviceInfo deviceInfo = deviceInfoService.queryByUid(punishInfo.getDeviceUid());
            if (deviceInfo != null && deviceInfo.getStatus() == 0) {
                deviceInfoService.updateStatusEnable(punishInfo.getDeviceUid());
            }
            responseWriter.write(messageModel.replaceAll(valueReplace, "解除惩罚成功!"));
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
            List<PunishInfo> punishInfos = punishInfoService.queryDevicePunishInfo(deviceUid);
            if (CollectionUtils.isEmpty(punishInfos)) {
                punishInfos = null;
            }

            LOGGER.info("[queryByUid] deviceUid:{} punishInfos:{}", deviceUid, punishInfos);
            return HomePage.buildAtSuccess("成功", punishInfos);
        } catch (Exception e) {
            LOGGER.error("[queryByUid] catch exception", e);
            return HomePage.buildAtFailed("服务器异常");
        }
    }
}
