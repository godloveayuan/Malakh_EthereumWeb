<%@ page language="java"
         pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String contractQueryDir = rootDir + "/contract/dataQuery";
    String contractInsertDir = rootDir + "/contract/dataInsert";
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
                    owner: {
                        required: true,
                    },
                    address: {
                        required: true,
                    }
                },
                messages: {
                    name: {
                        required: "<font color=red>合约名称不能为空</font>",
                    },
                    type: {
                        required: "<font color=red>请选择合约类型</font>",
                    },
                    owner: {
                        required: "<font color=red>合约拥有者不能为空</font>",
                    },
                    address: {
                        required: "<font color=red>合约地址不能为空</font>",
                    }
                }
            });
        });
    </script>
</head>

<body>
<div class="title">添加合约</div>
<form id="dataForm" action=<%=contractInsertDir %> method="post">
    <table border="0">
        <tr>
            <td class="tableLeft">合约名称</td>
            <td class="tableRight">
                <input name="name">
            </td>
        </tr>
        <tr>
            <td class="tableLeft">合约类型</td>
            <td class="tableRight">
                <select name="type" %>
                    <option value="">--请选择--</option>
                    <option value="private">专属合约</option>
                    <option value="public">公共合约</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="tableLeft">拥有者Uid</td>
            <td class="tableRight">
                <input name="owner">
            </td>
        </tr>
        <tr>
            <td class="tableLeft">合约地址</td>
            <td class="tableRight">
                <input name="address">
            </td>
        </tr>
        <tr>
            <td class="tableLeft">是否可用</td>
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
                <a href=<%=contractQueryDir %>><input type="button" name="back" id="backButton" value="返 回"></a>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
