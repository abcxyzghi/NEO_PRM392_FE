package com.example.electronics_store.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.electronics_store.R;
import com.example.electronics_store.adapter.UserProductAdapter;
import com.example.electronics_store.retrofit.*;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSort = findViewById(R.id.spinnerSort);
        minPrice = findViewById(R.id.minPrice);
        maxPrice = findViewById(R.id.maxPrice);
        btnFilter = findViewById(R.id.btnFilter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiService = RetrofitClient.getClient().create(ApiService.class);

        setupSortSpinner();
        loadCategories();
        fetchProducts(null, null, null, "DESC", null);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchProducts(null, null, null, getSortDirection(), query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) { return false; }
        });

        btnFilter.setOnClickListener(view -> applyFilters());
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
            public void onFailure(Call<List<CategoryResponse>> call, Throwable t) {}
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
                            userProductAdapter = new UserProductAdapter(ProductListActivity.this, response.body());
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