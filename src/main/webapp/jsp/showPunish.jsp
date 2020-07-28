<%@ page import="com.ethereum.bean.PunishInfo" %>
<%@ page import="org.apache.commons.collections.CollectionUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.ListIterator" %>
<%@ page language="java" pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String punishQueryDir = rootDir + "/punish/dataQueryFilter";
    String punishUpdateDir = rootDir + "/punish/queryById";
    String punishDeleteDir = rootDir + "/punish/dataDelete";
    String punishStatusDir = rootDir + "/punish/dataDisable";
    String punishInsertDir = rootDir + "/jsp/insertPunish.jsp";

    ArrayList<PunishInfo> punishInfoList = null;
    if (session != null) {
        punishInfoList = (ArrayList<PunishInfo>) session.getAttribute("punishInfoList");
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
            document.getElementById("query_device_name").value = null;
            document.getElementById("query_rule_name").value = null;
            document.getElementById("query_rule_type").value = "-1";
            document.getElementById("query_punish").value = "-1";
            document.getElementById("query_status").value = "-1";
        }

        function confirmDel(param) {
            if (window.confirm("确定要删除设备的惩罚信息吗？")) {
                document.location = "<%=punishDeleteDir %>?id=" + param
            }
        }

        function confirmDisable(param) {
            if (window.confirm("确定要解除设备的惩罚状态吗？")) {
                document.location = "<%=punishStatusDir %>?id=" + param
            }
        }

        function dateUpdate(param) {
            document.location = "<%=punishUpdateDir %>?id=" + param
        }
    </script>
</head>

<body onload="upColor()">

<!-- 筛选框 -->
<form action=<%=punishQueryDir %> method="post">
    <table id="queryFormTable" border="0">
        <tr>
            <td class="tableLeft">ID</td>
            <td class="tableRight"><input id="query_id" name="queryId" value=${queryId}></td>

            <td class="tableLeft">惩罚设备</td>
            <td class="tableRight"><input id="query_device_name" name="queryDeviceName" value=${queryDeviceName}></td>

            <td class="tableLeft">违反规则</td>
            <td class="tableRight"><input id="query_rule_name" name="queryRuleName" value=${queryRuleName}></td>

            <td class="tableLeft">规则类型</td>
            <td class="tableRight">
                <select id="query_rule_type" name="queryRuleType">
                    <option value=${queryRuleType} selected>${queryRuleTypeName}</option>
                    <option value="1">访问频率限制</option>
                    <option value="2">身份认证限制</option>
                    <option value="3">越权访问限制</option>
                    <option value="0">其他</option>
                    <option value="-1">不限</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">惩罚类型</td>
            <td class="tableRight">
                <select id="query_punish" name="queryPunish">
                    <option value=${queryPunish} selected>${queryPunishName}</option>
                    <option value="1">隔离</option>
                    <option value="2">驱逐</option>
                    <option value="-1">不限</option>
                </select>
            </td>
            <td class="tableLeft">惩罚状态</td>
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
        <th>惩罚设备</th>
        <th>违反规则</th>
        <th>规则类型</th>
        <th>惩罚</th>
        <th>惩罚开始</th>
        <th>惩罚结束</th>
        <th>惩罚状态</th>
        <th>操作</th>
    </tr>
    <%
        if (CollectionUtils.isNotEmpty(punishInfoList)) {
            ListIterator<PunishInfo> lit = punishInfoList.listIterator();
            PunishInfo data = null;
            while (lit.hasNext()) {
                data = lit.next();
    %>
    <tr>
        <%-- id --%>
        <td><%=data.getId() %>
        </td>
        <%-- 惩罚设备 --%>
        <td><%=data.getDeviceName() %>
        </td>
        <%-- 违反规则 --%>
        <td><%=data.getRuleName() %>
        </td>
        <%-- 规则类型 --%>
        <td><%=data.getRuleTypeName() %>
        </td>
        <%-- 惩罚 --%>
        <td><%=data.getPunishName() %>
        </td>
        <%-- 惩罚开始 --%>
        <td><%=data.getPunishStartStr() %>
        </td>
        <%-- 惩罚结束 --%>
        <td><%=data.getPunishEndStr() %>
        </td>
        <%-- 惩罚状态 --%>
        <td><%=data.getStatusName() %>
        </td>
        <%-- 操作 --%>
        <td>
            <button onclick="confirmDisable(<%=data.getId() %>)">解除</button>
            <button onclick="confirmDel(<%=data.getId() %>)">删除</button>
            <%--            <button onclick="dateUpdate(<%=data.getId() %>)">修改</button>--%>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>

<%--<hr/>--%>
<%--<div class="add"><a href="<%=punishInsertDir %>" target="right">添加惩罚</a></div>--%>
</body>
</html>
