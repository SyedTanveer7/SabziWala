package com.example.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.admin.Fragments.LoginFragment;

public class SessionActivity extends AppCompatActivity {

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        context = getApplicationContext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SaveSessionCredentials.getAdmin_UserName(this).length() == 0) {
            // call Login Activity
            replaceFragment(new LoginFragment());
        } else {
            // Stay at the current activity.
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.session_content, fragment);
        fragmentTransaction.commit();
    }


}
