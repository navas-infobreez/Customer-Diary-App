package com.plannet.apps.diarybook.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CustomerDiaryModel {

     int id ;
    @SerializedName("remoteId")
    @Expose
    int diaryId ;

     String customerName;
    @SerializedName("customerId")
    @Expose
     int customerId ;
    @SerializedName("diaryDate")
    @Expose
     String date ;

     String time ;

     String salesman_name;
    @SerializedName("salesRepId")
    @Expose
     int salesmanId ;
    @SerializedName("invoiceNo")
    @Expose
     String invoice_no ;
    @SerializedName("description")
    @Expose
     String descripion ;

    @SerializedName("status")
    @Expose
     String status;
    @SerializedName("customerAddress")
    @Expose
     String customerAddress;

     String customerPhone;
    @SerializedName("quotationNo")
    @Expose
     String quotationNo;

     boolean isVisit;

     boolean isQuotation;

     boolean isInvoiced;
    @SerializedName("totalAmount")
    @Expose
     BigDecimal totalAmount;
    @SerializedName("customerDiaryLineDTOList")
    @Expose
     List<CustomerDiaryLineModel>CustomerDiaryLineDTOList=new ArrayList<>();

    @SerializedName("active")
    @Expose
    boolean isActive;

    @SerializedName("documentNo")
    @Expose
    int  documentNo;

    @SerializedName("createdDate")
    @Expose
    String  createdDate;

    @SerializedName("shiptoCustomerAddress")
    @Expose
    boolean  isShipToCustomerAddress;

    @SerializedName("purpose")
    @Expose
    String  purpose;

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
    @SerializedName("pinNo")
    @Expose
    String pinNo;
    @SerializedName("address1")
    @Expose
    String address1;
    @SerializedName("address2")
    @Expose
    String address2;
    @SerializedName("contactNo")
    @Expose
    String contactNo;

    CustomerModel customerModel=new CustomerModel();


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<CustomerDiaryLineModel> getCustomerDiaryLineDTOList() {
        return CustomerDiaryLineDTOList;
    }

    public void setCustomerDiaryLineDTOList(List<CustomerDiaryLineModel> customerDiaryLineDTOList) {
        CustomerDiaryLineDTOList = customerDiaryLineDTOList;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getQuotationNo() {
        return quotationNo;
    }

    public void setQuotationNo(String quotationNo) {
        this.quotationNo = quotationNo;
    }

    public boolean isVisit() {
        return isVisit;
    }

    public void setVisit(boolean visit) {
        isVisit = visit;
    }

    public boolean isQuotation() {
        return isQuotation;
    }

    public void setQuotation(boolean quotation) {
        isQuotation = quotation;
    }

    public boolean isInvoiced() {
        return isInvoiced;
    }

    public void setInvoiced(boolean invoiced) {
        isInvoiced = invoiced;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSalesman_name() {
        return salesman_name;
    }

    public void setSalesman_name(String salesman_name) {
        this.salesman_name = salesman_name;
    }

    public int getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(int salesmanId) {
        this.salesmanId = salesmanId;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getDescripion() {
        return descripion;
    }

    public void setDescripion(String descripion) {
        this.descripion = descripion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(int diaryId) {
        this.diaryId = diaryId;
    }

    public int getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(int documentNo) {
        this.documentNo = documentNo;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isShipToCustomerAddress() {
        return isShipToCustomerAddress;
    }

    public void setShipToCustomerAddress(boolean shipToCustomerAddress) {
        isShipToCustomerAddress = shipToCustomerAddress;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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

    public String getPinNo() {
        return pinNo;
    }

    public void setPinNo(String pinNo) {
        this.pinNo = pinNo;
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

    public CustomerModel getCustomerModel() {
        return customerModel;
    }

    public void setCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }
}
