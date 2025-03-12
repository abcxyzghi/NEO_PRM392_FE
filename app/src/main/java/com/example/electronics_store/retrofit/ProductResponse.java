package com.example.electronics_store.retrofit;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductResponse implements Parcelable {
    private int id;
    private String createdAt;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;

    public int getId() { return id; }
    public String getCreatedAt() { return createdAt; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }

    protected ProductResponse(Parcel in) {
        id = in.readInt();
        createdAt = in.readString();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        stock = in.readInt();
        imageUrl = in.readString();
    }

    public static final Creator<ProductResponse> CREATOR = new Creator<ProductResponse>() {
        @Override
        public ProductResponse createFromParcel(Parcel in) {
            return new ProductResponse(in);
        }

        @Override
        public ProductResponse[] newArray(int size) {
            return new ProductResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(createdAt);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeDouble(price);
        parcel.writeInt(stock);
        parcel.writeString(imageUrl);
    }
}
