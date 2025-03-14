package com.example.electronics_store.adapter;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronics_store.R;
import com.example.electronics_store.activity.OrderDetailActivity;
import com.example.electronics_store.model.OrderDetail;
import com.example.electronics_store.model.Product;
import com.example.electronics_store.retrofit.OrderResponse;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.List;
public class OrderManagementAdapter extends RecyclerView.Adapter<OrderManagementAdapter.ViewHolder> {
    private List<OrderResponse> orderList;

    public OrderManagementAdapter(List<OrderResponse> orderList) {
        this.orderList = orderList;
    }

    public void setOrderList(List<OrderResponse> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderResponse order = orderList.get(position);
        holder.txtOrderId.setText("Mã đơn: " + order.getId());
        holder.txtUserId.setText("Mã khách: " + order.getUserId());
        holder.txtTotalPrice.setText("Tổng tiền: " + order.getTotalPrice() + " VNĐ");
        holder.txtStatus.setText("Trạng thái: " + order.getStatus());
        holder.txtCreatedAt.setText("Ngày tạo: " + order.getCreatedAt());
        holder.btnViewDetails.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), OrderDetailActivity.class);
            intent.putExtra("orderId", order.getId());
            v.getContext().startActivity(intent);
        });
    StringBuilder productDetails = new StringBuilder();
    if (order.getOrderDetails() != null) {
        for (OrderDetail detail : order.getOrderDetails()) {
            Product product = detail.getProduct();
            if (product != null) {
                productDetails.append("Tên: ").append(product.getName()).append("\n");
                productDetails.append("Số lượng: ").append(detail.getQuantity()).append("\n");
                productDetails.append("Giá: ").append(detail.getPrice()).append(" VNĐ\n\n");
            }
        }
    }
    holder.txtProducts.setText(productDetails.toString());
}
    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtUserId, txtTotalPrice, txtStatus, txtCreatedAt, txtProducts;
        Button btnViewDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.Order_txtId);
            txtUserId = itemView.findViewById(R.id.Order_txtUserId);
            txtTotalPrice = itemView.findViewById(R.id.Order_txtTotalPrice);
            txtStatus = itemView.findViewById(R.id.Order_txtStatus);
            txtCreatedAt = itemView.findViewById(R.id.Order_txtCreatedDate);
            txtProducts = itemView.findViewById(R.id.Order_txtProducts);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }

}
