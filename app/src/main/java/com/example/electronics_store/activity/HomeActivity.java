package com.example.electronics_store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.electronics_store.R;
import com.example.electronics_store.adapter.BannerAdapter;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ImageView hamburger, notifications, cart;
    private SearchView searchView;
    private ViewPager2 bannerViewPager;
    private BannerAdapter adapter;
    private List<Integer> bannerImages = Arrays.asList(
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Ánh xạ view
        hamburger = findViewById(R.id.hamburger);
        notifications = findViewById(R.id.notifications);
        cart = findViewById(R.id.cart);
        searchView = findViewById(R.id.searchView);
        bannerViewPager = findViewById(R.id.bannerViewPager);

        // Thiết lập adapter cho banner
        bannerViewPager = findViewById(R.id.bannerViewPager);
        adapter = new BannerAdapter(this, bannerImages);
        bannerViewPager.setAdapter(adapter);
        // Tự động trượt banner
        autoSlideBanner();

        // Xử lý sự kiện click
        hamburger.setOnClickListener(v -> {
            // Mở menu
        });

        notifications.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, NotificationsActivity.class));
        });

        cart.setOnClickListener(v -> {
            // Mở giỏ hàng
//            startActivity(new Intent(HomeActivity.this, CartActivity.class));
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý tìm kiếm khi nhấn submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Xử lý khi nội dung search thay đổi
                return false;
            }
        });
    }

    private void autoSlideBanner() {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = (bannerViewPager.getCurrentItem() + 1) % bannerImages.size();
                bannerViewPager.setCurrentItem(nextItem, true);
                handler.postDelayed(this, 3000); // Trượt ảnh sau mỗi 3 giây
            }
        };
        handler.postDelayed(runnable, 3000);
    }
}
