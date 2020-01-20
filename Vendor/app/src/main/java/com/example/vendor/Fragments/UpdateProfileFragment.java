package com.example.vendor.Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Webservices.Models.Vendor;
import com.example.vendor.Webservices.RetrofiltClient;
import com.example.vendor.Webservices.WebAPIs;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.vendor.Fragments.LoginFragment.vendor;
import static com.example.vendor.MainActivity.toolbarTitle;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProfileFragment extends Fragment {

    private ImageView imageBack;
    private Button buttonUpdate;
    private EditText userName;
    private EditText name;
    private EditText password;
    private EditText repeatPassword;
    private EditText contact;
    private EditText address;
    private ProgressDialog dialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public UpdateProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update_profile, container, false);
        initialzieEvents(v);
        return v;
    }


    public void initialzieEvents(View v) {

        dialog = new ProgressDialog(getActivity());
        imageBack = v.findViewById(R.id.image_back);
        buttonUpdate = v.findViewById(R.id.button_update);
        name = v.findViewById(R.id.edit_name);
        userName = v.findViewById(R.id.edit_username);
        password = v.findViewById(R.id.edit_password);
        repeatPassword = v.findViewById(R.id.edit_repeat_Password);
        contact = v.findViewById(R.id.edit_mobile);
        address = v.findViewById(R.id.edit_address);


        name.setText(vendor.getShopName());
        userName.setText(vendor.getVendorEmail());
        password.setText(vendor.getPassword());
        repeatPassword.setText(vendor.getPassword());
        contact.setText(vendor.getMobile());
        address.setText(vendor.getLocation());


        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.main_content, new ProfileFragment())
                        .commit();
            }
        });


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAdmin();
            }
        });


        buttonUpdate.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    buttonUpdate.setBackgroundResource(R.drawable.btn_shape);
                    buttonUpdate.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonUpdate.setBackgroundResource(R.drawable.btn_selected);
                    buttonUpdate.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                return false;
            }

        });


    }

    public void validateAdmin() {

        String uname = userName.getText().toString().trim();
        String nam = name.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String repeatPas = repeatPassword.getText().toString().trim();
        String cont = contact.getText().toString().trim();
        String add = address.getText().toString().trim();

        if (TextUtils.isEmpty(nam)) {
            name.setError("Name required!");
        } else if (TextUtils.isEmpty(uname) || !(uname.matches(emailPattern))) {
            userName.setError("Invalid Email");
        } else if (TextUtils.isEmpty(pass)) {
            password.setError("Password required!");
        } else if (TextUtils.isEmpty(repeatPas)) {
            repeatPassword.setError("Repeat Password!");
        } else if (!pass.equalsIgnoreCase(repeatPas)) {
            repeatPassword.setError("Passwords do not match!");
        } else if (TextUtils.isEmpty(cont)) {
            contact.setError("Contact required!");
        } else if (TextUtils.isEmpty(add)) {
            address.setError("Address required!");
        } else {

            dialog.setMessage("Updating..");
            dialog.show();
            Vendor v = new Vendor();
            v.setShopId(vendor.getShopId());
            v.setShopName(nam);
            v.setVendorEmail(vendor.getVendorEmail());
            v.setPassword(pass);
            v.setLocation(vendor.getLocation());
            v.setMobile(cont);
            v.setLattitude(vendor.getLattitude());
            v.setLongitude(vendor.getLongitude());
            v.setComission(vendor.getComission());

            updateVendorProfileInServer(v);

        }


    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.commit();
    }

    public void updateVendorProfileInServer(final Vendor v) {

        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Vendor> apiCall = webAPIs.updateVendor(v);
        apiCall.enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(Call<Vendor> call, Response<Vendor> response) {
                if (response.isSuccessful()) {
                    Parse.initialize(getActivity());
                    ParseInstallation.getCurrentInstallation().saveInBackground();
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    currentUser.setPassword(v.getPassword());
                    currentUser.saveInBackground();

                    replaceFragment(new ProfileFragment());
                    toolbarTitle.setText(R.string.title_profile);
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Vendor Updated Successfully!", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Vendor> call, Throwable t) {
                Log.i("FailureMessage", t.getMessage());
                dialog.dismiss();
            }
        });

    }


}
