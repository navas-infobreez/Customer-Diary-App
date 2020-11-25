package com.plannet.apps.diarybook.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.plannet.apps.diarybook.DatabaseHandler;
import com.plannet.apps.diarybook.DatabaseHandlerController;
import com.plannet.apps.diarybook.ErrorMsg;
import com.plannet.apps.diarybook.forms.UomModel;
import com.plannet.apps.diarybook.models.ProductPriceDto;
import com.plannet.apps.diarybook.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductPrice extends DatabaseHandlerController {

    public static final String TABLE_NAME = "ProductPrice";
    public static final String id = "id";
    public static final String productId="productId";
    public static final String  uomId="uomId";
    public static final String  purchasePrice="purchasePrice";
    public static final String salesPrice="salesPrice";
    public static final String  discntSalesPrice="discntSalesPrice";
    public static final String  productPriceId="productPriceId";

    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;

    public ProductPrice(Context context) {
        this.context = context;
    }

    public void insertProductPrice(List<ProductPriceDto> productPriceDtoList,int productId) {

        try {
            dbhelper = DatabaseHandler.getInstance(context);
            sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.beginTransaction();

            for (ProductPriceDto tuple : productPriceDtoList) {
                Object[] values_ar = {productId, tuple.getUomId(), tuple.getSalesPrice(),
                        tuple.getSalesPrice(), tuple.getDiscntSalesPrice(),tuple.getProductPriceId()};
                String[] fields_ar = { ProductPrice.productId,ProductPrice.uomId, ProductPrice.purchasePrice,
                        ProductPrice.salesPrice, ProductPrice.discntSalesPrice,ProductPrice.productPriceId};
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

    public List<ProductPriceDto> getAll() {
        String query="select * from "+TABLE_NAME;
        List<ProductPriceDto> list = prepareUomModels(super.executeQuery(context,query));

        return list;

    }

    public void deleteAll() {
        super.delete(context,TABLE_NAME,"");

    }

    public List<ProductPriceDto> getAllById(int productId) {
        String query="select * from "+TABLE_NAME + " where productId ="+productId;
        List<ProductPriceDto> list = prepareUomModels(super.executeQuery(context,query));

        return list;

    }

    public List<ProductPriceDto> prepareUomModels(ArrayList<ArrayList<String>> data)
    {
        List<ProductPriceDto> productPriceDtos = new ArrayList<>();
        for (ArrayList<String> tuple : data) {
            ProductPriceDto temp = new ProductPriceDto();
            temp.setId( CommonUtils.toInt(tuple.get(0)));
            temp.setProductId(CommonUtils.toInt(tuple.get(1)));
            temp.setUomId(CommonUtils.toInt(tuple.get(2)));
            temp.setPurchasePrice(CommonUtils.toBigDecimal(tuple.get(3)));
            temp.setSalesPrice(CommonUtils.toBigDecimal(tuple.get(4)));
            temp.setDiscntSalesPrice(CommonUtils.toBigDecimal(tuple.get(5)));
            temp.setProductPriceId(CommonUtils.toInt(tuple.get(6)));
            productPriceDtos.add(temp);

        }
        return productPriceDtos;
    }
}
