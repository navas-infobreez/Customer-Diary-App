package com.plannet.apps.diarybook.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerModel {

    int id;
    @SerializedName("firstName")
    @Expose
    String customerName;
    @SerializedName("customerId")
    @Expose
    int customerId;
    @SerializedName("searchKey")
    @Expose
    String customerCode;

    String location;

    int locationId;

    String region;
    @SerializedName("11111111")
    @Expose
    String gst_no;


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    CustomerContact customerContact;

    public CustomerContact getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(CustomerContact customerContact) {
        this.customerContact = customerContact;
    }

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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getGst_no() {
        return gst_no;
    }

    public void setGst_no(String gst_no) {
        this.gst_no = gst_no;
    }

}





