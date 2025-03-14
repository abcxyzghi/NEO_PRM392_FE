package com.example.electronics_store.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.electronics_store.R;
import com.example.electronics_store.adapter.CartItemAdapter;
import com.example.electronics_store.retrofit.ProductResponse;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    private ListView cartItemListView;
    private TextView totalPriceText;
    private RadioGroup paymentOptions;
    private Button btnConfirmPayment;
    private List<ProductResponse> cartList;
    private CartItemAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);

        // Ánh xạ View
        cartItemListView = findViewById(R.id.cartItemListView);
        totalPriceText = findViewById(R.id.totalPrice);
        paymentOptions = findViewById(R.id.paymentMethodGroup);
        btnConfirmPayment = findViewById(R.id.orderButton);

        // Nhận dữ liệu giỏ hàng từ Intent
        cartList = getIntent().getParcelableArrayListExtra("cartItems");

        if (cartList == null || cartList.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị giỏ hàng trong ListView
        cartAdapter = new CartItemAdapter(this, cartList);
        cartItemListView.setAdapter(cartAdapter);

        // Tính tổng giá trị đơn hàng
        updateTotalPrice();

        // Xử lý nút đặt hàng
        btnConfirmPayment.setOnClickListener(v -> {
            int selectedId = paymentOptions.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRadioButton = findViewById(selectedId);
            String paymentMethod = selectedRadioButton.getText().toString();

            Toast.makeText(this, "Bạn đã chọn: " + paymentMethod, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updateTotalPrice() {
        double total = 0.0;
        for (ProductResponse product : cartList) {
            total += product.getPrice() * product.getQuantity();
        }
        totalPriceText.setText(String.format("%.0fđ", total));
    }
}
