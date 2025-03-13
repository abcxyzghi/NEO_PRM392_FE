package com.example.electronics_store.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.electronics_store.R;
import com.example.electronics_store.retrofit.ApiService;
import com.example.electronics_store.retrofit.OrderResponse;
import com.example.electronics_store.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView txtOrderDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        txtOrderDetail = findViewById(R.id.txtOrderDetail);

        int orderId = getIntent().getIntExtra("orderId", -1);
        if (orderId != -1) {
            fetchOrderDetail(orderId);
        }
    }

    private void fetchOrderDetail(int orderId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<OrderResponse> call = apiService.getOrderDetail(orderId);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderResponse order = response.body();
                    txtOrderDetail.setText("Mã đơn: " + order.getId() + "\nTổng tiền: " + order.getTotalPrice() + " VNĐ\nTrạng thái: " + order.getStatus());
                } else {
                    txtOrderDetail.setText("Không tìm thấy thông tin đơn hàng.");
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                txtOrderDetail.setText("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}