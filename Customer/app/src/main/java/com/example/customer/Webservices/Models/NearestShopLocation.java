package com.example.customer.Webservices.Models;

import com.google.gson.annotations.SerializedName;

public class NearestShopLocation {
    @SerializedName("longitude")
    String longitude;
    @SerializedName("latitude")
    String latitude;


    public NearestShopLocation(String latitude,String longitude ) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}

