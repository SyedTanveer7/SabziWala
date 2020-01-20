package com.example.admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.admin.Webservices.Models.Admin;

public class SaveSessionCredentials {

    static final String Admin_ID = "id";
    static final String Admin_UserName = "userName";
    static final String Admin_Name = "name";
    static final String Admin_Password = "password";
    static final String Admin_Mobile = "mobile";
    static final String Admin_Address = "address";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setAdmin(Context ctx, Admin admin) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Admin_ID, admin.getId());
        editor.putString(Admin_UserName, admin.getUserName());
        editor.putString(Admin_Password, admin.getPassword());
        editor.putString(Admin_Name, admin.getName());
        editor.putString(Admin_Mobile, admin.getMobile());
        editor.putString(Admin_Address, admin.getAddress());
        editor.commit();
    }


    public static String getAdmin_UserName(Context ctx) {
        return getSharedPreferences(ctx).getString(Admin_UserName, "");
    }

    public static String getAdmin_ID(Context ctx) {
        return getSharedPreferences(ctx).getString(Admin_ID, "");
    }

    public static String getAdmin_Name(Context ctx) {
        return getSharedPreferences(ctx).getString(Admin_Name, "");
    }

    public static String getAdmin_Password(Context ctx) {
        return getSharedPreferences(ctx).getString(Admin_Password, "");
    }

    public static String getAdmin_Mobile(Context ctx) {
        return getSharedPreferences(ctx).getString(Admin_Mobile, "");
    }

    public static String getAdmin_Address(Context ctx) {
        return getSharedPreferences(ctx).getString(Admin_Address, "");
    }


}
