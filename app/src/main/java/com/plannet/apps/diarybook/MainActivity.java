package com.plannet.apps.diarybook;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.plannet.apps.diarybook.SyncManager.DiaryBookJsonObjectRequest;
import com.plannet.apps.diarybook.SyncManager.JsonFormater;
import com.plannet.apps.diarybook.activity.LoginActivity;
import com.plannet.apps.diarybook.activity.PendingDiaryFragment;
import com.plannet.apps.diarybook.databases.Customer;
import com.plannet.apps.diarybook.databases.CustomerDiaryDao;
import com.plannet.apps.diarybook.databases.CustomerDiaryLinesDao;
import com.plannet.apps.diarybook.databases.ProductCategory;
import com.plannet.apps.diarybook.databases.ProductPrice;
import com.plannet.apps.diarybook.databases.Products;
import com.plannet.apps.diarybook.databases.Role;
import com.plannet.apps.diarybook.databases.Uom;
import com.plannet.apps.diarybook.databases.User;
import com.plannet.apps.diarybook.forms.CreateProductsActivity;
import com.plannet.apps.diarybook.forms.ReceptionForm;
import com.plannet.apps.diarybook.forms.UomModel;
import com.plannet.apps.diarybook.forms.UserCreationActivity;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.models.CustomerModel;
import com.plannet.apps.diarybook.models.ProductCategoryModel;
import com.plannet.apps.diarybook.models.ProductModel;
import com.plannet.apps.diarybook.models.ProductPriceDto;
import com.plannet.apps.diarybook.models.RoleModel;
import com.plannet.apps.diarybook.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Fragment[]  PAGES;
    String []  PAGE_TITLES;
    public PendingDiaryFragment pendingDiaryFragment=new PendingDiaryFragment(true);
    public PendingDiaryFragment currentDiaryFragment=new PendingDiaryFragment(false);
    public MyPagerAdapter myPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    public int customerId;
    Role roleDb;
    User userDb;
    CustomerDiaryDao customerDiaryDb;
    Products productsDb;
    Customer customerDb;
    Uom uomDb;
    ProductCategory productCategoryDb;
    SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );
        initDb();
        myPagerAdapter = new MyPagerAdapter( getSupportFragmentManager() );
        viewPager = (ViewPager) findViewById( R.id.view_pager );
        viewPager.setAdapter( myPagerAdapter );
        tabLayout = (TabLayout) findViewById( R.id.tab_layout );
        tabLayout.setupWithViewPager( viewPager );

        viewPager.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        } );
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById( R.id.bottom_navigation );

        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_user:
                        if (AppController.getInstance().getLoggedUser().getRole_name().equalsIgnoreCase( "Manager" )||
                                AppController.getInstance().getLoggedUser().getRole_name().equalsIgnoreCase( "Admin" )) {
                            Intent intent = new Intent(MainActivity.this, UserCreationActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(MainActivity.this,"You have no permission for this action",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.action_products:
                        if (AppController.getInstance().getLoggedUser().getRole_name().equalsIgnoreCase( "Manager" )||
                                AppController.getInstance().getLoggedUser().getRole_name().equalsIgnoreCase( "Admin" )) {
                            Intent intent1 = new Intent(MainActivity.this, CreateProductsActivity.class);
                            startActivity(intent1);
                        }else {
                            Toast.makeText(MainActivity.this,"You have no permission for this action",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.action_customer:
                        Intent intent2 = new Intent( MainActivity.this, ReceptionForm.class );
                        startActivity( intent2 );
                        break;
                }
                return true;
            }
        } );
//        if (!Preference.isDownloadCompleted()) {
//            final ProgressDialog progressBar = new ProgressDialog( this );
//            progressBar.setCancelable( true );//you can cancel it by pressing back button
//            progressBar.setMessage( " downloading data from server..." );
//            progressBar.setProgressStyle( ProgressDialog.STYLE_SPINNER );
//            progressBar.setProgress( 0 );//initially progress is 0
//            progressBar.setMax( 100 );//sets the maximum value 100
//            progressBar.show();//displays the progress bar
//            doDownload( new OnCompleteCallback() {
//
//                @Override
//                public void onError(Exception error) {
//                    if (Preference.isProductDownloaded() && Preference.isCategoryDownloaded() && Preference.isCustomerDownloaded()
//                            && Preference.isUomDownloaded() && Preference.isDiaryDownloaded() && Preference.isUserDownloaded()) {
//                        progressBar.dismiss();
//                        Preference.setDownloadCompleted( true );
//                    }
//                }
//
//                @Override
//                public void onComplete() {
//                    if (Preference.isProductDownloaded() && Preference.isCategoryDownloaded() && Preference.isCustomerDownloaded()
//                            && Preference.isUomDownloaded() && Preference.isDiaryDownloaded() && Preference.isUserDownloaded()) {
//                        progressBar.dismiss();
//                        Preference.setDownloadCompleted( true );
//                    }
//                }
//            } );
//        }
    }

    private void doDownload(OnCompleteCallback onCompleteCallback) {

        getAllUsers( onCompleteCallback );
        getallCustomers(onCompleteCallback);
        getAllProducts(onCompleteCallback);
        getAllProductsCategory(onCompleteCallback);
        getallUom(onCompleteCallback);
        getAllDiary(onCompleteCallback);


    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
            MenuInflater inflater = getMenuInflater();

            inflater.inflate(R.menu.option_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.sync) {
            sweetAlertDialog=new SweetAlertDialog(MainActivity.this,SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog .setTitleText("Downloading Datas..");
            sweetAlertDialog.setContentText( "Please wait" );
            sweetAlertDialog .show();


            getAllUsers( new OnCompleteCallback() {
                @Override
                public void onError(Exception error) {
                }
                @Override
                public void onComplete() {
                    getallCustomers( new OnCompleteCallback() {
                        @Override
                        public void onError(Exception error) {
                        }
                        @Override
                        public void onComplete() {
                            getAllProducts( new OnCompleteCallback() {
                                @Override
                                public void onError(Exception error) {
                                }
                                @Override
                                public void onComplete() {
                                    getAllProductsCategory( new OnCompleteCallback() {
                                        @Override
                                        public void onError(Exception error) {
                                        }
                                        @Override
                                        public void onComplete() {
                                            getallUom( new OnCompleteCallback() {
                                                @Override
                                                public void onError(Exception error) {
                                                }
                                                @Override
                                                public void onComplete() {
                                                    getAllDiary( new OnCompleteCallback() {
                                                        @Override
                                                        public void onError(Exception error) {
                                                        }
                                                        @Override
                                                        public void onComplete() {
                                                            if (pendingDiaryFragment!=null) {
                                                                pendingDiaryFragment.refreshview();
                                                            }
                                                            if (currentDiaryFragment!=null) {
                                                                currentDiaryFragment.refreshview();
                                                            }
                                                            sweetAlertDialog.dismiss();
                                                        }

                                                        @Override
                                                        public void onComplete(Object object) {
                                                        }
                                                    } );
                                                }

                                                @Override
                                                public void onComplete(Object object) {
                                                }
                                            } );
                                        }

                                        @Override
                                        public void onComplete(Object object) {
                                        }
                                    } );
                                }
                                @Override
                                public void onComplete(Object object) {
                                }
                            } );
                        }
                        @Override
                        public void onComplete(Object object) {
                        }
                    } );
                }

                @Override
                public void onComplete(Object object) {

                }
            } );

        }
        return super.onOptionsItemSelected( item );
    }

    private void  initDb(){
        roleDb=new Role(this);
        userDb=new User(this);
        customerDb =new Customer(this);
        productsDb=new Products(this);
        productCategoryDb = new ProductCategory(this);
        customerDiaryDb = new CustomerDiaryDao(this);
        uomDb=new Uom(this);
    }
     public void getAllUsers(final OnCompleteCallback onCompleteCallback) {
         Preference.setUserDownloaded( false );
         final String url = "https://planet-customerdiary.herokuapp.com/user/getalluserdetails";
         JsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, null,
                 new Response.Listener<JSONObject>() {
                     @Override
                     public void onResponse(JSONObject response) {
                         try {
                             VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                             Log.d( "Response", response.toString() );
                             JSONArray jsonArrayChanged = response.getJSONArray("result");
                             List<UserModel> userModelList=new ArrayList<>();
                             for(int i=0;i<jsonArrayChanged.length();i++){
                                 String str = jsonArrayChanged.getString(i);
                                 Gson gson = new Gson();
                                 UserModel userModel=gson.fromJson(str,UserModel.class);
                                 userModelList.add(userModel);
                             }
                             userDb.deleteAll();
                             userDb.insertUser(userModelList);
                             //progressBar.setMax( userModelList.size() );
                             List<UserModel>test=userDb.getAllUser();
                             Log.d( "Response", test.toString());
                             Preference.setUserDownloaded( true );
                             if (onCompleteCallback!=null){
                                 onCompleteCallback.onComplete();
                             }

                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Log.d( "Response", error.toString() );
                 Preference.setUserDownloaded( true );
                 if (onCompleteCallback!=null){
                     onCompleteCallback.onComplete();
                 }
             }
         } );

         AppController.getInstance().submitServerRequest( req, "getUser" );
     }

    public void getAllProducts(final OnCompleteCallback onCompleteCallback) {
        Preference.setProductDownloaded( false );
        final JsonFormater formatter = new JsonFormater();
        final String url = "https://planet-customerdiary.herokuapp.com/product/getallproduct";
        JsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );
                            JSONArray jsonArrayChanged = response.getJSONArray("result");
                            List<ProductModel> productModels=new ArrayList<>();
                            for(int i=0;i<jsonArrayChanged.length();i++){
                                String str = jsonArrayChanged.getString(i);
                                Gson gson = new Gson();
                                ProductModel productModel=gson.fromJson(str,ProductModel.class);
                                productModels.add(productModel);
                            }
                            ProductPrice productPriceDb=new ProductPrice(MainActivity.this);
                            productPriceDb.deleteAll();
                            productsDb.deleteAll();
                            productsDb.insertProducts(productModels);
                            List<ProductModel>test=productsDb.selectAll();
                            Log.d( "Response", test.toString());
                            Preference.setProductDownloaded( true );
                            if (onCompleteCallback!=null){
                                onCompleteCallback.onComplete();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "Response", error.toString() );
                Preference.setProductDownloaded( true );
                if (onCompleteCallback!=null){
                    onCompleteCallback.onComplete();
                }
            }
        } );

        AppController.getInstance().submitServerRequest( req, "getProduct" );
    }

    private void getAllDiary(final OnCompleteCallback onCompleteCallback) {
        Preference.setDiaryDownloaded( false );
        final String url = "https://planet-customerdiary.herokuapp.com/customerdiary/getallcustomerdiary";
        JsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );
                            JSONArray jsonArrayChanged = response.getJSONArray("result");
                            List<CustomerDiaryModel> customerDiaryModels=new ArrayList<>();
                            for(int i=0;i<jsonArrayChanged.length();i++){
                                String str = jsonArrayChanged.getString(i);
                                Gson gson = new Gson();
                                CustomerDiaryModel cDiary=gson.fromJson(str,CustomerDiaryModel.class);
                                customerDiaryModels.add(cDiary);
                            }
                            CustomerDiaryLinesDao customerDiaryLinesDao=new CustomerDiaryLinesDao(MainActivity.this);
                            customerDiaryLinesDao.deleteAll();
                            customerDiaryDb.deleteall();
                            customerDiaryDb.insertCustomerDiary(customerDiaryModels);
                            List<CustomerDiaryModel>test=customerDiaryDb.getAll();
                            Preference.setDiaryDownloaded( true );
                            Log.d( "Response", test.toString());
                            if (onCompleteCallback!=null){
                                onCompleteCallback.onComplete();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "Response", error.toString() );
                Preference.setDiaryDownloaded( true );
                if (onCompleteCallback!=null){
                    onCompleteCallback.onComplete();
                }
            }
        } );

        AppController.getInstance().submitServerRequest( req, "getDiary" );
    }


    public void getallUom(final OnCompleteCallback onCompleteCallback) {
        Preference.setUomDownloaded( false );
        final JsonFormater formatter = new JsonFormater();
        final String url = " https://planet-customerdiary.herokuapp.com/uom/getalluom";
        JsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );
                            JSONArray jsonArrayChanged = response.getJSONArray("result");
                            List<UomModel> uomModels=new ArrayList<>();
                            for(int i=0;i<jsonArrayChanged.length();i++){
                                String str = jsonArrayChanged.getString(i);
                                Gson gson = new Gson();
                                UomModel UomModel=gson.fromJson(str,UomModel.class);
                                uomModels.add(UomModel);
                            }
                            uomDb.deleteAll();
                            uomDb.insertUom(uomModels);
                            List<UomModel>test=uomDb.getAll();
                            Preference.setUomDownloaded( true );
                            Log.d( "Response", test.toString());
                            if (onCompleteCallback!=null){
                                onCompleteCallback.onComplete();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "Response", error.toString() );
                Preference.setUomDownloaded( true );
                if (onCompleteCallback!=null){
                    onCompleteCallback.onComplete();
                }
            }
        } );

        AppController.getInstance().submitServerRequest( req, "getUom" );
    }


    public void getallCustomers(final OnCompleteCallback onCompleteCallback) {
        Preference.setCustomerDownloaded( false );
        final JsonFormater formatter = new JsonFormater();
        final String url = "https://planet-customerdiary.herokuapp.com/customer/getallcustomerdetails";
        JsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );
                            JSONArray jsonArrayChanged = response.getJSONArray("result");
                            List<CustomerModel> customerModelList=new ArrayList<>();
                             for(int i=0;i<jsonArrayChanged.length();i++){
                                String str = jsonArrayChanged.getString(i);
                                Gson gson = new Gson();
                                CustomerModel customerModel=gson.fromJson(str,CustomerModel.class);
                                customerModelList.add(customerModel);
                            }
                             customerDb.deleteAll();
                            customerDb.insertCustomers(customerModelList);
                            List<CustomerModel>test=customerDb.getAll();
                            Log.d( "Response", test.toString());
                            Preference.setCustomerDownloaded( true );
                            if (onCompleteCallback!=null){
                                onCompleteCallback.onComplete();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "Response", error.toString() );
                Preference.setCustomerDownloaded( true );
                if (onCompleteCallback!=null){
                    onCompleteCallback.onComplete();
                }
            }
        } );

        AppController.getInstance().submitServerRequest( req, "getCustomer" );
    }


    public void getAllProductsCategory(final OnCompleteCallback onCompleteCallback) {
        Preference.setCategoryDownloaded( false );
        final JsonFormater formatter = new JsonFormater();
        final String url = "https://planet-customerdiary.herokuapp.com/product/getallproductcategory";
        JsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );
                            JSONArray jsonArrayChanged = response.getJSONArray("result");
                            List<ProductCategoryModel> productCategoryModels=new ArrayList<>();
                            for(int i=0;i<jsonArrayChanged.length();i++){
                                String str = jsonArrayChanged.getString(i);
                                Gson gson = new Gson();
                                ProductCategoryModel productCategoryModel=gson.fromJson(str, ProductCategoryModel.class);
                                productCategoryModels.add(productCategoryModel);
                            }
                            productCategoryDb.deleteAll();
                            productCategoryDb.insertProductCategory(productCategoryModels);
                            List<ProductCategoryModel>test=productCategoryDb.getAll();
                            Log.d( "Response", test.toString());
                            Preference.setCategoryDownloaded( true );
                            if (onCompleteCallback!=null){
                                onCompleteCallback.onComplete();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "Response", error.toString() );
                Preference.setCategoryDownloaded( true );
                if (onCompleteCallback!=null){
                    onCompleteCallback.onComplete();
                }
            }
        } );

        AppController.getInstance().submitServerRequest( req, "getProductCategory" );
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            PAGES = new Fragment[]{pendingDiaryFragment,currentDiaryFragment};
            PAGE_TITLES = new String[]{"Pending","Current"};

        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }

    }


}
