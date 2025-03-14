/*package com.example.electronics_store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronics_store.R;

public class HomeActivity extends AppCompatActivity {
    private ImageButton btnCart, btnFavorite;
    private SearchView searchView;
    private Spinner spinnerCategory, spinnerSort;
    private EditText minPrice, maxPrice;
    private Button btnFilter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_product_list); // Đảm bảo layout này là layout vừa gửi

        // Ánh xạ view
        searchView = findViewById(R.id.searchView);
        btnCart = findViewById(R.id.btnCart);
        btnFavorite = findViewById(R.id.btnFavorite);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSort = findViewById(R.id.spinnerSort);
        minPrice = findViewById(R.id.minPrice);
        maxPrice = findViewById(R.id.maxPrice);
        btnFilter = findViewById(R.id.btnFilter);
        recyclerView = findViewById(R.id.recyclerView);

        // Xử lý click
        btnCart.setOnClickListener(v -> {
            Log.d("HomeActivity", "Cart icon clicked");
            startActivity(new Intent(HomeActivity.this, CartActivity.class));
        });

        btnFavorite.setOnClickListener(v -> {
            Log.d("HomeActivity", "Favorite icon clicked");
            // TODO: Chuyển sang màn yêu thích nếu có
        });

        btnFilter.setOnClickListener(v -> {
            Log.d("HomeActivity", "Lọc theo giá: " + minPrice.getText() + " - " + maxPrice.getText());
            // TODO: Lọc sản phẩm theo giá và hiển thị trên recyclerView
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("HomeActivity", "Search submit: " + query);
                // TODO: Thực hiện tìm kiếm
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("HomeActivity", "Search change: " + newText);
                // TODO: Gợi ý tìm kiếm nếu cần
                return false;
            }
        });
    }
}
*/