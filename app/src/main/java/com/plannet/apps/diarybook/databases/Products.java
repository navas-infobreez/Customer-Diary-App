package com.plannet.apps.diarybook.databases;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.plannet.apps.diarybook.DatabaseHandler;
import com.plannet.apps.diarybook.DatabaseHandlerController;
import com.plannet.apps.diarybook.ErrorMsg;
import com.plannet.apps.diarybook.forms.UomModel;
import com.plannet.apps.diarybook.models.ProductModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class  Products extends DatabaseHandlerController {

    public static final String TABLE_NAME = "Products";
    public static final String id = "id";
    public static final String product_name = "product_name";
    public static final String product_id = "product_id";
    public static final String product_category = "product_category";
    public static final String product_category_Id = "product_category_Id";
    public static final String uom = "uom";
    public static final String uomId= "uomId";
    public static final String description = "description";
    public static final String sale_price = "sale_price";
    public static final String cost_price = "cost_price";
    public static final String taxId = "taxId";

    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;
    ProductPrice productPriceDb;


    public Products(Context context) {
        this.context = context;
        productPriceDb=new ProductPrice(context);
    }

    public void insertProducts(List<ProductModel> productModel) {

        try {
            dbhelper = DatabaseHandler.getInstance(context);
            sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.beginTransaction();

            for (ProductModel tuple : productModel) {
                    Object[] values_ar = {tuple.getProduct_name(), tuple.getProduct_id(), tuple.getProduct_category(),
                            tuple.getProductCategoryId(), tuple.getUom(), tuple.getUomId(), tuple.getDescription(),
                            tuple.getSale_price(), tuple.getCost_price(), tuple.getTaxId()};
                    String[] fields_ar = { Products.product_name,Products.product_id, Products.product_category,
                            Products.product_category_Id, Products.uom, Products.uomId, Products.description,
                            Products.sale_price, Products.cost_price, Products.taxId};
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

                productPriceDb.insertProductPrice(tuple.getProductPriceDTOList(),tuple.getProduct_id());
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

    public void deleteAll() {
        super.delete(context,TABLE_NAME,"");

    }

    public List<ProductModel> prepareProductModels(ArrayList<ArrayList<String>> data)
    {
        List<ProductModel> productModels = new ArrayList<>();
        for (ArrayList<String> tuple : data) {
            ProductModel temp = new ProductModel();
            temp.setId( CommonUtils.toInt(tuple.get(0)));
            temp.setProduct_name(tuple.get(1));
            temp.setProduct_id(CommonUtils.toInt(tuple.get(2)));
            temp.setProduct_category(tuple.get(3));
            temp.setProductCategoryId(CommonUtils.toInt(tuple.get(4)));
            temp.setUom(tuple.get(5));
            temp.setUomId( CommonUtils.toInt(tuple.get(6)));
            temp.setDescription(tuple.get(7));
            temp.setSale_price(CommonUtils.toBigDecimal(tuple.get(8)));
            temp.setCost_price(CommonUtils.toBigDecimal(tuple.get(9)));
            temp.setProductPriceDTOList(productPriceDb.getAllById(CommonUtils.toInt(tuple.get(2))));
            temp.setTaxId(CommonUtils.toInt(tuple.get(10)));
            productModels.add(temp);

        }
        return productModels;
    }

    public List<ProductModel> selectAllByCategory(int categoryId) {
        String query = "select * from " + TABLE_NAME + " where  product_category_Id =" + categoryId;

        List<ProductModel> productModels = prepareProductModels( super.executeQuery( context, query ) );
        return productModels;
    }

    public ProductModel selectProductById(int id) {
        String query = "select * from " + TABLE_NAME + " where  product_id =" + id ;

        List<ProductModel> productModels = prepareProductModels( super.executeQuery( context, query ) );
        if (productModels.size()>0)
            return productModels.get(0);
        else
            return null;
    }

    public List<ProductModel> selectAll() {
        String query = "select * from " + TABLE_NAME ;

        List<ProductModel> productModels = prepareProductModels( super.executeQuery( context, query ) );
        return productModels;
    }

}

