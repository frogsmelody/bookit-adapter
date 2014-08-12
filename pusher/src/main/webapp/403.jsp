<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="UTF-8">
<head>
    <title>Choice-Endpoint - 抱歉，您没有权限访问</title>
    <%
        String logoutUrl = request.getContextPath() + "/j_spring_security_logout";
    %>
    <style type="text/css">
        div.msg.error {
            background-color: #FFEEB2;
            border: 1px solid #E4494A;
            min-height: 50px;
        }

        div.msg.error h2 {
            float: left;
            margin-left: -2px;
            margin-top: -2px;
            text-indent: -9999px;
            width: 54px;
            height: 51px;
        }

        div.msg.error p {
            font-size: 14px;
            line-height: 50px;
        }

        div.msg.error p a {
            margin: 0 0.1em;
        }

        div.msg {
            margin: 50px auto;
        }

    </style>
</head>
<body id="error403Page" class="error">
<div id="container">
    <div class="http403 error msg">
        <h2>403</h2>

        <p>您没有权限访问次功能,请先获得访问此功能的权限,然后 <a href="<%= logoutUrl %>" title="重新登录">重新登录</a></p>
    </div>
</div>
</body>
</html>