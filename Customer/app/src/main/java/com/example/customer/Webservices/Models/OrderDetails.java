package com.example.customer.Webservices.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetails {

    @SerializedName("orderId")
    String orderId;
    @SerializedName("shopName")
    String shopName;
    @SerializedName("products")
    List<Product> products;
    @SerializedName("totalBill")
    String totalBill;
    @SerializedName("date")
    String date;
    @SerializedName("location")
    String shopLocation;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(String totalBill) {
        this.totalBill = totalBill;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
