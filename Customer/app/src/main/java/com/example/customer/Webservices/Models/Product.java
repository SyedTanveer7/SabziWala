package com.example.customer.Webservices.Models;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("ProductId")
    private String id;
    @SerializedName("ProductName")
    private String name;
    @SerializedName("ProductImg")
    private String image;
    @SerializedName("Price")
    private String price;
    @SerializedName("ShopName")
    private String shopName;
    @SerializedName("Quantity")
    private String quantity;

    private boolean selected = false;


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

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
