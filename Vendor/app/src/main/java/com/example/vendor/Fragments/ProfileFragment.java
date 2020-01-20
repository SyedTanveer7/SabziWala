package com.example.vendor.Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Webservices.Models.Vendor;
import com.example.vendor.Webservices.RetrofiltClient;
import com.example.vendor.Webservices.WebAPIs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.vendor.Fragments.LoginFragment.vendor;
import static com.example.vendor.MainActivity.toolbarTitle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private Button editButton;
    private ImageView imageBack;
    private TextView userName;
    private TextView name;
    private TextView password;
    private TextView contact;
    private TextView address;
    private ProgressDialog dialog;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        initialzieEvents(v);
        return v;
    }

    private void initialzieEvents(View v) {

        dialog = new ProgressDialog(getActivity());
        editButton = v.findViewById(R.id.button_edit);
        imageBack = v.findViewById(R.id.image_back);
        name = v.findViewById(R.id.textView);
        userName = v.findViewById(R.id.textView3);
        password = v.findViewById(R.id.textView6);
        contact = v.findViewById(R.id.textView7);
        address = v.findViewById(R.id.textView9);


        getVendorProfileFromServer(vendor);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new UpdateProfileFragment());
                toolbarTitle.setText(R.string.title_update_profile);
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.main_content, new SettingsFragment())
                        .commit();
                //  replaceFragment(new SettingsFragment());
                toolbarTitle.setText(R.string.title_settings);
            }
        });


        editButton.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    editButton.setBackgroundResource(R.drawable.btn_shape);
                    editButton.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    editButton.setBackgroundResource(R.drawable.btn_selected);
                    editButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                return false;
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


    public void getVendorProfileFromServer(final Vendor vendor1) {
//
//        dialog.setMessage("Loading..");
//        dialog.show();
        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Vendor> apiCall = webAPIs.getVendorProfile(vendor1);

        apiCall.enqueue(new Callback<Vendor>() {

            @Override
            public void onResponse(Call<Vendor> call, Response<Vendor> response) {
                if (response.isSuccessful()) {

                    vendor = response.body();
                    userName.setText(vendor.getVendorEmail());
                    name.setText(vendor.getShopName());
                    password.setText(vendor.getPassword());
                    address.setText(vendor.getLocation());
                    contact.setText(vendor.getMobile());
                    //     dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Vendor> call, Throwable t) {
                Log.i("FailureMessage", t.getMessage());
                //  dialog.dismiss();
                Toast.makeText(getActivity(), "Error while fetching Profile", Toast.LENGTH_SHORT).show();
            }

        });

    }


}
