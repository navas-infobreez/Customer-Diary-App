package com.plannet.apps.diarybook.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.plannet.apps.diarybook.DatabaseHandler;
import com.plannet.apps.diarybook.DatabaseHandlerController;
import com.plannet.apps.diarybook.ErrorMsg;
import com.plannet.apps.diarybook.models.UserModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;


public class User extends DatabaseHandlerController {

    public static final String TABLE_NAME = "User";
    public static final String id = "id";
    public static final String name = "name";
    public static final String role_name = "role_name";
    public static final String role_id = "role_id";
    public static final String userName = "userName";
    public static final String password = "password";
    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;

    public User(Context context) {
        this.context = context;
    }
    public void insertUser( List<UserModel> userModels) {

        try {
            dbhelper = DatabaseHandler.getInstance(context);
            sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.beginTransaction();



            for (UserModel tuple :userModels ) {
                Object[] values_ar = {tuple.getName(),tuple.getRole_name(),tuple.getRole_id(),tuple.getUserName()
                ,tuple.getPassword()};

                String[] fields_ar = {name,role_name,role_id,userName,password};
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
                    Log.d("User Insert", query);
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

    public List<UserModel> prepareUserModels(ArrayList<ArrayList<String>> data) {
        List<UserModel> roleModels = new ArrayList<>();
        for (ArrayList<String> tuple : data) {
            UserModel temp = new UserModel();
            temp.setId(CommonUtils.toInt(tuple.get(0)));
            temp.setName((tuple.get(1)));
            temp.setRole_name((tuple.get(2)));
            temp.setRole_id(CommonUtils.toInt(tuple.get(3)));
            temp.setUserName((tuple.get(4)));
            temp.setPassword((tuple.get(5)));
            roleModels.add(temp);
        }
        return roleModels;

    }

    public UserModel selectUser(String user_name) {

        String query = "select * from " + TABLE_NAME + " where  userName =" + CommonUtils.quoteString( user_name );

        List<UserModel> userModelList = prepareUserModels( super.executeQuery( context, query ) );
        return userModelList.size() > 0 ? userModelList.get( 0 ) : null;
    }

    public UserModel getUser(int user_id) {

        String query = "select * from " + TABLE_NAME + " where  id =" + user_id;

        List<UserModel> userModelList = prepareUserModels( super.executeQuery( context, query ) );
        return userModelList.size() > 0 ? userModelList.get( 0 ) : null;
    }
    public List<UserModel> getAllUser() {

        String query = "select * from " + TABLE_NAME ;

        List<UserModel> userModelList = prepareUserModels( super.executeQuery( context, query ) );
        return userModelList;
    }

}

