package com.example.electronics_store.retrofit;

public class ProductResponse {
    private int id;
    private String createdAt;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;

    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
