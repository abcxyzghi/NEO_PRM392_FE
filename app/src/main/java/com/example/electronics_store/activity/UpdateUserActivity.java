package com.example.electronics_store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.electronics_store.R;
import com.example.electronics_store.retrofit.ApiService;
import com.example.electronics_store.retrofit.RetrofitClient;
import com.example.electronics_store.retrofit.UserUpdateRequest;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView ivAvatar;
    private EditText etUserName, etPhoneNumber, etAvatarUrl;
    private Button btnSave;
    private int userId;
    private String avatarUrl;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        Toolbar toolbar = findViewById(R.id.up_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cập nhật thông tin");

        drawerLayout = findViewById(R.id.up_drawer_layout);

        NavigationView navigationView = findViewById(R.id.up_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ivAvatar = findViewById(R.id.up_ivAvatar);
        etUserName = findViewById(R.id.up_etUserName);
        etPhoneNumber = findViewById(R.id.up_etPhoneNumber);
        etAvatarUrl = findViewById(R.id.up_etAvatarUrl);
        btnSave = findViewById(R.id.up_btnSave);

        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);
        etUserName.setText(intent.getStringExtra("USER_NAME"));
        etPhoneNumber.setText(intent.getStringExtra("USER_PHONE"));
        avatarUrl = intent.getStringExtra("USER_AVATAR");

        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            etAvatarUrl.setText(avatarUrl);
            Glide.with(this).load(avatarUrl).placeholder(R.drawable.ic_avatar).into(ivAvatar);
        }
        etAvatarUrl.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String newAvatarUrl = etAvatarUrl.getText().toString().trim();
                if (!newAvatarUrl.isEmpty()) {
                    Glide.with(this).load(newAvatarUrl).placeholder(R.drawable.ic_avatar).into(ivAvatar);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_user_order_history) {
            startActivity(new Intent(this, OrderHistoryActivity.class));
        } else if (id == R.id.nav_user_change_password) {
            startActivity(new Intent(this, ChangePasswordActivity.class));
        } else if (id == R.id.nav_user_store_location) {
            startActivity(new Intent(this, InfoActivity.class));
        } else if (id == R.id.nav_user_product) {
            startActivity(new Intent(this, ProductListActivity.class));
        } else if (id == R.id.nav_user_logout) {
            logout(); // Đóng ứng dụng hoặc xử lý đăng xuất
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        RetrofitClient.setAuthToken(null);

        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
        finish();
    }
}