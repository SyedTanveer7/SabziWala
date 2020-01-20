package com.example.vendor.Webservices;

import com.example.vendor.Webservices.Models.Invoice;
import com.example.vendor.Webservices.Models.Order;
import com.example.vendor.Webservices.Models.Product;
import com.example.vendor.Webservices.Models.Report;
import com.example.vendor.Webservices.Models.Token;
import com.example.vendor.Webservices.Models.Vendor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebAPIs {


    @POST("authenticateVendor.php")
    Call<Vendor> authenticateVendor(@Body Vendor vendor);

    @POST("getVendorProfile.php")
    Call<Vendor> getVendorProfile(@Body Vendor vendor);

    @POST("updatevendor.php")
    Call<Vendor> updateVendor(@Body Vendor vendor);

    @POST("getShopOrderDetails.php")
    Call<Invoice> getInvoice(@Body Invoice order);

    @POST("updateOrderState.php")
    Call<Order> updateOrderState(@Body Order order);

    @POST("getOrders.php")
    Call<List<Order>> getCustomerOrders(@Body Vendor vendor);

    @POST("getShopSalesReport.php")
    Call<List<Report>> getSalesReport(@Body Report reportDates);

    @POST("registerToken.php")
    Call<Token> registerToken(@Body Token token);

    @POST("deleteToken.php")
    Call<Token> deleteToken(@Body Token token);


}
