package com.plannet.apps.diarybook.models;

import java.math.BigDecimal;

public class ProductPriceDto {

       int id;
       int uomId;
       BigDecimal purchasePrice;
       BigDecimal salesPrice;
       BigDecimal discntSalesPrice;
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
}
