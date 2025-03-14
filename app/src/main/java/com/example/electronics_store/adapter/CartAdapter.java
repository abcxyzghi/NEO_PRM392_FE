package com.example.electronics_store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.electronics_store.R;
import com.example.electronics_store.retrofit.CartUtils;
import com.example.electronics_store.retrofit.ProductResponse;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<ProductResponse> cartList;
    private Context context;
    private OnCartUpdateListener cartUpdateListener;
    private OnQuantityChangeListener quantityChangeListener;

    // Interface cho callback xóa sản phẩm
    public interface OnCartUpdateListener {
        void onItemRemove(int position);
    }

    // Interface cho callback tăng/giảm số lượng
    public interface OnQuantityChangeListener {
        void onQuantityChange(int position, int newQuantity);
    }

    public CartAdapter(List<ProductResponse> cartList, Context context, OnCartUpdateListener cartUpdateListener, OnQuantityChangeListener quantityChangeListener) {
        this.cartList = cartList;
        this.context = context;
        this.cartUpdateListener = cartUpdateListener;
        this.quantityChangeListener = quantityChangeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductResponse product = cartList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("%.0fđ", product.getPrice()));
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));

        Glide.with(context).load(product.getImageUrl()).into(holder.productImage);

        // Xóa sản phẩm
        holder.btnDelete.setOnClickListener(v -> {
            if (cartUpdateListener != null) {
                cartUpdateListener.onItemRemove(position);
            }
        });

        // Nút tăng số lượng
        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = product.getQuantity() + 1;
            if (quantityChangeListener != null) {
                quantityChangeListener.onQuantityChange(position, newQuantity);
            }
        });

        // Nút giảm số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            if (product.getQuantity() > 1) {
                int newQuantity = product.getQuantity() - 1;
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChange(position, newQuantity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void updateData(List<ProductResponse> newCartList) {
        cartList = newCartList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage, btnDelete;
        Button btnIncrease, btnDecrease;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.title);
            productPrice = itemView.findViewById(R.id.price);
            productQuantity = itemView.findViewById(R.id.count);
            productImage = itemView.findViewById(R.id.image);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
        }
    }
}
