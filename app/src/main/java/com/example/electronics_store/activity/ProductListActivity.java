package com.example.electronics_store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.electronics_store.R;
import com.example.electronics_store.adapter.BannerAdapter;
import com.example.electronics_store.adapter.UserProductAdapter;
import com.example.electronics_store.retrofit.*;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserProductAdapter userProductAdapter;
    private ApiService apiService;
    private SearchView searchView;
    private Spinner spinnerCategory, spinnerSort;
    private EditText minPrice, maxPrice;
    private Button btnFilter;
    private List<CategoryResponse> categoryList;
    private ImageButton btnCart, btnFavorite, btnNotification;

    private ViewPager2 bannerViewPager;
    private BannerAdapter bannerAdapter;
    private List<String> bannerList;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable bannerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // Initialize views
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSort = findViewById(R.id.spinnerSort);
        minPrice = findViewById(R.id.minPrice);
        maxPrice = findViewById(R.id.maxPrice);
        btnFilter = findViewById(R.id.btnFilter);
        btnCart = findViewById(R.id.btnCart);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnNotification = findViewById(R.id.btnNotification);
        bannerViewPager = findViewById(R.id.bannerViewPager);

        // Initialize banner carousel
        initializeBanner();

        // Handle notification intent
        Intent notificationIntent = getIntent();
        if (notificationIntent != null && notificationIntent.getData() != null) {
            String action = notificationIntent.getData().getHost();
            if ("payment_success".equals(action)) {
                Toast.makeText(this, "Thanh toán thành công! Quay về trang sản phẩm.", Toast.LENGTH_SHORT).show();
            }
        }

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Set up Spinner and load categories
        setupSortSpinner();
        loadCategories();

        // Fetch products
        fetchProducts(null, null, null, "DESC", null);

        // Set up search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchProducts(null, null, null, getSortDirection(), query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Set up filter button
        btnFilter.setOnClickListener(view -> applyFilters());

        // Set up navigation buttons
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, CartActivity.class);
            startActivity(intent);
        });

        btnFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, FavoriteActivity.class);
            startActivity(intent);
        });

        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> {
                Intent intent = new Intent(ProductListActivity.this, NotificationActivity.class);
                startActivity(intent);
            });
        } else {
            Log.e("ProductListActivity", "btnNotification is null!");
        }

        // Change search view text color
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        if (searchEditText != null) {
            searchEditText.setTextColor(getResources().getColor(android.R.color.black));
            searchEditText.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    private void initializeBanner() {
        bannerList = Arrays.asList(
                "https://th.bing.com/th/id/OIP.ng6GrR09uCxfwX4OTZYY_QHaDC?rs=1&pid=ImgDetMain",
                "https://th.bing.com/th/id/OIP.4fxHY-leAvBN4saDO7ng3AHaCL?w=1200&h=353&rs=1&pid=ImgDetMain",
                "https://cdn.tgdd.vn/2022/03/banner/830-300-830x300-23.png"
        );

        bannerAdapter = new BannerAdapter(this, bannerList);
        bannerViewPager.setAdapter(bannerAdapter);

        // Auto-scroll banner
        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = (bannerViewPager.getCurrentItem() + 1) % bannerList.size();
                bannerViewPager.setCurrentItem(nextItem, true);
                handler.postDelayed(this, 3000);
            }
        };

        handler.postDelayed(bannerRunnable, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(bannerRunnable, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(bannerRunnable);
    }

    private void setupSortSpinner() {
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"Giá tăng dần", "Giá giảm dần"});
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadCategories() {
        apiService.getCategories().enqueue(new Callback<List<CategoryResponse>>() {
            @Override
            public void onResponse(Call<List<CategoryResponse>> call, Response<List<CategoryResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body();
                    categoryList.add(0, new CategoryResponse(-1, "Tất cả"));

                    ArrayAdapter<CategoryResponse> adapter = new ArrayAdapter<>(ProductListActivity.this,
                            android.R.layout.simple_spinner_item, categoryList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<CategoryResponse>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Lỗi tải danh mục!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyFilters() {
        Integer categoryId = null;
        if (spinnerCategory.getSelectedItem() != null) {
            CategoryResponse selectedCategory = (CategoryResponse) spinnerCategory.getSelectedItem();
            if (selectedCategory.getId() != -1) {
                categoryId = selectedCategory.getId();
            }
        }

        Double min = minPrice.getText().toString().isEmpty() ? null : Double.parseDouble(minPrice.getText().toString());
        Double max = maxPrice.getText().toString().isEmpty() ? null : Double.parseDouble(maxPrice.getText().toString());

        fetchProducts(categoryId, min, max, getSortDirection(), searchView.getQuery().toString());
    }

    private String getSortDirection() {
        return spinnerSort.getSelectedItemPosition() == 0 ? "ASC" : "DESC";
    }

    private void fetchProducts(Integer categoryId, Double minPrice, Double maxPrice, String sortDirection, String searchQuery) {
        apiService.getAllProducts("price", sortDirection, categoryId, minPrice, maxPrice, searchQuery, null, null)
                .enqueue(new Callback<List<ProductResponse>>() {
                    @Override
                    public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            userProductAdapter = new UserProductAdapter(ProductListActivity.this, response.body(), false);
                            recyclerView.setAdapter(userProductAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ProductResponse>> call, Throwable t) {
                        Toast.makeText(ProductListActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}