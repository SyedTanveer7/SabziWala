package com.example.customer.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customer.R;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.customer.Adapters.ShopProductsAdapter.selectedProducts;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPasswordFragment extends Fragment {


    ImageView closeButton;
    TextView signUp;
    Button resetPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    EditText inputEmail;

    public ForgetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);

        initializeViews(view);

        return view;
    }

    private void initializeViews(final View view) {
        closeButton = view.findViewById(R.id.imageView19);
        signUp = view.findViewById(R.id.textView56);
        resetPassword = view.findViewById(R.id.button8);
        inputEmail = view.findViewById(R.id.editText);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.show();
                String email = inputEmail.getText().toString().trim();
                if (email.matches(emailPattern) && email.length() > 0) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                    Parse.initialize(getActivity());
                    ParseInstallation.getCurrentInstallation().saveInBackground();

                    ParseUser.requestPasswordResetInBackground(email,
                            new RequestPasswordResetCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        dialog.dismiss();
                                        new iOSDialogBuilder(getActivity())
                                                .setTitle("Message")
                                                .setSubtitle("Hurray!We've successfully sent you a password reset email.")
                                                .setBoldPositiveLabel(true)
                                                .setCancelable(false)
                                                .setPositiveListener("Okay", new iOSDialogClickListener() {
                                                    @Override
                                                    public void onClick(iOSDialog dialog) {
                                                        selectedProducts.clear();
dialog.dismiss();
                                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.session_content, new LoginFragment()).commit();

                                                    }
                                                })
                                                .build().show();
                                        // An email was successfully sent with reset instructions.
                                    } else {
                                        // Something went wrong. Look at the ParseException to see what's up.
                                        Snackbar.make(view, "Oh no! Please enter a valid email", Snackbar.LENGTH_SHORT).show();

                                        dialog.dismiss();
                                    }
                                }
                            });
                } else {
                    if (email.length() == 0) {
                        Snackbar.make(view, "Please enter email to reset your password", Snackbar.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                        Snackbar.make(view, "Oh no! Please enter a valid email", Snackbar.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.session_content, new SignUpFragment())
                        .commit();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.session_content, new LoginFragment())
                        .commit();
            }
        });
    }


}
