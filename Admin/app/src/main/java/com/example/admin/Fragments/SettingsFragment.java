package com.example.admin.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.R;
import com.example.admin.SaveSessionCredentials;
import com.example.admin.SessionActivity;
import com.example.admin.Webservices.Models.Admin;

import static com.example.admin.Fragments.LoginFragment.admin;
import static com.example.admin.MainActivity.navigation;
import static com.example.admin.MainActivity.toolbar;
import static com.example.admin.MainActivity.toolbarTitle;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    private TextView adminText;
    private TextView nameText;
    private TextView profileText;
    private TextView logOutText;

    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        initializeEvents(v);

        return v;
    }

    private void initializeEvents(View v) {
        adminText = v.findViewById(R.id.text_admin);
        nameText = v.findViewById(R.id.text_name);
        profileText = v.findViewById(R.id.text_Profile);
        logOutText = v.findViewById(R.id.text_logOut);

        nameText.setText(admin.getName());

        adminText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProfileFragment());
                toolbarTitle.setText(R.string.title_profile);
            }
        });


        nameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProfileFragment());
                toolbarTitle.setText(R.string.title_profile);
            }
        });

        profileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProfileFragment());
                toolbarTitle.setText(R.string.title_profile);
            }
        });


        logOutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Logging Out.");
                dialog.show();


                // Start lengthy operation in a background thread
                new Thread(new Runnable() {
                    public void run() {
                        while (mProgressStatus < 60) {

                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            mProgressStatus += 1;

                            // Update the progress bar
                            mHandler.post(new Runnable() {
                                public void run() {

                                }
                            });
                        }


                        dialog.dismiss();


                        admin = new Admin();
                        SaveSessionCredentials.setAdmin(getActivity(), admin);
                        startActivity(new Intent(getActivity(), SessionActivity.class));
                        getActivity().finish();

                    }


                }).start();


            }
        });

    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.commit();
    }


}
