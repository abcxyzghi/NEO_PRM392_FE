package com.example.electronics_store.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import com.example.electronics_store.R;
import com.example.electronics_store.retrofit.ApiService;
import com.example.electronics_store.retrofit.RetrofitClient;
import com.example.electronics_store.retrofit.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        final EditText yourName = findViewById(R.id.name);
        final EditText emailAddress = findViewById(R.id.email);
        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final EditText confirmPassword = findViewById(R.id.reenter_password);
        final Button registerBtn = findViewById(R.id.signup_button);

        registerBtn.setOnClickListener(v -> {
            final String nameText = yourName.getText().toString();
            final String emailText = emailAddress.getText().toString();
            final String phoneText = phone.getText().toString();
            final String passwordText = password.getText().toString();
            final String confirmPasswordText = confirmPassword.getText().toString();

            if (nameText.isEmpty() || emailText.isEmpty() || phoneText.isEmpty() || passwordText.isEmpty() || confirmPasswordText.isEmpty()) {
                Toast.makeText(Register.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!passwordText.equals(confirmPasswordText)) {
                Toast.makeText(Register.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User(nameText, emailText, phoneText, passwordText, "user");

            // Kiểm tra kết nối mạng trước khi gửi request
            if (!isNetworkAvailable()) {
                Toast.makeText(Register.this, "Không có kết nối mạng. Vui lòng kiểm tra lại.", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<Void> call = apiService.registerUser(user);

            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        // Có thể chuyển sang màn hình đăng nhập
                    } else {
                        String errorMessage = response.message();
                        if (response.code() == 400) {
                            errorMessage = "Email đã tồn tại hoặc thông tin không hợp lệ";
                        } else if (response.code() == 500) {
                            errorMessage = "Lỗi máy chủ. Vui lòng thử lại sau";
                        }
                        Toast.makeText(Register.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    // Thêm xử lý chi tiết hơn cho các lỗi kết nối
                    String errorMessage = "Lỗi kết nối: ";
                    if (t instanceof IOException) {
                        errorMessage += "Không thể kết nối đến máy chủ";
                    } else {
                        errorMessage += t.getMessage();
                    }
                    Toast.makeText(Register.this, errorMessage, Toast.LENGTH_SHORT).show();

                    // Thử reset client để tạo lại kết nối
                    RetrofitClient.resetClient();
                }
            });
        });
    }

    // Phương thức kiểm tra kết nối mạng
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}