package com.example.vendor;

import android.util.Log;

import com.example.vendor.Webservices.Models.Order;
import com.example.vendor.Webservices.RetrofiltClient;
import com.example.vendor.Webservices.WebAPIs;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.example.vendor.Fragments.LoginFragment.vendor;

public class FirebaseIntentIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
    //    registerToken(token);
        Log.i("Toke",token);

    }

    private void registerToken(String token) {





        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .add("shopId", vendor.getShopId() + "")
                .build();

        Request request = new Request.Builder()
                .url("http://viceless-fumes.000webhostapp.com/WebApis/registerToken.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
