package com.plannet.apps.diarybook.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCategoryModel {

    int id;
    @SerializedName("name")
    @Expose
    String categoryName;
    @SerializedName("CategoryId")
    @Expose
    int productCategoryId;

    int parentCategoryId;
    @SerializedName("searchKey")
    @Expose
    String searchKey;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("active")
    @Expose
    boolean active;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
}
