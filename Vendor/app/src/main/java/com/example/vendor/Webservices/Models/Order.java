package com.example.vendor.Webservices.Models;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("Id")
    private int id;
    @SerializedName("customerName")
    private String customerName;
    @SerializedName("date")
    private String date;
    @SerializedName("time")
    private String time;
    @SerializedName("totalProducts")
    private String totalProducts;
    @SerializedName("totalBill")
    private String totalBill;
    @SerializedName("status")
    private String status;
    @SerializedName("customerAddress")
    private String customerAddress;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(String totalProducts) {
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

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}

