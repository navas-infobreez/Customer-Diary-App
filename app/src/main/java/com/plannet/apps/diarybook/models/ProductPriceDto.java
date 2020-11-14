package com.plannet.apps.diarybook.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class ProductPriceDto {

       transient int id;
        @SerializedName("remoteId")
        @Expose
        int productPriceId;

        @SerializedName("uomId")
        @Expose
       int uomId;
        @SerializedName("purchasePrice")
         @Expose
        BigDecimal purchasePrice;
        @SerializedName("salesPrice")
         @Expose
        BigDecimal salesPrice;
        @SerializedName("discntSalesPrice")
        @Expose
        BigDecimal discntSalesPrice;
        @SerializedName("productId")
         @Expose
         int productId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUomId() {
        return uomId;
    }

    public void setUomId(int uomId) {
        this.uomId = uomId;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public BigDecimal getDiscntSalesPrice() {
        return discntSalesPrice;
    }

    public void setDiscntSalesPrice(BigDecimal discntSalesPrice) {
        this.discntSalesPrice = discntSalesPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductPriceId() {
        return productPriceId;
    }

    public void setProductPriceId(int productPriceId) {
        this.productPriceId = productPriceId;
    }
}
