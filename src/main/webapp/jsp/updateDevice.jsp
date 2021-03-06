<%@ page import="com.ethereum.bean.DeviceInfo" %>
<%@ page language="java"
         pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String deviceQueryDir = rootDir + "/device/dataQueryFilter";
    String deviceUpdateDir = rootDir + "/device/dataUpdate";

    // 要更新的数据
    DeviceInfo updateInfo = null;
    if (session != null) {
        updateInfo = (DeviceInfo) session.getAttribute("updateDeviceInfo");
    }
    if (updateInfo == null) {
        updateInfo = new DeviceInfo();
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
                    deviceType: {
                        required: true,
                    },
                    attributePosition: {
                        required: true,
                    },
                    attributeSystem: {
                        required: true,
                    },
                    attributeType: {
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
                    deviceType: {
                        required: "<font color=red>设备类型不能为空</font>",
                    },
                    attributePosition: {
                        required: "<font color=red>请选择位置属性</font>",
                    },
                    attributeSystem: {
                        required: "<font color=red>请选择系统属性</font>",
                    },
                    attributeType: {
                        required: "<font color=red>请选择类型属性</font>",
                    }
                }
            });
        });
    </script>
</head>

<body>
<div class="title">修改设备信息</div>
<form id="dataForm" action=<%=deviceUpdateDir %> method="post">
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
                <input name="deviceMac" value=<%=updateInfo.getDeviceMac() %>>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">设备名称</td>
            <td class="tableRight">
                <input name="deviceName" value=<%=updateInfo.getDeviceName() %>>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">代理Uid</td>
            <td class="tableRight">
                <input name="agentDevice" value=<%=updateInfo.getAgentDevice() %>>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">类型属性</td>
            <td class="tableRight">
                <select name="attributeType" %>
                    <option value="<%=updateInfo.getAttributeType() %>"
                            selected><%=updateInfo.getAttributeTypeName() %>
                    </option>
                    <option value="displayer">显示设备</option>
                    <option value="controller">控制设备</option>
                    <option value="terminal">终端设备</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">位置属性</td>
            <td class="tableRight">
                <select name="attributePosition" %>
                    <option value="<%=updateInfo.getAttributePosition() %>"
                            selected><%=updateInfo.getAttributePositionName() %>
                    </option>
                    <option value="drawingRoom">客厅</option>
                    <option value="bedRoom">主卧</option>
                    <option value="secondaryBedRoom">次卧</option>
                    <option value="studyRoom">书房</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">系统属性</td>
            <td class="tableRight">
                <select name="attributeSystem" %>
                    <option value="<%=updateInfo.getAttributeSystem() %>"
                            selected><%=updateInfo.getAttributeSystemName() %>
                    </option>
                    <option value="lighting">灯光系统</option>
                    <option value="temperature">温度系统</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">设备类型</td>
            <td class="tableRight">
                <select name="deviceType" %>
                    <option value="<%=updateInfo.getDeviceType() %>"
                            selected><%=updateInfo.getDeviceTypeName() %>
                    </option>
                    <option value="wifi">wifi设备</option>
                    <option value="bluetooth">蓝牙设备</option>
                    <option value="other">其他</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">生产厂商</td>
            <td class="tableRight">
                <input name="manufacture" VALUE="<%=updateInfo.getManufacture() %>">
            </td>
        </tr>
        <tr>
            <td class="tableLeft">设备状态</td>
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
                <a href=<%=deviceQueryDir %>><input type="button" name="back" id="backButton" value="返 回"></a>
            </td>
            <td align="center">
                <input type="submit" name="submit" id="submitButton" value="保 存">
            </td>
        </tr>
    </table>
</form>
</body>
</html>
