package com.example.shop.dao;

import com.example.shop.model.Product;
import com.example.shop.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    // 获取所有产品
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()){
                Product product = extractProductFromResultSet(rs);
                products.add(product);
            }
            System.out.println("获取所有产品成功，数量: " + products.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // 根据ID获取产品
    public Product getProductById(int id){
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                Product product = extractProductFromResultSet(rs);
                // 可选：打印信息
                System.out.println("获取产品成功，ID: " + id);
                return product;
            } else {
                // 可选：打印警告
                System.out.println("未找到产品，ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 根据关键字搜索产品
    public List<Product> searchProducts(String keyword){
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Product product = extractProductFromResultSet(rs);
                products.add(product);
            }
            // 可选：打印信息
            System.out.println("搜索产品成功，关键字: " + keyword + "，结果数量: " + products.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // 添加新产品
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("添加产品失败，未影响任何行");
                return false;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                } else {
                    System.out.println("添加产品失败，未获取到生成的ID");
                    return false;
                }
            }

            System.out.println("添加产品成功，ID: " + product.getId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新现有产品
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("更新产品成功，ID: " + product.getId());
                return true;
            } else {
                System.out.println("更新产品失败，未影响任何行，ID: " + product.getId());
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除产品
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("删除产品成功，ID: " + id);
                return true;
            } else {
                System.out.println("删除产品失败，未影响任何行，ID: " + id);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更改订单状态
    public boolean changeOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置参数
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);

            // 执行更新
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  // 返回 true 表示更新成功，false 表示更新失败
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // 发生异常时返回 false
        }
    }

    // 从 ResultSet 中提取产品信息
    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        return product;
    }
}