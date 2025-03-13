package com.example.electronics_store.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronics_store.R;
import com.example.electronics_store.adapter.OrderHistoryAdapter;
import com.example.electronics_store.adapter.OrderListResponse;
import com.example.electronics_store.retrofit.OrderResponse;
import com.example.electronics_store.retrofit.ApiService;
import com.example.electronics_store.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmptyMessage;
    private OrderHistoryAdapter adapter;
    private List<OrderResponse> orderList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderHistoryAdapter(orderList);
        recyclerView.setAdapter(adapter);

        loadOrderHistory();
    }
    public void updateOrderList(List<OrderResponse> newOrderList) {
        this.orderList.clear(); // Xóa dữ liệu cũ
        this.orderList.addAll(newOrderList); // Thêm dữ liệu mới
        adapter.notifyDataSetChanged(); // ✅ Đúng

    }

    private void loadOrderHistory() {
        Log.d("OrderHistory", "Bắt đầu gọi API lấy danh sách đơn hàng");
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = preferences.getString("auth_token", null);

        if (token == null || token.isEmpty()) {
            Log.e("OrderHistory", "❌ Token bị null hoặc rỗng!");
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<OrderListResponse> call = apiService.getUserOrders("Bearer " + token);

        call.enqueue(new Callback<OrderListResponse>() {
            @Override
            public void onResponse(Call<OrderListResponse> call, Response<OrderListResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d("OrderHistory", "API trả về mã: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    List<OrderResponse> orders = response.body().getData();

                    if (orders == null || orders.isEmpty()) {
                        Log.w("OrderHistory", "📭 Không có đơn hàng nào!");
                        tvEmptyMessage.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        Log.d("OrderHistory", "📦 Số đơn hàng nhận được: " + orders.size());
                        adapter.updateOrderList(orders);  // Cập nhật dữ liệu cho adapter
                        tvEmptyMessage.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("OrderHistory", "API thất bại, mã lỗi: " + response.code());
                    Toast.makeText(OrderHistoryActivity.this, "Lỗi khi tải đơn hàng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderListResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("OrderHistory", "Lỗi kết nối API: " + t.getMessage());
                Toast.makeText(OrderHistoryActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
