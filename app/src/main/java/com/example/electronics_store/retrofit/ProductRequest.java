package com.example.electronics_store.retrofit;

public class ProductRequest {
    private String name;
    private String description;
    private double price;
    private int stock;
    private int categoryId;
    private String imageUrl;

    public ProductRequest(String name, String description, double price, int stock, int categoryId, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
    }
}
