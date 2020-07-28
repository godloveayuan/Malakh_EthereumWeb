<%@ page import="com.ethereum.bean.SecurityRule" %>
<%@ page import="org.apache.commons.collections.CollectionUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.ListIterator" %>
<%@ page language="java" pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String securityRuleQueryDir = rootDir + "/securityRule/dataQueryFilter";
    String securityRuleUpdateDir = rootDir + "/securityRule/queryById";
    String securityRuleDeleteDir = rootDir + "/securityRule/dataDelete";
    String securityRuleInsertDir = rootDir + "/jsp/insertSecurityRule.jsp";

    ArrayList<SecurityRule> securityRuleList = null;
    if (session != null) {
        securityRuleList = (ArrayList<SecurityRule>) session.getAttribute("securityRuleList");
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
            document.getElementById("query_kick_out").value = "all";
            document.getElementById("query_type").value = "-1";
            document.getElementById("query_status").value = "-1";
        }

        function confirmDel(param) {
            if (window.confirm("确定要删除这条规则吗？")) {
                document.location = "<%=securityRuleDeleteDir %>?id=" + param
            }
        }

        function dateUpdate(param) {
            document.location = "<%=securityRuleUpdateDir %>?id=" + param
        }
    </script>
</head>

<body onload="upColor()">

<!-- 筛选框 -->
<form action=<%=securityRuleQueryDir %> method="post">
    <table id="queryFormTable" border="0">
        <tr>
            <td class="tableLeft">ID</td>
            <td class="tableRight"><input id="query_id" name="queryId" value=${queryId}></td>

            <td class="tableLeft">规则名称</td>
            <td class="tableRight"><input id="query_name" name="queryName" value=${queryName}></td>

            <td class="tableLeft">惩罚类型</td>
            <td class="tableRight">
                <select id="query_kick_out" name="queryKickOut">
                    <option value=${queryKickOut} selected>${queryKickName}</option>
                    <option value="true">驱逐</option>
                    <option value="false">隔离</option>
                    <option value="all">不限</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">规则类型</td>
            <td class="tableRight">
                <select id="query_type" name="queryType">
                    <option value=${queryType} selected>${queryTypeName}</option>
                    <option value="1">访问频率限制</option>
                    <option value="2">身份认证限制</option>
                    <option value="3">越权访问限制</option>
                    <option value="0">其他</option>
                    <option value="-1">全部</option>
                </select>
            </td>
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
        <th>规则名称</th>
        <th>规则类型</th>
        <th>允许次数</th>
        <th>监测时间</th>
        <th>惩罚措施</th>
        <th>惩罚时间</th>
        <th>规则状态</th>
        <th>操作</th>
    </tr>
    <%
        if (CollectionUtils.isNotEmpty(securityRuleList)) {
            ListIterator<SecurityRule> lit = securityRuleList.listIterator();
            SecurityRule data = null;
            while (lit.hasNext()) {
                data = lit.next();
    %>
    <tr>
        <%-- id --%>
        <td><%=data.getId() %>
        </td>
        <%-- 规则名称 --%>
        <td><%=data.getName() %>
        </td>
        <%-- 规则类型 --%>
        <td><%=data.getTypeName() %>
        </td>
        <%-- 监测次数 --%>
        <td><%=data.getCheckCount() %>
        </td>
        <%-- 监测时间 --%>
        <td><%=data.getCheckTimeNumber() %> <%=data.getCheckTimeUnitName() %>
        </td>
        <%-- 惩罚措施 --%>
        <td><%=data.getKickName() %>
        </td>
        <%-- 惩罚时间 --%>
        <td><%=data.getPunishNumber() %> <%=data.getPunishUnitName() %>
        </td>
        <%-- 规则状态 --%>
        <td><%=data.getStatusName() %>
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
<div class="add"><a href="<%=securityRuleInsertDir %>" target="right">添加规则</a></div>
</body>
</html>
