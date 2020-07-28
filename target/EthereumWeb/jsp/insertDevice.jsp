<%@ page language="java"
         pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String deviceQueryDir = rootDir + "/device/dataQueryFilter";
    String deviceInsertDir = rootDir + "/device/dataInsert";
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
<div class="title">注册设备</div>
<form id="dataForm" action=<%=deviceInsertDir %> method="post">
    <table border="0">
        <tr>
            <td class="tableLeft">设备名称</td>
            <td class="tableRight">
                <input name="deviceName">
            </td>
        </tr>
        <tr>
            <td class="tableLeft">出厂标识</td>
            <td class="tableRight">
                <input name="deviceMac">
            </td>
        </tr>
        <tr>
            <td class="tableLeft">生产厂商</td>
            <td class="tableRight">
                <input name="manufacture">
            </td>
        </tr>
        <tr>
            <td class="tableLeft">代理Uid</td>
            <td class="tableRight">
                <input name="agentDevice">
            </td>
        </tr>
        <tr>
            <td class="tableLeft">设备类型</td>
            <td class="tableRight">
                <select name="deviceType" %>
                    <option value="">--请选择--</option>
                    <option value="wifi">wifi设备</option>
                    <option value="bluetooth">蓝牙设备</option>
                    <option value="other">其他</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">位置属性</td>
            <td class="tableRight">
                <select name="attributePosition" %>
                    <option value="">--请选择--</option>
                    <option value="drawingRoom">客厅</option>
                    <option value="bedRoom">卧室</option>
                    <option value="secondaryBedRoom">次卧</option>
                    <option value="studyRoom">书房</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">系统属性</td>
            <td class="tableRight">
                <select name="attributeSystem" %>
                    <option value="">--请选择--</option>
                    <option value="lighting">灯光系统</option>
                    <option value="temperature">温度系统</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">类型属性</td>
            <td class="tableRight">
                <select name="attributeType" %>
                    <option value="">--请选择--</option>
                    <option value="displayer">显示设备</option>
                    <option value="controller">控制设备</option>
                    <option value="terminal">终端设备</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">设备状态</td>
            <td class="tableRight">
                <select name="status" %>
                    <option value="1" selected>可用</option>
                    <option value="0">不可用</option>
                </select>
            </td>
        </tr>

        <tr>
            <td class="tableLeft">
                <input type="submit" name="submit" id="submitButton" value="注 册">
            </td>
            <td>
                <a href=<%=deviceQueryDir %>><input type="button" name="back" id="backButton" value="返 回"></a>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
