package com.plannet.apps.diarybook.databases;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.plannet.apps.diarybook.DatabaseHandler;
import com.plannet.apps.diarybook.DatabaseHandlerController;
import com.plannet.apps.diarybook.ErrorMsg;
import com.plannet.apps.diarybook.forms.UomModel;
import com.plannet.apps.diarybook.models.CustomerModel;
import com.plannet.apps.diarybook.models.ProductCategoryModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductCategory extends DatabaseHandlerController {

    public static final String TABLE_NAME = "ProductCategory";
    public static final String id = "id";
    public static final String category_name = "category_name";
    public static final String category_id = "category_id";
    public static final String parentCategoryId = "parentCategoryId";


    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;
    public ProductCategory(Context context){
        this.context=context;
    }


    public void insertProductCategory( List<ProductCategoryModel> productCategoryModels) {

        try {
            dbhelper = DatabaseHandler.getInstance(context);
            sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.beginTransaction();



            for (ProductCategoryModel tuple :productCategoryModels ) {
                    Object[] values_ar = {tuple.getCategoryName(),tuple.getProductCategoryId(), tuple.getParentCategoryId()};

                    String[] fields_ar = {category_name,category_id, parentCategoryId};
                    String values = "", fields = "";
                    for (int i = 0; i < values_ar.length; i++) {
                        if (values_ar[i] != null) {
                            values += CommonUtils.quoteIfString(values_ar[i]) + ",";
                            fields += fields_ar[i] + ",";
                        }
                    }
                    if (!values.isEmpty()) {
                        values = values.substring(0, values.length() - 1);
                        fields = fields.substring(0, fields.length() - 1);
                        String query = "INSERT INTO " + TABLE_NAME + "(" + fields + ") values(" + values + ");";
                        Log.d("Product Category Insert", query);
                        sqliteDB.execSQL(query);
                    }
            }
            sqliteDB.setTransactionSuccessful();
        } catch (Exception e) {
            ErrorMsg.showError(context, "Error while running DB query", e,"");
        } finally {
            sqliteDB.endTransaction();
            dbhelper.close();

        }

    }

    public void deleteAll() {
        super.delete(context,TABLE_NAME,"");

    }

    public List<ProductCategoryModel> getAll() {
        String query="select * from "+TABLE_NAME;
        List<ProductCategoryModel> list = prepareProductCAtegoryModel(super.executeQuery(context,query));

        return list;

    }

    public List<ProductCategoryModel> prepareProductCAtegoryModel(ArrayList<ArrayList<String>> data) {
        List<ProductCategoryModel> categoryModels = new ArrayList<>();
        for (ArrayList<String> tuple : data) {
            ProductCategoryModel temp = new ProductCategoryModel();
            temp.setId(CommonUtils.toInt(tuple.get(0)));
            temp.setCategoryName((tuple.get(1)));
            temp.setProductCategoryId(CommonUtils.toInt(tuple.get(2)));
            categoryModels.add(temp);
        }
        return categoryModels;

    }

}
