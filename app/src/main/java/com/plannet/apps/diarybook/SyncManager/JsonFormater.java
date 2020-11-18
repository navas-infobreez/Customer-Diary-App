package com.plannet.apps.diarybook.SyncManager;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.plannet.apps.diarybook.forms.UomModel;
import com.plannet.apps.diarybook.models.CustomerContact;
import com.plannet.apps.diarybook.models.CustomerDiaryLineModel;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.models.CustomerModel;
import com.plannet.apps.diarybook.models.ProductCategoryModel;
import com.plannet.apps.diarybook.models.ProductModel;
import com.plannet.apps.diarybook.models.ProductPriceDto;
import com.plannet.apps.diarybook.models.RoleModel;
import com.plannet.apps.diarybook.models.UserContacts;
import com.plannet.apps.diarybook.models.UserModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonFormater {

    public JSONObject toJson(String user,String pass) {
        JSONObject jsonObject = new JSONObject();
        try {


            jsonObject.put("username", user);
            jsonObject.put("password", pass);
            Log.d("loginJson", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public JSONObject RollCreationJson(RoleModel roleModel) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id",roleModel.getId());
            jsonObject.put("name",roleModel.getRoleName() );
            jsonObject.put("description",roleModel.getDescription() );
            jsonObject.put("priority", roleModel.getPriority());
            jsonObject.put("active", roleModel.isActive());
            Log.d("RollCreationJson", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }


    public JSONObject customerDiaryJson(CustomerDiaryModel customerDiaryModel) {

        JSONObject jsonObject = new JSONObject();
        try {
            String s=customerDiaryModel.getStatus();
            jsonObject.put("remoteId",customerDiaryModel.getDiaryId()>0?customerDiaryModel.getDiaryId():0);
            if (customerDiaryModel.getInvoice_no()!=null&&!customerDiaryModel.getInvoice_no().isEmpty()&&!customerDiaryModel.getInvoice_no().equals( " " ))
                 jsonObject.put("invoiceNo",customerDiaryModel.getInvoice_no());
            if (customerDiaryModel.getQuotationNo()!=null&&!customerDiaryModel.getQuotationNo().isEmpty()&&!customerDiaryModel.getQuotationNo().equals( "" ))
                jsonObject.put("quotationNo",customerDiaryModel.getQuotationNo() );
            jsonObject.put("searchKey", "");
            jsonObject.put("status",s);
            jsonObject.put("description",customerDiaryModel.getDescripion() );
            jsonObject.put("active", true);
            if (customerDiaryModel.isVisit()){
                jsonObject.put("purpose", "VISTED");
            }else if (customerDiaryModel.isQuotation()){
                jsonObject.put("purpose", "QUOTATION");
            }else {
                jsonObject.put("purpose","INVOICE");
            }
            jsonObject.put("shiptoCustomerAddress", true);
            jsonObject.put("customerId",customerDiaryModel.getCustomerId() );
            jsonObject.put("salesRepId", customerDiaryModel.getSalesmanId());
            jsonObject.put("totalAmount", customerDiaryModel.getTotalAmount());
            if (customerDiaryModel.getCustomerDiaryLineDTOList()!=null&&customerDiaryModel.getCustomerDiaryLineDTOList().size()>0){
                jsonObject.put( "CustomerDiaryLineDTOList", diaryLineJson( customerDiaryModel.getCustomerDiaryLineDTOList() ) );
            }else {
                jsonObject.put( "CustomerDiaryLineDTOList",null );
            }
            Log.d("customerDiaryJson", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }


    public JSONObject customerJson(CustomerModel customerModel) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id",customerModel.getId());
            jsonObject.put("firstName",customerModel.getCustomerName() );
            jsonObject.put("lastName", "kk");
            jsonObject.put("searchKey","key" );
            jsonObject.put("gstNo", customerModel.getGst_no());
            jsonObject.put("customerContact", customerContactJson( customerModel.getCustomerContact() ));
            Log.d("customerJson", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public JSONArray diaryLineJson(List<CustomerDiaryLineModel> customerDiaryLineModel) {
        JSONArray jArray = new JSONArray();
        try {
            //Todo  please fill the json

            for (CustomerDiaryLineModel c : customerDiaryLineModel)
            {
                JSONObject studentJSON = new JSONObject();
                studentJSON.put("id",c.getId());
                studentJSON.put("remoteId",c.getHeaderId()>0?c.getHeaderId():0 );
                studentJSON.put("productId", c.getProduct_id());
                studentJSON.put("uomId",c.getUomId());
                studentJSON.put("productCategoryId",c.getCategoryId());
                studentJSON.put("salesPrice",c.getPrice());
                studentJSON.put("Qty", c.getQty());
                studentJSON.put("Description",c.getDetails() );
                jArray.put(studentJSON);
            }
            Log.d("diaryLineJson", jArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jArray;
    }

    public JSONObject customerContactJson(CustomerContact customerContact) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("email",customerContact.getEmail());
            jsonObject.put("country",customerContact.getCountry() );
            jsonObject.put("city", customerContact.getCity());
            jsonObject.put("state",customerContact.getState() );
            jsonObject.put("address1",customerContact.getAddress1());
            jsonObject.put("address2",customerContact.getAddress2() );
            jsonObject.put("contactNo", customerContact.getContactNo());
            jsonObject.put("contactNo2",customerContact.getContactNo2() );
            jsonObject.put("landmark", customerContact.getLandmark());
            Log.d("customerContactJson", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public JSONObject toUserJson(UserModel userModel) {
        JSONObject jsonObject = new JSONObject();
        try {


            jsonObject.put("remoteId", 0);
            jsonObject.put("firstName", userModel.getName());
            jsonObject.put("lastName", "h");
            jsonObject.put("userName", userModel.getUserName());
            jsonObject.put("password", userModel.getPassword());
            jsonObject.put("userContact", null);



            if(userModel.getRoles() != null ) {
                Gson gson = new Gson();
                JSONArray json_lines = new JSONArray();
                Type type = new TypeToken<List<UserModel>>() {}.getType();
                json_lines = new JSONArray(gson.toJson(userModel.getRoles(), type));
                jsonObject.put("roles", json_lines);
            }
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<UserContacts>>() {}.getType();
//            List<UserContacts> lines =gson.fromJson(userModel.getUserContacts(),type);
//            if(lines != null && lines.size()>0) {
//                //Gson gson = CustomGsonBuilder.getGson();
//                JSONArray json_lines = new JSONArray();
//                //Type type = new TypeToken<List<CustomerContactsModel>>() {}.getType();
//                json_lines = new JSONArray(gson.toJson(lines, type));
//                jsonObject.put("contacts", json_lines);
//            }

            Log.d("userJson", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public JSONObject CategoryJson(ProductCategoryModel productCategoryModel) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id",productCategoryModel.getId());
            jsonObject.put("name",productCategoryModel.getCategoryName() );
            jsonObject.put("description",productCategoryModel.getDescription() );
            jsonObject.put("priority", productCategoryModel.getSearchKey());
            jsonObject.put("active", productCategoryModel.isActive());


            Log.d("CategoryJson", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }


    public JSONObject ProductJson(ProductModel productModel) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("remoteId",productModel.getProduct_id()>0?productModel.getProduct_id():0);
            jsonObject.put("name",productModel.getProduct_name());
            jsonObject.put("description",productModel.getDescription() );
            jsonObject.put("searchKey", productModel.getProduct_name());
            jsonObject.put("active", true);
            jsonObject.put("productCategoryId", productModel.getProductCategoryId());
            jsonObject.put("productPriceDTOList", productPriceJson(productModel.getProductPriceDTOList()));


            Log.d("ProductJson", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public JSONArray productPriceJson(List<ProductPriceDto> productPriceDtoList) {
        JSONArray jArray = new JSONArray();
        try {
            for (ProductPriceDto c : productPriceDtoList)
            {
                JSONObject studentJSON = new JSONObject();
                studentJSON.put("remoteId",c.getProductPriceId()>0?c.getProductPriceId():0);
                studentJSON.put("productId", c.getProductId()>0?c.getProductId():0);
                studentJSON.put("uomId",c.getUomId()>0?c.getUomId():0);
                studentJSON.put("purchasePrice",c.getPurchasePrice());
                studentJSON.put("salesPrice",c.getSalesPrice());
                studentJSON.put("discntSalesPrice", c.getDiscntSalesPrice());
                jArray.put(studentJSON);
            }
            Log.d("productPriceJson", jArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jArray;
    }

    public JSONObject UomJson(UomModel uomModel) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id",uomModel.getId());
            jsonObject.put("name",uomModel.getName() );
            jsonObject.put("description",uomModel.getDescription() );
            jsonObject.put("searchKey", uomModel.getSearchKey());
            jsonObject.put("active", uomModel.isActive());
            Log.d("UomJson", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }
}
