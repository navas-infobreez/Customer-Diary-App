package com.plannet.apps.diarybook.databases;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.plannet.apps.diarybook.DatabaseHandler;
import com.plannet.apps.diarybook.DatabaseHandlerController;
import com.plannet.apps.diarybook.ErrorMsg;
import com.plannet.apps.diarybook.forms.UomModel;
import com.plannet.apps.diarybook.models.RoleModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class UomDao extends DatabaseHandlerController {

    public static final String TABLE_NAME = "Uom";
    public static final String id = "id";
    public static final String name = "name";
    public static final String searchKey = "searchKey";
    public static final String description = "description";
    public static final String isActive = "isActive";
    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;

    public UomDao(Context context) {
        this.context = context;
    }
    public void insertUom( List<UomModel> uomModels) {

        try {
            dbhelper = DatabaseHandler.getInstance(context);
            sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.beginTransaction();



            for (UomModel tuple :uomModels ) {
                Object[] values_ar = {tuple.getName(),tuple.getSearchKey(),tuple.getDescription(),tuple.isActive()?1:0};

                String[] fields_ar = {name,searchKey,description,isActive};
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
                    Log.d("Role Insert", query);
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

    public List<UomModel> prepareModel(ArrayList<ArrayList<String>> data) {
        List<UomModel> uomModels = new ArrayList<>();
        for (ArrayList<String> tuple : data) {
            UomModel temp = new UomModel();
            temp.setId(CommonUtils.toInt(tuple.get(0)));
            temp.setName((tuple.get(1)));
            temp.setSearchKey(tuple.get(2));
            temp.setDescription(tuple.get(3));
            temp.setDescription(tuple.get(4));
            int active=CommonUtils.toInt(tuple.get(5));
            temp.setActive(active==1);
            uomModels.add(temp);
        }
        return uomModels;

    }

}



