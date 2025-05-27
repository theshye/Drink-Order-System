package com.example.drink_order_system;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.OrderViewHolder> {

    private final ArrayList<Order> mList;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public BillAdapter(LayoutInflater layoutInflater, ArrayList<Order> list, Context context) {
        this.mList = list;
        this.mLayoutInflater = layoutInflater;
        this.mContext = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OrderViewHolder(
                mLayoutInflater.inflate(R.layout.order_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = mList.get(position);
        holder.bindBean(order);

        holder.ratingBar.setRating(order.getRating());
        holder.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                order.setRating((int) rating);
                saveOrderRating(order);
            }
        });
    }

    private void saveOrderRating(Order order) {
        try {
            String fileName = order.getOrder_number().split("_")[0] + "bill.txt"; // 假设订单号格式包含用户信息
            java.io.FileOutputStream fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            for (Order o : mList) {
                fos.write((o.toString() + "\n").getBytes());
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView_number;
        private final TextView textView_time;
        private final TextView textView_takeAway;
        private final TextView textView_cost;
        private final RatingBar ratingBar;

        OrderViewHolder(View itemView) {
            super(itemView);
            textView_number = itemView.findViewById(R.id.textView_number);
            textView_time = itemView.findViewById(R.id.textView_time);
            textView_takeAway = itemView.findViewById(R.id.textView_takeAway);
            textView_cost = itemView.findViewById(R.id.textView_cost);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        void bindBean(Order bean) {
            textView_number.setText("订单编号：" + bean.getOrder_number());
            textView_time.setText(bean.getTime());
            textView_takeAway.setText(bean.getTakeAway().equals("1") ? "外带" : "堂食");
            textView_cost.setText("总价：￥ " + bean.getCost());
        }
    }
}