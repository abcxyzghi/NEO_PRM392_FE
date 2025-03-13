package com.example.electronics_store.model;

import com.google.gson.annotations.SerializedName;

public class OrderDetail {
    @SerializedName("product")
    private Product product;

    private int quantity;
    private double price;

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}