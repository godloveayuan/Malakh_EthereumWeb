<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page language="java" pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String loginCheckDir = rootDir + "/user/login";

    // 是否登录失败
    boolean loginFailed = false;
    if (session != null) {
        String loginCheckStr = (String) session.getAttribute("loginCheck");
        if (StringUtils.isNotEmpty(loginCheckStr) && StringUtils.equalsIgnoreCase("false", loginCheckStr)) {
            loginFailed = true;
        }
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>基于区块链的物联网访问控制后台</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <style type="text/css">
        body {
            font-family: "楷体";
            background-repeat: no-repeat;
            background-size: 95% 90%;
        }

        #title {
            font-family: "华文新魏";
            color: #65493B;
            font-size: 2.5em;
            margin-top: 5%;
            margin-left: 30%;
            width: 40%;
            text-align: center;
        }

        #hr {
            width: 40%;
            margin-left: 30%;
        }

        table {
            width: 40%;
            height: 35%;
            table-layout: fixed;
            margin-left: 30%;
            background-color: #ECE5DD;
        }

        .table_left {
            width: 35%;
            text-align: center;
        }

        .table_right {
            text-align: left;
        }

        .innertext {
            color: #65493B;
            font-size: 1.2em;
        }

        .button {
            width: 100px;
            height: 30px;
        }

        #submit {
            margin-left: 10%;
        }

        #reset {
            margin-left: 30%;
        }

        .text {
            width: 60%;
            height: 40%;
        }

        .failMessage {
            display: block;
            color: red;
            text-align: center;
        }


    </style>

    <!-- 导入jquery插件 -->
    <script type="text/javascript" src="<%=rootDir %>/jquery/jquery-1.4.2.js"></script>
    <!-- 导入表单校验插件 -->
    <script type="text/javascript" src="<%=rootDir %>/jquery/jquery.validate.js"></script>

    <!-- jquery语句进行表单校验 -->
    <script type="text/javascript">
        $(document).ready(function () {
            //***** 自定义校验规则 *****
            // 1. 用户名和密码的格式是：4-20位大小写英文字母或数字组成
            $.validator.addMethod("cinValueType", function (cinValue, obj, rules) {
                // 第一个参数是输入的数据,即输入的用户名
                // 第二个参数是作用的元素对象，
                // 第三个参数是校验规则传输的参数，true
                if (rules) {
                    // 正则规则
                    var reg = /^[0-9a-zA-Z]{4,20}$/;
                    return reg.test(cinValue.trim());
                }
                // 参数rules如果是false,则不校验
                else {
                    return true;
                }
            });

            // ***** 表单验证 *****
            $("#login_form").validate({
                rules: {
                    userName: {
                        required: true,
                        cinValueType: true
                    },
                    userPwd: {
                        required: true,
                        cinValueType: true
                    }

                },
                messages: {
                    userName: {
                        required: "<font color=red>请输入管理员帐号</font>",
                        cinValueType: "<br/><font color=red>帐号由4-20位字母或数字组成</font>"
                    },
                    userPwd: {
                        required: "<font color=red>请输入登录密码</font>",
                        cinValueType: "<br/><font color=red>密码由4-20位字母或数字组成</font>"
                    }
                }
            });
        });

    </script>

</head>

<body>
<div id="title">基于区块链的物联网访问控制后台</div>
<div id="hr">
    <hr size="3px" color="#65493B"/>
</div>

<form id="login_form" method="post" action=<%=loginCheckDir%>>
    <table id="login_table" border="0">
        <tr>
            <td class="table_left"><span class="innertext">帐  号</span></td>
            <td class="table_right"><input type="text" name="userName" class="text"/></td>
        </tr>

        <tr>
            <td class="table_left"><span class="innertext">密  码</span></td>
            <td class="table_right"><input type="password" name="userPwd" class="text"/></td>
        </tr>
        <%
            if (loginFailed) {
        %>
        <tr>
            <td colspan="2">
                <div class="failMessage">您输入的用户名或密码有误，请重新输入！</div>
            </td>
        </tr>
        <%
            }
        %>
        <tr>
            <td class="table_left"><input type="submit" name="submit" id="submit" class="button" value="登  录"/></td>
            <td class="table_right"><input type="reset" name="reset" id="reset" class="button" value="重新输入"/></td>
        </tr>
    </table>
</form>
</body>
</html>
