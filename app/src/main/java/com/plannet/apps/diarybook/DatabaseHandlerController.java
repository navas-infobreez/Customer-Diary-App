package com.plannet.apps.diarybook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseHandlerController {


    private final String TAG="DatabaseHandlerController";

    protected void delete(Context context, String tableName, String statement) {
        Log.d(TAG, "delete");
        DatabaseHandler dbhelper = DatabaseHandler.getInstance(context);
        SQLiteDatabase sqliteDB = dbhelper.getWritableDatabase();
        int count = sqliteDB.delete(tableName, statement, null);
        //sqliteDB.close();
        dbhelper.close();
        Log.d("query", statement + ". " + count + " rows deleted");
    }


    public static String getSqliteVersion(Context context) {
        DatabaseHandler dbhelper = DatabaseHandler.getInstance(context);
        SQLiteDatabase sqliteDB = dbhelper.getReadableDatabase();
        Cursor cursor = sqliteDB.rawQuery("select sqlite_version() AS sqlite_version", null);
        String sqliteVersion = "";
        while(cursor.moveToNext()){
            sqliteVersion += cursor.getString(0);
        }
        cursor.close();
        sqliteDB.close();
        dbhelper.close();
        Log.d("sqlite version: ", sqliteVersion);
        return sqliteVersion;
    }

    protected void delete(Context context, String tableName, String statement, SQLiteDatabase sqliteDB) {
        try {
            Log.d(TAG, "delete");
            sqliteDB.delete(tableName, statement, null);
            Log.d("query", statement);
        }catch (Exception e){
            throw e;
        }
    }

    protected void execute(Context context, String query) {
        try {
            Log.d("insertines",query);
            DatabaseHandler dbhelper = DatabaseHandler.getInstance(context);
            SQLiteDatabase sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.execSQL(query);
            //sqliteDB.close();
            dbhelper.close();
        } catch (Exception e) {
            //ErrorMsg.showError(context, "Error while running DB query", e, TAG);
            Log.d(TAG,e.getMessage());
        }
    }


    protected int execInsert(Context context, String query) {
        long newId = 0;
        try {
            Log.d("query",query);
            DatabaseHandler dbhelper = DatabaseHandler.getInstance(context);
            SQLiteDatabase sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.execSQL(query);
            Cursor cursor = sqliteDB.rawQuery("select last_insert_rowid() AS newId", null);
            while(cursor.moveToNext()){
                newId = cursor.getLong(0);
            }
            //sqliteDB.close();
            dbhelper.close();
        } catch (Exception e) {
           // ErrorMsg.showError(context, "Error while running DB query", e, TAG);
            Log.d(TAG,e.getMessage());
        }
        return (int) newId;
    }

    protected void execute(Context context, String query, Object[] bindArgs) {
        try {
            Log.d("query",query);
            Log.d("values", Arrays.toString(bindArgs));
            DatabaseHandler dbhelper = DatabaseHandler.getInstance(context);
            SQLiteDatabase sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.execSQL(query, bindArgs);
            //sqliteDB.close();
            dbhelper.close();
        } catch (Exception e) {
           // ErrorMsg.showError(context, "Error while running DB query", e, TAG);
        }
    }
    protected void execute(Context context, String query, Object[] bindArgs,SQLiteDatabase sqLiteDatabase) {
        try {
            Log.d("query",query);
            Log.d("values", Arrays.toString(bindArgs));
            //DatabaseHandler defautDbhelper = DatabaseHandler.getArchive(context);

            //SQLiteDatabase sqliteDB = defautDbhelper.getWritableDatabase();
            sqLiteDatabase.execSQL(query, bindArgs);
            //sqliteDB.close();
            //defautDbhelper.close();
        } catch (Exception e) {
            //ErrorMsg.showError(context, "Error while running DB query", e, TAG);
        }
    }
    protected void execute(Context context, String query,SQLiteDatabase sqLiteDatabase) {
        try {
            Log.d("querytoarchive",query);

            sqLiteDatabase.execSQL(query);
            //sqliteDB.close();
        } catch (Exception e) {
            //ErrorMsg.showError(context, "Error while running DB query", e, TAG);
            Log.d(TAG,e.getMessage());
            throw e;
        }
    }

    protected ArrayList<ArrayList<String>> executeQuery(Context context, String query) {
        return executeQuery(context, query,null);
    }
    protected ArrayList<ArrayList<String>> executeQuery(Context context, String query,SQLiteDatabase sqLiteDatabase,boolean fromArchiveDb) {
        return executeQuery(context, query, null,sqLiteDatabase);
    }

    protected ArrayList<ArrayList<String>> executeQuery(Context context, String query, String[] selectionArgs) {
        Log.d("query",query);
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        DatabaseHandler dbhelper = null;//DatabaseHandler.getInstance(context);
        SQLiteDatabase sqliteDB = null;
        Cursor c = null;
        try {
            dbhelper = DatabaseHandler.getInstance(context);
            sqliteDB = dbhelper.getReadableDatabase();
            c = sqliteDB.rawQuery(query, selectionArgs);
            if (c.moveToFirst()) {
                do {
                    ArrayList<String> subList = new ArrayList<String>();
                    for (int i = 0; i < c.getColumnCount(); i++) {
                        subList.add(c.getString(i));
                    }
                    list.add(subList);
                } while (c.moveToNext());
                Log.d("query", "" + list.size() +" rows");
            } else {
                Log.d("query", "0 rows");
            }
        } catch (Exception e) {
            Log.e("DB", e.getMessage());
            throw e;
        } finally {
            if(c != null)
                c.close();
            //sqliteDB.close();
            if(dbhelper != null)
                dbhelper.close();
        }
        return list;
    }

    protected ArrayList<ArrayList<String>> executeQuery(Context context, String query, String[] selectionArgs,SQLiteDatabase sqLiteDatabase) {
        Log.d("query",query);
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        Cursor c = null;
        try {
            c = sqLiteDatabase.rawQuery(query, selectionArgs);
            if (c.moveToFirst()) {
                do {
                    ArrayList<String> subList = new ArrayList<String>();
                    for (int i = 0; i < c.getColumnCount(); i++) {
                        subList.add(c.getString(i));
                    }
                    list.add(subList);
                } while (c.moveToNext());
                Log.d("query", "" + list.size() +" rows");
            } else {
                Log.d("query", "0 rows");
            }
        } catch (Exception e) {
            Log.e("DB", e.getMessage());
            throw e;
        } finally {
            if(c != null)
                c.close();
        }
        return list;
    }


}