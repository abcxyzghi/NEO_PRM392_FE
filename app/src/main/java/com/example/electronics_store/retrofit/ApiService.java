package com.example.electronics_store.retrofit;

import java.util.List;

import retrofit2.*;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth/register")
    Call<Void> registerUser(@Body User user);

    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @PATCH("auth/update-profile")
    Call<Void> updateProfile(@Body UpdateProfileRequest updateProfileRequest);

    @GET("admin/accounts/{id}")
    Call<UserDetail> getUserDetail(@Path("id") int userId);

    @GET("admin/getAllUser")
    Call<List<User>> getAllUsers();

    @PUT("admin/accounts/{id}/ban")
    Call<Void> banUser(@Path("id") int userId);

    @PUT("admin/accounts/{id}/unban")
    Call<Void> unbanUser(@Path("id") int userId);

}
