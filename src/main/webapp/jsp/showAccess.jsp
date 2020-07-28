<%@ page import="com.ethereum.bean.AccessLog" %>
<%@ page import="org.apache.commons.collections.CollectionUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.ListIterator" %>
<%@ page language="java" pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String accessQueryDir = rootDir + "/access/dataQueryFilter";
    String accessUpdateDir = rootDir + "/access/queryById";
    String accessDeleteDir = rootDir + "/access/dataDelete";
    String accessInsertDir = rootDir + "/jsp/insertAccess.jsp";

    ArrayList<AccessLog> accessLogList = null;
    if (session != null) {
        accessLogList = (ArrayList<AccessLog>) session.getAttribute("accessLogList");
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <style type="text/css">
        #showTable {
            line-height: 30px;
            text-align: center;
            /*font-family: "楷体";*/
            font-size: 12px;
            width: 100%;
            table-layout: fixed;
        }

        #showTable tr td {
            /* for IE */
            text-overflow: ellipsis;
            /* for Firefox,mozilla */
            -moz-text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
            text-align: center;
        }

        table th {
            background-color: #6C4C39;
            color: #ECE5DD;
            font-family: "楷体";
            font-size: 15px;
        }

        form {
            background-color: #ECE5DD;
            width: 100%;
            height: 20%;
        }

        #queryFormTable {
            line-height: 30px;
            width: 100%;
            height: 80%;
            text-align: left;
            font-family: "楷体";
            font-size: 15px;
        }

        #queryFormButton {
            line-height: 30px;
            width: 30%;
            height: 20%;
            text-align: left;
            font-family: "楷体";
            font-size: 15px;
        }

        .add a {
            font-family: "楷体";
            display: block;
            font-size: 1em;
            vertical-align: middle;
            text-align: center;
            text-decoration: none;
            padding: 6px 6px;
            margin-left: 1%;
            width: 120px;

            border-radius: 20px;
            box-shadow: inset 0px 0px 1px 1px rgba(0, 0, 0, 0.1);
            background-color: #DDD1C5;
            color: #6C4C39;
        }

        .add a:hover {
            border-radius: 20px;
            box-shadow: inset 0px 0px 1px 1px rgba(0, 0, 0, 0.1);
        }
    </style>

    <script>
        // 为每一行设置不同的背景色
        function upColor() {
            var table = document.getElementById("showTable");
            for (var i = 0; i < table.rows.length; i++) {
                if (i % 2 != 0) {
                    table.rows[i].style.background = '#ffffff';
                } else {
                    table.rows[i].style.background = '#ECE5DD';
                }
            }
        }

        function clearForm() {
            document.getElementById("query_id").value = null;
            document.getElementById("query_subject_name").value = null;
            document.getElementById("query_operate_type").value = "all";
            document.getElementById("query_access_allow").value = "all";
            document.getElementById("query_fail_reason").value = "-1";
        }

        function confirmDel(param) {
            if (window.confirm("确定要删除这条日志吗？")) {
                document.location = "<%=accessDeleteDir %>?id=" + param
            }
        }

        function dateUpdate(param) {
            document.location = "<%=accessUpdateDir %>?id=" + param
        }
    </script>
</head>

<body onload="upColor()">

<!-- 筛选框 -->
<form action=<%=accessQueryDir %> method="post">
    <table id="queryFormTable" border="0">
        <tr>
            <td class="tableLeft">ID</td>
            <td class="tableRight"><input id="query_id" name="queryId" value=${queryId}></td>

            <td class="tableLeft">请求主体</td>
            <td class="tableRight"><input id="query_subject_name" name="querySubjectName" value=${querySubjectName}>
            </td>

            <td class="tableLeft">请求类型</td>
            <td class="tableRight">
                <select id="query_operate_type" name="queryOperateType">
                    <option value=${queryOperateType} selected>${queryOperateTypeName}</option>
                    <option value="read">read</option>
                    <option value="control">control</option>
                    <option value="all">不限</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">访问状态</td>
            <td class="tableRight">
                <select id="query_access_allow" name="queryAccessAllow">
                    <option value=${queryAccessAllow} selected>${queryAccessAllowName}</option>
                    <option value="true">允许</option>
                    <option value="false">拒绝</option>
                    <option value="all">不限</option>
                </select>
            </td>
            <td class="tableLeft">失败原因</td>
            <td class="tableRight">
                <select id="query_fail_reason" name="queryFailReason">
                    <option value=${queryFailReason} selected>${queryFailReasonName}</option>
                    <option value="1">访问频率过高</option>
                    <option value="2">身份认证不通过</option>
                    <option value="3">访问权限不通过</option>
                    <option value="4">设备惩罚中</option>
                    <option value="5">未失败</option>
                    <option value="-1">不限</option>
                </select>
            </td>
            <td class="tableRight">
                <input type="button" onclick=clearForm() value="清空条件">
            </td>
            <td class="tableRight">
                <input type="submit" name="submit" id="submitButton" value="查 询">
            </td>
        </tr>

    </table>
    <table id="queryFormButton">
        <tr>
            <td>
                <button type="submit" name="pageIndex" value="1">首页</button>
            </td>
            <td>
                <button type="submit" name="pageIndex" value=${currentPageNum-1}>上一页</button>
            </td>
            <td>
                <i>${currentPageNum} / ${totalPageNum}</i>
            </td>
            <td>
                <button type="submit" name="pageIndex" value=${currentPageNum+1}>下一页</button>
            </td>
            <td>
                <button type="submit" name="pageIndex" value=${totalPageNum}>尾页</button>
            </td>
            <td>
                <i>总条数:${totalNumSize}</i>
            </td>
        </tr>
    </table>
</form>

<table border=0 id="showTable">
    <tr>
        <th>ID</th>
        <th>请求主体</th>
        <th>请求客体</th>
        <th>请求操作</th>
        <th>请求状态</th>
        <th>失败原因</th>
        <th>操作数据</th>
        <th>结果数据</th>
        <th>请求时间</th>
        <th>响应时间</th>
        <th>操作</th>
    </tr>
    <%
        if (CollectionUtils.isNotEmpty(accessLogList)) {
            ListIterator<AccessLog> lit = accessLogList.listIterator();
            AccessLog data = null;
            while (lit.hasNext()) {
                data = lit.next();
    %>
    <tr>
        <%-- id --%>
        <td><%=data.getId() %>
        </td>
        <%-- 请求主体 --%>
        <td><%=data.getSubjectName() %>
        </td>
        <%-- 请求客体 --%>
        <td><%=data.getObjectName() %>
        </td>
        <%-- 请求操作 --%>
        <td><%=data.getOperateType() %>
        </td>
        <%-- 请求状态 --%>
        <td><%=data.getAccessAllowName() %>
        </td>
        <%-- 失败原因 --%>
        <td><%=data.getFailReasonName() %>
        </td>
        <%-- 操作数据 --%>
        <td><%=data.getOperateData() %>
        </td>
        <%-- 结果数据 --%>
        <td><%=data.getResultData() %>
        </td>
        <%-- 请求时间 --%>
        <td><%=data.getRequestTimeStr() %>
        </td>
        <%-- 响应时间 --%>
        <td><%=data.getResponseTimeStr() %>
        </td>
        <%-- 操作 --%>
        <td>
            <%--<button onclick="confirmDel(<%=data.getId() %>)">删除</button>--%>
            <button onclick="dateUpdate(<%=data.getId() %>)">详情</button>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>

<%--<hr/>--%>
<%--<div class="add"><a href="<%=accessInsertDir %>" target="right">添加日志</a></div>--%>
</body>
</html>
