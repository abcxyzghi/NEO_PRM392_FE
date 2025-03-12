package com.example.electronics_store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.electronics_store.R;
import com.example.electronics_store.retrofit.ProductResponse;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productPrice, productDescription;
    private Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product")) {
            ProductResponse product = intent.getParcelableExtra("product");

            if (product != null) {
                productName.setText(product.getName() != null ? product.getName() : "Không có tên");
                productPrice.setText(formatPrice(product.getPrice()) + " VND");
                productDescription.setText(product.getDescription() != null ? product.getDescription() : "Không có mô tả");

                if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                    Glide.with(this).load(product.getImageUrl()).into(productImage);
                } else {
                    productImage.setImageResource(R.drawable.bug_report);
                }

                btnAddToCart.setOnClickListener(v -> {
                    addToCart(product);
                    Toast.makeText(ProductDetailActivity.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(this, "Không thể tải dữ liệu sản phẩm!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Không tìm thấy dữ liệu sản phẩm!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void addToCart(ProductResponse product) {
        // Ở đây bạn có thể thêm logic lưu sản phẩm vào giỏ hàng (SharedPreferences, Room DB, Redux, v.v.)
    }

    private String formatPrice(double price) {
        return String.format("%,.0f", price); // Format thành dạng 1,000,000
    }
}
