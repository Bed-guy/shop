<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page isELIgnored="false" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>订单管理</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2 style="text-align: center;">订单管理</h2>
    <button type="button" class="btn btn-secondary mb-3" onclick="window.location.href='dashboard'">
        返回
    </button>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <table class="table">
        <thead>
        <tr>
            <th>订单ID</th>
            <th>用户ID</th>
            <th>总价</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="order" items="${orders}">
            <tr>
                <td>${order.id}</td>
                <td>${order.userId}</td>
                <td>${order.totalPrice}</td>
                <td>${order.status}</td>
                <td>
                    <!-- 查看详细 -->
                    <button
                            class="btn btn-info view-details-btn"
                            data-order-id="${order.id}"
                            data-bs-toggle="modal"
                            data-bs-target="#orderDetailsModal">
                        查看详细
                    </button>

                    <!-- 修改状态按钮 -->
                    <c:choose>
                        <c:when test="${order.status != '已发货'}">
                            <a href="changeStatus?id=${order.id}" class="btn btn-primary">发货</a>
                        </c:when>
                    </c:choose>

                    <!-- 删除按钮 -->
                    <a href="deleteOrder?id=${order.id}" class="btn btn-danger" onclick="return confirm('确认删除此订单？');">删除</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- 订单详情模态框 -->
<div class="modal fade" id="orderDetailsModal" tabindex="-1" aria-labelledby="orderDetailsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><span id="modalOrderId">订单ID</span> 详情</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="关闭"></button>
            </div>
            <div class="modal-body">
                <table class="table" id="orderDetailsTable">
                    <thead>
                    <tr>
                        <th>商品ID</th>
                        <th>商品名称</th>
                        <th>描述</th>
                        <th>价格</th>
                    </tr>
                    </thead>
                    <tbody>
                    <!-- 动态填充订单详情 -->
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- 使用Fetch API进行AJAX请求 -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const viewButtons = document.querySelectorAll('.view-details-btn');
        viewButtons.forEach(button => {
            button.addEventListener('click', function() {
                const orderId = this.getAttribute('data-order-id');

                fetch(`getOrderDetails?orderId=` + orderId)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        // 更新模态框标题
                        document.getElementById('modalOrderId').textContent = `订单 #${orderId}`;

                        // 获取模态框的表格tbody
                        const tbody = document.querySelector('#orderDetailsTable tbody');
                        tbody.innerHTML = ''; // 清空之前的内容

                        if (data.length === 0) {
                            tbody.innerHTML = '<tr><td colspan="4" class="text-center">无订单详情</td></tr>';
                            return;
                        }

                        // 遍历订单详情并填充表格
                        data.forEach(item => {
                            const tr = document.createElement('tr');

                            const tdProductId = document.createElement('td');
                            tdProductId.textContent = item.productId;
                            tr.appendChild(tdProductId);

                            const tdProductName = document.createElement('td');
                            tdProductName.textContent = item.productName;
                            tr.appendChild(tdProductName);

                            const tdDescription = document.createElement('td');
                            tdDescription.textContent = item.description;
                            tr.appendChild(tdDescription);

                            const tdPrice = document.createElement('td');
                            tdPrice.textContent = item.price;
                            tr.appendChild(tdPrice);

                            tbody.appendChild(tr);
                        });
                    })
                    .catch(error => {
                        console.error('Error fetching order details:', error);
                        alert('获取订单详情时出错');
                    });
            });
        });
    });
</script>
</body>
</html>




