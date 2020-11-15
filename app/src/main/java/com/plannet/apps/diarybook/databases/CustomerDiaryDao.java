package com.plannet.apps.diarybook.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.plannet.apps.diarybook.DatabaseHandler;
import com.plannet.apps.diarybook.DatabaseHandlerController;
import com.plannet.apps.diarybook.ErrorMsg;
import com.plannet.apps.diarybook.activity.PendingDiaryFragment;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.models.ProductCategoryModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.plannet.apps.diarybook.DatabaseHandler.ALL;
import static com.plannet.apps.diarybook.DatabaseHandler.APPROVED;
import static com.plannet.apps.diarybook.DatabaseHandler.APPROVERETURN;
import static com.plannet.apps.diarybook.DatabaseHandler.COMPLETED;
import static com.plannet.apps.diarybook.DatabaseHandler.PENDING;
import static com.plannet.apps.diarybook.DatabaseHandler.PICKED;


public class CustomerDiaryDao extends DatabaseHandlerController {

    public static final String TABLE_NAME = "CustomerDiary";
    public static final String id = "id";
    public static final String diaryId = "diaryId";
    public static final String customerName = "customerName";
    public static final String address = "address";
    public static final String phone = "phone";
    public static final String customerId = "customerId";
    public static final String date = "date";
    public static final String time = "time";
    public static final String salesman_name = "salesman_name";
    public static final String salesmanId = "salesmanId";
    public static final String invoice_no = "invoice_no";
    public static final String quotation_no = "quotation_no";
    public static final String descripion = "descripion";
    public static final String status = "status";
    public static final String isVisit = "isVisit";
    public static final String isQuotation = "isQuotation";
    public static final String isInvoiced = "isInvoiced";
    public static final String totalAmount = "totalAmount";

    private DatabaseHandler dbhelper;
    private SQLiteDatabase sqliteDB;
    private Context context;
    Customer customerDb;
    CustomerDiaryLinesDao customerDiaryLinesDao;

    public CustomerDiaryDao(Context context) {
        this.context = context;
        customerDb=new Customer(context);
        customerDiaryLinesDao=new CustomerDiaryLinesDao(context);
    }
    public void insertCustomerDiary( List<CustomerDiaryModel> customerDiaryModels) {

        try {
            dbhelper = DatabaseHandler.getInstance(context);
            sqliteDB = dbhelper.getWritableDatabase();
            sqliteDB.beginTransaction();

            for (CustomerDiaryModel tuple :customerDiaryModels ) {
                if (tuple.getStatus()==null)
                    tuple.setStatus(PENDING);
                if (tuple.getStatus().equals("DRAFTED")) {
                    tuple.setStatus(PENDING);
                    tuple.setSalesmanId(0);
                }
                if (tuple.getPurpose()!=null&&tuple.getPurpose().equals("VISTED")){
                    tuple.setVisit(true);
                    tuple.setQuotation(false);
                    tuple.setInvoiced(false);
                }else if (tuple.getPurpose()!=null&&tuple.getPurpose().equals("QUOTATION")){
                    tuple.setVisit(false);
                    tuple.setQuotation(true);
                    tuple.setInvoiced(false);
                }else if (tuple.getPurpose()!=null&&tuple.getPurpose().equals("INVOICED")){
                    tuple.setVisit(false);
                    tuple.setQuotation(false);
                    tuple.setInvoiced(true);
                }

                Object[] values_ar = {tuple.getDiaryId(),tuple.getCustomerName(),tuple.getCustomerAddress(),tuple.getCustomerPhone(),tuple.getCustomerId(), tuple.getDate(),
                        tuple.getTime(),tuple.getSalesman_name(), tuple.getSalesmanId(),
                        tuple.getInvoice_no(),tuple.getQuotationNo(),tuple.getDescripion(), tuple.getStatus(),tuple.isVisit()?1:0,tuple.isQuotation()?1:0,tuple.isInvoiced()?1:0,
                        tuple.getTotalAmount()};

                String[] fields_ar = {diaryId,customerName,address,phone,customerId, date,time,salesman_name,salesmanId,invoice_no,quotation_no,descripion,
                        status,isVisit,isQuotation,isInvoiced,totalAmount};
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
                    Log.d("Customer Diary Insert", query);
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
    public List<CustomerDiaryModel> getAll() {
        String query="select * from "+TABLE_NAME;
        List<CustomerDiaryModel> list = prepareCustomerDiaryModel(super.executeQuery(context,query));

        return list;

    }

    public List<CustomerDiaryModel> getCustomerDiary(String status) {
        String query="select * from "+TABLE_NAME ;
        if (!status.equalsIgnoreCase( ALL )) {
            query=query+" where status = " + CommonUtils.quoteString( status );
        }
        List<CustomerDiaryModel> list = prepareCustomerDiaryModel(super.executeQuery(context,query));

        return list;

    }

    public List<CustomerDiaryModel> getCustomerCurrentDiary(int salesmanID,String status) {
        String query="select * from "+TABLE_NAME+ " where salesmanId in ("+ salesmanID +"," +0+")";
        if(status.equals(ALL)){
            query=query+" and status in (" + CommonUtils.quoteString( COMPLETED )+","+ CommonUtils.quoteString(PICKED)+","+
                    CommonUtils.quoteString( APPROVED )+")";
        }else if (!status.equalsIgnoreCase( ALL )) {
            query=query+" and status = " + CommonUtils.quoteString( status );
        }
        List<CustomerDiaryModel> list = prepareCustomerDiaryModel(super.executeQuery(context,query));

        return list;

    }

    public List<CustomerDiaryModel> getCustomerDiary(int salesmanID,String status) {
        String query="select * from "+TABLE_NAME+ " where salesmanId in ("+ salesmanID +"," +0+")";
        if(status.equals(PENDING)){
            query=query+" and status in (" + CommonUtils.quoteString( PENDING )+","+ CommonUtils.quoteString(APPROVERETURN)+")";
        }else if (!status.equalsIgnoreCase( ALL )) {
            query=query+" and status = " + CommonUtils.quoteString( status );
        }
        List<CustomerDiaryModel> list = prepareCustomerDiaryModel(super.executeQuery(context,query));

        return list;

    }

    public CustomerDiaryModel getAll(int id) {
        String query="select * from "+TABLE_NAME+ " where diaryId = "+ id;
        List<CustomerDiaryModel> list = prepareCustomerDiaryModel(super.executeQuery(context,query));

        return list.size()>0?list.get(0):null;

    }

    public CustomerDiaryModel getCustomerDiary(int customerId) {
        String query="select * from "+TABLE_NAME+ " where customerId = "+ customerId;
        List<CustomerDiaryModel> list = prepareCustomerDiaryModel(super.executeQuery(context,query));

        return list.size()>0?list.get(0):null;

    }

    public List<CustomerDiaryModel> getAll(String status) {
        String query="select * from "+TABLE_NAME+ " where status = "+CommonUtils.quoteString( status );
        List<CustomerDiaryModel> list = prepareCustomerDiaryModel(super.executeQuery(context,query));

        return list;

    }

    public List<CustomerDiaryModel> prepareCustomerDiaryModel(ArrayList<ArrayList<String>> data) {
        List<CustomerDiaryModel> customerDiaryModels = new ArrayList<>();
        for (ArrayList<String> tuple : data) {
            CustomerDiaryModel temp = new CustomerDiaryModel();
            temp.setId(CommonUtils.toInt(tuple.get(0)));
            temp.setDiaryId(CommonUtils.toInt(tuple.get(1)));
            temp.setCustomerName((tuple.get(2)));
            temp.setCustomerAddress((tuple.get(3)));
            temp.setCustomerPhone((tuple.get(4)));
            temp.setCustomerId(CommonUtils.toInt(tuple.get(5)));
            temp.setDate((tuple.get(6)));
            temp.setTime((tuple.get(7)));
            temp.setSalesman_name((tuple.get(8)));
            temp.setSalesmanId(CommonUtils.toInt(tuple.get(9)));
            temp.setInvoice_no(tuple.get(10));
            temp.setQuotationNo(tuple.get(11));
            temp.setDescripion(tuple.get(12));
            temp.setStatus(tuple.get(13));
            int visit=CommonUtils.toInt(tuple.get(14));
            temp.setVisit(visit==1);
            int quotation=CommonUtils.toInt(tuple.get(15));
            temp.setQuotation(quotation==1);
            int invoiced=CommonUtils.toInt(tuple.get(16));
            temp.setInvoiced(invoiced==1);
            BigDecimal total=customerDiaryLinesDao.getSumOfLineTotal(CommonUtils.toInt(tuple.get(1)));
            temp.setTotalAmount(total);
            temp.setCustomerModel(customerDb.getCustomer(CommonUtils.toInt(tuple.get(5))));
            temp.setCustomerDiaryLineDTOList(customerDiaryLinesDao.getAll(CommonUtils.toInt(tuple.get(1))));
            customerDiaryModels.add(temp);
        }
        return customerDiaryModels;

    }

    public void delete() {
        String query="delete from "+TABLE_NAME;
        List<CustomerDiaryModel> list = prepareCustomerDiaryModel(super.executeQuery(context,query));

    }

    public void deleteall() {
        String query="delete from "+TABLE_NAME+ " where status not in('PICKED')";
        List<CustomerDiaryModel> list = prepareCustomerDiaryModel(super.executeQuery(context,query));

    }

    public void updateStatus(int i_d, String status, int salesmanID) {

        String query = "UPDATE " + TABLE_NAME + " set status =" + CommonUtils.quoteString( status ) +
                ", salesmanId ="+salesmanID+
                " where diaryId =" + i_d;
        super.execute( context, query );

    }

    public void updateDiaryStatus(int i_d, String status) {

        String query = "UPDATE " + TABLE_NAME + " set status =" + CommonUtils.quoteString( status ) +
                " where diaryId =" + i_d;
        super.execute( context, query );

    }

    public void updateDiary(int i_d,String status, String invoiceNo,String quotationNo,String discriptions,boolean visit,boolean invoiced,boolean quotation,String total) {
        int isvisit=visit?1:0;
        int isinvoiced=invoiced?1:0;
        int isquotation=quotation?1:0;

        String query = "UPDATE " + TABLE_NAME + " set status =" + CommonUtils.quoteString( status ) +
                ", invoice_no ="+CommonUtils.quoteString(invoiceNo)+
                ", quotation_no ="+CommonUtils.quoteString(quotationNo)+
                ", descripion ="+CommonUtils.quoteString(discriptions)+
                ", isVisit ="+isvisit+
                ", isQuotation ="+isquotation+
                ", isInvoiced ="+isinvoiced+
                ", totalAmount = "+CommonUtils.quoteString(total)
                +"  where diaryId =" + i_d;
        super.execute( context, query );

    }
}

