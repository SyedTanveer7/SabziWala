package com.example.customer.Webservices;

import com.example.customer.Webservices.Models.Invoice;
import com.example.customer.Webservices.Models.NearestShopLocation;
import com.example.customer.Webservices.Models.OrderDetails;
import com.example.customer.Webservices.Models.Product;
import com.example.customer.Webservices.Models.RecentOrder;
import com.example.customer.Webservices.Models.User;
import com.example.customer.Webservices.Models.VendorLocation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebAPIs {

    @POST("getNearestShopProducts.php")
    Call<List<Product>> getNearestShopProducts(@Body NearestShopLocation location);

    @GET("getAllShopLocations.php")
    Call<List<VendorLocation>> getAllShopLocations();

    @POST("authenticateUser.php")
    Call<User> authenticateUser(@Body User user);

    @POST("registeruser.php")
    Call<User> registerUser(@Body User user);

    @POST("getInvoice.php")
    Call<Invoice> sendOrderInvoice(@Body Invoice invoice);

    @POST("getOrdersHistory.php")
    Call<List<RecentOrder>> getOrdersHistory(@Body RecentOrder order);

    @POST("updateUser.php")
    Call<User> updateUser(@Body User user);

    @POST("getOrderLog.php")
    Call<OrderDetails> getOrderDetails(@Body OrderDetails orderDetails);

}
