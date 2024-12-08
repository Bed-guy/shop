package com.example.shop.model;

import java.math.BigDecimal;

public class Order {
    private int id;
    private int userId;
    private BigDecimal totalPrice;
    private String status;

    public Order() {
    }

    public Order(int userId, BigDecimal totalPrice, String status) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
