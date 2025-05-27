package com.example.drink_order_system;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class BillFragment extends Fragment {

    private String userName;
    private ArrayList<Order> orders = new ArrayList<>();
    private RecyclerView order_listView;
    private View view;
    private LinearLayoutManager llM;
    private LayoutInflater layoutInflater;
    private TextView averageRatingTextView;

    public BillFragment() {
        // Required empty public constructor
    }

    public static BillFragment newInstance(String userName) {
        BillFragment fragment = new BillFragment();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString("userName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater;
        view = inflater.inflate(R.layout.fragment_bill, container, false);
        order_listView = view.findViewById(R.id.RV_bill);
        llM = new LinearLayoutManager(getActivity());
        order_listView.setLayoutManager(llM);

        averageRatingTextView = view.findViewById(R.id.average_rating_text_view);

        loadOrders();
        calculateAndDisplayAverageRating();

        BillAdapter billAdapter = new BillAdapter(layoutInflater, orders, getActivity());
        order_listView.setAdapter(billAdapter);
        return view;
    }

    private void loadOrders() {
        try {
            FileInputStream fis = getActivity().openFileInput(userName + "bill.txt");
            Reader in = new InputStreamReader(fis, StandardCharsets.UTF_8);
            LineNumberReader reader = new LineNumberReader(in);
            String s;
            while ((s = reader.readLine()) != null) {
                s = s.replace("/n", "");
                orders.add(new Order(s));
            }
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateAndDisplayAverageRating() {
        int totalRating = 0;
        int ratedOrders = 0;
        for (Order order : orders) {
            if (order.getRating() > 0) {
                totalRating += order.getRating();
                ratedOrders++;
            }
        }
        float averageRating = ratedOrders > 0 ? (float) totalRating / ratedOrders : 0;
        averageRatingTextView.setText("该店平均星级：" + String.format("%.2f", averageRating) + "⭐");
    }

    private void refresh() {
        orders.clear();
        loadOrders();
        calculateAndDisplayAverageRating();
        BillAdapter billAdapter = new BillAdapter(layoutInflater, orders, getActivity());
        order_listView.setAdapter(billAdapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refresh();
        }
    }
}