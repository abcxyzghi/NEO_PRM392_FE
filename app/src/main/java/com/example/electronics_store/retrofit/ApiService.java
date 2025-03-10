package com.example.electronics_store.retrofit;

import com.example.electronics_store.Model.User;
import com.example.electronics_store.Model.UserDetail;

import java.util.List;

import retrofit2.*;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @PATCH("auth/update-profile")
    Call<Void> updateProfile(@Body UpdateProfileRequest updateProfileRequest);

    @GET("admin/accounts/{id}")
    Call<UserDetail> getUserDetail(@Path("id") int userId);

    @PUT("admin/accounts/{id}/ban")
    Call<Void> banUser(@Path("id") int userId);

    @PUT("admin/accounts/{id}/unban")
    Call<Void> unbanUser(@Path("id") int userId);

    @GET("admin/accounts")
    Call<List<UserResponse>> getAllUsers();

    @GET("admin/products")
    Call<List<ProductResponse>> getProducts();

    @DELETE("admin/products/{id}")
    Call<Void> deleteProduct(@Path("id") int productId);

    @POST("admin/products")
    Call<Void> addProduct(@Body ProductRequest product);

    @GET("admin/categories")
    Call<List<CategoryResponse>> getCategories();

    @POST("admin/categories")
    Call<Void> addCategory(@Body CategoryRequest category);

    @PUT("admin/categories/{id}")
    Call<Void> updateCategory(@Path("id") int id, @Body CategoryRequest category);

    @DELETE("admin/categories/{id}")
    Call<Void> deleteCategory(@Path("id") int id);
}
