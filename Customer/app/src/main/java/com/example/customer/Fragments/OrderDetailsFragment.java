package com.example.customer.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.customer.Adapters.OrderDetailsAdapter;
import com.example.customer.Adapters.OrdersHistoryAdapter;
import com.example.customer.R;
import com.example.customer.Webservices.Models.OrderDetails;
import com.example.customer.Webservices.Models.User;
import com.example.customer.Webservices.RetrofiltClient;
import com.example.customer.Webservices.WebAPIs;
import com.gmail.samehadar.iosdialog.CamomileSpinner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.customer.Adapters.OrdersHistoryAdapter.selectedOrderId;
import static com.example.customer.MainActivity.toolbarTitle;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment {

    public TextView orderId, date, shopName, location, totalBill, back;
    ImageView backArrow;
    RecyclerView orderDetailsList;
    ProgressDialog dialog;
    ConstraintLayout constraintLayout;

    public static RelativeLayout transparent_layer, progressDialog;
    CamomileSpinner progressBar;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        initializeViews(view);
        return view;
    }

    public void initializeViews(View view) {
        progressBar = view.findViewById(R.id.restaurantProgress);
        progressBar.start();
        progressDialog = view.findViewById(R.id.progressDialog);
        transparent_layer = view.findViewById(R.id.transparent_layer);
        orderId = view.findViewById(R.id.textView38);
        date = view.findViewById(R.id.textView39);
        shopName = view.findViewById(R.id.textView41);
        location = view.findViewById(R.id.textView45);
        totalBill = view.findViewById(R.id.textView47);
        orderDetailsList = view.findViewById(R.id.recycler_view_order_details);
        constraintLayout = view.findViewById(R.id.detials);
        back = view.findViewById(R.id.textView49);
        backArrow = view.findViewById(R.id.imageView18);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new RecentOrdersFragment())
                        .commit();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, new RecentOrdersFragment())
                        .commit();
            }
        });

        toolbarTitle.setText("Customer Invoice");
        getOrderDetails(selectedOrderId);
    }

    public void getOrderDetails(final String ordeId) {


        final OrderDetails details = new OrderDetails();
        details.setOrderId(ordeId);

        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<OrderDetails> apiCall = webAPIs.getOrderDetails(details);
        apiCall.enqueue(new Callback<OrderDetails>() {
            @Override
            public void onResponse(Call<OrderDetails> call, Response<OrderDetails> response) {

                if (response.isSuccessful()) {
                    orderId.setText(String.valueOf(response.body().getOrderId()));
                    date.setText(response.body().getDate());
                    shopName.setText(response.body().getShopName());
                    location.setText(response.body().getShopLocation());
                    totalBill.setText(response.body().getTotalBill());

                    OrderDetailsAdapter adapter = new OrderDetailsAdapter(response.body().getProducts(), getActivity());
                    orderDetailsList.setHasFixedSize(true);
                    orderDetailsList.setLayoutManager(new LinearLayoutManager(getActivity()));
                    orderDetailsList.setItemAnimator(new DefaultItemAnimator());
                    orderDetailsList.setAdapter(adapter);
                    progressDialog.setVisibility(View.GONE);
                    transparent_layer.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<OrderDetails> call, Throwable t) {
                Log.i("ErrorMessage", t.getMessage());
                Snackbar snackbar1 = Snackbar.make(constraintLayout, "Internet Problem! Please Try Later", Snackbar.LENGTH_LONG);
                snackbar1.show();
                progressDialog.setVisibility(View.GONE);
                transparent_layer.setVisibility(View.GONE);

            }
        });

    }


}
