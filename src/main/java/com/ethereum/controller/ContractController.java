package com.ethereum.controller;

import com.ethereum.bean.ConstantValue;
import com.ethereum.bean.ContractInfo;
import com.ethereum.bean.HomePage;
import com.ethereum.service.ContractInfoService;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
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
import java.util.Date;
import java.util.List;

/**
 * @Author: Malakh
 * @Date: 2020/2/8
 * @Description:
 */
@Controller
@RequestMapping("/contract")
public class ContractController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractController.class);

    private static final String valueReplace = "###";     // 字符串占位符
    private static final String messageModel = "<div style=\"font-family:'楷体';color:#624638; margin-left:40%;margin-top:10%;\"><font size=7>" + valueReplace + "</font>";
    private static final Integer refreshTime = 1;         // 刷新重定向之前的停留时间,单位：秒

    @Resource
    private ContractInfoService contractInfoService;

    @RequestMapping("dataQuery")
    public void dataQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            // 获取 session
            HttpSession session = request.getSession(false);
            response.setContentType("text/html;charset=UTF-8");
            String rootDir = request.getContextPath();
            String consoleShowDir = rootDir + "/jsp/showContract.jsp";

            List<ContractInfo> queryInfoList = contractInfoService.queryAll();

            session.setAttribute("contractInfoList", queryInfoList);
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
            String consoleShowDir = rootDir + "/jsp/showContract.jsp";

            String queryId = request.getParameter("queryId");
            session.setAttribute("queryId", queryId);

            String queryName = request.getParameter("queryName");
            session.setAttribute("queryName", queryName);

            String queryAddress = request.getParameter("queryAddress");
            session.setAttribute("queryAddress", queryAddress);

            String queryOwner = request.getParameter("queryOwner");
            session.setAttribute("queryOwner", queryOwner);
            String queryOwnerName = request.getParameter("queryOwnerName");
            session.setAttribute("queryOwnerName", queryOwnerName);

            String queryType = request.getParameter("queryType");
            queryType = StringUtils.isNotEmpty(queryType) ? queryType : ConstantValue.QUERY_ALL_STR;
            session.setAttribute("queryType", queryType);
            session.setAttribute("queryTypeName", ConstantValue.CONTRACT_TYPE_MAP.get(queryType));

            String queryStatus = request.getParameter("queryStatus");
            queryStatus = StringUtils.isNotEmpty(queryStatus) ? queryStatus : ConstantValue.QUERY_ALL_INT.toString();
            session.setAttribute("queryStatus", queryStatus);
            session.setAttribute("queryStatusName", ConstantValue.STATUS_MAP.get(Integer.valueOf(queryStatus)));

            ContractInfo queryParam = new ContractInfo();
            queryParam.setId(StringUtils.isEmpty(queryId) ? null : Integer.valueOf(queryId));
            queryParam.setName(StringUtils.isEmpty(queryName) ? null : queryName);
            queryParam.setAddress(StringUtils.isEmpty(queryAddress) ? null : queryAddress);
            queryParam.setType((StringUtils.isEmpty(queryType) || StringUtils.equalsIgnoreCase(ConstantValue.QUERY_ALL_STR, queryType)) ? null : queryType);
            queryParam.setOwner(StringUtils.isEmpty(queryOwner) ? null : queryOwner);
            queryParam.setOwnerName(StringUtils.isEmpty(queryOwnerName) ? null : queryOwnerName);
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
            PageInfo<ContractInfo> pageInfo = contractInfoService.queryFilterByPage(queryParam, pageIndex, pageSize);
            int currentPageNum = pageInfo.getPageNum();        // 当前页
            int currentPageSize = pageInfo.getPageSize();      // 页面大小
            int totalPageNum = pageInfo.getPages();            // 总页数
            long totalNumSize = pageInfo.getTotal();           // 总条数
            List<ContractInfo> queryInfoList = pageInfo.getList();

            session.setAttribute("contractInfoList", queryInfoList);
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
            String updateInfoDir = rootDir + "/jsp/updateContract.jsp";

            // 获取 session
            HttpSession session = request.getSession(false);
            String idStr = request.getParameter("id");

            if (StringUtils.isNotEmpty(idStr)) {
                ContractInfo queryInfo = contractInfoService.queryById(Integer.valueOf(idStr));
                LOGGER.info("[queryById] updateContractInfo:{}", queryInfo);
                session.setAttribute("updateContractInfo", queryInfo);
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
            String dataQueryDir = rootDir + "/contract/dataQueryFilter";

            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String owner = request.getParameter("owner");
            String address = request.getParameter("address");
            String status = request.getParameter("status");

            ContractInfo insertInfo = new ContractInfo();
            insertInfo.setName(name);
            insertInfo.setType(type);
            insertInfo.setOwner(owner);
            insertInfo.setAddress(address);
            insertInfo.setCreateTime(new Date());
            insertInfo.setUpdateTime(new Date());
            insertInfo.setStatus(StringUtils.isNotEmpty(status) ? Integer.valueOf(status) : 1);

            LOGGER.info("[dataInsert] insertInfo:{}", insertInfo);
            contractInfoService.insert(insertInfo);

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
            String dataQueryDir = rootDir + "/contract/dataQueryFilter";

            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String owner = request.getParameter("owner");
            String address = request.getParameter("address");
            String status = request.getParameter("status");

            ContractInfo updateInfo = new ContractInfo();
            updateInfo.setId(Integer.valueOf(id));
            updateInfo.setName(name);
            updateInfo.setType(type);
            updateInfo.setOwner(owner);
            updateInfo.setAddress(address);
            updateInfo.setUpdateTime(new Date());
            updateInfo.setStatus(StringUtils.isNotEmpty(status) ? Integer.valueOf(status) : 1);

            LOGGER.info("[dataUpdate] updateInfo:{}", updateInfo);
            contractInfoService.update(updateInfo);

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
            String dataQueryDir = rootDir + "/contract/dataQueryFilter";

            String deleteId = request.getParameter("id");
            Preconditions.checkArgument(StringUtils.isNotEmpty(deleteId), "请提供ID");

            LOGGER.info("[dataDelete] deleteId:{}", deleteId);
            contractInfoService.delete(Integer.valueOf(deleteId));

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
     * 根据设备uid查询对应的公共策略合约和专属策略合约
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

            List<ContractInfo> resultContracts = Lists.newArrayList();

            List<ContractInfo> publicContracts = contractInfoService.queryPublicContract();
            if (CollectionUtils.isNotEmpty(publicContracts)) {
                resultContracts.addAll(publicContracts);
            }

            List<ContractInfo> privateContracts = contractInfoService.queryPrivateContract(deviceUid);
            if (CollectionUtils.isNotEmpty(privateContracts)) {
                resultContracts.addAll(privateContracts);
            }

            if (CollectionUtils.isEmpty(resultContracts)) {
                resultContracts = null;
            }

            LOGGER.info("[queryByUid] deviceUid:{} resultContracts:{}", deviceUid, resultContracts);
            return HomePage.buildAtSuccess("成功", resultContracts);
        } catch (Exception e) {
            LOGGER.error("[queryByUid] catch exception", e);
            return HomePage.buildAtFailed("服务器异常");
        }
    }
}
