package com.example.electronics_store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronics_store.R;
import com.example.electronics_store.adapter.CartAdapter;
import com.example.electronics_store.retrofit.CartUtils;
import com.example.electronics_store.retrofit.ProductResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView totalPriceText;
    private Button btnContinueShopping;
    private Button continue_button;
    private List<ProductResponse> cartList;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        // Ánh xạ View
        recyclerView = findViewById(R.id.recyclerview);
        totalPriceText = findViewById(R.id.total_price_text);
        btnContinueShopping = findViewById(R.id.btn_continue_shopping);
        continue_button = findViewById(R.id.continue_button); // ⚠️ Thêm dòng này!

        // Kiểm tra nếu continue_button bị null
        if (continue_button == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy continue_button", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thiết lập RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tải dữ liệu giỏ hàng
        loadCartData();

        // Khởi tạo Adapter với callback
        cartAdapter = new CartAdapter(cartList, this, this::removeItemFromCart, this::updateItemQuantity);
        recyclerView.setAdapter(cartAdapter);

        // Cập nhật tổng tiền
        updateTotalPrice();

        // Nút "Tiếp tục mua sắm"
        btnContinueShopping.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, ProductListActivity.class));
            finish();
        });

        continue_button.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(cartList));
            startActivity(intent);
        });

    }


    private void loadCartData() {
        List<ProductResponse> originalCartList = CartUtils.getCartList(this);
        if (originalCartList == null || originalCartList.isEmpty()) {
            cartList = new ArrayList<>();
            return;
        }

        Map<Integer, ProductResponse> groupedCartMap = new HashMap<>();

        for (ProductResponse product : originalCartList) {
            if (groupedCartMap.containsKey(product.getId())) {
                groupedCartMap.get(product.getId()).setQuantity(groupedCartMap.get(product.getId()).getQuantity() + 1);
            } else {
                product.setQuantity(1);
                groupedCartMap.put(product.getId(), product);
            }
        }

        cartList = new ArrayList<>(groupedCartMap.values());
        CartUtils.saveCartList(this, cartList);
    }

    private void removeItemFromCart(int position) {
        if (position < 0 || position >= cartList.size()) return;

        ProductResponse product = cartList.get(position);
        if (product.getQuantity() > 1) {
            product.setQuantity(product.getQuantity() - 1);
        } else {
            cartList.remove(position);
        }

        CartUtils.saveCartList(this, cartList);
        cartAdapter.updateData(cartList);
        updateTotalPrice();

        Toast.makeText(this, "Đã cập nhật giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    private void updateItemQuantity(int position, int newQuantity) {
        if (position < 0 || position >= cartList.size() || newQuantity < 1) return;

        cartList.get(position).setQuantity(newQuantity);
        CartUtils.saveCartList(this, cartList);
        cartAdapter.updateData(cartList);
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = 0.0;
        for (ProductResponse product : cartList) {
            total += product.getPrice() * product.getQuantity();
        }
        totalPriceText.setText(String.format("%.0fđ", total));
    }
}
