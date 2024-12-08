package com.example.shop.dao;

import com.example.shop.model.Order;
import com.example.shop.model.OrderItem;
import com.example.shop.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    // 获取所有订单
    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM orders";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                orders.add(order);
            }
            System.out.println("获取所有订单成功，数量: " + orders.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // 根据订单ID获取订单
    public Order getOrderById(int id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractOrderFromResultSet(rs);
            } else {
                System.out.println("未找到订单，ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 根据用户ID获取订单
    public List<Order> getOrdersByUserId(int userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
            System.out.println("根据用户ID查询订单成功，用户ID: " + userId + "，订单数量: " + orders.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // 添加新订单，包括订单详情
    public boolean addOrder(Order order, List<OrderItem> orderItems) {
        String sqlOrder = "INSERT INTO orders (user_id, total_price, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmtOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {

            // 添加订单
            stmtOrder.setInt(1, order.getUserId());
            stmtOrder.setBigDecimal(2, order.getTotalPrice());
            stmtOrder.setString(3, order.getStatus());

            int affectedRows = stmtOrder.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("添加订单失败，未影响任何行");
                return false;
            }

            try (ResultSet generatedKeys = stmtOrder.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));  // 获取生成的订单ID
                } else {
                    System.out.println("添加订单失败，未获取到生成的ID");
                    return false;
                }
            }

            // 添加订单详情（order_items）
            String sqlOrderItems = "INSERT INTO order_items (order_id, product_id, price) VALUES (?, ?, ?)";
            try (PreparedStatement stmtItems = conn.prepareStatement(sqlOrderItems)) {
                for (OrderItem item : orderItems) {
                    stmtItems.setInt(1, order.getId());  // 关联到当前订单ID
                    stmtItems.setInt(2, item.getProductId());
                    stmtItems.setBigDecimal(3, item.getPrice());
                    stmtItems.addBatch();
                }
                stmtItems.executeBatch();  // 执行批量插入
            }

            System.out.println("添加订单成功，ID: " + order.getId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新订单（不涉及订单详情）
    public boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET user_id = ?, total_price = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getUserId());
            stmt.setBigDecimal(2, order.getTotalPrice());
            stmt.setString(3, order.getStatus());
            stmt.setInt(4, order.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("更新订单成功，ID: " + order.getId());
                return true;
            } else {
                System.out.println("更新订单失败，未影响任何行，ID: " + order.getId());
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除订单，包括删除订单详情
    public boolean deleteOrder(int id) {
        String sqlOrderItems = "DELETE FROM order_items WHERE order_id = ?";
        String sqlOrder = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmtOrderItems = conn.prepareStatement(sqlOrderItems);
             PreparedStatement stmtOrder = conn.prepareStatement(sqlOrder)) {

            // 删除订单详情
            stmtOrderItems.setInt(1, id);
            stmtOrderItems.executeUpdate();

            // 删除订单
            stmtOrder.setInt(1, id);
            int affectedRows = stmtOrder.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("删除订单及订单详情成功，ID: " + id);
                return true;
            } else {
                System.out.println("删除订单失败，未影响任何行，ID: " + id);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 从 ResultSet 中提取订单信息
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        order.setStatus(rs.getString("status"));
        return order;
    }

    // 根据订单ID获取订单详情
    public List<OrderItem> getOrderDetailsByOrderId(int orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        List<OrderItem> orderItems = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getInt("id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setPrice(rs.getBigDecimal("price"));
                orderItems.add(item);
            }
            System.out.println("根据订单ID查询订单详情成功，订单ID: " + orderId + "，商品数量: " + orderItems.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
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
            System.out.println("Rows affected: " + rowsAffected);
            return rowsAffected > 0;  // 返回 true 表示更新成功，false 表示更新失败
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // 发生异常时返回 false
        }
    }
}



