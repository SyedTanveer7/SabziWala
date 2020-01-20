package com.example.admin.Fragments;

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

import com.example.admin.MainActivity;
import com.example.admin.R;
import com.example.admin.Webservices.Models.Admin;
import com.example.admin.Webservices.RetrofiltClient;
import com.example.admin.Webservices.WebAPIs;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.MainActivity.navigation;
import static com.example.admin.MainActivity.toolbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    private Button loginBuuton;
    private EditText userName;
    private EditText password;
    public static Admin admin;
    ProgressDialog dialog;
    TextView forgetPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        initializeEvents(v);

        return v;
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.commit();
    }


    public void initializeEvents(View v) {

        dialog = new ProgressDialog(getActivity());
        loginBuuton = v.findViewById(R.id.button_login);
        userName = v.findViewById(R.id.editText_username);
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

                validateAdmin();

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

    public void validateAdmin() {

        String name = userName.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(name) || !name.matches(emailPattern)) {
            userName.setError("Invalid Email!");
        } else if (TextUtils.isEmpty(pass)) {
            password.setError("Invalid Password!");
        } else {

            dialog.setMessage("Signing In..");
            dialog.show();
            Admin admin = new Admin();

            admin.setUserName(name);
            admin.setPassword(pass);

            authenticateAdmin(admin);
        }


    }

    public void authenticateAdmin(Admin admin1) {

        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Admin> apiCall = webAPIs.authenticateAdmin(admin1);

        apiCall.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                if (response.isSuccessful()) {
                    admin = response.body();
                    if (!(admin.getName().equalsIgnoreCase(""))) {
                        dialog.dismiss();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();

                    } else {
                        dialog.dismiss();
                        password.setText("");
                        Toast.makeText(getActivity(), "Login Failed! Invalid Email or Password", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                Log.i("FailureMessage", t.getMessage());
                dialog.dismiss();
            }
        });
    }


    private void registerUserinBack4App() {

        try {


            //
            // Reset errors

            Parse.initialize(getActivity());
            ParseInstallation.getCurrentInstallation().saveInBackground();

            // Sign up with Parse
            ParseUser user = new ParseUser();
            user.setUsername("shumair124@gmail.com");
            user.setPassword("admin");
            user.setEmail("shumair124@gmail.com");

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        ParseUser.logOut();
                        //sendData(n, em, p, a);

                    } else {
                        //    progressDialog.dismiss();
                        ParseUser.logOut();


                    }
                }
            });
        } catch (Exception ex) {
            //      progressDialog.dismiss();
            ex.printStackTrace();
        }

    }


}
