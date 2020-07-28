<%@ page language="java" pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String mainTopDir = rootDir + "/jsp/top.jsp";
    String mainLeftDir = rootDir + "/jsp/menu.jsp";
    String mainRightDir = rootDir + "/device/dataQueryFilter";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>基于区块链的物联网访问控制后台</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>

<!-- 嵌套划分 -->
<frameset rows="14%,*" frameborder="yes" border="2">    <!-- *号代表剩余 -->
    <frame name="top" src=<%=mainTopDir %>>
    <!-- 嵌套划分 -->
    <frameset cols="14%,*">
        <frame name="left" src=<%=mainLeftDir %>>
        <frame name="right" src=<%=mainRightDir %>>
    </frameset>
</frameset>

<!-- 如果浏览器不支持框架结构，则出现如下提示 -->
<noframes>
    <body>
    <span>您的浏览器不支持框架结构，推荐使用谷歌浏览器！</span>
    </body>
</noframes>
</html>
