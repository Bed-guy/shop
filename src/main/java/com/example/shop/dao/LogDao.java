package com.example.shop.dao;

import com.example.shop.model.UserLog;
import com.example.shop.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogDao {

    /**
     * 插入日志记录
     * @param userId 用户ID
     * @param actionType 操作类型
     * @param details 操作详情
     */
    public void insertLog(int userId, String actionType, String details) {
        String sql = "INSERT INTO user_logs (user_id, action_type, details) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, actionType);
            stmt.setString(3, details);

            stmt.executeUpdate();
            System.out.println("日志记录插入成功，操作类型: " + actionType);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("日志记录插入失败，操作类型: " + actionType);
        }
    }

    public List<UserLog> getAllLogs() {
        List<UserLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM user_logs ORDER BY action_time DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UserLog log = new UserLog();
                log.setId(rs.getInt("id"));
                log.setUserId(rs.getInt("user_id"));
                log.setActionTime(Timestamp.from(rs.getTimestamp("action_time").toInstant()));
                log.setActionType(rs.getString("action_type"));
                log.setDetails(rs.getString("details"));

                logs.add(log);
            }
            System.out.println("成功获取所有日志记录，总计: " + logs.size() + " 条");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("获取日志记录失败");
        }

        return logs;
    }

}

