<%@ page import="java.util.List" %>
<%@ page import="com.example.shop.model.Product" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>购物车</title>
    <!-- 引入 jQuery -->
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
            box-shadow: 0 0 10px rgba(0,0,0,0.3);
            border-radius: 8px;
            z-index: 1001;
            text-align: center;
        }

        .popup button {
            margin-top: 20px;
            padding: 8px 16px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .popup button:hover {
            background-color: #218838;
        }

        .popup-overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            z-index: 1000;
        }
    </style>
</head>
<body>

<div class="container">
    <!-- 操作按钮 -->
    <div class="action-buttons">
        <a href="dashboard" class="return-button">返回</a>
    </div>
    <h1>购物车列表</h1>

    <!-- 产品列表 -->
    <form id="paymentForm" action="${pageContext.request.contextPath}/pay" method="POST">
        <table>
            <thead>
            <tr>
                <th>选择</th>
                <th>名称</th>
                <th>描述</th>
                <th>价格</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <%-- 如果有产品列表，展示数据 --%>
            <%
                List<Product> productList = (List<Product>) request.getAttribute("cartItems");
                if (productList == null || productList.isEmpty()) {
            %>
            <tr>
                <td colspan="5" style="text-align: center;">购物车为空</td>
            </tr>
            <% } else {
                for (Product product : productList) {
            %>
            <tr id="product-row-<%= product.getId() %>">
                <td><input type="checkbox" name="selectedProductIds" value="<%= product.getId() %>"></td>
                <td><%= product.getName() %></td>
                <td><%= product.getDescription() %></td>
                <td>￥<%= product.getPrice() %></td>
                <td>
                    <a href="${pageContext.request.contextPath}/removeFromCart?productId=<%= product.getId() %>" class="delete-button">删除</a>
                </td>
            </tr>
            <% } } %>
            </tbody>
        </table>

        <!-- 付款按钮 -->
        <div style="text-align: center;">
            <button type="submit" class="add-button">付款</button>
        </div>
    </form>

    <!-- 弹窗 -->
    <div class="popup-overlay"></div>
    <div class="popup">
        <div class="message"></div>
        <button id="closePopup">确定</button>
    </div>
</div>

<script>
    $(document).ready(function() {
        $('#paymentForm').submit(function(event) {
            event.preventDefault(); // 阻止默认表单提交

            var selectedCheckboxes = $('input[name="selectedProductIds"]:checked');
            var selectedItems = selectedCheckboxes.length;
            if (selectedItems === 0) {
                alert("请选择至少一件商品进行付款！");
                return;
            }

            // 使用AJAX提交表单
            $.ajax({
                url: $(this).attr('action'),
                type: 'POST',
                data: $(this).serialize(),
                dataType: 'json',
                success: function(response) {
                    if (response.success) {
                        // 遍历已选中的复选框，移除对应的行
                        selectedCheckboxes.each(function() {
                            var productId = $(this).val();
                            $('#product-row-' + productId).remove();
                        });

                        // 检查表格是否还有商品
                        if ($('tbody tr').length === 0) {
                            $('tbody').html('<tr><td colspan="5" style="text-align: center;">购物车为空</td></tr>');
                        }

                        // 显示弹窗
                        $('.popup .message').text(response.message);
                        $('.popup-overlay').fadeIn();
                        $('.popup').fadeIn();
                    } else {
                        alert(response.message);
                    }
                },
                error: function() {
                    alert("支付过程中发生错误，请稍后再试。");
                }
            });
        });

        // 关闭弹窗
        $('#closePopup').click(function() {
            $('.popup').fadeOut();
            $('.popup-overlay').fadeOut();
        });
    });
</script>

</body>
</html>



