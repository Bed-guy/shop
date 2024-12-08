package com.example.shop.dao;

import com.example.shop.model.Cart;
import com.example.shop.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDao {

    // 获取用户的所有购物车项
    public List<Cart> getCartByUserId(int userId) {
        String sql = "SELECT * FROM cart WHERE user_id = ?";
        List<Cart> cartItems = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cart cart = extractCartFromResultSet(rs);
                cartItems.add(cart);
            }
            System.out.println("获取用户购物车项成功，用户ID: " + userId + "，数量: " + cartItems.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    // 判断某商品是否已存在购物车
    public boolean isProductInCart(int userId, int productId) {
        String sql = "SELECT COUNT(*) FROM cart WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // 如果存在则返回true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // 商品不存在购物车中
    }

    // 添加产品到购物车
    public boolean addProductToCart(Cart cart) {
        String sql = "INSERT INTO cart (user_id, product_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cart.getUserId());
            stmt.setInt(2, cart.getProductId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("添加产品到购物车成功，用户ID: " + cart.getUserId() + ", 产品ID: " + cart.getProductId());
                return true;
            } else {
                System.out.println("添加产品到购物车失败，未影响任何行");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除购物车中的产品
    public boolean removeProductFromCart(int userId, int productId) {
        String sql = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, productId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("删除购物车中的产品成功，用户ID: " + userId + ", 产品ID: " + productId);
                return true;
            } else {
                System.out.println("删除购物车中的产品失败，未影响任何行");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 清空用户购物车
    public boolean clearCart(int userId) {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("清空用户购物车成功，用户ID: " + userId);
                return true;
            } else {
                System.out.println("清空用户购物车失败，未影响任何行，用户ID: " + userId);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 从 ResultSet 中提取购物车项信息
    private Cart extractCartFromResultSet(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setId(rs.getInt("id"));
        cart.setUserId(rs.getInt("user_id"));
        cart.setProductId(rs.getInt("product_id"));
        return cart;
    }
}
