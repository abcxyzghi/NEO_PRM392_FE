package com.example.electronics_store.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronics_store.R;
import com.example.electronics_store.adapter.OrderDetailAdapter;
import com.example.electronics_store.model.OrderDetail;
import com.example.electronics_store.retrofit.ApiService;
import com.example.electronics_store.retrofit.OrderResponse;
import com.example.electronics_store.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerViewOrderDetails;
    private OrderDetailAdapter orderDetailAdapter;
    private List<OrderDetail> orderDetailList = new ArrayList<>();
    TextView orderId, userId, totalPrice, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);

        orderId = (TextView) findViewById(R.id.od_order_id);
        userId = (TextView) findViewById(R.id.od_user_id);
        totalPrice = (TextView) findViewById(R.id.od_tvTongTien);
        status = (TextView) findViewById(R.id.od_status);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(view -> {
            finish();
        });

        recyclerViewOrderDetails = findViewById(R.id.recyclerViewOrderDetails);
        recyclerViewOrderDetails.setLayoutManager(new LinearLayoutManager(this));

        orderDetailAdapter = new OrderDetailAdapter(orderDetailList);
        recyclerViewOrderDetails.setAdapter(orderDetailAdapter);

        int orderIdI = getIntent().getIntExtra("order_id", -1);
        int userIdI = getIntent().getIntExtra("order_user_id", -1);
        String statusI = getIntent().getStringExtra("order_status");
        Double price = getIntent().getDoubleExtra("order_price", -1);

        orderId.setText(orderIdI);
        userId.setText(userIdI);
        status.setText(statusI);
        totalPrice.setText("" + price);
        if (orderIdI != -1) {
            fetchOrderDetails(orderIdI);
        }
    }

    private void fetchOrderDetails(int orderId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<OrderResponse> call = apiService.getOrderById(orderId);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call, @NonNull Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderDetailList = response.body().getOrderDetails();
                    orderDetailAdapter.setOrderDetailList(orderDetailList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}