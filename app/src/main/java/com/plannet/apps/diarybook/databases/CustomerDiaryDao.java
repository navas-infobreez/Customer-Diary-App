package com.plannet.apps.diarybook.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.plannet.apps.diarybook.DatabaseHandler;
import com.plannet.apps.diarybook.DatabaseHandlerController;
import com.plannet.apps.diarybook.ErrorMsg;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.models.ProductCategoryModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;


public class CustomerDiaryDao extends DatabaseHandlerController {

    public static final String TABLE_NAME = "CustomerDiary";
    public static final String id = "id";
    public static final String customerName = "customerName";
    public static final String customerId = "customerId";
    public static final String date = "date";
    public static final String time = "time";
    public static final String salesman_name = "salesman_name";
    public static final String salesmanId = "salesmanId";
    public static final String invoice_no = "invoice_no";
    public static final String descripion = "descripion";
    public static final String status = "status";

    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;

    public CustomerDiaryDao(Context context) {
        this.context = context;
    }
    public void insertCustomerDiary( List<CustomerDiaryModel> customerDiaryModels) {

        try {
            dbhelper = DatabaseHandler.getInstance(context);
            sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.beginTransaction();



            for (CustomerDiaryModel tuple :customerDiaryModels ) {
                Object[] values_ar = {tuple.getCustomerName(),tuple.getCustomerId(), tuple.getDate(),
                        tuple.getTime(),tuple.getSalesman_name(), tuple.getSalesmanId(),
                        tuple.getInvoice_no(),tuple.getDescripion(), tuple.getStatus()};

                String[] fields_ar = {customerName,customerId, date,time,salesman_name,salesmanId,invoice_no,descripion,status};
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
                    Log.d("Customer Diary Insert", query);
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


    public List<CustomerDiaryModel> getAll(String status) {
        String query="select * from "+TABLE_NAME+ " where status = "+CommonUtils.quoteString( status );
        List<CustomerDiaryModel> list = prepareCustomerDiaryModel(super.executeQuery(context,query));

        return list;

    }

    public List<CustomerDiaryModel> prepareCustomerDiaryModel(ArrayList<ArrayList<String>> data) {
        List<CustomerDiaryModel> customerDiaryModels = new ArrayList<>();
        for (ArrayList<String> tuple : data) {
            CustomerDiaryModel temp = new CustomerDiaryModel();
            temp.setId(CommonUtils.toInt(tuple.get(0)));
            temp.setCustomerName((tuple.get(1)));
            temp.setCustomerId(CommonUtils.toInt(tuple.get(2)));
            temp.setDate((tuple.get(1)));
            temp.setTime((tuple.get(1)));
            temp.setSalesman_name((tuple.get(1)));
            temp.setSalesmanId(CommonUtils.toInt(tuple.get(2)));
            temp.setInvoice_no(tuple.get(2));
            temp.setDescripion(tuple.get(2));
            temp.setStatus(tuple.get(2));
            customerDiaryModels.add(temp);
        }
        return customerDiaryModels;

    }

    public void delete() {
        String query="delete from "+TABLE_NAME;
        List<CustomerDiaryModel> list = prepareCustomerDiaryModel(super.executeQuery(context,query));

    }
}

