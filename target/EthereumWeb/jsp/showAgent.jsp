<%@ page import="com.ethereum.bean.AgentInfo" %>
<%@ page import="org.apache.commons.collections.CollectionUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.ListIterator" %>
<%@ page language="java" pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String agentQueryDir = rootDir + "/agent/dataQueryFilter";
    String agentUpdateDir = rootDir + "/agent/queryById";
    String agentDeleteDir = rootDir + "/agent/dataDelete";
    String agentInsertDir = rootDir + "/jsp/insertAgent.jsp";

    ArrayList<AgentInfo> agentInfoList = null;
    if (session != null) {
        agentInfoList = (ArrayList<AgentInfo>) session.getAttribute("agentInfoList");
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
            document.getElementById("query_name").value = null;
            document.getElementById("query_mac").value = null;
            document.getElementById("query_uid").value = null;
            document.getElementById("query_status").value = "-1";
        }

        function confirmDel(param) {
            if (window.confirm("确定要删除这条设备吗？")) {
                document.location = "<%=agentDeleteDir %>?id=" + param
            }
        }

        function dateUpdate(param) {
            document.location = "<%=agentUpdateDir %>?id=" + param
        }
    </script>
</head>

<body onload="upColor()">
<!-- 筛选框 -->
<form action=<%=agentQueryDir %> method="post">
    <table id="queryFormTable" border="0">
        <tr>
            <td class="tableLeft">ID</td>
            <td class="tableRight"><input id="query_id" name="queryId" value=${queryId}></td>

            <td class="tableLeft">设备名称</td>
            <td class="tableRight"><input id="query_name" name="queryDeviceName" value=${queryDeviceName}></td>

            <td class="tableLeft">出厂标识</td>
            <td class="tableRight"><input id="query_mac" name="queryDeviceMac" value=${queryDeviceMac}></td>

            <td class="tableLeft">注册标识</td>
            <td class="tableRight"><input id="query_uid" name="queryDeviceUid" value=${queryDeviceUid}></td>
        </tr>
        <tr>
            <td class="tableLeft">可用状态</td>
            <td class="tableRight">
                <select id="query_status" name="queryStatus">
                    <option value=${queryStatus} selected>${queryStatusName}</option>
                    <option value="1">可用</option>
                    <option value="0">不可用</option>
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
        <th>设备名称</th>
        <th>出厂标识</th>
        <th>注册标识</th>
        <th>网络地址</th>
        <th>注册时间</th>
        <th>操作</th>
    </tr>
    <%
        if (CollectionUtils.isNotEmpty(agentInfoList)) {
            ListIterator<AgentInfo> lit = agentInfoList.listIterator();
            AgentInfo data = null;
            while (lit.hasNext()) {
                data = lit.next();
    %>
    <tr>
        <%-- id --%>
        <td><%=data.getId() %>
        </td>
        <%-- 设备名称 --%>
        <td><%=data.getDeviceName() %>
        </td>
        <%-- 出厂标识 --%>
        <td><%=data.getDeviceMac() %>
        </td>
        <%-- 注册标识 --%>
        <td><%=data.getDeviceUid() %>
        </td>
        <%-- 设备地址 --%>
        <td><%=data.getAddress() %>
        </td>
        <%-- 注册时间 --%>
        <td><%=data.getCreateTimeStr() %>
        </td>
        <%-- 操作 --%>
        <td>
            <button onclick="confirmDel(<%=data.getId() %>)">删除</button>
            <button onclick="dateUpdate(<%=data.getId() %>)">修改</button>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>

<hr/>
<div class="add"><a href="<%=agentInsertDir %>" target="right">注册新代理</a></div>
</body>
</html>
