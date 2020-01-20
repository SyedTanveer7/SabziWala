package com.example.vendor.Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.MainActivity;
import com.example.vendor.R;
import com.example.vendor.SaveSessionCredentials;
import com.example.vendor.Webservices.Models.Vendor;
import com.example.vendor.Webservices.RetrofiltClient;
import com.example.vendor.Webservices.WebAPIs;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.vendor.MainActivity.navigation;
import static com.example.vendor.MainActivity.toolbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private Button loginBuuton;
    private EditText shopName;
    private EditText password;
    private TextView forgetPassword;
    public static Vendor vendor;
    ProgressDialog dialog;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    View view;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        initializeEvents(view);
        return view;

    }


    public void initializeEvents(View v) {

        dialog = new ProgressDialog(getActivity());
        loginBuuton = v.findViewById(R.id.button_login);
        shopName = v.findViewById(R.id.editText_username);
        password = v.findViewById(R.id.editText_password);
        forgetPassword = v.findViewById(R.id.textView57);


        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.session_content, new ForgetPasswordFragment())
                        .commit();
            }
        });

        loginBuuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateAdmin(v);

            }
        });


        loginBuuton.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    loginBuuton.setBackgroundResource(R.drawable.btn_shape);
                    loginBuuton.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    loginBuuton.setBackgroundResource(R.drawable.btn_selected);
                    loginBuuton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                return false;
            }

        });


    }

    public void validateAdmin(View view) {

        String name = shopName.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(name) || !(name.matches(emailPattern))) {
            Snackbar.make(view, "Invalid Email", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass)) {
            Snackbar.make(view, "Invalid Password", Snackbar.LENGTH_SHORT).show();
        } else {

            dialog.setMessage("Signing In..");
            dialog.show();
            Vendor v = new Vendor();

            v.setVendorEmail(name);
            v.setPassword(pass);

            loginThroughBack4App(v);

        }


    }

    public void authenticateVendor(final Vendor vendor1) {

        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Vendor> apiCall = webAPIs.authenticateVendor(vendor1);

        apiCall.enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(Call<Vendor> call, Response<Vendor> response) {
                if (response.isSuccessful()) {
                    vendor = response.body();
                    if (!(vendor.getShopId().isEmpty())) {
                        dialog.dismiss();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        dialog.dismiss();
                        Snackbar snackbar1 = Snackbar.make(view, "Invalid Email or Password!", Snackbar.LENGTH_LONG);
                        snackbar1.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Vendor> call, Throwable t) {
                Log.i("FailureMessage", t.getMessage());
                dialog.dismiss();
            }
        });
    }


    private void loginThroughBack4App(final Vendor v) {

        Parse.initialize(getActivity());
        ParseInstallation.getCurrentInstallation().saveInBackground();


        ParseUser.logInInBackground(v.getVendorEmail(), v.getPassword(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    if (parseUser.getBoolean("emailVerified")) {

                        authenticateVendor(v);

                    } else {
                        password.setText("");
                        dialog.dismiss();
                        ParseUser.logOut();

                        Snackbar snackbar = Snackbar.make(view, "Please verify your email before Login!", Snackbar.LENGTH_LONG);
                        snackbar.show();


                    }
                } else {
                    password.setText("");

                    dialog.dismiss();
                    ParseUser.logOut();

                    Snackbar snackbar = Snackbar.make(view, "Invalid Email or Password!", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            }
        });
    }


}
