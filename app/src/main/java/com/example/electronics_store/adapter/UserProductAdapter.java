package com.example.electronics_store.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.electronics_store.R;
import com.example.electronics_store.activity.ProductDetailActivity;
import com.example.electronics_store.retrofit.FavoriteManager;
import com.example.electronics_store.retrofit.ProductResponse;
import com.example.electronics_store.retrofit.CartUtils;

import java.text.DecimalFormat;
import java.util.List;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.ViewHolder> {

    private Context context;
    private List<ProductResponse> productList;
    private boolean isWishlistScreen;  // Xác định có phải màn hình yêu thích hay không

    public UserProductAdapter(Context context, List<ProductResponse> productList, boolean isWishlistScreen) {
        this.context = context;
        this.productList = productList;
        this.isWishlistScreen = isWishlistScreen;  // Lưu trạng thái của màn hình
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductResponse product = productList.get(position);
        holder.productName.setText(product.getName());

        // Định dạng giá tiền và hiển thị
        holder.productPrice.setText(formatPrice(product.getPrice()));

        // Load ảnh sản phẩm bằng Glide
        Glide.with(context).load(product.getImageUrl()).into(holder.productImage);

        // Xử lý click vào item (mở ProductDetailActivity)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });

        // Xử lý nút thêm vào giỏ hàng
        holder.btnAddToCart.setOnClickListener(v -> {
            CartUtils.addToCart(context, product); // Dùng SharedPreferences lưu giỏ hàng
            Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        // Xử lý nút thêm vào yêu thích
        holder.btnAddToWishlist.setOnClickListener(v -> {
            FavoriteManager.addFavorite(context, product);
            Toast.makeText(context, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
        });

        // Xử lý nút "Xóa khỏi yêu thích"
        holder.btnRemoveFavorite.setOnClickListener(v -> {
            FavoriteManager.removeFavorite(context, product.getId());
            productList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, productList.size());
            Toast.makeText(context, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
        });

        // Ẩn/hiện các nút phù hợp với màn hình đang hiển thị
        holder.bindData(product, isWishlistScreen);
    }

    private String formatPrice(double price) {
        DecimalFormat formatter = new DecimalFormat("#,###,### VND");
        return formatter.format(price);
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ImageView productImage;
        Button btnAddToCart, btnAddToWishlist, btnRemoveFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnAddToWishlist = itemView.findViewById(R.id.btnAddToWishlist);
            btnRemoveFavorite = itemView.findViewById(R.id.btnRemoveFavorite);
        }

        // Ẩn hoặc hiển thị nút phù hợp với màn hình
        public void bindData(ProductResponse product, boolean isWishlistScreen) {
            productName.setText(product.getName());
            productPrice.setText(String.format("%s VND", product.getPrice()));

            // Nếu là màn hình yêu thích -> Hiện nút Xóa, Ẩn nút Thêm vào giỏ hàng & yêu thích
            if (isWishlistScreen) {
                btnRemoveFavorite.setVisibility(View.VISIBLE);
                btnAddToCart.setVisibility(View.GONE);
                btnAddToWishlist.setVisibility(View.GONE);
            } else {
                btnRemoveFavorite.setVisibility(View.GONE);
                btnAddToCart.setVisibility(View.VISIBLE);
                btnAddToWishlist.setVisibility(View.VISIBLE);
            }
        }
    }
}
