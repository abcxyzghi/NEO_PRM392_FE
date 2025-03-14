package com.example.electronics_store.activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.electronics_store.R;
import com.example.electronics_store.adapter.CartItemAdapter;
import com.example.electronics_store.retrofit.ProductResponse;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    private EditText fullName, phoneNumber, email, address;
    private ListView cartItemListView;
    private TextView totalPriceText;
    private RadioGroup paymentOptions;
    private Button btnConfirmPayment;
    private List<ProductResponse> cartList;
    private CartItemAdapter cartAdapter;
    private static final String CHANNEL_ID = "order_notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);

        // Ánh xạ View
        fullName = findViewById(R.id.fullName);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
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

        // Tạo kênh thông báo nếu cần
        createNotificationChannel();

        // Xử lý nút đặt hàng
        btnConfirmPayment.setOnClickListener(v -> handleOrder());
    }

    private void handleOrder() {
        String customerName = fullName.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();
        String customerEmail = email.getText().toString().trim();
        String customerAddress = address.getText().toString().trim();
        int selectedId = paymentOptions.getCheckedRadioButtonId();

        if (customerName.isEmpty() || phone.isEmpty() || customerEmail.isEmpty() || customerAddress.isEmpty() || selectedId == -1) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin và chọn phương thức thanh toán", Toast.LENGTH_LONG).show();
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedId);
        String paymentMethod = selectedRadioButton.getText().toString();

        if (paymentMethod.equals("Thanh toán tiền mặt khi nhận hàng")) {
            saveOrderNotification("Đơn hàng của bạn đã được đặt thành công!");
            showOrderNotification();
            sendOrderConfirmationEmail(customerEmail, customerName, customerAddress, paymentMethod);
            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else if (paymentMethod.equals("Thanh toán qua ZaloPay")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://zalopay.vn/"));
            startActivity(intent);
        }
    }

    private void updateTotalPrice() {
        double total = 0.0;
        for (ProductResponse product : cartList) {
            total += product.getPrice() * product.getQuantity();
        }
        totalPriceText.setText(String.format("%.0fđ", total));
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Order Notifications";
            String description = "Thông báo về đơn hàng";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showOrderNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications)
                .setContentTitle("Đặt hàng thành công!")
                .setContentText("Đơn hàng của bạn đã được xác nhận.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private void saveOrderNotification(String message) {
        SharedPreferences sharedPreferences = getSharedPreferences("OrderPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastOrderNotification", message);
        editor.apply();
    }

    private void sendOrderConfirmationEmail(String recipientEmail, String name, String address, String paymentMethod) {
        String subject = "Xác nhận đơn hàng từ Electronics Store";
        String message = "Xin chào " + name + ",\n\n"
                + "Cảm ơn bạn đã đặt hàng tại Electronics Store.\n\n"
                + "Thông tin đơn hàng:\n"
                + "Tên: " + name + "\n"
                + "Địa chỉ: " + address + "\n"
                + "Phương thức thanh toán: " + paymentMethod + "\n\n"
                + "Chúng tôi sẽ sớm xử lý đơn hàng của bạn.\n\n"
                + "Trân trọng,\n"
                + "Electronics Store Team";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + recipientEmail));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Gửi email..."));
        } catch (Exception e) {
            Toast.makeText(this, "Không thể gửi email", Toast.LENGTH_SHORT).show();
        }
    }
}
