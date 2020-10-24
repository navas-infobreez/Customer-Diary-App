package com.plannet.apps.diarybook.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.plannet.apps.diarybook.DatabaseHandler;
import com.plannet.apps.diarybook.DatabaseHandlerController;
import com.plannet.apps.diarybook.ErrorMsg;
import com.plannet.apps.diarybook.models.RoleModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class Role extends DatabaseHandlerController {

    public static final String TABLE_NAME = "Role";
    public static final String id = "id";
    public static final String role_name = "role_name";
    public static final String role_id = "role_id";
    public static final String description = "description";
    public static final String priority = "priority";
    public static final String isActive = "isActive";
    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;

    public Role(Context context) {
        this.context = context;
    }
    public void insertRole( List<RoleModel> roleModels) {

        try {
            dbhelper = DatabaseHandler.getInstance(context);
            sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.beginTransaction();



            for (RoleModel tuple :roleModels ) {
                Object[] values_ar = {tuple.getRoleName(),tuple.getRoleId(),tuple.getDescription(),tuple.getPriority(),tuple.isActive()?1:0};

                String[] fields_ar = {role_name,role_id,description,priority,isActive};
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

    public List<RoleModel> getAllRoles() {

        String query = "select * from " + TABLE_NAME ;

        List<RoleModel> roleModels = prepareRoleModel( super.executeQuery( context, query ) );
        return roleModels;
    }

    public List<RoleModel> prepareRoleModel(ArrayList<ArrayList<String>> data) {
        List<RoleModel> roleModels = new ArrayList<>();
        for (ArrayList<String> tuple : data) {
            RoleModel temp = new RoleModel();
            temp.setId(CommonUtils.toInt(tuple.get(0)));
            temp.setRoleName((tuple.get(1)));
            temp.setRoleId(CommonUtils.toInt(tuple.get(2)));
            temp.setDescription(tuple.get(3));
            temp.setPriority(CommonUtils.toInt(tuple.get(4)));
            int active=CommonUtils.toInt(tuple.get(5));
            temp.setActive(active==1);
            roleModels.add(temp);
        }
        return roleModels;

    }

}



