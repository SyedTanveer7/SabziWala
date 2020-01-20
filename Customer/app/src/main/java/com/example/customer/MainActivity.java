package com.example.customer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.emmanuelkehinde.shutdown.Shutdown;
import com.example.customer.Fragments.CartSummaryFragment;
import com.example.customer.Fragments.HomeFragment;
import com.example.customer.Fragments.RecentOrdersFragment;
import com.example.customer.Fragments.SettingsFragment;
import com.example.customer.Webservices.Models.User;

import static com.example.customer.Fragments.LoginFragment.loggedinUser;


public class MainActivity extends AppCompatActivity {


    public static TextView toolbarTitle;
    public static Toolbar toolbar;
    private Fragment fragment = null;
    public static BottomNavigationView navigation;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .READ_EXTERNAL_STORAGE};
    public static Context context;
    public static Activity activity;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbarTitle.setText(R.string.sabzi_wala);
                    fragment = new HomeFragment();
                    replaceFragment(fragment);
                    return true;
                case R.id.navigation_recent_orders:
                    toolbarTitle.setText(R.string.recent_orders);
                    fragment = new RecentOrdersFragment();
                    replaceFragment(fragment);
                    return true;
                case R.id.navigation_cart:
                    toolbarTitle.setText(R.string.sabzi_wala);
                    fragment = new CartSummaryFragment();
                    replaceFragment(fragment);
                    return true;
                case R.id.navigation_account:
                    toolbarTitle.setText(R.string.my_account);
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

        context = getApplicationContext();
        activity = MainActivity.this;
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (SaveSessionCredentials.getCustomer_UserName(this).length() == 0) {
            SaveSessionCredentials.setCustomer(this, loggedinUser);
        } else {
            User a = new User();
            a.setId(SaveSessionCredentials.getCustomer_ID(this));
            a.setName(SaveSessionCredentials.getCustomer_Name(this));
            a.setUsername(SaveSessionCredentials.getCustomer_UserName(this));
            a.setPassword(SaveSessionCredentials.getCustomer_Password(this));
            a.setMobile(SaveSessionCredentials.getCustomer_Mobile(this));
            a.setAddress(SaveSessionCredentials.getCustomer_Address(this));
            loggedinUser = a;
        }


        replaceFragment(new HomeFragment());
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

}
