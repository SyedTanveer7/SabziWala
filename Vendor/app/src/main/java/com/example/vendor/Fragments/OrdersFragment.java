package com.example.vendor.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vendor.Adapters.OrdersAdapter;
import com.example.vendor.Adapters.ViewPagerAdapter;
import com.example.vendor.R;
import com.example.vendor.SaveSessionCredentials;
import com.example.vendor.Webservices.Models.Order;
import com.example.vendor.Webservices.Models.Vendor;
import com.example.vendor.Webservices.RetrofiltClient;
import com.example.vendor.Webservices.WebAPIs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.vendor.Fragments.LoginFragment.vendor;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;
    ProgressDialog dialog;
    public static String selectedTab = "Pending";
    public static List<Order> allCustomerOrders = new ArrayList<>();


    public static List<Order> selectedCustomerOrders = new ArrayList<>();


    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        initializeEvents(view);
        return view;
    }


    public void initializeEvents(View view) {

        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tab);
        dialog = new ProgressDialog(getActivity());
        allCustomerOrders.clear();


        getOrdersFromServer();


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPagerAdapter.addFragment(new PendingOrdersFragment(), "Requests");
        viewPagerAdapter.addFragment(new InProgressOrdersFragment(), "Processing");
        viewPagerAdapter.addFragment(new DeliveredOrdersFragment(), "Delivered");
        viewPager.setAdapter(viewPagerAdapter);
    }


    public void getOrdersFromServer() {

        dialog.show();
        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<List<Order>> apiCall = webAPIs.getCustomerOrders(vendor);
        apiCall.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    allCustomerOrders = response.body();

                    setupViewPager(viewPager);
                    tabLayout.setupWithViewPager(viewPager);
                    dialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.i("failureMessage", t.getMessage());
            }
        });


    }


}
