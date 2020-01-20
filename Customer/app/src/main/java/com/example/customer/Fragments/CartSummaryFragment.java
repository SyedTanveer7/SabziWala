package com.example.customer.Fragments;


import android.app.ProgressDialog;
import android.location.LocationListener;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customer.Adapters.CartSummaryAdapter;
import com.example.customer.R;
import com.example.customer.Webservices.Models.Invoice;
import com.example.customer.Webservices.Models.NearestShopLocation;
import com.example.customer.Webservices.Models.OrderedProducts;
import com.example.customer.Webservices.Models.Product;
import com.example.customer.Webservices.RetrofiltClient;
import com.example.customer.Webservices.WebAPIs;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.example.customer.Adapters.ShopProductsAdapter.selectedProducts;
import static com.example.customer.Fragments.HomeFragment.longitude;
import static com.example.customer.Fragments.HomeFragment.latitude;
import static com.example.customer.Fragments.HomeFragment.shopProductss;
import static com.example.customer.Fragments.LoginFragment.loggedinUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartSummaryFragment extends Fragment {

    private RecyclerView cartList;
    public static TextView productsQty;
    public static TextView totalBill;
    private Button checkOut;
    public static TextView cartEmptyText;
    public static double bill = 0.0;
    public static int productItemsSize = 0;
    public List<OrderedProducts> orderedProducts = new ArrayList<>();
    String currentTime = "", currentDate = "";
    RelativeLayout transparent_layer, progressDialog;
    CamomileSpinner progressBar;

    public CartSummaryFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart_summary, container, false);
        initializeViews(view);
        Log.i("locationL", latitude + longitude + "");
        return view;
    }

    public void initializeViews(final View view) {
        // selectedProducts.clear();
        bill = 0.0;
        productItemsSize = 0;
        progressBar = view.findViewById(R.id.restaurantProgress);

        progressDialog = view.findViewById(R.id.progressDialog);
        transparent_layer = view.findViewById(R.id.transparent_layer);
        cartList = view.findViewById(R.id.recyclerView1);
        productsQty = view.findViewById(R.id.textView6);
        totalBill = view.findViewById(R.id.textView8);
        checkOut = view.findViewById(R.id.button2);
        cartEmptyText = view.findViewById(R.id.textView21);

        productItemsSize = selectedProducts.size();
        Log.i("size", productItemsSize + "");
        if (productItemsSize == 0) {
            cartEmptyText.setVisibility(View.VISIBLE);
        } else {
            cartEmptyText.setVisibility(View.GONE);

            CartSummaryAdapter adapter = new CartSummaryAdapter(selectedProducts, getActivity());
            cartList.setHasFixedSize(true);
            cartList.setLayoutManager(new LinearLayoutManager(getActivity()));
            cartList.setItemAnimator(new DefaultItemAnimator());
            cartList.setAdapter(adapter);


            for (int i = 0; i < productItemsSize; i++) {
                bill += Double.valueOf(selectedProducts.get(i).getPrice()) * Integer.valueOf(selectedProducts.get(i).getQuantity());
            }

            totalBill.setText("Rs " + bill + "/-");
            productsQty.setText(productItemsSize + "");
            checkOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (Math.abs(bill) < 300) {
                        Snackbar.make(view,"Sorry for Inconvenience! Minimum order should be 300",Snackbar.LENGTH_LONG).show();
                    } else {
                        Invoice invoice = calculateInvoice();
                        sendCustomerOrder(invoice);
                    }
                }


            });

        }
    }


    public void sendCustomerOrder(Invoice invoice) {
        progressBar.start();
        transparent_layer.setVisibility(View.VISIBLE);
        progressDialog.setVisibility(View.VISIBLE);

        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);

        Call<Invoice> apiCall = webAPIs.sendOrderInvoice(invoice);

        apiCall.enqueue(new Callback<Invoice>() {
            @Override
            public void onResponse(Call<Invoice> call, Response<Invoice> response) {
                if (response.isSuccessful()) {

//                    dialog.dismiss();
                    progressDialog.setVisibility(View.GONE);
                    transparent_layer.setVisibility(View.GONE);

                    new iOSDialogBuilder(getActivity())
                            .setTitle("Message")
                            .setSubtitle("Order Sent Successfully!")
                            .setBoldPositiveLabel(true)
                            .setCancelable(false)
                            .setPositiveListener("Okay", new iOSDialogClickListener() {
                                @Override
                                public void onClick(iOSDialog dialog) {
                                    selectedProducts.clear();
                                    dialog.dismiss();
                                    ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.main_content, new CartSummaryFragment())
                                            .commit();

                                }
                            })
                            .build().show();

                }
            }

            @Override
            public void onFailure(Call<Invoice> call, Throwable t) {
                //   dialog.dismiss();
                progressDialog.setVisibility(View.GONE);
                transparent_layer.setVisibility(View.GONE);
                Log.i("ErrorMessage", t.getMessage());
                Toast.makeText(getActivity(), "Internet Problem!Please Try Later", Toast.LENGTH_SHORT).show();

            }
        });

    }


    public Invoice calculateInvoice() {
        getCurrentDateAndTime();
        for (int i = 0; i < selectedProducts.size(); i++) {
            OrderedProducts orderedProduct = new OrderedProducts();
            orderedProduct.setProductId(Integer.parseInt(selectedProducts.get(i).getId()));
            orderedProduct.setProductQuantity(Integer.parseInt(selectedProducts.get(i).getQuantity()));
            orderedProducts.add(orderedProduct);
        }


        Invoice invoice = new Invoice();
        invoice.setDate(currentDate);
        invoice.setTime(currentTime);
        invoice.setStatus("Pending");
        invoice.setTotalBill(String.valueOf(bill));
        invoice.setTotalProducts(productItemsSize);
        invoice.setUserId(Integer.parseInt(loggedinUser.getId()));
        invoice.setVendorId(shopProductss.get(0).getShopName());
        invoice.setProducts(orderedProducts);

        if (!(longitude.equalsIgnoreCase("")) && !(latitude.equalsIgnoreCase(""))) {
            invoice.setCustomerLatitude(latitude);
            invoice.setCustomerLongitude(longitude);
        }

        return invoice;

    }


    public void getCurrentDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        int x = hour / 12;
        int mod = hour;
        String s = "AM";
        if (x > 0) {
            mod = hour % 12;
            s = "PM";
        }
        if (mod == 0) {
            mod = 12;
        }
        if (minute < 10) {
            currentTime = mod + ":0" + minute + " " + s;
        } else {
            currentTime = mod + ":" + minute + " " + s;
        }

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        currentDate = day + "/" + (month + 1) + "/" + year;

    }


}
