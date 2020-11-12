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

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {


            jsonObject.put("username", "superuser");
            jsonObject.put("password", "superuser");
            Log.d("orderjson", jsonObject.toString());

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
            Log.d("orderjson", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }


    public JSONObject customerDiaryJson(CustomerDiaryModel customerDiaryModel) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("invoiceNo",customerDiaryModel.getInvoice_no());
            jsonObject.put("quotationNo",customerDiaryModel.getQuotationNo() );
            jsonObject.put("searchKey", "");
            jsonObject.put("description",customerDiaryModel.getDescripion() );
            jsonObject.put("active", true);
            if (customerDiaryModel.isVisit()){
                jsonObject.put("purpose", "VISTED");
            }else if (customerDiaryModel.isQuotation()){
                jsonObject.put("purpose", "QUOTATION");
            }else {
                jsonObject.put("purpose","INVOICE");
            }

            jsonObject.put("shiptoCustomerAddress", customerDiaryModel.getCustomerAddress());
            jsonObject.put("customerId",customerDiaryModel.getCustomerId() );
            jsonObject.put("salesRepId", customerDiaryModel.getSalesmanId());
            jsonObject.put("totalAmount", customerDiaryModel.getTotalAmount());
            jsonObject.put("status", customerDiaryModel.getState());
            jsonObject.put("CustomerDiaryLineDTOList", diaryLineJson( customerDiaryModel.getCustomerDiaryLineDTOList() ));
            Log.d("orderjson", jsonObject.toString());

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
            jsonObject.put("CustomerContact", customerContactJson( customerModel.getCustomerContact() ));
            Log.d("orderjson", jsonObject.toString());

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
                studentJSON.put("remoteId",c.getHeaderId() );
                studentJSON.put("productId", c.getProduct_id());
                studentJSON.put("uomId","");
                studentJSON.put("productCategoryId",c.getCategory());
                studentJSON.put("salesPrice",c.getPrice());
                studentJSON.put("Qty", c.getQty());
                studentJSON.put("Description","" );
                jArray.put(studentJSON);
            }
            Log.d("line", jArray.toString());
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
            Log.d("contcat", jsonObject.toString());

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

            jsonObject.put("userContact", null);


            JSONArray payments = new JSONArray();
            List<RoleModel> lines = new ArrayList<>();
            RoleModel line = new RoleModel();
            line.setRoleId( 1 );
            line.setRoleName( userModel.getRole_name() );
            line.setActive( true );
            line.setDescription( userModel.getRole_name() );
            line.setPriority( 1 );
            lines.add( line );

            if(lines != null ) {
                Gson gson = new Gson();
                JSONArray json_lines = new JSONArray();
                Type type = new TypeToken<List<UserModel>>() {}.getType();
                json_lines = new JSONArray(gson.toJson(lines, type));
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


            Log.d("Category", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }


    public JSONObject ProductJson(ProductModel productModel) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id",0);
            jsonObject.put("name",productModel.getProduct_name() );
            jsonObject.put("description",productModel.getDescription() );
            jsonObject.put("searchKey", "dd");
            jsonObject.put("active", true);
            jsonObject.put("productCategoryId", 1);


            List<ProductPriceDto> lines = new ArrayList<>();
            ProductPriceDto line = new ProductPriceDto();
            //line.setId( 0 );
            line.setUomId( 1 );
            line.setPurchasePrice( CommonUtils.toBigDecimal( "100" ));
            line.setSalesPrice( CommonUtils.toBigDecimal( "120" ));
            line.setDiscntSalesPrice( CommonUtils.toBigDecimal( "110" ));
            //line.setProductId( 0 );
            lines.add( line );

            if(lines != null ) {
                Gson gson = new Gson();
                JSONArray json_lines = new JSONArray();
                Type type = new TypeToken<List<ProductPriceDto>>() {}.getType();
                json_lines = new JSONArray(gson.toJson(lines, type));
                jsonObject.put("productPriceDTOList", json_lines);
            }

            Log.d("Products", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public JSONObject UomJson(UomModel uomModel) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("id",uomModel.getId());
            jsonObject.put("name",uomModel.getName() );
            jsonObject.put("description",uomModel.getDescription() );
            jsonObject.put("searchKey", uomModel.getSearchKey());
            jsonObject.put("active", uomModel.isActive());
            Log.d("uom", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }
}
