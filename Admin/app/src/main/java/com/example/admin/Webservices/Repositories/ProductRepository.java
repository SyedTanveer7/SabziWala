package com.example.admin.Webservices.Repositories;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.admin.Adapters.ProductsAdapter;
import com.example.admin.Adapters.ShopProductsAdapter;
import com.example.admin.Fragments.ProductsFragment;
import com.example.admin.Fragments.VendorsFragment;
import com.example.admin.R;
import com.example.admin.Webservices.Models.Product;
import com.example.admin.Webservices.Models.Vendor;
import com.example.admin.Webservices.RetrofiltClient;
import com.example.admin.Webservices.WebAPIs;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.Fragments.ProductsFragment.progressDialog;
import static com.example.admin.Fragments.ProductsFragment.transparent_layer;
import static com.example.admin.Fragments.ShopProductsFragment.sprogressDialog;
import static com.example.admin.Fragments.ShopProductsFragment.stransparent_layer;

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

    public void getProductsFromServer(final RecyclerView recyclerView) {

        //  dialog.setMessage("Please Wait");
        //  dialog.show();
        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<List<Product>> apiCall = webAPIs.getProducts();
        apiCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    products = response.body();
                    recyclerView.setHasFixedSize(true);
                    ProductsAdapter adapter = new ProductsAdapter(products, context);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);


                    // recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                    recyclerView.setLayoutManager(staggeredGridLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    //dialog.dismiss();
                    progressDialog.setVisibility(View.GONE);
                    transparent_layer.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.i("failureMessage", t.getMessage());
                progressDialog.setVisibility(View.GONE);
                transparent_layer.setVisibility(View.GONE);
                Toast.makeText(context, "Error while fetching Products!", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void getGridViewProductsFromServer(final RecyclerView recyclerView, Vendor vendor) {

//        dialog.setMessage("Please Wait");
//        dialog.show();
        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<List<Product>> apiCall = webAPIs.getShopProducts(vendor);
        apiCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    shopProducts = response.body();
                    if (shopProducts.size() != 0) {
                        recyclerView.setHasFixedSize(true);
                        ShopProductsAdapter adapter = new ShopProductsAdapter(shopProducts, context);
                        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);
//                        dialog.dismiss();
                        sprogressDialog.setVisibility(View.GONE);
                        stransparent_layer.setVisibility(View.GONE);

                    } else {

                        Toast.makeText(context, "No Products available in this Shop", Toast.LENGTH_LONG).show();
                        sprogressDialog.setVisibility(View.GONE);
                        stransparent_layer.setVisibility(View.GONE);

                        //dialog.dismiss();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.i("failureMessage", t.getMessage());
                sprogressDialog.setVisibility(View.GONE);
                stransparent_layer.setVisibility(View.GONE);
                Toast.makeText(context, "Error while fetching Shop Products!", Toast.LENGTH_SHORT).show();


            }
        });


    }


    public void updateProductInServer(Product product) {
        dialog.setMessage("Updating..");
        dialog.show();
        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Product> apiCall = webAPIs.updateProduct(product);

        apiCall.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(context, "Product Updated Successfully!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.i("failureMessage", t.getMessage());
                dialog.dismiss();
                Toast.makeText(context, "Error while updating Product!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void deleteProductInServer(Product product) {

        dialog.setMessage("Deleting..");
        dialog.show();
        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Product> apiCall = webAPIs.deleteProduct(product);
        apiCall.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    //  dialog.dismiss();
                    Toast.makeText(context, "Product Deleted Successfully!", Toast.LENGTH_LONG).show();
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, new ProductsFragment())
                            .commit();
                    progressDialog.setVisibility(View.GONE);
                    transparent_layer.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.i("failureMessage", t.getMessage());
                dialog.dismiss();
                Toast.makeText(context, "Error while deleting product!", Toast.LENGTH_SHORT).show();

            }
        });

    }


}
