package com.example.vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.emmanuelkehinde.shutdown.Shutdown;
import com.example.vendor.Fragments.LoginFragment;
import com.example.vendor.Fragments.OrdersFragment;
import com.example.vendor.Fragments.ReportsFragment;
import com.example.vendor.Fragments.SettingsFragment;
import com.example.vendor.Webservices.Models.Order;
import com.example.vendor.Webservices.Models.Token;
import com.example.vendor.Webservices.Models.Vendor;
import com.example.vendor.Webservices.RetrofiltClient;
import com.example.vendor.Webservices.WebAPIs;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.vendor.Fragments.LoginFragment.vendor;

public class MainActivity extends AppCompatActivity {

    public static TextView toolbarTitle;
    public static Toolbar toolbar;
    private Fragment fragment = null;
    public static BottomNavigationView navigation;
    public static String tokenId = "";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_orders:
                    toolbarTitle.setText(R.string.title_orders);
                    fragment = new OrdersFragment();
                    replaceFragment(fragment);
                    return true;
                case R.id.navigation_reports:
                    toolbarTitle.setText(R.string.title_reports);
                    fragment = new ReportsFragment();
                    replaceFragment(fragment);
                    return true;
                case R.id.navigation_settings:
                    toolbarTitle.setText(R.string.title_settings);
                    fragment = new SettingsFragment();
                    replaceFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);

        replaceFragment(new OrdersFragment());
        navigation = findViewById(R.id.navigation);
        navigation.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FirebaseMessaging.getInstance().subscribeToTopic("SabziWala");
        String t = FirebaseInstanceId.getInstance().getToken();
        Log.i("tyu", t);
        tokenId = t;

        if (SaveSessionCredentials.getShopName(this).length() == 0) {
            SaveSessionCredentials.setVendor(this, vendor, t);
        } else {
            Vendor v = new Vendor();
            v.setShopId(SaveSessionCredentials.getShopId(this));
            v.setShopName(SaveSessionCredentials.getShopName(this));
            v.setVendorEmail(SaveSessionCredentials.getVendorName(this));
            v.setImage(SaveSessionCredentials.getImage(this));
            v.setLongitude(SaveSessionCredentials.getLongitude(this));
            v.setLattitude(SaveSessionCredentials.getLatitude(this));
            v.setLocation(SaveSessionCredentials.getLocation(this));
            v.setMobile(SaveSessionCredentials.getMobile(this));
            v.setComission(SaveSessionCredentials.getComision(this));
            v.setPassword(SaveSessionCredentials.getPassword(this));
            tokenId = SaveSessionCredentials.getToken(this);
            vendor = v;
        }
        registerToken();
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        Shutdown.now(this, "Press back again to Exit", 2000);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_content);
        fragment.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void registerToken() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();

        Token t = new Token();
        t.setToken(tokenId);
        t.setShopId(SaveSessionCredentials.getShopId(this));

        Log.i("Test", tokenId + "\n" + vendor.getShopId());


        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Token> apiCall = webAPIs.registerToken(t);

        apiCall.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    if (response.body().getToken().equalsIgnoreCase("failed")) {
                        Log.i("test", "no");
                        dialog.dismiss();
                    } else {
                        Log.i("test", "yes");
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                dialog.dismiss();
                Log.i("test", t.getMessage());

            }
        });


    }


}
