package com.example.customer.Webservices.Repositories;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.example.customer.Adapters.ShopProductsAdapter;
import com.example.customer.Webservices.Models.NearestShopLocation;
import com.example.customer.Webservices.Models.Product;
import com.example.customer.Webservices.RetrofiltClient;
import com.example.customer.Webservices.WebAPIs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    private List<Product> products;
    private List<Product> shopProducts;
    WebAPIs webAPIs;
    private Context context;
    ProgressDialog dialog;

    public ProductRepository(Context context) {
        products = new ArrayList<>();
        shopProducts = new ArrayList<>();
        this.context = context;
        dialog = new ProgressDialog(context);
    }

    public void getShopProductsFromServer(String latitude, String longitude,final RecyclerView recyclerView) {

        dialog.setMessage("Please Wait");
        dialog.show();
        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<List<Product>> apiCall = webAPIs.getNearestShopProducts(new NearestShopLocation(latitude,longitude));
        apiCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    products = response.body();
                    recyclerView.setHasFixedSize(true);
                    ShopProductsAdapter adapter = new ShopProductsAdapter(products, context);
                    recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                    dialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.i("failureMessage", t.getMessage());
            }
        });


    }




}
