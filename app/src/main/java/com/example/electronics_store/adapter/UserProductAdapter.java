package com.example.electronics_store.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
    private final Context context;
    private final List<ProductResponse> productList;
    private final boolean isWishlistScreen;

    public UserProductAdapter(Context context, List<ProductResponse> productList, boolean isWishlistScreen) {
        this.context = context;
        this.productList = productList;
        this.isWishlistScreen = isWishlistScreen;
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
        holder.bindData(context, product, isWishlistScreen, position);
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    private static String formatPrice(double price) {
        DecimalFormat formatter = new DecimalFormat("#,###,### VND");
        return formatter.format(price);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productStock;
        ImageView productImage;
        Button btnAddToCart, btnAddToWishlist, btnRemoveFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            productStock = itemView.findViewById(R.id.productStock);  // Thêm TextView hiển thị "Hết hàng"
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnAddToWishlist = itemView.findViewById(R.id.btnAddToWishlist);
            btnRemoveFavorite = itemView.findViewById(R.id.btnRemoveFavorite);
        }

        public void bindData(Context context, ProductResponse product, boolean isWishlistScreen, int position) {
            productName.setText(product.getName());
            productPrice.setText(formatPrice(product.getPrice()));

            // Hiển thị ảnh sản phẩm
            Glide.with(context).load(product.getImageUrl()).into(productImage);

            // Hiển thị trạng thái "Hết hàng"
            if (product.getStock() == 0) {
                btnAddToCart.setVisibility(View.GONE);
                btnAddToWishlist.setVisibility(View.GONE);
                productStock.setVisibility(View.VISIBLE);
                productStock.setText("Hết hàng");
                productStock.setTextColor(Color.RED);
            } else {
                btnAddToCart.setVisibility(View.VISIBLE);
                btnAddToWishlist.setVisibility(View.VISIBLE);
                productStock.setVisibility(View.GONE);
            }

            // Nếu là màn hình yêu thích
            if (isWishlistScreen) {
                btnRemoveFavorite.setVisibility(View.VISIBLE);
                btnAddToCart.setVisibility(View.GONE);
                btnAddToWishlist.setVisibility(View.GONE);
            } else {
                btnRemoveFavorite.setVisibility(View.GONE);
            }

            // Click vào item -> mở ProductDetailActivity
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product", product);
                context.startActivity(intent);
            });

            // Thêm vào giỏ hàng
            btnAddToCart.setOnClickListener(v -> {
                CartUtils.addToCart(context, product);
                Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            });

            // Thêm vào danh sách yêu thích
            btnAddToWishlist.setOnClickListener(v -> {
                FavoriteManager.addFavorite(context, product);
                Toast.makeText(context, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            });

            // Xóa khỏi yêu thích
            btnRemoveFavorite.setOnClickListener(v -> {
                FavoriteManager.removeFavorite(context, product.getId());
                productList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
