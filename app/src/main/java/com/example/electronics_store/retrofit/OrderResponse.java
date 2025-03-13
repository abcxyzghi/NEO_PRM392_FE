package com.example.electronics_store.retrofit;

import com.example.electronics_store.model.OrderDetail;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponse {
    private int id;
    private int userId;
    private double totalPrice;
    private String status;
    private String createdAt;
    private String updatedAt;

    @SerializedName("orderDetail") // Khớp với JSON
    private List<OrderDetail> orderDetails;

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

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }
}
