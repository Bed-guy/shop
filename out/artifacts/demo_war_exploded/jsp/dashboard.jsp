<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page isELIgnored="false" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>控制台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .navbar {
            margin-bottom: 30px;
        }
        .dashboard-section {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">控制台</a>
        <div class="d-flex">
            <a class="btn btn-outline-light" href="${pageContext.request.contextPath}/logout" role="button">退出</a>
        </div>
    </div>
</nav>

<div class="container mt-5">
    <div class="dashboard-section">
        <h2>欢迎, ${fn:escapeXml(sessionScope.user.username)}!</h2>

        <c:choose>
            <c:when test="${role == 'ADMIN'}">
                <div class="mt-4">
                    <h3>管理员功能</h3>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/users">用户管理</a></li>
                        <li><a href="${pageContext.request.contextPath}/products">商品管理</a></li>
                        <li><a href="${pageContext.request.contextPath}/orders">订单管理</a></li>
                        <li><a href="${pageContext.request.contextPath}/salesChart">销售统计</a></li>
                        <li><a href="${pageContext.request.contextPath}/userLogs">用户操作日志</a></li>
                    </ul>
                </div>
            </c:when>
            <c:otherwise>
                <div class="mt-4">
                    <h3>用户功能</h3>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/product-list">商品列表</a></li>
                        <li><a href="${pageContext.request.contextPath}/cart">购物车</a></li>
                        <li><a href="${pageContext.request.contextPath}/orders-list">我的订单</a></li>
                    </ul>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
