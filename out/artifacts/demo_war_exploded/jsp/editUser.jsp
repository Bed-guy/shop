<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.shop.model.User" %>
<%
    // 从请求中获取User对象
    User user = (User) request.getAttribute("user");
    // 获取错误消息（如果有）
    String errorMessage = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title><%= (user != null) ? "编辑用户" : "添加用户" %></title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f9f9f9;
        }
        .container {
            max-width: 600px;
            margin: 50px auto;
            padding: 25px;
            background: #ffffff;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 25px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            font-weight: bold;
            display: block;
            margin-bottom: 8px;
            color: #555;
        }
        input[type="text"], input[type="email"], input[type="password"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
        }
        .button-group {
            text-align: center;
            margin-top: 30px;
        }
        .button-group input[type="submit"], .button-group input[type="button"] {
            padding: 10px 25px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            margin: 0 10px;
        }
        .button-group input[type="submit"] {
            background-color: #ffc107;
            color: #fff;
        }
        .button-group input[type="submit"]:hover {
            background-color: #e0a800;
        }
        .button-group input[type="button"] {
            background-color: #6c757d;
            color: #fff;
        }
        .button-group input[type="button"]:hover {
            background-color: #5a6268;
        }
        .error-message {
            color: red;
            text-align: center;
            margin-top: 15px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1><%= (user != null) ? "编辑用户" : "添加用户" %></h1>
    <% if (errorMessage != null && !errorMessage.trim().isEmpty()) { %>
    <p class="error-message"><%= errorMessage %></p>
    <% } %>
    <form action="<%= (user != null) ? "editUser" : "addUser" %>" method="post">
        <% if (user != null) { %>
        <!-- 隐藏字段，用于传递用户ID -->
        <input type="hidden" name="id" value="<%= user.getId() %>">
        <% } %>
        <div class="form-group">
            <label for="username">用户名：</label>
            <input type="text" id="username" name="username"
                   value="<%= (user != null) ? user.getUsername() : "" %>"
                   required>
        </div>
        <div class="form-group">
            <label for="email">邮箱：</label>
            <input type="email" id="email" name="email"
                   value="<%= (user != null) ? user.getEmail() : "" %>"
                   required>
        </div>
        <div class="form-group">
            <label for="role">角色：</label>
            <input type="text" id="role" name="role"
                   value="<%= (user != null) ? user.getRole() : "" %>"
                   required>
        </div>
        <div class="button-group">
            <input type="submit" value="<%= (user != null) ? "保存修改" : "添加用户" %>">
            <input type="button" value="取消" onclick="window.location.href='users';">
        </div>
    </form>
</div>
</body>
</html>

