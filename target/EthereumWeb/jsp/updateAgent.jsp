<%@ page import="com.ethereum.bean.DeviceInfo" %>
<%@ page import="com.ethereum.bean.AgentInfo" %>
<%@ page language="java"
         pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String agentQueryDir = rootDir + "/agent/dataQuery";
    String agentUpdateDir = rootDir + "/agent/dataUpdate";

    // 要更新的数据
    AgentInfo updateInfo = null;
    if (session != null) {
        updateInfo = (AgentInfo) session.getAttribute("updateAgentInfo");
    }
    if (updateInfo == null) {
        updateInfo = new AgentInfo();
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <style type="text/css">
        body {
            margin-top: 1%;
        }

        div {
            background-color: #6C4C39;
            color: #ECE5DD;
            font-size: 20px;
            width: 80%;
            margin-left: 10%;
            text-align: center;
        }

        table {
            background-color: #ECE5DD;
            line-height: 45px;
            width: 80%;
            margin-left: 10%;
        }

        .tableLeft {
            width: 40%;
            padding-left: 22%;
        }

        .tableRight {
            text-align: left;
        }

        input {
            height: 35px;
            color: #111111;
            width: 60%;
        }

        #submitButton {
            margin-left: 0%;
            height: 30px;
            width: 100px;
        }

        #backButton {
            margin-left: 36%;
            height: 30px;
            width: 100px;
        }
    </style>

    <!-- 导入jquery插件 -->
    <script type="text/javascript" src="<%=rootDir %>/jquery/jquery-1.4.2.js"></script>
    <!-- 导入表单校验插件 -->
    <script type="text/javascript" src="<%=rootDir %>/jquery/jquery.validate.js"></script>

    <!-- jquery语句进行表单校验 -->
    <script type="text/javascript">
        $(document).ready(function () {
            // ***** 表单验证 *****
            $("#dataForm").validate({
                rules: {
                    deviceName: {
                        required: true,
                    },
                    deviceMac: {
                        required: true,
                    },
                    address: {
                        required: true,
                    }
                },
                messages: {
                    deviceName: {
                        required: "<font color=red>设备名称不能为空</font>",
                    },
                    deviceMac: {
                        required: "<font color=red>出厂标识不能为空</font>",
                    },
                    address: {
                        required: "<font color=red>请填写网络地址</font>",
                    }
                }
            });
        });
    </script>
</head>

<body>
<div class="title">修改代理设备信息</div>
<form id="dataForm" action=<%=agentUpdateDir %> method="post">
    <table border="0">
        <tr>
            <td class="tableLeft">ID</td>
            <td class="tableRight">
                <input type="hidden" name="id" value=<%=updateInfo.getId() %>>
                <%=updateInfo.getId() %>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">设备标识</td>
            <td class="tableRight">
                <input type="hidden" name="deviceUid" value=<%=updateInfo.getDeviceUid() %>>
                <%=updateInfo.getDeviceUid() %>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">出厂标识</td>
            <td class="tableRight">
                <input type="hidden" name="deviceMac" value=<%=updateInfo.getDeviceMac() %>>
                <%=updateInfo.getDeviceMac() %>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">设备名称</td>
            <td class="tableRight">
                <input name="deviceName" value=<%=updateInfo.getDeviceName() %>>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">网络地址</td>
            <td class="tableRight">
                <input name="address" value=<%=updateInfo.getAddress() %>>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">生产厂商</td>
            <td class="tableRight">
                <input name="manufacture" VALUE="<%=updateInfo.getManufacture() %>">
            </td>
        </tr>
        <tr>
            <td class="tableLeft">是否可用</td>
            <td class="tableRight">
                <select name="status" %>
                    <option value="<%=updateInfo.getStatus() %>" selected><%=updateInfo.getStatusName() %>
                    </option>
                    <option value="1">可用</option>
                    <option value="0">不可用</option>
                </select>
            </td>
        </tr>

        <tr>
            <td align="center">
                <a href=<%=agentQueryDir %>><input type="button" name="back" id="backButton" value="返 回"></a>
            </td>
            <td align="center">
                <input type="submit" name="submit" id="submitButton" value="保 存">
            </td>
        </tr>
    </table>
</form>
</body>
</html>
