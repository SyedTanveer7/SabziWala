package com.example.vendor.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.SaveSessionCredentials;
import com.example.vendor.SessionActivity;
import com.example.vendor.Webservices.Models.Token;
import com.example.vendor.Webservices.Models.Vendor;
import com.example.vendor.Webservices.RetrofiltClient;
import com.example.vendor.Webservices.WebAPIs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.vendor.Fragments.LoginFragment.vendor;
import static com.example.vendor.MainActivity.navigation;
import static com.example.vendor.MainActivity.tokenId;
import static com.example.vendor.MainActivity.toolbar;
import static com.example.vendor.MainActivity.toolbarTitle;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    private TextView vendorText;
    private TextView nameText;
    private TextView profileText;
    private TextView logOutText;
    SaveSessionCredentials credentials;

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
        credentials = new SaveSessionCredentials();
        vendorText = v.findViewById(R.id.text_admin);
        nameText = v.findViewById(R.id.text_name);
        profileText = v.findViewById(R.id.text_Profile);
        logOutText = v.findViewById(R.id.text_logOut);

        nameText.setText(vendor.getShopName());

        vendorText.setOnClickListener(new View.OnClickListener() {
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

                deleteToken();

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

    public void deleteToken() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Logging Out");
        dialog.show();
        Token t = new Token();
        t.setToken(tokenId);
        t.setShopId(vendor.getShopId());
        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Token> apiCall = webAPIs.deleteToken(t);

        apiCall.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    tokenId = "";
                    vendor = new Vendor();
                    SaveSessionCredentials.setVendor(getActivity(), vendor, "");
                    dialog.dismiss();
                    startActivity(new Intent(getActivity(), SessionActivity.class));
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(),"Internet Problem! Please Try Later",Toast.LENGTH_SHORT).show();
            }
        });


    }


}
