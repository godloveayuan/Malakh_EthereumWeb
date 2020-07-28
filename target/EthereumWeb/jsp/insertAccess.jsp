<%@ page language="java"
         pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String accessQueryDir = rootDir + "/access/dataQueryFilter";
    String accessInsertDir = rootDir + "/access/dataInsert";
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
                    subjectUid: {
                        required: true,
                    },
                    objectUid: {
                        required: true,
                    },
                    operateType: {
                        required: true,
                    },
                    accessAllow: {
                        required: true,
                    },
                    failReason: {
                        required: true,
                    }
                },
                messages: {
                    subjectUid: {
                        required: "<font color=red>请求主体不能为空</font>",
                    },
                    objectUid: {
                        required: "<font color=red>请求客体不能为空</font>",
                    },
                    operateType: {
                        required: "<font color=red>请选择操作类型</font>",
                    },
                    accessAllow: {
                        required: "<font color=red>请选择访问状态</font>",
                    },
                    failReason: {
                        required: "<font color=red>请选择失败原因</font>",
                    }
                }
            });
        });
    </script>
</head>

<body>
<div class="title">添加访问日志</div>
<form id="dataForm" action=<%=accessInsertDir %> method="post">
    <table border="0">
        <tr>
            <td class="tableLeft">请求主体Uid</td>
            <td class="tableRight">
                <input name="subjectUid">
            </td>
        </tr>
        <tr>
            <td class="tableLeft">请求客体Uid</td>
            <td class="tableRight">
                <input name="objectUid">
            </td>
        </tr>
        <tr>
            <td class="tableLeft">请求操作</td>
            <td class="tableRight">
                <select name="operateType" %>
                    <option value="">--请选择--</option>
                    <option value="read">read</option>
                    <option value="control">control</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">操作数据</td>
            <td class="tableRight">
                <input name="operateData">
            </td>
        </tr>
        <tr>
            <td class="tableLeft">访问状态</td>
            <td class="tableRight">
                <select name="accessAllow" %>
                    <option value="">--请选择--</option>
                    <option value="true">允许</option>
                    <option value="false">拒绝</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">失败原因</td>
            <td class="tableRight">
                <select name="failReason" %>
                    <option value="">--请选择--</option>
                    <option value="1">访问频率过高</option>
                    <option value="2">身份认证不通过</option>
                    <option value="3">访问权限不通过</option>
                    <option value="4">设备惩罚中</option>
                    <option value="5">未失败</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">请求时间</td>
            <td class="tableRight">
                <input name="requestTimeStr">
            </td>
        </tr>
        <tr>
            <td class="tableLeft">响应时间</td>
            <td class="tableRight">
                <input name="responseTimeStr">
            </td>
        </tr>

        <tr>
            <td class="tableLeft">
                <input type="submit" name="submit" id="submitButton" value="添 加">
            </td>
            <td>
                <a href=<%=accessQueryDir %>><input type="button" name="back" id="backButton" value="返 回"></a>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
