package com.plannet.apps.diarybook.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.plannet.apps.diarybook.DatabaseHandler;
import com.plannet.apps.diarybook.DatabaseHandlerController;
import com.plannet.apps.diarybook.ErrorMsg;
import com.plannet.apps.diarybook.forms.UomModel;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.models.ProductModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class Uom extends DatabaseHandlerController {
    public static final String TABLE_NAME = "Uom";
    public static final String id = "id";
    public static final String name="name";
    public static final String  searchKey="searchKey";
    public static final String  description="description";
    public static final String active="active";
    public static final String  remoteId="remoteId";
    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;


    public Uom(Context context) {
        this.context = context;
    }

    public void insertUom(List<UomModel> uomModels) {

        try {
            dbhelper = DatabaseHandler.getInstance(context);
            sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.beginTransaction();

            for (UomModel tuple : uomModels) {
                Object[] values_ar = {tuple.getName(), tuple.getSearchKey(), tuple.getDescription(),
                        tuple.isActive()?1:0, tuple.getUomId()};
                String[] fields_ar = { Uom.name,Uom.searchKey, Uom.description,
                        Uom.active, Uom.remoteId};
                String values = "", fields = "";
                for (int i = 0; i < values_ar.length; i++) {
                    if (values_ar[i] != null) {
                        values += CommonUtils.quoteIfString(values_ar[i]) + ",";
                        fields += fields_ar[i] + ",";
                    }
                }
                if (!values.isEmpty()) {
                    values = values.substring( 0, values.length() - 1 );
                    fields = fields.substring( 0, fields.length() - 1 );
                    String query = "INSERT INTO " + TABLE_NAME + "(" + fields + ") values(" + values + ");";
                    Log.d( "Insert Products", query );
                    sqliteDB.execSQL( query );
                }

            }
            sqliteDB.setTransactionSuccessful();
        } catch (Exception e) {
            ErrorMsg.showError(context, "Error while running DB query", e,"");
        } finally {
            if(sqliteDB != null) {
                sqliteDB.endTransaction();
            }
            if(dbhelper != null)
                dbhelper.close();
        }
    }
    public List<UomModel> getAll() {
        String query="select * from "+TABLE_NAME;
        List<UomModel> list = prepareUomModels(super.executeQuery(context,query));

        return list;

    }

    public void deleteAll() {
        String query="delete from "+TABLE_NAME;
        List<UomModel> list = prepareUomModels(super.executeQuery(context,query));

    }

    public UomModel getUom(int id) {
        String query="select * from "+TABLE_NAME+" where remoteId ="+id;
        List<UomModel> list = prepareUomModels(super.executeQuery(context,query));
        if (list.size()>0)
            return list.get(0);
        else
            return null;

    }

    public List<UomModel> prepareUomModels(ArrayList<ArrayList<String>> data)
    {
        List<UomModel> uomModels = new ArrayList<>();
        for (ArrayList<String> tuple : data) {
            UomModel temp = new UomModel();
            temp.setId( CommonUtils.toInt(tuple.get(0)));
            temp.setName(tuple.get(1));
            temp.setSearchKey(tuple.get(2));
            temp.setDescription(tuple.get(3));
            int active=CommonUtils.toInt(tuple.get(4));
            temp.setActive(active==1);
            temp.setUomId(CommonUtils.toInt(tuple.get(5)));
            uomModels.add(temp);

        }
        return uomModels;
    }
}
