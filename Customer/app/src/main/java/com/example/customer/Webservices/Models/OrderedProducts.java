package com.example.customer.Webservices.Models;

import com.google.gson.annotations.SerializedName;

public class OrderedProducts {
    @SerializedName("productId")
    int productId;
    @SerializedName("productQuantity")
    int productQuantity;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
}
