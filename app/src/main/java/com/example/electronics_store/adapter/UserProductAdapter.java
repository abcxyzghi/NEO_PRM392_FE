package com.example.electronics_store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.electronics_store.R;
import com.example.electronics_store.retrofit.ProductResponse;

import java.text.DecimalFormat;
import java.util.List;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.ViewHolder> {

    private Context context;
    private List<ProductResponse> productList;

    public UserProductAdapter(Context context, List<ProductResponse> productList) {
        this.context = context;
        this.productList = productList;
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

        // Định dạng giá tiền
        String formattedPrice = formatCurrency(product.getPrice());
        holder.productPrice.setText(formattedPrice);

        // Load ảnh sản phẩm
        Glide.with(context).load(product.getImageUrl()).into(holder.productImage);
    }

    private String formatCurrency(double price) {
        DecimalFormat formatter = new DecimalFormat("#,###,### VND");
        return formatter.format(price);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}