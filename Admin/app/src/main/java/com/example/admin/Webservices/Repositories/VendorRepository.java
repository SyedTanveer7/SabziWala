package com.example.admin.Webservices.Repositories;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.admin.Adapters.VendorsAdapter;
import com.example.admin.Fragments.VendorsFragment;
import com.example.admin.R;
import com.example.admin.Webservices.Models.Vendor;
import com.example.admin.Webservices.RetrofiltClient;
import com.example.admin.Webservices.WebAPIs;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.Fragments.NewProductFragment.shops;
import static com.example.admin.Fragments.NewVendorFragment.progressDialog1;
import static com.example.admin.Fragments.ProductsFragment.progressDialog;
import static com.example.admin.Fragments.ProductsFragment.transparent_layer;
import static com.example.admin.Fragments.VendorsFragment.vprogressDialog;
import static com.example.admin.Fragments.VendorsFragment.vtransparent_layer;

public class VendorRepository {

    private Context context;
    private WebAPIs webAPIs;
    private List<Vendor> vendors;
    ProgressDialog dialog;


    public VendorRepository(Context context) {
        vendors = new ArrayList<>();
        this.context = context;
        dialog = new ProgressDialog(context);
    }


    public void getVendorsFromServer(final RecyclerView recyclerView) {


//        dialog.setMessage("Please Wait");
//        dialog.show();
        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<List<Vendor>> apiCall = webAPIs.getVendors();
        apiCall.enqueue(new Callback<List<Vendor>>() {
            @Override
            public void onResponse(Call<List<Vendor>> call, Response<List<Vendor>> response) {
                if (response.isSuccessful()) {
                    vendors = response.body();

                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(mLayoutManager);
                    VendorsAdapter adapter = new VendorsAdapter(vendors, context);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                    //  dialog.dismiss();
                    vprogressDialog.setVisibility(View.GONE);
                    vtransparent_layer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Vendor>> call, Throwable t) {
                Log.i("failureMessage", t.getMessage());
                vprogressDialog.setVisibility(View.GONE);
                vtransparent_layer.setVisibility(View.GONE);
            }
        });


    }

    public void getShopsFromServer() {

//        dialog.setMessage("Please Wait");
//        dialog.show();
        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<List<Vendor>> apiCall = webAPIs.getVendors();
        apiCall.enqueue(new Callback<List<Vendor>>() {
            @Override
            public void onResponse(Call<List<Vendor>> call, Response<List<Vendor>> response) {
                if (response.isSuccessful()) {
                    shops.clear();
                    vendors = response.body();
                    shops.add("Select Shop");
                    for (int i = 0; i < vendors.size(); i++) {
                        shops.add(vendors.get(i).getShopName());
                    }

                    //   dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Vendor>> call, Throwable t) {
                Log.i("failureMessage", t.getMessage());
                // dialog.dismiss();
            }
        });


    }


    public void addVendorInServer(Vendor vendor) {

        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Vendor> apiCall = webAPIs.addVendor(vendor);

        apiCall.enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(Call<Vendor> call, Response<Vendor> response) {
                if (response.isSuccessful()) {
                    progressDialog1.dismiss();
                    Toast.makeText(context, "Vendor Added Successfully!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Vendor> call, Throwable t) {
                Log.i("failureMessage", t.getMessage());
                progressDialog1.dismiss();
            }
        });

    }

    public void updateVendorInServer(Vendor vendor) {

        dialog.setMessage("Please Wait");
        dialog.show();
        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Vendor> apiCall = webAPIs.updateVendor(vendor);
        apiCall.enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(Call<Vendor> call, Response<Vendor> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(context, "Vendor Updated Successfully!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Vendor> call, Throwable t) {
                Log.i("failureMessage", t.getMessage());
                dialog.dismiss();
            }
        });

    }

    public void deleteVendorInServer(Vendor vendor, List<Vendor> vendors, int pos) {











//        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("User");
//// Query parameters based on the item name
//        Toast.makeText(context,vendors.get(pos).getObjectId()+"",Toast.LENGTH_SHORT).show();
//        query.whereEqualTo("objectId", vendors.get(pos).getObjectId()+"");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(final List<ParseObject> object, ParseException e) {
//                if (e == null) {
//                    object.get(0).deleteInBackground(new DeleteCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e == null) {
//                                // Success
//                            } else {
//                                // Failed
//                            }
//                        }
//                    });
//                } else {
//                    // Something is wrong
//                }
//            }
//
//
//        });
//

        dialog.setMessage("Please Wait");
        dialog.show();

        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Vendor> apiCall = webAPIs.deleteVendor(vendor);
        apiCall.enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(Call<Vendor> call, Response<Vendor> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(context, "Vendor Deleted Successfully!", Toast.LENGTH_LONG).show();
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, new VendorsFragment())
                            .commit();

                }
            }

            @Override
            public void onFailure(Call<Vendor> call, Throwable t) {
                Log.i("failureMessage", t.getMessage());
                dialog.dismiss();
            }
        });

    }


}