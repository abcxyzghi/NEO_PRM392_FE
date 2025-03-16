package com.example.electronics_store.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.electronics_store.R;
import com.example.electronics_store.retrofit.RetrofitClient;
import com.example.electronics_store.retrofit.ApiService;
import com.example.electronics_store.retrofit.ChangePasswordRequest;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Initialize views
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        // Set up the authentication token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String authToken = sharedPreferences.getString("auth_token", "");

        // Set the auth token in RetrofitClient
        if (!authToken.isEmpty()) {
            RetrofitClient.setAuthToken(authToken);
        } else {
            Log.e("ChangePassword", "Auth token is empty");
        }

        // Change Password button click listener
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String oldPassword = edtOldPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        // Validate inputs
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            edtConfirmPassword.setError("Passwords do not match");
            edtConfirmPassword.requestFocus();
            return;
        }

        if (newPassword.length() < 8) {
            Toast.makeText(this, "New password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the auth token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String authToken = sharedPreferences.getString("auth_token", "");  // Đổi từ "authToken" thành "auth_token"

        Log.d("ChangePassword", "Old Password: " + oldPassword);
        Log.d("ChangePassword", "New Password: " + newPassword);
        Log.d("ChangePassword", "Auth Token: " + authToken);

        // Create the API service
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Create the change password request
        ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, newPassword);

        // Make the API call
        Call<Void> call = apiService.changePassword("Bearer " + authToken, request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("ChangePassword", "Response Code: " + response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    // Clear the input fields
                    edtOldPassword.setText("");
                    edtNewPassword.setText("");
                    edtConfirmPassword.setText("");
                } else {
                    if (response.code() == 403) {
                        Toast.makeText(ChangePasswordActivity.this, "Authentication failed. Please log in again.", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 400) {
                        Toast.makeText(ChangePasswordActivity.this, "Old password is incorrect", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Failed to change password (Error " + response.code() + ")", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ChangePassword", "Error: " + t.getMessage());
                Toast.makeText(ChangePasswordActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
