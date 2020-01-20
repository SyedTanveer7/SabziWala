package com.example.customer.Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customer.R;
import com.example.customer.Webservices.Models.User;
import com.example.customer.Webservices.RetrofiltClient;
import com.example.customer.Webservices.WebAPIs;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.customer.Adapters.ShopProductsAdapter.selectedProducts;
import static com.example.customer.MainActivity.navigation;
import static com.example.customer.MainActivity.toolbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    EditText name, username, password, repPassword, address, mobile;
    Button signUp;
    TextView signIn, back;
    ImageView backArrow;
    TextInputLayout textInputLayout1, textInputLayout2, textInputLayout3, textInputLayout4, textInputLayout5, textInputLayout6;
    ConstraintLayout constraintLayout;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog dialog1;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initializeViews(view);
        return view;
    }

    public void initializeViews(final View view) {
        textInputLayout1 = view.findViewById(R.id.textInputLayout);
        textInputLayout2 = view.findViewById(R.id.textInputLayout2);
        textInputLayout3 = view.findViewById(R.id.textInputLayout3);
        textInputLayout4 = view.findViewById(R.id.textInputLayout4);
        textInputLayout5 = view.findViewById(R.id.textInputLayout5);
        textInputLayout6 = view.findViewById(R.id.textInputLayout6);
        constraintLayout = view.findViewById(R.id.co);
        back = view.findViewById(R.id.textView24);
        backArrow = view.findViewById(R.id.imageView9);
        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.uname);
        password = view.findViewById(R.id.passw);
        repPassword = view.findViewById(R.id.repPassw);
        address = view.findViewById(R.id.address);
        mobile = view.findViewById(R.id.mobile);
        signUp = view.findViewById(R.id.button5);
        signIn = view.findViewById(R.id.textView25);

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


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUser(view);
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new LoginFragment());
            }
        });


        signUp.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    signUp.setBackgroundResource(R.drawable.cart_shape_style);
                    signUp.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    signUp.setBackgroundResource(R.drawable.cart_shape_style_unselected);
                    signUp.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                return false;
            }

        });


    }


    public void validateUser(View view) {
        if (name.getText().toString().trim().isEmpty()) {
            textInputLayout1.setError("Required!");
        } else if (username.getText().toString().trim().isEmpty()) {
            textInputLayout2.setError("Required!");
            textInputLayout1.setErrorEnabled(false);

        } else if (!(username.getText().toString().trim().matches(emailPattern))) {
            textInputLayout2.setError("Invalid Email!");
            textInputLayout1.setErrorEnabled(false);
        } else if (password.getText().toString().trim().isEmpty()) {
            textInputLayout3.setError("Required!");
            textInputLayout2.setErrorEnabled(false);
        } else if (repPassword.getText().toString().trim().isEmpty()) {
            textInputLayout4.setError("Enter password again");
            textInputLayout3.setErrorEnabled(false);
        } else if (!(password.getText().toString().trim().equalsIgnoreCase(repPassword.getText().toString().trim()))) {
            textInputLayout4.setError("Passwords do not match!");
            textInputLayout3.setErrorEnabled(false);
        } else if (address.getText().toString().trim().isEmpty()) {
            textInputLayout5.setError("Required");
            textInputLayout4.setErrorEnabled(false);
        } else if (mobile.getText().toString().trim().isEmpty()) {
            textInputLayout6.setError("Required!");
            textInputLayout5.setErrorEnabled(false);
        } else {
            textInputLayout6.setErrorEnabled(false);
            User user = new User();
            user.setUsername(username.getText().toString().trim());
            user.setName(name.getText().toString().trim());
            user.setPassword(password.getText().toString().trim());
            user.setAddress(address.getText().toString());
            user.setMobile(mobile.getText().toString());
            registerUserinBack4App(view, user);
            // registerUser(user);
            name.setText("");
            username.setText("");
            password.setText("");
            repPassword.setText("");
            address.setText("");
            mobile.setText("");
        }

    }

    public void registerUser(final User user) {

        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<User> apiCall = webAPIs.registerUser(user);
        apiCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {

                    User user1 = response.body();
                    if (user1.getUsername().equalsIgnoreCase("yes")) {
                        dialog1.dismiss();
                        Snackbar snackbar1 = Snackbar.make(constraintLayout, "User already Exists!", Snackbar.LENGTH_LONG);
                        snackbar1.show();

                    } else if (user1.getUsername().equalsIgnoreCase("")) {
                        dialog1.dismiss();
                        new iOSDialogBuilder(getActivity())
                                .setTitle("Message")
                                .setSubtitle("Verify Email to complete the Registration!")
                                .setBoldPositiveLabel(true)
                                .setCancelable(false)
                                .setPositiveListener("Okay", new iOSDialogClickListener() {
                                    @Override
                                    public void onClick(iOSDialog dialog) {
                                        selectedProducts.clear();
                                        dialog.dismiss();
                                        replaceFragment(new LoginFragment());
                                        //          dialog1.dismiss();
                                    }
                                })
                                .build().show();


                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("ErrorMessage", t.getMessage());
                Snackbar snackbar1 = Snackbar.make(constraintLayout, "Internet Problem! Please Try Later", Snackbar.LENGTH_LONG);
                snackbar1.show();

                dialog1.dismiss();
            }
        });


    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.session_content, fragment);
        fragmentTransaction.commit();

    }


    private void registerUserinBack4App(final View view, final User u) {

        try {

            dialog1 = new ProgressDialog(getActivity());
            dialog1.setMessage("Please Wait.");
            dialog1.show();
            //
            // Reset errors

            Parse.initialize(getActivity());
            ParseInstallation.getCurrentInstallation().saveInBackground();

            // Sign up with Parse
            ParseUser user = new ParseUser();
            user.setUsername(u.getUsername().toLowerCase());
            user.setPassword(u.getPassword());
            user.setEmail(u.getUsername());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        ParseUser.logOut();
                        registerUser(u);
                        //sendData(n, em, p, a);

                    } else {
                        //    progressDialog.dismiss();
                        ParseUser.logOut();
                        Snackbar snackbar = Snackbar.make(view, "User already Registered! Account could not be created!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialog1.dismiss();


                    }
                }
            });
        } catch (Exception ex) {
            //      progressDialog.dismiss();
            ex.printStackTrace();
        }

    }


}
