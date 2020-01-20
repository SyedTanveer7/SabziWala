package com.example.admin.Webservices.Repositories;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.admin.Webservices.Models.Admin;
import com.example.admin.Webservices.RetrofiltClient;
import com.example.admin.Webservices.WebAPIs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRepository {

    private Context context;
    private Admin admin;
    private WebAPIs webAPIs;

    public AdminRepository(Context context) {
        this.context = context;
    }


    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void getAdminProfileFromServer(Admin admin1) {

        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Admin> apiCall = webAPIs.getAdminPr0file(admin1);
        apiCall.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                if (response.isSuccessful()) {
                    admin = response.body();
                    Toast.makeText(context, "Admin " + admin.getName(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                Log.i("FailureMessage", t.getMessage());
            }
        });

    }

    public void updateAdminProfileInServer(Admin admin) {

        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Admin> apiCall = webAPIs.updateAdminPr0file(admin);
        apiCall.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Admin Updated Successfully!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                Log.i("FailureMessage", t.getMessage());
            }
        });

    }

    public void authenticateAdmin(Admin admin1) {
        webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Admin> apiCall = webAPIs.authenticateAdmin(admin1);

        apiCall.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                if (response.isSuccessful()) {
                    admin = response.body();

                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                Log.i("FailureMessage", t.getMessage());
            }
        });
    }

}
