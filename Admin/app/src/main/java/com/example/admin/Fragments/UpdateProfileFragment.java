package com.example.admin.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.admin.R;
import com.example.admin.Webservices.Models.Admin;
import com.example.admin.Webservices.RetrofiltClient;
import com.example.admin.Webservices.WebAPIs;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.Fragments.LoginFragment.admin;
import static com.example.admin.Fragments.ProductsFragment.progressDialog;
import static com.example.admin.Fragments.ProductsFragment.transparent_layer;
import static com.example.admin.MainActivity.toolbarTitle;


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
    public CamomileSpinner pprogressBar;
    public RelativeLayout ptransparent_layer, pprogressDialog;
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

        pprogressBar = v.findViewById(R.id.restaurantProgress);
        pprogressBar.start();
        pprogressDialog = v.findViewById(R.id.progressDialog);
        ptransparent_layer = v.findViewById(R.id.transparent_layer);

        dialog = new ProgressDialog(getActivity());
        imageBack = v.findViewById(R.id.image_back);
        buttonUpdate = v.findViewById(R.id.button_update);
        name = v.findViewById(R.id.edit_name);
        userName = v.findViewById(R.id.edit_username);
        password = v.findViewById(R.id.edit_password);
        repeatPassword = v.findViewById(R.id.edit_repeat_Password);
        contact = v.findViewById(R.id.edit_mobile);
        address = v.findViewById(R.id.edit_address);

        getAdminProfileFromServer(LoginFragment.admin);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.main_content, new ProfileFragment())
                        .commit();
                toolbarTitle.setText(R.string.title_profile);
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
            userName.setError("Invalid Email!");
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

//            dialog.setMessage("Updating..");
//            dialog.show();
            pprogressDialog.setVisibility(View.VISIBLE);
            ptransparent_layer.setVisibility(View.VISIBLE);
            Admin admin = new Admin();
            admin.setId(LoginFragment.admin.getId());
            admin.setName(nam);
            admin.setUserName(uname);
            admin.setPassword(pass);
            admin.setMobile(cont);
            admin.setAddress(add);
            updateAdminProfileInServer(admin);

        }


    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.commit();
    }

    public void updateAdminProfileInServer(final Admin admin) {

        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Admin> apiCall = webAPIs.updateAdminPr0file(admin);
        apiCall.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                if (response.isSuccessful()) {
                    replaceFragment(new ProfileFragment());

                    toolbarTitle.setText(R.string.title_profile);
                    pprogressDialog.setVisibility(View.GONE);
                    ptransparent_layer.setVisibility(View.GONE);
                    //   dialog.dismiss();
                    Toast.makeText(getActivity(), "Admin Updated Successfully!", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                Log.i("FailureMessage", t.getMessage());
                //    dialog.dismiss();
                pprogressDialog.setVisibility(View.GONE);
                ptransparent_layer.setVisibility(View.GONE);
            }
        });

    }

    public void getAdminProfileFromServer(final Admin admin1) {

        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<Admin> apiCall = webAPIs.getAdminPr0file(admin1);

        apiCall.enqueue(new Callback<Admin>() {

            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                if (response.isSuccessful()) {
                    admin = response.body();
                    userName.setText(admin.getUserName());
                    name.setText(admin.getName());
                    password.setText(admin.getPassword());
                    repeatPassword.setText(admin.getPassword());
                    address.setText(admin.getAddress());
                    contact.setText(admin.getMobile());
                    //         dialog.dismiss();
                    pprogressDialog.setVisibility(View.GONE);
                    ptransparent_layer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                Log.i("FailureMessage", t.getMessage());
                //   dialog.dismiss();
                Toast.makeText(getActivity(), "Error While fetching Profile", Toast.LENGTH_SHORT).show();
                pprogressDialog.setVisibility(View.GONE);
                ptransparent_layer.setVisibility(View.GONE);
            }

        });

    }

}
