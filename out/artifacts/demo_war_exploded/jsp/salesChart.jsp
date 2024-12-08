<%@ page import="java.util.List" %>
<%@ page import="com.example.shop.dao.SalesDao.ProductSales" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8"/>
    <title>商品销售折线图</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.8.0/chart.min.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background: #f5f5f5;
        }

        h1 {
            text-align: center;
        }

        .chart-container {
            width: 80%;
            margin: 0 auto;
        }

    </style>
</head>
<body>
<h1>商品销售情况</h1>
<div class="chart-container">
    <div class="action-buttons">
        <a href="dashboard" class="return-button">返回</a>
    </div>
    <canvas id="salesChart"></canvas>
</div>

<%
    List<ProductSales> salesData = (List<ProductSales>) request.getAttribute("salesData");
    // 准备商品名称和销量数据
    StringBuilder productNames = new StringBuilder();
    StringBuilder productSales = new StringBuilder();

    productNames.append("[");
    productSales.append("[");

    if (salesData != null && !salesData.isEmpty()) {
        for (int i = 0; i < salesData.size(); i++) {
            ProductSales ps = salesData.get(i);
            productNames.append("\"").append(ps.getProductName()).append("\"");
            productSales.append(ps.getTotalSales());

            if (i < salesData.size() - 1) {
                productNames.append(",");
                productSales.append(",");
            }
        }
    }

    productNames.append("]");
    productSales.append("]");
%>

<script>
    const ctx = document.getElementById('salesChart').getContext('2d');

    const productNames = <%= productNames.toString() %>;
    const productSales = <%= productSales.toString() %>;

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: productNames,
            datasets: [{
                label: '销量',
                data: productSales,
                borderColor: 'rgba(75, 192, 192, 1)',
                fill: false,
                tension: 0.1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: '商品销量折线图'
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: '商品名称'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: '销售数量'
                    },
                    beginAtZero: true
                }
            }
        }
    });
</script>
</body>
</html>

