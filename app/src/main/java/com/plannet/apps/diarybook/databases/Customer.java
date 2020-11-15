package com.plannet.apps.diarybook.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.plannet.apps.diarybook.DatabaseHandler;
import com.plannet.apps.diarybook.DatabaseHandlerController;
import com.plannet.apps.diarybook.ErrorMsg;
import com.plannet.apps.diarybook.models.CustomerContact;
import com.plannet.apps.diarybook.models.CustomerModel;
import com.plannet.apps.diarybook.models.UserModel;
import com.plannet.apps.diarybook.utils.CommonUtils;
import java.util.ArrayList;
import java.util.List;


public class Customer extends DatabaseHandlerController {
    public static final String TABLE_NAME = "Customer";
    public static final String id = "id";
    public static final String customer_id = "customer_id";
    public static final String customer_name = "customer_name";
    public static final String customer_code = "customer_code";
    public static final String country = "country";
    public static final String city = "city";
    public static final String address1 = "address1";
    public static final String address2 = "address2";
    public static final String email = "email";
    public static final String phone_no = "phone_no";
    public static final String region = "region";
    public static final String location = "location";
    public static final String locationId = "locationId";
    public static final String gst_no = "gst_no";

    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;
    public Customer(Context context) {
        this.context = context;
    }


    public void insertCustomers(List<CustomerModel> customers) {

        dbhelper = DatabaseHandler.getInstance(context);
        sqliteDB = dbhelper.getWritableDatabase();
        sqliteDB.beginTransaction();
        int count = 0;
        try {

            String[] fields_ar = {Customer.customer_id, Customer.customer_name, Customer.customer_code,
                    Customer.country, Customer.city, Customer.address1, Customer.address2,Customer.email,
                    Customer.phone_no,Customer.location, Customer.gst_no};

            for (CustomerModel customer : customers) {
                count++;
                Object[] values_ar;
                if (customer.getCustomerContact()!=null){
                     values_ar = new Object[]{customer.getCustomerId(), customer.getCustomerName(), customer.getCustomerCode(),
                             customer.getCustomerContact().getCountry(), customer.getCustomerContact().getCity(), customer.getCustomerContact().getAddress1(),
                             customer.getCustomerContact().getAddress2(), customer.getCustomerContact().getEmail(),
                             customer.getCustomerContact().getContactNo(), customer.getLocation(), customer.getGst_no()};
                }else {
                     values_ar = new Object[]{customer.getCustomerId(),customer.getCustomerName(),customer.getCustomerCode(),
                            "","","",
                           "","",
                            "",customer.getLocation(),customer.getGst_no()};
                }

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
                        Log.d("Insert Customer", query);
                        sqliteDB.execSQL(query);
                    }
            }
            sqliteDB.setTransactionSuccessful();
        } catch (Exception e) {
            ErrorMsg.showError(context, "Error while running DB query", e, "");
        } finally {
            sqliteDB.endTransaction();
            //sqliteDB.close();
            dbhelper.close();

        }
    }

    public void deleteAll() {
        super.delete(context,TABLE_NAME,"");

    }

    public List<CustomerModel> selectAllCustomer(String user_name) {

        String query = "select * from " + TABLE_NAME + " where  userName =" + CommonUtils.quoteString( user_name );

        List<CustomerModel> customerModelList = prepareModelList( super.executeQuery( context, query ) );
        return customerModelList;
    }



    public List<CustomerModel> getAll() {
        String query="select * from "+TABLE_NAME;
        List<CustomerModel> list = prepareModelList(super.executeQuery(context,query));

        return list;

    }

    public CustomerModel getCustomer(int cusId) {
        String query = "select * from " + TABLE_NAME + " where customer_id= " + cusId;
        List<CustomerModel> list = prepareModelList( super.executeQuery( context, query ) );
        if (list != null&&list.size()>0) {
            return list.get( 0 );
        }
        return null;
    }


    public ArrayList<CustomerModel> prepareModelList(ArrayList<ArrayList<String>> data)

    {
        ArrayList<CustomerModel> customerModels=new ArrayList<>();
        for (ArrayList<String> tuple:data){

            CustomerModel temp= new CustomerModel();
            CustomerContact customerContact=new CustomerContact();
            temp.setId( CommonUtils.toInt(tuple.get(0)));
            temp.setCustomerId(CommonUtils.toInt(tuple.get(1)) );
            temp.setCustomerName(tuple.get(2));// Add this in getTitle() // + (tuple.get(16) != null ? "("+tuple.get(16)+")":"")); // add location name if exists
            temp.setCustomerCode(tuple.get(3));
            customerContact.setCountry(tuple.get(4));
            customerContact.setCity(tuple.get(5));
            customerContact.setAddress1(tuple.get(6));
            customerContact.setAddress2(tuple.get(7));
            customerContact.setEmail(tuple.get(8));
            customerContact.setContactNo(tuple.get(9));
            temp.setRegion(tuple.get(10));
            temp.setLocation(tuple.get(11));
            //temp.setLocationId(CommonUtils.toInt(tuple.get(19)));
            temp.setGst_no(tuple.get(12));
            temp.setCustomerContact( customerContact );
            customerModels.add(temp);

        }

        return customerModels;
    }

}