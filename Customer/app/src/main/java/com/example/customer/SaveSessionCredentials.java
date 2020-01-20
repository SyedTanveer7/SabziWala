package com.example.customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.customer.Webservices.Models.User;

public class SaveSessionCredentials {

    static final String CUSTOMER_ID = "id";
    static final String CUSTOMER_UserName = "userName";
    static final String CUSTOMER_Name = "name";
    static final String CUSTOMER_Password = "password";
    static final String CUSTOMER_Mobile = "mobile";
    static final String CUSTOMER_Address = "address";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setCustomer(Context ctx, User user) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(CUSTOMER_ID, user.getId());
        editor.putString(CUSTOMER_UserName, user.getUsername());
        editor.putString(CUSTOMER_Password, user.getPassword());
        editor.putString(CUSTOMER_Name, user.getName());
        editor.putString(CUSTOMER_Mobile, user.getMobile());
        editor.putString(CUSTOMER_Address, user.getAddress());
        editor.commit();
    }


    public static String getCustomer_UserName(Context ctx) {
        return getSharedPreferences(ctx).getString(CUSTOMER_UserName, "");
    }

    public static String getCustomer_ID(Context ctx) {
        return getSharedPreferences(ctx).getString(CUSTOMER_ID, "");
    }

    public static String getCustomer_Name(Context ctx) {
        return getSharedPreferences(ctx).getString(CUSTOMER_Name, "");
    }

    public static String getCustomer_Password(Context ctx) {
        return getSharedPreferences(ctx).getString(CUSTOMER_Password, "");
    }

    public static String getCustomer_Mobile(Context ctx) {
        return getSharedPreferences(ctx).getString(CUSTOMER_Mobile, "");
    }

    public static String getCustomer_Address(Context ctx) {
        return getSharedPreferences(ctx).getString(CUSTOMER_Address, "");
    }


}
