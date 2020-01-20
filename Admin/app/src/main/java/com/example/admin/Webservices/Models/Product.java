package com.example.admin.Webservices.Models;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("ProductId")
    private String id;
    @SerializedName("ProductName")
    private String name;
    @SerializedName("ProductImg")
    private String image;
    @SerializedName("ProductPrice")
    private String price;
    @SerializedName("ShopName")
    private String shopName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}