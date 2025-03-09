package com.example.electronics_store.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronics_store.R;

public class NotificationsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        recyclerViewNotifications = findViewById(R.id.recyclerViewNotifications);
        // Thêm code để hiển thị danh sách thông báo nếu cần
    }
}