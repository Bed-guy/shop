<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8"/>
    <title>在线购物系统 - 用户注册</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background: linear-gradient(to bottom right, #e6f3f5, #ffffff);
            font-family: 'Microsoft Yahei', sans-serif;
            margin: 0;
            padding: 0;
        }

        .header-title {
            text-align: center;
            margin-top: 50px;
            font-size: 32px;
            font-weight: bold;
            color: #333;
        }

        .form-container {
            max-width: 400px;
            margin: 60px auto;
            background: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }

        .form-container h2 {
            margin-bottom: 20px;
            font-weight: bold;
            color: #333;
            text-align: center;
        }

        .form-container .form-label {
            font-weight: bold;
        }

        .form-container .btn-primary {
            width: 100%;
            padding: 10px;
            font-size: 16px;
        }

        .form-container p {
            margin-top: 15px;
            text-align: center;
        }

        .form-container p a {
            text-decoration: none;
            color: #007BFF;
        }

        .form-container p a:hover {
            color: #0056b3;
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="header-title">
    在线购物系统
</div>

<div class="form-container">
    <h2>用户注册</h2>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">
                ${fn:escapeXml(error)}
        </div>
    </c:if>
    <form action="${pageContext.request.contextPath}/register" method="post">
        <div class="mb-3">
            <label class="form-label">用户名:</label>
            <input type="text" name="username" class="form-control" required/>
        </div>
        <div class="mb-3">
            <label class="form-label">密码:</label>
            <input type="password" name="password" class="form-control" required/>
        </div>
        <div class="mb-3">
            <label class="form-label">邮箱:</label>
            <input type="email" name="email" class="form-control" required/>
        </div>
        <button type="submit" class="btn btn-primary">注册</button>
    </form>
    <p class="mt-3">已有账号？ <a href="${pageContext.request.contextPath}/login">登录</a></p>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
