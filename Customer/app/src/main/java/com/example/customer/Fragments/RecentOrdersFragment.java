package com.example.customer.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customer.Adapters.CartSummaryAdapter;
import com.example.customer.Adapters.OrdersHistoryAdapter;
import com.example.customer.R;
import com.example.customer.Webservices.Models.RecentOrder;
import com.example.customer.Webservices.Models.User;
import com.example.customer.Webservices.RetrofiltClient;
import com.example.customer.Webservices.WebAPIs;
import com.gmail.samehadar.iosdialog.CamomileSpinner;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.customer.Adapters.ShopProductsAdapter.selectedProducts;
import static com.example.customer.Fragments.LoginFragment.loggedinUser;
import static com.example.customer.MainActivity.toolbarTitle;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecentOrdersFragment extends Fragment {

    RecyclerView recyclerView;
    TextView noOrders;
    RelativeLayout transparent_layer,progressDialog;
    CamomileSpinner progressBar;

    public RecentOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent_orders, container, false);
        initializeEvents(view);
        return view;
    }

    public void initializeEvents(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.restaurantProgress);
        progressBar.start();
        progressDialog = view.findViewById(R.id.progressDialog);
        transparent_layer = view.findViewById(R.id.transparent_layer);
        noOrders = view.findViewById(R.id.textView21);
        RecentOrder order = new RecentOrder();
        order.setUserId(loggedinUser.getId());
        getRecentOrders(order);
        toolbarTitle.setText(R.string.recent_orders);

    }

    public void getRecentOrders(RecentOrder recentOrders) {
//        final ProgressDialog dialog = new ProgressDialog(getActivity());
//        dialog.setMessage("Loading..");
//        dialog.show();
        transparent_layer.setVisibility(View.VISIBLE);
        progressDialog.setVisibility(View.VISIBLE);

        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<List<RecentOrder>> apiCall = webAPIs.getOrdersHistory(recentOrders);
        apiCall.enqueue(new Callback<List<RecentOrder>>() {
            @Override
            public void onResponse(Call<List<RecentOrder>> call, Response<List<RecentOrder>> response) {


                if (response.isSuccessful()) {


                    List<RecentOrder> recentOrders = response.body();

                    if (recentOrders.get(0).getShopName().equalsIgnoreCase("no")) {
                        noOrders.setVisibility(View.VISIBLE);

                    } else {
                        noOrders.setVisibility(View.GONE);
                        OrdersHistoryAdapter adapter = new OrdersHistoryAdapter(recentOrders, getActivity());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);


                    }
                   // dialog.dismiss();
                    progressDialog.setVisibility(View.GONE);
                    transparent_layer.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<RecentOrder>> call, Throwable t) {
                Log.i("ErrorMessage", t.getMessage());
                Toast.makeText(getActivity(), "Internet Problem!Please Try Later", Toast.LENGTH_SHORT).show();
             //   dialog.dismiss();
                progressDialog.setVisibility(View.GONE);
                transparent_layer.setVisibility(View.GONE);
            }
        });

    }


}
