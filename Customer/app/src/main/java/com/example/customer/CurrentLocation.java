package com.example.customer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.customer.Fragments.CartSummaryFragment;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;

import static android.content.Context.MODE_PRIVATE;
import static com.example.customer.Adapters.ShopProductsAdapter.selectedProducts;
import static com.example.customer.Fragments.HomeFragment.progressDialog;
import static com.example.customer.Fragments.HomeFragment.transparent_layer;

public class CurrentLocation implements LocationListener {

    Context context;
    LocationManager locationManager;
    public static String latitude = "";
    public static String longitude = "";
    public static boolean gps_enabled = false;
    public static boolean network_enabled = false;

    public CurrentLocation(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            ActivityCompat.requestPermissions(
                    (Activity) context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    10
            );

            return;
        }


        if (!gps_enabled && !network_enabled) {
            // notify user


            new iOSDialogBuilder(context)
                    .setTitle("GPS Settings")
                    .setSubtitle("Please enable GPS Location!")
                    .setBoldPositiveLabel(true)
                    .setCancelable(false)
                    .setPositiveListener("Settings", new iOSDialogClickListener() {
                        @Override
                        public void onClick(iOSDialog dialog) {


                            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            try {
                                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            } catch (Exception ex) {
                            }

                            try {
                                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                            } catch (Exception ex) {
                            }
                            dialog.dismiss();

                            if (gps_enabled && network_enabled) {
                                progressDialog.setVisibility(View.GONE);
                                transparent_layer.setVisibility(View.GONE);
                            }

                        }
                    }).setNegativeListener("Cancel", new iOSDialogClickListener() {
                @Override
                public void onClick(iOSDialog dialog) {
                    dialog.dismiss();
                    Toast.makeText(context, "GPS not enabled", Toast.LENGTH_SHORT).show();
                }
            })
                    .build().show();


//            new AlertDialog.Builder(context)
//                    .setMessage("GPS not enabled")
//                    .setPositiveButton("Open Location Settings", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        }
//                    }).setNegativeButton("Cancel", null)
//                    .show();
        }


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);
    }


    public void stopGettingLocation() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {

        double lat = location.getLatitude();
        double lng = location.getLongitude();
        latitude = String.valueOf(lat);
        longitude = String.valueOf(lng);
        Log.i("Loghhhg", latitude + " " + longitude);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
