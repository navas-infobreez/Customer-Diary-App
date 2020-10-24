package com.plannet.apps.diarybook.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UomModel {

    int id;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("uomId")
    @Expose
    int uomId;
    @SerializedName("searchKey")
    @Expose
    String searchKey;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("active")
    @Expose
    boolean active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public int getUomId() {
        return uomId;
    }

    public void setUomId(int uomId) {
        this.uomId = uomId;
    }
}
