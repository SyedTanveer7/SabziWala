package com.example.admin.Webservices;

import com.example.admin.Webservices.Models.Admin;
import com.example.admin.Webservices.Models.Overview;
import com.example.admin.Webservices.Models.Product;
import com.example.admin.Webservices.Models.Report;
import com.example.admin.Webservices.Models.Vendor;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebAPIs {


    @GET("getproducts.php")
    Call<List<Product>> getProducts();

    @GET("getvendors.php")
    Call<List<Vendor>> getVendors();

    @GET("overview.php")
    Call<Overview> getAdminOverview();

    @POST("fetchadmin.php")
    Call<Admin> getAdminPr0file(@Body Admin admin);

    @POST("updateadmin.php")
    Call<Admin> updateAdminPr0file(@Body Admin admin);

    @POST("login.php")
    Call<Admin> authenticateAdmin(@Body Admin admin);

    @POST("addproduct.php")
    Call<Product> addProduct(@Body Product product);

    @POST("updateproduct.php")
    Call<Product> updateProduct(@Body Product product);

    @POST("updatevendor.php")
    Call<Vendor> updateVendor(@Body Vendor product);

    @POST("getShopproducts.php")
    Call<List<Product>> getShopProducts(@Body Vendor vendor);

    @POST("addvendor.php")
    Call<Vendor> addVendor(@Body Vendor vendor);

    @POST("deleteVendor.php")
    Call<Vendor> deleteVendor(@Body Vendor vendor);


    @POST("deleteProduct.php")
    Call<Product> deleteProduct(@Body Product product);


    @POST("getAdminSalesReport.php")
    Call<List<Report>> getSalesReport(@Body Report reportDates);

}
