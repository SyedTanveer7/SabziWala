package com.example.customer.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.customer.R;
import com.example.customer.Webservices.Models.User;
import com.example.customer.Webservices.RetrofiltClient;
import com.example.customer.Webservices.WebAPIs;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.customer.Fragments.LoginFragment.loggedinUser;


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
    User updatingUser = new User();
    RelativeLayout transparent_layer, progressDialog;
    CamomileSpinner progressBar;

    public UpdateProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        initialzieEvents(view);
        return view;
    }

    public void initialzieEvents(View v) {

        dialog = new ProgressDialog(getActivity());
        progressBar = v.findViewById(R.id.restaurantProgress);

        progressDialog = v.findViewById(R.id.progressDialog);
        transparent_layer = v.findViewById(R.id.transparent_layer);

        imageBack = v.findViewById(R.id.image_back);
        buttonUpdate = v.findViewById(R.id.button_update);
        name = v.findViewById(R.id.edit_name);
        userName = v.findViewById(R.id.edit_username);
        password = v.findViewById(R.id.edit_password);
        repeatPassword = v.findViewById(R.id.edit_repeat_Password);
        contact = v.findViewById(R.id.edit_mobile);
        address = v.findViewById(R.id.edit_address);

        name.setText(loggedinUser.getName());
        userName.setText(loggedinUser.getUsername());
        password.setText(loggedinUser.getPassword());
        repeatPassword.setText(loggedinUser.getPassword());
        contact.setText(loggedinUser.getMobile());
        address.setText(loggedinUser.getAddress());


        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
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
        } else if (TextUtils.isEmpty(uname)) {
            userName.setError("Email required!");
        } else if (TextUtils.isEmpty(pass)) {
            password.setError("Password required!");
        } else if (TextUtils.isEmpty(repeatPas)) {
            repeatPassword.setError("Repeat Password!");
        } else if (!pass.equalsIgnoreCase(repeatPas)) {
            repeatPassword.setError("Passwords do not match!");
        } else if (TextUtils.isEmpty(cont)) {
            contact.setError("Mobile required!");
        } else if (TextUtils.isEmpty(add)) {
            address.setError("Address required!");
        } else {

//            dialog.setMessage("Updating..");
//            dialog.show();
            progressBar.start();
            transparent_layer.setVisibility(View.VISIBLE);
            progressDialog.setVisibility(View.VISIBLE);
            updatingUser.setId(loggedinUser.getId());
            updatingUser.setName(nam);
            updatingUser.setUsername(uname);
            updatingUser.setPassword(pass);
            updatingUser.setMobile(cont);
            updatingUser.setAddress(add);


            updateProfile(updatingUser);
        }


    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.commit();
    }

    public void updateProfile(User user) {


        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<User> apiCall = webAPIs.updateUser(user);

        apiCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user1 = response.body();
                    if (user1.getUsername().equalsIgnoreCase("updated")) {
                        loggedinUser = updatingUser;
                        // dialog.dismiss();
                        Parse.initialize(getActivity());
                        ParseInstallation.getCurrentInstallation().saveInBackground();
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        currentUser.setPassword(loggedinUser.getPassword());
                        currentUser.saveInBackground();
                        progressDialog.setVisibility(View.GONE);
                        transparent_layer.setVisibility(View.GONE);
                        replaceFragment(new ProfileFragment());
                    } else {
                        Toast.makeText(getActivity(), "Error While Updating profile!", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("ErrorMessage", t.getMessage());
                Toast.makeText(getActivity(), "Internet Problem!Please Try Later", Toast.LENGTH_SHORT).show();
                //    dialog.dismiss();
                progressDialog.setVisibility(View.GONE);
                transparent_layer.setVisibility(View.GONE);
            }
        });


    }

}
