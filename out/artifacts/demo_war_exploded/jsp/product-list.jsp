<%@ page import="java.util.List" %>
<%@ page import="com.example.shop.model.Product" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>商品列表</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 900px;
            margin: 50px auto;
            padding: 20px;
            background-color: white;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        h1 {
            text-align: center;
            color: #333;
            font-size: 24px;
            margin-bottom: 20px;
        }

        .action-buttons {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
        }

        .action-buttons a {
            padding: 10px 15px;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            text-align: center;
            font-size: 16px;
        }

        .add-button {
            background-color: #007BFF;
        }

        .add-button:hover {
            background-color: #0056b3;
        }

        .return-button {
            background-color: #6c757d;
        }

        .return-button:hover {
            background-color: #5a6268;
        }

        .search-container {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }

        .search-container input[type="text"] {
            flex: 1;
            padding: 8px;
            font-size: 16px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        .search-container button {
            padding: 8px 20px;
            font-size: 16px;
            cursor: pointer;
            background-color: #007BFF;
            color: white;
            border: none;
            border-radius: 5px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ddd;
        }

        th {
            background-color: #007BFF;
            color: white;
            font-weight: bold;
        }

        .button {
            padding: 5px 10px;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin-right: 5px;
        }

        .edit-button {
            background-color: #28a745;
        }

        .edit-button:hover {
            background-color: #218838;
        }

        .delete-button {
            background-color: #dc3545;
        }

        .delete-button:hover {
            background-color: #b02a37;
        }

        /* 弹窗样式 */
        .popup {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            padding: 20px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            z-index: 1000;
        }

        .popup .message {
            font-size: 16px;
            margin-bottom: 20px;
        }

        .popup button {
            padding: 8px 16px;
            background-color: #007BFF;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .popup button:hover {
            background-color: #0056b3;
        }

        /* 背景遮罩 */
        .popup-overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0, 0, 0, 0.5);
            z-index: 999;
        }
    </style>
</head>
<body>

<div class="container">
    <!-- 操作按钮 -->
    <div class="action-buttons">
        <a href="dashboard" class="return-button">返回</a>
    </div>
    <h1>商品列表</h1>

    <!-- 搜索表单 -->
    <form action="searchProduct-user" method="get" class="search-container">
        <input type="text" name="keyword" id="searchKeyword" value="${param.keyword}" placeholder="请输入产品名称进行搜索">
        <button type="submit">搜索</button>
    </form>

    <!-- 产品列表 -->
    <table>
        <thead>
        <tr>
            <th>名称</th>
            <th>描述</th>
            <th>价格</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%-- 如果有产品列表，展示数据 --%>
        <%
            List<Product> productList = (List<Product>) request.getAttribute("products");
            if (productList == null || productList.isEmpty()) {
        %>
        <tr>
            <td colspan="4" style="text-align: center;">没有匹配的产品</td>
        </tr>
        <% } else {
            for (Product product : productList) {
        %>
        <tr>
            <td><%= product.getName() %></td>
            <td><%= product.getDescription() %></td>
            <td>￥<%= product.getPrice() %></td>
            <td>
                <button class="add-to-cart" data-product-id="<%= product.getId() %>">加入购物车</button>
            </td>
        </tr>
        <% } } %>
        </tbody>
    </table>
</div>

<!-- 弹窗部分 -->
<div class="popup-overlay" id="popup-overlay"></div>
<div class="popup" id="popup">
    <div class="message" id="popup-message"></div>
    <button onclick="closePopup()">关闭</button>
</div>

<script>
    // 处理加入购物车点击事件
    $(document).ready(function() {
        $('.add-to-cart').on('click', function() {
            var productId = $(this).data('product-id');
            $.ajax({
                url: 'addToCart',
                type: 'POST',
                data: { productId: productId },
                success: function(response) {
                    var message = response.message;
                    $('#popup-message').text(message);
                    $('#popup').show();
                    $('#popup-overlay').show();
                },
                error: function() {
                    $('#popup-message').text("发生错误，请重试！");
                    $('#popup').show();
                    $('#popup-overlay').show();
                }
            });
        });

        // 关闭弹窗
        function closePopup() {
            $('#popup').hide();
            $('#popup-overlay').hide();
        }

        // 关闭弹窗
        window.closePopup = closePopup;
    });
</script>

</body>
</html>


