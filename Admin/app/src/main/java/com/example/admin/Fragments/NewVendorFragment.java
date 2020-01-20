package com.example.admin.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.R;
import com.example.admin.Webservices.Models.Vendor;
import com.example.admin.Webservices.Repositories.VendorRepository;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.example.admin.MainActivity.toolbarTitle;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewVendorFragment extends Fragment implements View.OnClickListener {


    private ImageView imageBack, selectLocation;
    private Button buttonAddVendor;
    private EditText shopName, vendorName, mobile, password, commission;
    private TextView location;
    private String LocationName, latLng;
    VendorRepository repository;
    View view;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static ProgressDialog progressDialog1;

    public NewVendorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_vendor, container, false);
        initializeEvents(view);
        return view;
    }

    public void initializeEvents(View v) {

        repository = new VendorRepository(getActivity());
        progressDialog1 = new ProgressDialog(getActivity());
        buttonAddVendor = v.findViewById(R.id.button_add_vendor);
        imageBack = v.findViewById(R.id.image_back);
        selectLocation = v.findViewById(R.id.imageView3);

        shopName = v.findViewById(R.id.editText_shop_name);
        vendorName = v.findViewById(R.id.editText_vendor_name);
        mobile = v.findViewById(R.id.editText_mobile);
        password = v.findViewById(R.id.editText_password);
        location = v.findViewById(R.id.textView12);
        commission = v.findViewById(R.id.editText_comission);

        imageBack.setOnClickListener(this);
        buttonAddVendor.setOnClickListener(this);
        selectLocation.setOnClickListener(this);

        buttonAddVendor.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    buttonAddVendor.setBackgroundResource(R.drawable.btn_shape);
                    buttonAddVendor.setTextColor(getResources().getColor(R.color.colorWhite));

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonAddVendor.setBackgroundResource(R.drawable.btn_selected);
                    buttonAddVendor.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
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

    public void validateVendor() {

        String shop = shopName.getText().toString().trim();
        String vName = vendorName.getText().toString().trim();
        String mobil = mobile.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String commis = commission.getText().toString().trim();

        if (TextUtils.isEmpty(shop)) {
            shopName.setError("Shop Name required!");
        } else if (TextUtils.isEmpty(vName) || !(vName.matches(emailPattern))) {
            vendorName.setError("Email required!");
        } else if (TextUtils.isEmpty(mobil)) {
            mobile.setError("Product Quantity required!");
        } else if (TextUtils.isEmpty(pass)) {
            password.setError("Digits required!");
        } else if (TextUtils.isEmpty(commis)) {
            password.setError("Comission required!");
        } else {

            Vendor vendor = new Vendor();
            vendor.setShopName(shop);
            vendor.setVendorName(vName);
            vendor.setMobile(mobil);
            vendor.setPassword(pass);
            vendor.setComission(commis);

            vendor.setLocation(LocationName);
            LatLng locat = getLongitudeLatitude(latLng);
            vendor.setLattitude(String.valueOf(locat.latitude));
            vendor.setLongitude(String.valueOf(locat.longitude));
            vendor.setImage("");
            registerVendorInBack4App(vendor);
            shopName.setText("");
            vendorName.setText("");
            mobile.setText("");
            password.setText("");
            commission.setText("");
            location.setText("Select Location");

        }
    }

    @Override
    public void onClick(View v) {
        if (v == imageBack) {
            ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, new VendorsFragment())
                    .commit();
            toolbarTitle.setText(R.string.title_vendors);
        } else if (v == buttonAddVendor) {
            validateVendor();
        } else if (v == selectLocation) {
            getLocation();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {
            if (requestCode == 303) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                LocationName = place.getName().toString();

                location.setText(LocationName);
                latLng = place.getLatLng().toString();

                Toast.makeText(getActivity(), LocationName, Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void getLocation() {
        if (!checkIfAlreadyhaveInternetPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getActivity().requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
            }
        } else {


            try {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent = builder.build(getActivity());
                getActivity().startActivityForResult(intent, 303);

            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }


        }

    }

    private boolean checkIfAlreadyhaveInternetPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private LatLng getLongitudeLatitude(String points) {

        String[] arr = points.split(" ");
        String[] arr1 = arr[1].split("\\(");
        String[] arr2 = arr1[1].split("\\,");
        String[] arr3 = arr2[1].split("\\)");

        System.out.println(arr2[0]);
        System.out.println(arr3[0]);
        LatLng x = new LatLng(Double.valueOf(arr2[0]), Double.valueOf(arr3[0]));
        return x;
    }


    private void registerVendorInBack4App(final Vendor vend) {
        try {
            // Reset errors

            progressDialog1.setMessage("Please Wait..");
            progressDialog1.show();

            Parse.initialize(getActivity());
            ParseInstallation.getCurrentInstallation().saveInBackground();


            // Sign up with Parse
            ParseUser user = new ParseUser();
            user.setUsername(vend.getVendorName());
            user.setPassword(vend.getPassword());
            user.setEmail(vend.getVendorName());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        String objectId = ParseUser.getCurrentUser().getObjectId();
                        ParseUser.logOut();

                        vend.setObjectId(objectId);
                        repository.addVendorInServer(vend);
                    } else {
                        progressDialog1.dismiss();
                        ParseUser.logOut();
                        Snackbar snackbar = Snackbar.make(view, "User already Registered! Account could not be created!", Snackbar.LENGTH_LONG);
                        snackbar.show();


                    }
                }
            });
        } catch (Exception ex) {
            progressDialog1.dismiss();
            ex.printStackTrace();
        }


    }


}



