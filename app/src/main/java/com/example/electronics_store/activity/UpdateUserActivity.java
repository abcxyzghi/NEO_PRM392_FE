package com.example.electronics_store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.electronics_store.R;
import com.example.electronics_store.retrofit.ApiService;
import com.example.electronics_store.retrofit.RetrofitClient;
import com.example.electronics_store.retrofit.UserUpdateRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {
    private ImageView ivAvatar;
    private EditText etUserName, etPhoneNumber, etAvatarUrl;
    private Button btnSave;
    private int userId;
    private String avatarUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        ivAvatar = findViewById(R.id.ivAvatar);
        etUserName = findViewById(R.id.etUserName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAvatarUrl = findViewById(R.id.etAvatarUrl);
        btnSave = findViewById(R.id.btnSave);

        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);
        etUserName.setText(intent.getStringExtra("USER_NAME"));
        etPhoneNumber.setText(intent.getStringExtra("USER_PHONE"));
        avatarUrl = intent.getStringExtra("USER_AVATAR");

        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            etAvatarUrl.setText(avatarUrl);
            Glide.with(this).load(avatarUrl).placeholder(R.drawable.placeholder).into(ivAvatar);
        }
        etAvatarUrl.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String newAvatarUrl = etAvatarUrl.getText().toString().trim();
                if (!newAvatarUrl.isEmpty()) {
                    Glide.with(this).load(newAvatarUrl).placeholder(R.drawable.placeholder).into(ivAvatar);
                }
            }
        });
        btnSave.setOnClickListener(v -> updateUser());
    }

    private void updateUser() {
        String name = etUserName.getText().toString().trim();
        String phone = etPhoneNumber.getText().toString().trim();
        String newAvatarUrl = etAvatarUrl.getText().toString().trim();

        if (userId == -1 || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        UserUpdateRequest request = new UserUpdateRequest(name, phone, newAvatarUrl);

        Call<Void> call = apiService.updateUser(userId, request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UpdateUserActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(UpdateUserActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateUserActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}