package com.plannet.apps.diarybook.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.plannet.apps.diarybook.DatabaseHandler;
import com.plannet.apps.diarybook.DatabaseHandlerController;
import com.plannet.apps.diarybook.ErrorMsg;
import com.plannet.apps.diarybook.models.CustomerDiaryLineModel;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.models.ProductModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CustomerDiaryLinesDao extends DatabaseHandlerController {

    public static final String TABLE_NAME = "CustomerDiaryLines";
    public static final String id = "id";
    public static final String headerId = "headerId";
    public static final String product_name = "product_name";
    public static final String product_id = "product_id";
    public static final String qty = "qty";
    public static final String price = "price";
    public static final String details = "details";
    public static final String category = "category";
    public static final String uomId = "uomId";
    public static final String categoryId = "categoryId";
    public static final String diaryLineId = "diaryLineId";


    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;
    Products productsDb;
    public CustomerDiaryLinesDao(Context context) {
        this.context = context;
        productsDb=new Products(context);
    }

    public void insertCustomerDiaryLines( List<CustomerDiaryLineModel> customerDiaryLineModels) {

        try {
            dbhelper = DatabaseHandler.getInstance(context);
            sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.beginTransaction();



            for (CustomerDiaryLineModel tuple :customerDiaryLineModels ) {
                Object[] values_ar = {tuple.getHeaderId(),tuple.getProduct_name(), tuple.getProduct_id(), tuple.getQty(),tuple.getPrice(),tuple.getDetails(),tuple.getCategory(),
                tuple.getUomId(),tuple.getCategoryId(),tuple.getDiaryLineId()};

                String[] fields_ar = {headerId,product_name, product_id,qty,price,details,category,uomId,categoryId,diaryLineId};
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


    public void deleteAll() {
        String query="delete from "+TABLE_NAME;
        List<CustomerDiaryLineModel> list = prepareCustomerDiaryLinesModel(super.executeQuery(context,query));
    }

    public void deleteLine(int id) {
        super.delete(context,TABLE_NAME," id = "+id);

    }

    public CustomerDiaryLineModel getLine(int id) {
        String query="select * from "+TABLE_NAME+ " where id = "+ id;
        List<CustomerDiaryLineModel> list = prepareCustomerDiaryLinesModel(super.executeQuery(context,query));

        return list.size()>0?list.get(0):null;

    }

    public void updateDiary(CustomerDiaryLineModel customerDiaryLineModel) {

        String query = "UPDATE " + TABLE_NAME +
                " set qty =" + customerDiaryLineModel.getQty()+
                ", price ="+CommonUtils.quoteString( String.valueOf(customerDiaryLineModel.getPrice()) )+
                ", details ="+CommonUtils.quoteString( customerDiaryLineModel.getDetails() ) +
                ", uomId ="+customerDiaryLineModel.getUomId()+
                " where id =" + customerDiaryLineModel.getId();
        super.execute( context, query );

    }

    public List<CustomerDiaryLineModel> getAll(int id) {
        String query="select * from "+TABLE_NAME+ " where headerId = "+ id+" and qty >0 "+" order by category";
        List<CustomerDiaryLineModel> list = prepareCustomerDiaryLinesModel(super.executeQuery(context,query));

        return list;

    }


    public List<CustomerDiaryLineModel> prepareCustomerDiaryLinesModel(ArrayList<ArrayList<String>> data) {
        List<CustomerDiaryLineModel> customerDiaryLineModels = new ArrayList<>();
        for (ArrayList<String> tuple : data) {
            CustomerDiaryLineModel temp = new CustomerDiaryLineModel();
            temp.setId(CommonUtils.toInt(tuple.get(0)));
            temp.setHeaderId(CommonUtils.toInt(tuple.get(1)));

            temp.setProduct_id(CommonUtils.toInt(tuple.get(3)));
            temp.setQty(CommonUtils.toInt(tuple.get(4)));
            temp.setPrice(CommonUtils.toBigDecimal(tuple.get(5)));
            temp.setDetails(tuple.get(6));
            temp.setCategory(tuple.get(7));
            temp.setUomId(CommonUtils.toInt(tuple.get(8)));
            temp.setCategoryId(CommonUtils.toInt(tuple.get(9)));
            temp.setDiaryLineId(CommonUtils.toInt(tuple.get(10)));
            ProductModel productModel=productsDb.selectProductById(CommonUtils.toInt(tuple.get(3)));
            temp.setProduct_name(productModel.getProduct_name());
            customerDiaryLineModels.add(temp);
        }
        return customerDiaryLineModels;

    }

    public BigDecimal getSumOfLineTotal(int diaryId) {
        String query="select sum(" + price+") from "+ TABLE_NAME + " where headerId ="+ diaryId ;
        ArrayList<ArrayList<String>> result=super.executeQuery(context,query);

        BigDecimal temp=BigDecimal.ZERO;
        try {

            temp=new BigDecimal(result.get(0).get(0).toString());

        }
        catch (Exception e)
        {
            temp=BigDecimal.ZERO;
        }
        return temp;
    }
}

