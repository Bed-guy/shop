<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.shop.model.Product" %>
<%
    // 从请求中获取Product对象
    Product product = (Product) request.getAttribute("product");
    // 获取错误消息（如果有）
    String errorMessage = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title><%= (product != null) ? "编辑产品" : "添加产品" %></title>
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
        input[type="text"], textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
        }
        textarea {
            resize: vertical;
            height: 100px;
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
    <script>
        // 客户端验证价格是否为非负数且格式正确
        function validatePrice() {
            var price = document.getElementById("price").value.trim();
            var pricePattern = /^\d+(\.\d{1,2})?$/; // 允许整数或最多两位小数
            if (!pricePattern.test(price)) {
                alert("请输入有效的价格（非负数，可以包含最多两位小数）。");
                return false; // 阻止表单提交
            }
            return true; // 允许提交表单
        }
    </script>
</head>
<body>
<div class="container">
    <h1><%= (product != null) ? "编辑产品" : "添加产品" %></h1>
    <% if (errorMessage != null && !errorMessage.trim().isEmpty()) { %>
    <p class="error-message"><%= errorMessage %></p>
    <% } %>
    <form action="<%= (product != null) ? "editProduct" : "addProduct" %>" method="post" onsubmit="return validatePrice();">
        <% if (product != null) { %>
        <!-- 隐藏字段，用于传递产品ID -->
        <input type="hidden" name="id" value="<%= product.getId() %>">
        <% } %>
        <div class="form-group">
            <label for="name">产品名称：</label>
            <input type="text" id="name" name="name"
                   value="<%= (product != null) ? product.getName() : "" %>"
                   required>
        </div>
        <div class="form-group">
            <label for="description">描述：</label>
            <textarea id="description" name="description"><%= (product != null && product.getDescription() != null) ? product.getDescription() : "" %></textarea>
        </div>
        <div class="form-group">
            <label for="price">价格：</label>
            <input type="text" id="price" name="price"
                   value="<%= (product != null) ? String.format("%.2f", product.getPrice()) : "" %>"
                   required>
        </div>
        <div class="button-group">
            <input type="submit" value="<%= (product != null) ? "保存修改" : "添加产品" %>">
            <input type="button" value="取消" onclick="window.location.href='products';">
        </div>
    </form>
</div>
</body>
</html>

