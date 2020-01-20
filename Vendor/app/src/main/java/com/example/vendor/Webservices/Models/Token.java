package com.example.vendor.Webservices.Models;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("token")
    String token;
    @SerializedName("shopId")
    String shopId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
