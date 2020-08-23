package com.plannet.apps.diarybook.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.plannet.apps.diarybook.DatabaseHandler;
import com.plannet.apps.diarybook.DatabaseHandlerController;
import com.plannet.apps.diarybook.ErrorMsg;
import com.plannet.apps.diarybook.models.VisitModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
public class VisitDao extends DatabaseHandlerController {
    public static final String TABLE_NAME = "Visit";
    public static final String id = "id";
    public static final String customerName = "customerName";
    public static final String customerId = "customerId";
    public static final String visit_date = "visit_date";
    public static final String visit_time = "visit_time";
    public static final String place ="place";
    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;

    public VisitDao(Context context) {
        this.context = context;
    }
    public void insertCustomerVisitStatus( List<VisitModel> visitModels) {

        try {
            dbhelper = DatabaseHandler.getInstance(context);
            sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.beginTransaction();



            for (VisitModel tuple :visitModels ) {
                Object[] values_ar = {tuple.getCustomerName(),tuple.getCustomerId(), tuple.getVisit_date(), tuple.getVisit_time(),tuple.getPlace()};

                String[] fields_ar = {customerName,customerId, visit_date,visit_time,place};
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
                    Log.d("VisitModels Insert", query);
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

    public List<VisitModel> prepareCustomerDiaryLinesModel(ArrayList<ArrayList<String>> data) {
        List<VisitModel> visitModels = new ArrayList<>();
        for (ArrayList<String> tuple : data) {
            VisitModel temp = new VisitModel();
            temp.setId(CommonUtils.toInt(tuple.get(0)));
            temp.setCustomerName((tuple.get(1)));
            temp.setCustomerId(CommonUtils.toInt(tuple.get(2)));
            temp.setVisit_date((tuple.get(3)));
            temp.setVisit_time((tuple.get(4)));
            temp.setPlace((tuple.get(5)));
            visitModels.add(temp);
        }
        return visitModels;

    }

}


