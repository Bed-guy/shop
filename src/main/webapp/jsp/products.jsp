<%@ page import="com.example.shop.model.Product" %>
<%@ page import="java.util.List" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>产品管理</title>
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
    </style>
</head>
<body>

<div class="container">
    <!-- 操作按钮 -->
    <div class="action-buttons">
        <a href="dashboard" class="return-button">返回</a>
        <a href="addProduct" class="add-button">添加新产品</a>
    </div>

    <h1>产品管理</h1>

    <!-- 搜索表单 -->
    <form action="searchProduct" method="get" class="search-container">
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
                <a href="editProduct?id=<%= product.getId() %>" class="button edit-button">编辑</a>
                <a href="deleteProduct?id=<%= product.getId() %>" class="button delete-button"
                   onclick="return confirm('确认删除此产品？');">删除</a>
            </td>
        </tr>
        <% } } %>
        </tbody>
    </table>
</div>

</body>
</html>




