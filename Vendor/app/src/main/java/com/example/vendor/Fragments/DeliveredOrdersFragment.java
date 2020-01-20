package com.example.vendor.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vendor.Adapters.OrdersAdapter;
import com.example.vendor.R;
import com.example.vendor.Webservices.Models.Order;

import java.util.ArrayList;
import java.util.List;

import static com.example.vendor.Fragments.OrdersFragment.allCustomerOrders;
import static com.example.vendor.Fragments.OrdersFragment.selectedCustomerOrders;
import static com.example.vendor.Fragments.OrdersFragment.selectedTab;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveredOrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Order> deliveredOrders = new ArrayList<>();
    ProgressDialog dialog;

    public DeliveredOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivered_orders, container, false);
        initializeEvents(view);
        return view;
    }

    public void initializeEvents(View v) {

        deliveredOrders.clear();
        dialog = new ProgressDialog(getActivity());
        recyclerView = v.findViewById(R.id.recyclerView);


        for (int i = 0; i < allCustomerOrders.size(); i++) {

            if (allCustomerOrders.get(i).getStatus().equals("Delivered"))
                deliveredOrders.add(allCustomerOrders.get(i));
        }

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        OrdersAdapter adapter = new OrdersAdapter(deliveredOrders, getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



    }


}
