package com.example.customer.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.customer.R;
import com.example.customer.SaveSessionCredentials;
import com.example.customer.SessionActivity;
import com.example.customer.Webservices.Models.User;


import static com.example.customer.Fragments.LoginFragment.loggedinUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    TextView logOut, textProfile, textName;
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
        initializeViews(v);
        return v;
    }

    public void initializeViews(View view) {
        logOut = view.findViewById(R.id.text_logOut);
        textProfile = view.findViewById(R.id.text_Profile);

        textName = view.findViewById(R.id.text_name);

        textName.setText(loggedinUser.getName());

        textProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProfileFragment());
            }
        });

        textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProfileFragment());
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  replaceFragment(new LoginFragment());
//                toolbar.setVisibility(View.GONE);
//                selectedProducts.clear();
//                bill = 0.0;
//                productItemsSize = 0;
//                navigation.setVisibility(View.GONE);


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

                        loggedinUser = new User();
                        SaveSessionCredentials.setCustomer(getActivity(), loggedinUser);
                        Intent intent = new Intent(getActivity(), SessionActivity.class);

                        //getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
                        startActivity(intent);
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
