package com.example.admin.Webservices.Models;

import com.google.gson.annotations.SerializedName;

public class Overview {

    @SerializedName("totalVendors")
    private String totalVendors;
    @SerializedName("totalSales")
    private String totalSales;
    @SerializedName("totalOrders")
    private String totalOrders;
    @SerializedName("totalComision")
    private String totalComission;
    @SerializedName("deliveredOrders")
    private String deliveredOrders;

    public String getTotalVendors() {
        return totalVendors;
    }

    public void setTotalVendors(String totalVendors) {
        this.totalVendors = totalVendors;
    }

    public String getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(String totalSales) {
        this.totalSales = totalSales;
    }

    public String getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(String totalOrders) {
        this.totalOrders = totalOrders;
    }

    public String getTotalComission() {
        return totalComission;
    }

    public void setTotalComission(String totalComission) {
        this.totalComission = totalComission;
    }

    public String getDeliveredOrders() {
        return deliveredOrders;
    }

    public void setDeliveredOrders(String deliveredOrders) {
        this.deliveredOrders = deliveredOrders;
    }
}
