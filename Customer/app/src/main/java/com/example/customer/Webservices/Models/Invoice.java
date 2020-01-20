package com.example.customer.Webservices.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Invoice {

    @SerializedName("userId")
    int userId;
    @SerializedName("shopId")
    String vendorId;
    @SerializedName("products")
    List<OrderedProducts> products;
    @SerializedName("totalProducts")
    int totalProducts;
    @SerializedName("totalBill")
    String totalBill;
    @SerializedName("status")
    String status;
    @SerializedName("date")
    String date;
    @SerializedName("time")
    String time;
    @SerializedName("customerLatitude")
    String customerLatitude;
    @SerializedName("customerLongitude")
    String customerLongitude;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public List<OrderedProducts> getProducts() {
        return products;
    }

    public void setProducts(List<OrderedProducts> products) {
        this.products = products;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public String getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(String totalBill) {
        this.totalBill = totalBill;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCustomerLatitude() {
        return customerLatitude;
    }

    public void setCustomerLatitude(String customerLatitude) {
        this.customerLatitude = customerLatitude;
    }

    public String getCustomerLongitude() {
        return customerLongitude;
    }

    public void setCustomerLongitude(String customerLongitude) {
        this.customerLongitude = customerLongitude;
    }
}

