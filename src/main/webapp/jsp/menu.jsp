<%@ page language="java" pageEncoding="UTF-8" %>

<%
    // 设置页面路径
    String rootDir = request.getContextPath();
    String deviceQueryDir = rootDir + "/device/dataQueryFilter";
    String agentQueryDir = rootDir + "/agent/dataQueryFilter";
    String contractQueryDir = rootDir + "/contract/dataQueryFilter";
    String securityRuleQueryDir = rootDir + "/securityRule/dataQueryFilter";
    String punishQueryDir = rootDir + "/punish/dataQueryFilter";
    String accessQueryDir = rootDir + "/access/dataQueryFilter";
    String contractInsertDir = rootDir + "/jsp/insertContract.jsp";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>主页的菜单栏</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <style type="text/css">
        body {
            background-color: #ECE5DD;
            background-attachment: fixed;
            margin-top: 5%;
        }

        /* 取消掉列表的索引  */
        ul, li {
            list-style: none;
            width: 85%;
        }

        /* 设置菜单按钮的文本属性 */
        .menu li a, span {
            font-family: "楷体";
            display: block;
            color: #500B10;
            padding: 6px 6px;
            margin: 8px 8px;
            vertical-align: middle;
            text-decoration: none;
        }

        /* 设置父菜单 */
        .menu span {
            margin-left: 10%;
            font-size: 1.2em;
            text-align: center;
            margin-left: 5%;
            background-image: linear-gradient(#ededed, #fff);
            border-radius: 20px;
            box-shadow: inset 0px 0px 1px 1px rgba(0, 0, 0, 0.1);
            color: #6C4C39;
        }

        /* 设置子菜单 */
        .menu li {
            font-size: 1.0em;
            margin-left: 15%;
            text-align: center;
        }

        /* 设置菜单按钮的鼠标悬浮属性  */
        .menu a:hover, span:hover {
            background-image: linear-gradient(#ededed, #fff);
            border-radius: 20px;
            box-shadow: inset 0px 0px 1px 1px rgba(0, 0, 0, 0.1);
            color: #6C4C39;
        }
    </style>

    <script type="text/javascript" src="<%=rootDir %>/jquery/jquery-1.4.2.js"></script>
    <script type="text/javascript">

        window.onload = function () {
            // 点击父菜单展开或收起子菜单
            document.getElementById("deviceMenu").onclick = function () {
                var display = document.getElementById("deviceMenuList").style.display;
                if (display == "none") {
                    // 切换展开与收起的符号
                    document.getElementById("deviceMenuMark").innerHTML = "-";
                    document.getElementById("deviceMenuList").style.display = "block";
                } else {
                    // 切换展开与收起的符号
                    document.getElementById("deviceMenuMark").innerHTML = "+";
                    document.getElementById("deviceMenuList").style.display = "none";
                }
            };
            // 点击父菜单展开或收起子菜单
            document.getElementById("contractMenu").onclick = function () {
                var display = document.getElementById("contractMenuList").style.display;
                if (display == "none") {
                    // 切换展开与收起的符号
                    document.getElementById("contractMenuMark").innerHTML = "-";
                    document.getElementById("contractMenuList").style.display = "block";
                } else {
                    // 切换展开与收起的符号
                    document.getElementById("contractMenuMark").innerHTML = "+";
                    document.getElementById("contractMenuList").style.display = "none";
                }
            };
            // 点击父菜单展开或收起子菜单
            document.getElementById("securityMenu").onclick = function () {
                var display = document.getElementById("securityMenuList").style.display;
                if (display == "none") {
                    // 切换展开与收起的符号
                    document.getElementById("securityMenuMark").innerHTML = "-";
                    document.getElementById("securityMenuList").style.display = "block";
                } else {
                    // 切换展开与收起的符号
                    document.getElementById("securityMenuMark").innerHTML = "+";
                    document.getElementById("securityMenuList").style.display = "none";
                }
            };
        };

    </script>
</head>

<body>
<div class="menu">
    <div id="deviceMenu">
		<span>
        	<text id="deviceMenuMark">-</text>&nbsp;设备管理
		</span>
    </div>
    <div>
        <ul id="deviceMenuList">
            <li><a href=<%=deviceQueryDir %> target="right">设备信息</a></li>
            <li><a href=<%=agentQueryDir %> target="right">代理信息</a></li>
        </ul>

    </div>
    <div id="contractMenu">
		<span>
        	<text id="contractMenuMark">-</text>&nbsp;合约管理
		</span>
    </div>
    <ul id="contractMenuList">
        <li><a href=<%=contractQueryDir %> target="right">全部合约</a></li>
        <li><a href=<%=contractInsertDir %> target="right">添加合约</a></li>
    </ul>
    <div id="securityMenu">
		<span>
        	<text id="securityMenuMark">-</text>&nbsp;安全机制
		</span>
    </div>
    <ul id="securityMenuList">
        <li><a href=<%=securityRuleQueryDir %> target="right">安全规则</a></li>
        <li><a href=<%=accessQueryDir %> target="right">访问日志</a></li>
        <li><a href=<%=punishQueryDir %> target="right">惩罚设备</a></li>
    </ul>
</div>
</body>
</html>
