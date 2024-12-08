<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>用户操作日志</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2 class="text-center">用户操作日志</h2>

    <button type="button" class="btn btn-secondary mb-3" onclick="window.location.href='dashboard'">
        返回
    </button>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>ID</th>
            <th>用户ID</th>
            <th>操作时间</th>
            <th>操作类型</th>
            <th>操作详情</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="log" items="${logs}">
            <tr>
                <td>${log.id}</td>
                <td>${log.userId}</td>
                <td>${log.actionTime}</td>
                <td>${log.actionType}</td>
                <td>${log.details}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
