package com.example.electronics_store.retrofit;

public class OrderResponse {
    private int id;
    private int userId;
    private double totalPrice;
    private String status;
    private String createdAt;
    private String updatedAt;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

}
