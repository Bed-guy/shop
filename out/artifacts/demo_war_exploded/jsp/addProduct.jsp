<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>添加产品</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            background-color: white;
            box-shadow: 0px 0px 10px rgba(0,0,0,0.1);
            border-radius: 8px;
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #333;
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .button {
            width: 100%;
            padding: 10px;
            font-size: 16px;
            color: white;
            background-color: #007BFF;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .button:hover {
            background-color: #0056b3;
        }
        .error-message {
            color: red;
            margin-top: 10px;
            text-align: center;
        }
        .back-link {
            display: block;
            text-align: center;
            margin-top: 15px;
            color: #007BFF;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
        }
    </style>
    <script>
        // 客户端验证价格是否为非负数
        function validatePrice() {
            var price = document.getElementById("price").value.trim();
            var pricePattern = /^\d+(\.\d{1,2})?$/; // 允许整数或两位小数
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
    <h1>添加新产品</h1>
    <%
        // 获取错误消息（如果有）
        String errorMessage = (String) request.getAttribute("error");
    %>
    <% if (errorMessage != null && !errorMessage.trim().isEmpty()) { %>
    <p class="error-message"><%= errorMessage %></p>
    <% } %>
    <form action="addProduct" method="post" onsubmit="return validatePrice()">
        <div class="form-group">
            <label for="name">产品名称：</label>
            <input type="text" id="name" name="name"
                   value="<%= request.getParameter("name") != null ? request.getParameter("name") : "" %>"
                   required>
        </div>
        <div class="form-group">
            <label for="description">描述：</label>
            <textarea id="description" name="description" rows="4"><%= request.getParameter("description") != null ? request.getParameter("description") : "" %></textarea>
        </div>
        <div class="form-group">
            <label for="price">价格：</label>
            <input type="text" id="price" name="price"
                   value="<%= request.getParameter("price") != null ? request.getParameter("price") : "" %>"
                   required>
        </div>
        <button type="submit" class="button">添加产品</button>
    </form>
    <a href="products" class="back-link">返回产品列表</a>
</div>
</body>
</html>


