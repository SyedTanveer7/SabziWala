package com.example.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.emmanuelkehinde.shutdown.Shutdown;
import com.example.admin.Fragments.HomeFragment;
import com.example.admin.Fragments.LoginFragment;
import com.example.admin.Fragments.ProductsFragment;
import com.example.admin.Fragments.ReportsFragment;
import com.example.admin.Fragments.SettingsFragment;
import com.example.admin.Fragments.VendorsFragment;
import com.example.admin.Webservices.Models.Admin;

import static com.example.admin.Fragments.LoginFragment.admin;

public class MainActivity extends AppCompatActivity {

    public static TextView toolbarTitle;
    public static Toolbar toolbar;
    private Fragment fragment = null;
    public static BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbarTitle.setText(R.string.title_home);
                    fragment = new HomeFragment();
                    replaceFragment(fragment);
                    return true;
                case R.id.navigation_products:
                    toolbarTitle.setText(R.string.title_products);
                    fragment = new ProductsFragment();
                    replaceFragment(fragment);
                    return true;
                case R.id.navigation_vendors:
                    toolbarTitle.setText(R.string.title_vendors);
                    fragment = new VendorsFragment();
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
        replaceFragment(new HomeFragment());
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        if (SaveSessionCredentials.getAdmin_UserName(this).length() == 0) {
            SaveSessionCredentials.setAdmin(this, admin);
        } else {
            Admin a = new Admin();
            a.setId(SaveSessionCredentials.getAdmin_ID(this));
            a.setName(SaveSessionCredentials.getAdmin_Name(this));
            a.setUserName(SaveSessionCredentials.getAdmin_UserName(this));
            a.setPassword(SaveSessionCredentials.getAdmin_Password(this));
            a.setMobile(SaveSessionCredentials.getAdmin_Mobile(this));
            a.setAddress(SaveSessionCredentials.getAdmin_Address(this));
            admin = a;
        }


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
