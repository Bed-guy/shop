package com.example.shop.dao;

import com.example.shop.model.Product;
import com.example.shop.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesDao {

    public static class ProductSales {
        private String productName;
        private int totalSales;

        public ProductSales(String productName, int totalSales) {
            this.productName = productName;
            this.totalSales = totalSales;
        }

        public String getProductName() {
            return productName;
        }

        public int getTotalSales() {
            return totalSales;
        }
    }

    /**
     * 获取各商品的总销量
     * 思路：从order_items表中根据product_id分组统计COUNT(*)即为销量
     * 然后根据product_id关联products表获取product_name
     */
    public List<ProductSales> getProductSales() {
        String sql = "SELECT p.name, COUNT(o.id) as total_sales " +
                "FROM order_items o " +
                "JOIN products p ON o.product_id = p.id " +
                "GROUP BY p.id, p.name " +
                "ORDER BY total_sales DESC";

        List<ProductSales> result = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                int total = rs.getInt("total_sales");
                result.add(new ProductSales(name, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}

