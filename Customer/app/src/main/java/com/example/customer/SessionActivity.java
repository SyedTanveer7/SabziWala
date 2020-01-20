package com.example.customer;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.emmanuelkehinde.shutdown.Shutdown;
import com.example.customer.Fragments.SessionFragment;

public class SessionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        replaceFragment(new SessionFragment());
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.session_content, fragment);
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
        if (SaveSessionCredentials.getCustomer_UserName(this).length() == 0) {
            // call Login Activity
            replaceFragment(new SessionFragment());
        } else {
            // Stay at the current activity.
            startActivity(new Intent(this, MainActivity.class));
        }
    }


}
