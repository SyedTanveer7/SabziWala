package com.example.customer.Webservices.Models;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

public class VendorLocation {

    @SerializedName("Lattitude")
    private String latitude;
    @SerializedName("Longitude")
    private String longitude;
    @SerializedName("location")
    private String locationName;
    private Location loc;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLocation(Location location) {
        this.loc = location;
    }

    public Location getLocation() {
        loc = new Location("");
        loc.setLatitude(Double.parseDouble(latitude));
        loc.setLongitude(Double.parseDouble(longitude));
        return loc;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
