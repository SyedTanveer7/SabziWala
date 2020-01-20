package com.example.customer.Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.service.autofill.TextValueSanitizer;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customer.CurrentLocation;
import com.example.customer.MainActivity;
import com.example.customer.R;
import com.example.customer.Webservices.Models.Product;
import com.example.customer.Webservices.Models.User;
import com.example.customer.Webservices.Models.VendorLocation;
import com.example.customer.Webservices.RetrofiltClient;
import com.example.customer.Webservices.WebAPIs;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.customer.CurrentLocation.latitude;
import static com.example.customer.CurrentLocation.longitude;
import static com.example.customer.MainActivity.navigation;
import static com.example.customer.MainActivity.toolbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText username, password;
    TextInputLayout textInputLayout1, textInputLayout2;
    Button signIn;
    TextView signUp, back, forgotPassword;
    ImageView backArrow;
    public static User loggedinUser;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    ProgressDialog dialog;
    ConstraintLayout constraintLayout;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initializeViews(view);
        return view;
    }

    public void initializeViews(final View view) {


        constraintLayout = view.findViewById(R.id.ca);
        back = view.findViewById(R.id.textView24);
        backArrow = view.findViewById(R.id.imageView9);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        signIn = view.findViewById(R.id.button4);
        signUp = view.findViewById(R.id.textView28);
        textInputLayout1 = view.findViewById(R.id.textInputLayout);
        textInputLayout2 = view.findViewById(R.id.textInputLayout2);
        forgotPassword = view.findViewById(R.id.textView57);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.session_content, new SessionFragment())
                        .commit();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.session_content, new SessionFragment())
                        .commit();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ForgetPasswordFragment());
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new SignUpFragment());
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username.getText().toString().trim().isEmpty() || !(username.getText().toString().trim().matches(emailPattern))) {
                    textInputLayout1.setError("Invalid Email!");
                } else if (password.getText().toString().trim().isEmpty()) {
                    textInputLayout2.setError("Invalid Password!");
                    textInputLayout1.setErrorEnabled(false);
                } else {
                    textInputLayout2.setErrorEnabled(false);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    verifiedEmailLogin(view, username.getText().toString().trim().toLowerCase(), password.getText().toString().trim());
                    //   authenticateUser(username.getText().toString().trim(), password.getText().toString().trim());
                }

            }
        });


        signIn.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    signIn.setBackgroundResource(R.drawable.cart_shape_style);
                    signIn.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    signIn.setBackgroundResource(R.drawable.cart_shape_style_unselected);
                    signIn.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                return false;
            }

        });


    }


    public void updateUserVerification(String email, String password) {
//        dialog = new ProgressDialog(getActivity());
//        dialog.setMessage("Signing In");
//        dialog.show();
        final User user = new User();
        user.setUsername(email);
        user.setPassword(password);

        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<User> apiCall = webAPIs.authenticateUser(user);

        apiCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user1 = response.body();
                    if (user1.getId().isEmpty()) {
                        dialog.dismiss();

                        Snackbar snackbar1 = Snackbar.make(constraintLayout, "Invalid Email or Password!", Snackbar.LENGTH_LONG);
                        snackbar1.show();
                    } else {
                        loggedinUser = user1;
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                        dialog.dismiss();

                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("ErrorMessage", t.getMessage());
                Snackbar snackbar1 = Snackbar.make(constraintLayout, "Internet Problem! Please Try Later", Snackbar.LENGTH_LONG);
                snackbar1.show();
                dialog.dismiss();
            }
        });


    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.session_content, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();

    }


    private void verifiedEmailLogin(final View view, final String email, final String pass) {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Signing In");
        dialog.show();
        Parse.initialize(getActivity());
        ParseInstallation.getCurrentInstallation().saveInBackground();


        ParseUser.logInInBackground(email, pass, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {

                    if (parseUser.getBoolean("emailVerified")) {


                        updateUserVerification(email, pass);


                    } else {
                        password.setText("");
//                        progressDialog.dismiss();
                        dialog.dismiss();
                        ParseUser.logOut();

                        Snackbar snackbar = Snackbar.make(view, "Please verify your email before Login!", Snackbar.LENGTH_LONG);
                        snackbar.show();


                    }
                } else {
//                    loginPassword.setText("");
//
                    dialog.dismiss();
//                    progressDialog.dismiss();
                    ParseUser.logOut();

                    Snackbar snackbar = Snackbar.make(view, "Invalid Email or Password!", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            }
        });

    }


}
