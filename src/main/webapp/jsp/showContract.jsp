<%@ page import="com.ethereum.bean.ContractInfo" %>
<%@ page import="org.apache.commons.collections.CollectionUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.ListIterator" %>
<%@ page language="java" pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String contractQueryDir = rootDir + "/contract/dataQueryFilter";
    String contractUpdateDir = rootDir + "/contract/queryById";
    String contractDeleteDir = rootDir + "/contract/dataDelete";
    String contractInsertDir = rootDir + "/jsp/insertContract.jsp";

    ArrayList<ContractInfo> contractInfoList = null;
    if (session != null) {
        contractInfoList = (ArrayList<ContractInfo>) session.getAttribute("contractInfoList");
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
            document.getElementById("query_owner").value = null;
            document.getElementById("query_address").value = null;
            document.getElementById("query_type").value = "all";
            document.getElementById("query_status").value = "-1";
        }

        function confirmDel(param) {
            if (window.confirm("确定要删除这条合约吗？")) {
                document.location = "<%=contractDeleteDir %>?id=" + param
            }
        }

        function dateUpdate(param) {
            document.location = "<%=contractUpdateDir %>?id=" + param
        }
    </script>
</head>

<body onload="upColor()">

<!-- 筛选框 -->
<form action=<%=contractQueryDir %> method="post">
    <table id="queryFormTable" border="0">
        <tr>
            <td class="tableLeft">ID</td>
            <td class="tableRight"><input id="query_id" name="queryId" value=${queryId}></td>

            <td class="tableLeft">合约名称</td>
            <td class="tableRight"><input id="query_name" name="queryName" value=${queryName}></td>

            <td class="tableLeft">合约地址</td>
            <td class="tableRight"><input id="query_address" name="queryAddress" value=${queryAddress}></td>

            <td class="tableLeft">拥有者</td>
            <td class="tableRight"><input id="query_owner" name="queryOwnerName" value=${queryOwnerName}></td>
        </tr>
        <tr>
            <td class="tableLeft">合约类型</td>
            <td class="tableRight">
                <select id="query_type" name="queryType">
                    <option value=${queryType} selected>${queryTypeName}</option>
                    <option value="public">公共合约</option>
                    <option value="private">专属合约</option>
                    <option value="all">不限</option>
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
        <th>合约名称</th>
        <th>合约类型</th>
        <th>拥有者</th>
        <th>合约地址</th>
        <th>合约状态</th>
        <th>操作</th>
    </tr>
    <%
        if (CollectionUtils.isNotEmpty(contractInfoList)) {
            ListIterator<ContractInfo> lit = contractInfoList.listIterator();
            ContractInfo data = null;
            while (lit.hasNext()) {
                data = lit.next();
    %>
    <tr>
        <%-- id --%>
        <td><%=data.getId() %>
        </td>
        <%-- 合约名称 --%>
        <td><%=data.getName() %>
        </td>
        <%-- 合约类型 --%>
        <td><%=data.getTypeName() %>
        </td>
        <%-- 拥有者 --%>
        <td><%=data.getOwnerName() %>
        </td>
        <%-- 合约地址 --%>
        <td><%=data.getAddress() %>
        </td>
        <%-- 合约状态 --%>
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

<%--<hr/>--%>
<%--<div class="add"><a href="<%=contractInsertDir %>" target="right">添加合约</a></div>--%>
</body>
</html>
