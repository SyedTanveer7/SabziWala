package com.example.customer.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import static android.app.Activity.RESULT_OK;

import com.example.customer.Adapters.ShopProductsAdapter;
import com.example.customer.MainActivity;
import com.example.customer.R;
import com.example.customer.Webservices.Models.NearestShopLocation;
import com.example.customer.Webservices.Models.Product;
import com.example.customer.Webservices.Models.VendorLocation;
import com.example.customer.Webservices.RetrofiltClient;
import com.example.customer.Webservices.WebAPIs;
import com.fusedbulblib.GetCurrentLocation;
import com.fusedbulblib.interfaces.GpsOnListner;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.customer.MainActivity.activity;
import static com.example.customer.MainActivity.context;
import static com.example.customer.MainActivity.navigation;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    CamomileSpinner progressBar;
    ViewFlipper viewFlipper;
    RecyclerView productsList;
    public static List<Product> shopProductss = new ArrayList<>();
    ProgressDialog dialog;
    TextView shopName, location;
    String nearestShopLocation = "";
    public static RelativeLayout transparent_layer, progressDialog;
    public static String latitude = "";
    public static String longitude = "";
    LocationManager locationManager;
    public static List<Location> allShopLocations = new ArrayList<>();
    public static List<VendorLocation> allShopLocationsNames = new ArrayList<>();
    public List<Double> distances = new ArrayList<>();

    private Location locat;
    private GoogleApiClient googleApiClient;
    private long UPDATE_INTERVAL = 5000;
    private long FASTEST_INTERVAL = 5000;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private static final int ALL_PERMISSIONS_RESULT = 1011;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewFlipper = view.findViewById(R.id.imageSlider);
        int[] images = {R.drawable.splash, R.drawable.fruits, R.drawable.fruits1};

        for (int image : images) {
            flipperImages(image);
        }
        initializeViews(view);
        return view;
    }


    public void initializeViews(View view) {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        dialog = new ProgressDialog(getActivity());
        progressBar = view.findViewById(R.id.restaurantProgress);

        progressDialog = view.findViewById(R.id.progressDialog);
        transparent_layer = view.findViewById(R.id.transparent_layer);
        productsList = view.findViewById(R.id.recyclerView1);
        shopName = view.findViewById(R.id.textView14);
        location = view.findViewById(R.id.textView17);
        shopProductss.clear();


        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        initializeGoogleApiClient();


    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resulCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resulCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resulCode)) {
                apiAvailability.getErrorDialog(getActivity(), resulCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                getActivity().finish();
            }
            return false;
        }
        return true;

    }

    public void flipperImages(int image) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setBackgroundResource(image);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(getActivity(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getActivity(), android.R.anim.slide_out_right);
    }

    public void getNearbyShopLocation(String latitude, String longitude) {
        Location locat = new Location("");
        locat.setLongitude(Double.parseDouble(latitude));
        locat.setLatitude(Double.parseDouble(longitude));

        if (allShopLocations.size() != 0) {
            try {
                nearByLocation(allShopLocations);
            } catch (Exception e) {
                Toast.makeText(context, "Getting Location.. Please Wait!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "No Shops Available", Toast.LENGTH_SHORT).show();
            location.setText("Not Available");
            shopName.setText("Not Available");
            progressDialog.setVisibility(View.GONE);
            transparent_layer.setVisibility(View.GONE);
        }
    }

    private void nearByLocation(List<Location> otherLocations) {


        //  int index = distance(currentLocation, otherLocations);
        int indexx = calculcateNearBy();


        Log.d("LocationPoints", indexx + " Last ");
        //  dialog.setMessage("Finding Nearby Shop..");
        // dialog.show();
//        int index = 0;
//        Double previousShortDistance = Math.sqrt(Math.pow(currentLocation.getLongitude() - otherLocations.get(0).getLongitude(), 2) + Math.pow(currentLocation.getLatitude() - otherLocations.get(0).getLatitude(), 2));
//        for (int i = 0; i < otherLocations.size(); i++) {
//            Double distanceFormula = Math.sqrt(Math.pow(currentLocation.getLongitude() - otherLocations.get(i).getLongitude(), 2) + Math.pow(currentLocation.getLatitude() - otherLocations.get(i).getLatitude(), 2));
//            if (distanceFormula < previousShortDistance) {
//                previousShortDistance = distanceFormula;
//                index = i;
//            }
//        }

        if (indexx == -1) {
            Toast.makeText(getActivity(), "No nearby shops available", Toast.LENGTH_SHORT).show();
            location.setText("Not Available");
            shopName.setText("Not Available");
            progressDialog.setVisibility(View.GONE);
            transparent_layer.setVisibility(View.GONE);
        } else {
            Log.d("Nearby Location", otherLocations.get(indexx).getLatitude() + " " + otherLocations.get(indexx).getLongitude() + "");
            nearestShopLocation = allShopLocationsNames.get(indexx).getLocationName();

            getNearestShopProducts(String.valueOf(otherLocations.get(indexx).getLatitude()), String.valueOf(otherLocations.get(indexx).getLongitude()));

        }
    }

    public void getNearestShopProducts(String latitude, String longitude) {

        transparent_layer.setVisibility(View.VISIBLE);
        progressDialog.setVisibility(View.VISIBLE);

        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);

        Call<List<Product>> apiCall = webAPIs.getNearestShopProducts(new NearestShopLocation(latitude, longitude));
        apiCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {

                    if (response.body().get(0).getName().equalsIgnoreCase("no vendor")) {
                        Toast.makeText(getActivity(), "No Shops Available", Toast.LENGTH_SHORT).show();
                        location.setText("Not Available");
                        shopName.setText("Not Available");
                        //dialog.dismiss();
                        progressDialog.setVisibility(View.GONE);
                        transparent_layer.setVisibility(View.GONE);
                    } else if (response.body().get(0).getName().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Products not Available", Toast.LENGTH_SHORT).show();

                        location.setText("Not Available");
                        shopName.setText("Not Available");
                        //

//
//  location.setText(nearestShopLocation);
//                        shopName.setText(response.body().get(0).getShopName());
//                        //  dialog.dismiss();
                        progressDialog.setVisibility(View.GONE);
                        transparent_layer.setVisibility(View.GONE);

                    } else {

//                        for (int i = 0; i < selectedProducts.size(); i++) {
//                            for (int j = 0; j < response.body().size(); j++) {
//                                if (response.body().get(j).getId() == selectedProducts.get(i).getId()) {
//                                    response.body().get(j).setSelected(true);
//                                }
//                            }
//                        }


                        shopProductss = response.body();
                        shopName.setText(response.body().get(0).getShopName());
                        location.setText(nearestShopLocation);
                        ShopProductsAdapter adapter = new ShopProductsAdapter(shopProductss, getActivity());
                        productsList.setHasFixedSize(true);
                        productsList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        productsList.setItemAnimator(new DefaultItemAnimator());
                        productsList.setAdapter(adapter);
                        //   dialog.dismiss();
                        progressDialog.setVisibility(View.GONE);
                        transparent_layer.setVisibility(View.GONE);
                        //     currentLocation.stopGettingLocation();
                    }
                }

                navigation.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.i("ErrorMessage", t.getMessage());
                Toast.makeText(getActivity(), "Internet Problem!Please Try Later", Toast.LENGTH_SHORT).show();
                progressDialog.setVisibility(View.GONE);
                transparent_layer.setVisibility(View.GONE);
                navigation.setVisibility(View.VISIBLE);
            }
        });


    }


    public void getAllShopLocations() {
        WebAPIs webAPIs = RetrofiltClient.getClient().create(WebAPIs.class);
        Call<List<VendorLocation>> apiCall = webAPIs.getAllShopLocations();

        apiCall.enqueue(new Callback<List<VendorLocation>>() {
            @Override
            public void onResponse(Call<List<VendorLocation>> call, Response<List<VendorLocation>> response) {
                if (response.isSuccessful()) {
                    allShopLocations.clear();
                    distances.clear();
                    List<VendorLocation> locations = response.body();
                    allShopLocationsNames = locations;
                    for (int i = 0; i < locations.size(); i++) {

                        Location l = new Location("");

                        String lat = locations.get(i).getLatitude();
                        String lng = locations.get(i).getLongitude();
                        if (!lat.isEmpty()) {
                            l.setLatitude(Double.parseDouble(lat));
                            l.setLongitude(Double.parseDouble(lng));
                            allShopLocations.add(l);

                            Log.i("LocationPoints", l.getLatitude() + " " + l.getLongitude() + "");
                            double x = distance(Double.valueOf(latitude), Double.valueOf(longitude), l.getLatitude(), l.getLongitude());
                            Log.i("LocationPoints", x + "");
                            distances.add(x);


                        }
                    }

                    // Log.i("ShopLocations", locations.get(0).getLocation().getLatitude() + "");

                    // Log.i("LocationPoints", longitude + " " + latitude + "");
                    int size = allShopLocations.size();

//                    if (!networkProvider) {
                    //getCurrentLocation();

//                    } else {
//                        Toast.makeText(getActivity(), "Please turn on GPS", Toast.LENGTH_SHORT).show();
//
//                        progressDialog.setVisibility(View.GONE);
//                        transparent_layer.setVisibility(View.GONE);
//
//                    }

                    getNearbyShopLocation(latitude, longitude);


                }
            }

            @Override
            public void onFailure(Call<List<VendorLocation>> call, Throwable t) {
                Log.i("ErrorMessage", t.getMessage());
                progressDialog.setVisibility(View.GONE);
                transparent_layer.setVisibility(View.GONE);
//                Snackbar snackbar1 = Snackbar.make(, "Internet Problem! Please Try Later", Snackbar.LENGTH_LONG);
//                snackbar1.show();
                Toast.makeText(getActivity(), "Internet Problem! Please Try Later", Toast.LENGTH_SHORT).show();

                navigation.setVisibility(View.VISIBLE);

            }
        });

    }


    //new Code

    private void initializeGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String permission : wantedPermissions) {
            if (!hasPermission(permission)) {
                result.add(permission);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
            // return getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

            googleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.start();
        if (!checkPlayServices()) {
            Toast.makeText(getActivity(), "You need to install google play services to use the app properly", Toast.LENGTH_SHORT).show();
        }

        if (googleApiClient != null) {
            if (latitude.equalsIgnoreCase("")) {
                startLocationUpdates();
            } else {
                getAllShopLocations();
            }
        }


    }

    public void getCurrentLocation() {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locat = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (locat != null) {
            latitude = String.valueOf(locat.getLatitude());
            longitude = String.valueOf(locat.getLongitude());
            getAllShopLocations();
//            getNearbyShopLocation(latitude, longitude);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = String.valueOf(locat.getLatitude());
            longitude = String.valueOf(locat.getLongitude());
            getAllShopLocations();

//            getNearbyShopLocation(latitude, longitude);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    private void startLocationUpdates() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);


        builder.setAlwaysShow(true); // this is the key ingredie


        PendingResult result =
                LocationServices.SettingsApi.checkLocationSettings(
                        googleApiClient,
                        builder.build()
                );


        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result
                        .getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //    toast("Success");
                        // All location settings are satisfied. The client can
                        // initialize location
                        // requests here.
                        //      getNearbyShopLocation(latitude, longitude);
                        getCurrentLocation();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:


                        new iOSDialogBuilder(getActivity())
                                .setTitle("GPS Settings")
                                .setSubtitle("Please enable GPS Location in High accuracy!")
                                .setBoldPositiveLabel(true)
                                .setCancelable(false)
                                .setPositiveListener("Settings", new iOSDialogClickListener() {
                                    @Override
                                    public void onClick(iOSDialog dialog) {


                                        getActivity().startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 10001);

                                        dialog.dismiss();


//                                            progressDialog.setVisibility(View.GONE);
//                                            transparent_layer.setVisibility(View.GONE);


                                    }
                                }).setNegativeListener("Cancel", new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();
                                Toast.makeText(context, "GPS not enabled in High accuracy", Toast.LENGTH_SHORT).show();
                            }
                        })
                                .build().show();


                        Toast.makeText(getActivity(), "Please Enable GPS in High accuracy", Toast.LENGTH_SHORT).show();
                        progressDialog.setVisibility(View.GONE);
                        transparent_layer.setVisibility(View.GONE);

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // toast("Setting change not allowed");
                        // Location settings are not satisfied. However, we have
                        // no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:

                for (String permission : permissionsToRequest) {
                    if (!hasPermission(permission)) {
                        permissionsRejected.add(permission);
                    }
                }

                if (permissionsRejected.size() > 0) {

                    progressDialog.setVisibility(View.GONE);
                    transparent_layer.setVisibility(View.GONE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {

                            new AlertDialog.Builder(getActivity())
                                    .setMessage("These permissions are mandatory to get your location. You need to allow them")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            permissionsRejected.clear();
                                        }
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(), "Location Permissions are not enabled", Toast.LENGTH_SHORT).show();

                                }
                            }).create().show();

                            return;


                        }

                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                        getCurrentLocation();
                    }

                }
                break;
        }
    }


    private int calculcateNearBy() {

        int index = -1;

        for (int i = 0; i < distances.size(); i++) {
            if (distances.get(i) < 2000) {
                index = i;
            }
        }

        return index;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        // haversine great circle distance approximation, returns meters
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60; // 60 nautical miles per degree of seperation
        dist = dist * 1852; // 1852 meters per nautical mile
        return (dist);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
           super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 10001) {

                getCurrentLocation();
            }


    }
}
