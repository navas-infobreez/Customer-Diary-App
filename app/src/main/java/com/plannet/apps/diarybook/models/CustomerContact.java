package com.plannet.apps.diarybook.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerContact {

    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("country")
    @Expose
    String country;
    @SerializedName("city")
    @Expose
    String city;
    @SerializedName("state")
    @Expose
    String state;
    @SerializedName("landmark")
    @Expose
    String landmark;
    @SerializedName("address1")
    @Expose
    String address1;
    @SerializedName("address2")
    @Expose
    String address2;
    @SerializedName("contactNo")
    @Expose
    String contactNo;
    @SerializedName("contactNo2")
    @Expose
    String contactNo2;
    @SerializedName("remoteId")
    @Expose
    int customerContactId;

    public int getCustomerContactId() {
        return customerContactId;
    }

    public void setCustomerContactId(int customerContactId) {
        this.customerContactId = customerContactId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getContactNo2() {
        return contactNo2;
    }

    public void setContactNo2(String contactNo2) {
        this.contactNo2 = contactNo2;
    }
}
