package com.example.vendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.vendor.Webservices.Models.Vendor;
import com.google.gson.annotations.SerializedName;

public class SaveSessionCredentials {

    static final String Vendor_ShopID = "shopID";
    static final String Vendor_ShopName = "shopName";
    static final String Vendor_VendorName = "vendorName";
    static final String Vendor_Password = "password";
    static final String Vendor_Image = "image";
    static final String Vendor_Latitude = "latitude";
    static final String Vendor_Longitude = "longitude";
    static final String Vendor_Mobile = "mobile";
    static final String Vendor_Location = "location";
    static final String Vendor_Comission = "comission";
    static final String Vendor_Token = "token";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setVendor(Context ctx, Vendor vendor, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Vendor_ShopID, vendor.getShopId());
        editor.putString(Vendor_ShopName, vendor.getShopName());
        editor.putString(Vendor_VendorName, vendor.getVendorEmail());
        editor.putString(Vendor_Image, vendor.getImage());
        editor.putString(Vendor_Latitude, vendor.getLattitude());
        editor.putString(Vendor_Longitude, vendor.getLongitude());
        editor.putString(Vendor_Mobile, vendor.getMobile());
        editor.putString(Vendor_Location, vendor.getLocation());
        editor.putString(Vendor_Comission, vendor.getComission());
        editor.putString(Vendor_Token, token);
        editor.commit();
    }

    public static String getShopId(Context ctx) {
        return getSharedPreferences(ctx).getString(Vendor_ShopID, "");
    }

    public static String getShopName(Context ctx) {
        return getSharedPreferences(ctx).getString(Vendor_ShopName, "");
    }

    public static String getVendorName(Context ctx) {
        return getSharedPreferences(ctx).getString(Vendor_VendorName, "");
    }

    public static String getImage(Context ctx) {
        return getSharedPreferences(ctx).getString(Vendor_Image, "");
    }

    public static String getLatitude(Context ctx) {
        return getSharedPreferences(ctx).getString(Vendor_Latitude, "");
    }

    public static String getLongitude(Context ctx) {
        return getSharedPreferences(ctx).getString(Vendor_Longitude, "");
    }

    public static String getMobile(Context ctx) {
        return getSharedPreferences(ctx).getString(Vendor_Mobile, "");
    }

    public static String getLocation(Context ctx) {
        return getSharedPreferences(ctx).getString(Vendor_Location, "");
    }

    public static String getComision(Context ctx) {
        return getSharedPreferences(ctx).getString(Vendor_Comission, "");
    }

    public static String getToken(Context ctx) {
        return getSharedPreferences(ctx).getString(Vendor_Token, "");
    }
    public static String getPassword(Context ctx) {
        return getSharedPreferences(ctx).getString(Vendor_Password, "");
    }


}
