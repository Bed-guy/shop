package com.example.shop.model;


import java.sql.Timestamp;

public class UserLog {
    private int id; //ID
    private int userId; // 用户ID
    private Timestamp actionTime; // 操作时间
    private String actionType; // 操作类型
    private String details; //操作描述

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getActionTime() {
        return actionTime;
    }

    public void setActionTime(Timestamp actionTime) {
        this.actionTime = actionTime;
    }
}
