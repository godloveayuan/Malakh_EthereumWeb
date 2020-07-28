<%@ page language="java"
         pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String securityRuleQueryDir = rootDir + "/securityRule/dataQuery";
    String securityRuleInsertDir = rootDir + "/securityRule/dataInsert";
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
                    name: {
                        required: true,
                    },
                    type: {
                        required: true,
                    },
                    checkCount: {
                        required: true,
                    },
                    checkTimeUnit: {
                        required: true,
                    },
                    punishUnit: {
                        required: true,
                    },
                    kickOut: {
                        required: true,
                    }
                },
                messages: {
                    name: {
                        required: "<font color=red>规则名称不能为空</font>",
                    },
                    type: {
                        required: "<font color=red>请选择规则类型</font>",
                    },
                    checkCount: {
                        required: "<font color=red>允许次数不能为空</font>",
                    },
                    checkTimeUnit: {
                        required: "<font color=red>请选择时间单位</font>",
                    },
                    punishUnit: {
                        required: "<font color=red>请选择时间单位</font>",
                    },
                    kickOut: {
                        required: "<font color=red>请选择惩罚类型</font>",
                    }
                }
            });
        });
    </script>
</head>

<body>
<div class="title">添加规则</div>
<form id="dataForm" action=<%=securityRuleInsertDir %> method="post">
    <table border="0">
        <tr>
            <td class="tableLeft">规则名称</td>
            <td class="tableRight">
                <input name="name">
            </td>
        </tr>
        <tr>
            <td class="tableLeft">规则类型</td>
            <td class="tableRight">
                <select name="type" %>
                    </option>
                    <option value="">--请选择--</option>
                    <option value="1">访问频率限制</option>
                    <option value="2">身份认证限制</option>
                    <option value="3">越权访问限制</option>
                    <option value="0">其他</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">允许次数</td>
            <td class="tableRight">
                <input name="checkCount">次
            </td>
        </tr>
        <tr>
            <td class="tableLeft">监测时间</td>
            <td class="tableRight">
                <input with="30%" name="checkTimeNumber">
                <select name="checkTimeUnit" %>
                    <option value="">--请选择时间单位--</option>
                    <option value="second">秒</option>
                    <option value="minute">分钟</option>
                    <option value="hour">小时</option>
                    <option value="day">天</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">惩罚类型</td>
            <td class="tableRight">
                <select name="kickOut" %>
                    <option value="">--请选择--</option>
                    <option value="false">隔离</option>
                    <option value="true">驱逐</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">惩罚时间</td>
            <td class="tableRight">
                <input with="30%" name="punishNumber">
                <select name="punishUnit" %>
                    <option value="">--请选择时间单位--</option>
                    <option value="second">秒</option>
                    <option value="minute">分钟</option>
                    <option value="hour">小时</option>
                    <option value="day">天</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">规则状态</td>
            <td class="tableRight">
                <select name="status" %>
                    <option value="1" selected>可用</option>
                    <option value="0">不可用</option>
                </select>
            </td>
        </tr>

        <tr>
            <td class="tableLeft">
                <input type="submit" name="submit" id="submitButton" value="添 加">
            </td>
            <td>
                <a href=<%=securityRuleQueryDir %>><input type="button" name="back" id="backButton" value="返 回"></a>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
