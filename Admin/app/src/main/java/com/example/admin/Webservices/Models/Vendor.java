package com.example.admin.Webservices.Models;

import com.google.gson.annotations.SerializedName;

public class Vendor {

    @SerializedName("ShopId")
    private String shopId;
    @SerializedName("ShopName")
    private String shopName;
    @SerializedName("VendorName")
    private String vendorName;
    @SerializedName("Password")
    private String password;
    @SerializedName("image")
    private String image;
    @SerializedName("Lattitude")
    private String lattitude;
    @SerializedName("Longitude")
    private String longitude;
    @SerializedName("Mobile")
    private String mobile;
    @SerializedName("location")
    private String location;
    @SerializedName("comission")
    private String comission;
    @SerializedName("objectId")
    private String objectId;


    public String getLocation() {
        return location;
    }


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getComission() {
        return comission;
    }

    public void setComission(String comission) {
        this.comission = comission;
    }
}
