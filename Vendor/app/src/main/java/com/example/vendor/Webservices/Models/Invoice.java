package com.example.vendor.Webservices.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Invoice {

    @SerializedName("orderId")
    private String id;
    @SerializedName("customerName")
    private String name;
    @SerializedName("products")
    private List<Product> products;
    @SerializedName("status")
    private String status;
    @SerializedName("totalBill")
    private String totalBill;
    @SerializedName("date")
    private String date;
    @SerializedName("time")
    private String time;
    @SerializedName("address")
    private String address;
    @SerializedName("customerLatitude")
    private String customerLatitude;
    @SerializedName("customerLongitude")
    private String customerLongitude;


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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
