<%@ page language="java" pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String logOutDir = rootDir + "/user/loginOut";

    // 要更新的数据
    String loginUser = "";
    if (session != null) {
        loginUser = (String) session.getAttribute("loginUser");
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>主页的标题栏</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <style type="text/css">
        body {
            background-repeat: no-repeat;
            background-color: #ECE5DD;
            background-attachment: fixed;
        }

        .title {
            font-family: "华文新魏", "楷体";
            font-size: 2.0em;
            color: #500B10;
            margin-left: 40%;
            margin-top: 1%;
        }

        table {
            width: 10%;
            height: 80%;
            font-family: "楷体";
            font-size: 1.0em;
            color: #500B10;
            text-align: center;
            layout: fixed;
            position: absolute;
            top: 20%;
            left: 90%;
        }

        /* 设置菜单按钮的文本属性 */
        a {
            font-family: "楷体";
            display: block;
            color: #500B10;
            padding: 6px 6px;
            margin: 8px 8px;
            vertical-align: middle;
            text-decoration: none;
        }

        /* 设置链接按钮的鼠标悬浮属性  */
        a:hover {
            background-image: linear-gradient(#ededed, #fff);
            border-radius: 20px;
            box-shadow: inset 0px 0px 1px 1px rgba(0, 0, 0, 0.1);
            color: #6C4C39;
        }
    </style>

</head>

<body>
<div class="title"><span>基于区块链的物联网访问控制后台</span></div>
<table border='0'>
    <tr>
        <td>
            欢迎回来：<%=loginUser %>
        </td>
    </tr>
    <tr>
        <td>
            <a href=<%=logOutDir %>>退出登录</a>
        </td>
    </tr>
</table>
</body>
</html>
