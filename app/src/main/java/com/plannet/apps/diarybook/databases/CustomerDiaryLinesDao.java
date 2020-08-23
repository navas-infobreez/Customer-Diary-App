package com.plannet.apps.diarybook.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.plannet.apps.diarybook.DatabaseHandler;
import com.plannet.apps.diarybook.DatabaseHandlerController;
import com.plannet.apps.diarybook.ErrorMsg;
import com.plannet.apps.diarybook.models.CustomerDiaryLineModel;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerDiaryLinesDao extends DatabaseHandlerController {

    public static final String TABLE_NAME = "CustomerDiaryLines";
    public static final String id = "id";
    public static final String headerId = "headerId";
    public static final String product_name = "product_name";
    public static final String product_id = "product_id";
    public static final String qty = "qty";

    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;
    public CustomerDiaryLinesDao(Context context) {
        this.context = context;
    }

    public void insertCustomerDiaryLines( List<CustomerDiaryLineModel> customerDiaryLineModels) {

        try {
            dbhelper = DatabaseHandler.getInstance(context);
            sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.beginTransaction();



            for (CustomerDiaryLineModel tuple :customerDiaryLineModels ) {
                Object[] values_ar = {tuple.getHeaderId(),tuple.getProduct_name(), tuple.getProduct_id(), tuple.getQty()};

                String[] fields_ar = {headerId,product_name, product_id,qty};
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
                    Log.d("DiaryLines Insert", query);
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

    public List<CustomerDiaryLineModel> prepareCustomerDiaryLinesModel(ArrayList<ArrayList<String>> data) {
        List<CustomerDiaryLineModel> customerDiaryLineModels = new ArrayList<>();
        for (ArrayList<String> tuple : data) {
            CustomerDiaryLineModel temp = new CustomerDiaryLineModel();
            temp.setId(CommonUtils.toInt(tuple.get(0)));
            temp.setHeaderId(CommonUtils.toInt(tuple.get(1)));
            temp.setProduct_name((tuple.get(2)));
            temp.setProduct_id(CommonUtils.toInt(tuple.get(3)));
            temp.setQty(CommonUtils.toInt(tuple.get(4)));
            customerDiaryLineModels.add(temp);
        }
        return customerDiaryLineModels;

    }

}

