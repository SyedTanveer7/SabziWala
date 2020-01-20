package com.example.vendor.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vendor.Adapters.OrdersAdapter;
import com.example.vendor.R;
import com.example.vendor.Webservices.Models.Order;

import java.util.ArrayList;
import java.util.List;

import static com.example.vendor.Fragments.OrdersFragment.allCustomerOrders;
import static com.example.vendor.Fragments.OrdersFragment.selectedTab;

/**
 * A simple {@link Fragment} subclass.
 */
public class InProgressOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Order> inProgressOrders = new ArrayList<>();

    public InProgressOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_in_progress_orders, container, false);
        initializeEvents(view);
        return view;

    }

    public void initializeEvents(View v) {

        recyclerView = v.findViewById(R.id.recyclerView);

        inProgressOrders.clear();

        for (int i = 0; i < allCustomerOrders.size(); i++) {

            if (allCustomerOrders.get(i).getStatus().equals("In Progress"))
                inProgressOrders.add(allCustomerOrders.get(i));
        }

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        OrdersAdapter adapter = new OrdersAdapter(inProgressOrders, getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


    }


}
