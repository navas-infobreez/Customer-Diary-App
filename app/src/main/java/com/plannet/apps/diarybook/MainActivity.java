package com.plannet.apps.diarybook;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.plannet.apps.diarybook.SyncManager.DiaryBookJsonObjectRequest;
import com.plannet.apps.diarybook.SyncManager.JsonFormater;
import com.plannet.apps.diarybook.activity.PendingDiaryFragment;
import com.plannet.apps.diarybook.databases.Customer;
import com.plannet.apps.diarybook.databases.ProductCategory;
import com.plannet.apps.diarybook.databases.Products;
import com.plannet.apps.diarybook.databases.UomDao;
import com.plannet.apps.diarybook.forms.CreateProductsActivity;
import com.plannet.apps.diarybook.forms.ReceptionForm;
import com.plannet.apps.diarybook.forms.UomModel;
import com.plannet.apps.diarybook.forms.UserCreationActivity;
import com.plannet.apps.diarybook.models.CustomerModel;
import com.plannet.apps.diarybook.models.ProductCategoryModel;
import com.plannet.apps.diarybook.models.ProductModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2);

        myPagerAdapter=new MyPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(myPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
//        if (AppController.getInstance().getLoggedUser().getRole_name().equalsIgnoreCase( "Manager" )){
//            bottomNavigationView.getMenu().getItem(R.id.action_user  ).setEnabled( true );
//            bottomNavigationView.getMenu().getItem(R.id.action_products  ).setEnabled( true );
//        }else {
//            bottomNavigationView.setVisibility( View.GONE );
//            bottomNavigationView.getMenu().getItem(R.id.action_user  ).setEnabled( false );
//            bottomNavigationView.getMenu().getItem(R.id.action_products  ).setEnabled( false );
//        }



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_user:
                        Intent intent = new Intent(MainActivity.this, UserCreationActivity.class );
                        startActivity(intent);
                        break;
                    case R.id.action_products:
                        Intent intent1 = new Intent(MainActivity.this, CreateProductsActivity.class );
                        startActivity(intent1);
                        break;
                    case R.id.action_customer:
                        Intent intent2 = new Intent(MainActivity.this, ReceptionForm.class );
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        });
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
            getAllUsers();
            getAllProducts();
            getAllProductsCategory();
            getallUom();
            getallCustomers();

        }
        return super.onOptionsItemSelected( item );
    }

     public void getAllUsers() {
         final String url = "https://planet-customerdiary.herokuapp.com/user/getalluserdetails";
         JsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, null,
                 new Response.Listener<JSONObject>() {
                     @Override
                     public void onResponse(JSONObject response) {
                         try {
                             VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                             Log.d( "Response", response.toString() );

                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Log.d( "Response", error.toString() );

             }
         } );

         AppController.getInstance().submitServerRequest( req, "submitShipmet" );
     }

    public void getAllProducts() {
        final JsonFormater formatter = new JsonFormater();
        final String url = "https://planet-customerdiary.herokuapp.com/product/getallproduct";
        JsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = null;
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );
                            JSONObject json= new JSONObject(response.toString());  //your response
                            try {
                                 result = json.getString("result");    //result is key for which you need to retrieve data
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<ProductModel>>() {
                            }.getType();

                            final List<ProductModel> productModels = gson.fromJson(result,type);
                            AsyncTask.execute( new Runnable() {
                                @Override
                                public void run() {
                                    Products products=new Products( getApplicationContext() );
                                    products.insertProducts(productModels); // Also inserts lines
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "Response", error.toString() );

            }
        } );

        AppController.getInstance().submitServerRequest( req, "submitShipmet" );
    }


    public void getallUom() {
        final JsonFormater formatter = new JsonFormater();
        final String url = " https://planet-customerdiary.herokuapp.com/uom/getalluom";
        JsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );

                            String result = null;
                            JSONObject json= new JSONObject(response.toString());  //your response
                            try {
                                result = json.getString("result");    //result is key for which you need to retrieve data
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<UomModel>>() {
                            }.getType();

                            final List<UomModel> uomModels = gson.fromJson(result.toString(), type);
                            AsyncTask.execute( new Runnable() {
                                @Override
                                public void run() {
                                    UomDao uomDao=new UomDao( getApplicationContext() );
                                    uomDao.insertUom(uomModels);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "Response", error.toString() );

            }
        } );

        AppController.getInstance().submitServerRequest( req, "submitShipmet" );
    }


    public void getallCustomers() {
        final JsonFormater formatter = new JsonFormater();
        final String url = "https://planet-customerdiary.herokuapp.com/customer/getallcustomerdetails";
        JsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );

                            String result = null;
                            JSONObject json= new JSONObject(response.toString());  //your response
                            try {
                                result = json.getString("result");    //result is key for which you need to retrieve data
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<CustomerModel>>() {
                            }.getType();

                            final List<CustomerModel> customerModels = gson.fromJson(result.toString(), type);
                            AsyncTask.execute( new Runnable() {
                                @Override
                                public void run() {
                                    Customer customer=new Customer( getApplicationContext() );
                                    customer.insertCustomers(customerModels);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "Response", error.toString() );

            }
        } );

        AppController.getInstance().submitServerRequest( req, "submitShipmet" );
    }


    public void getAllProductsCategory() {
        final JsonFormater formatter = new JsonFormater();
        final String url = "https://planet-customerdiary.herokuapp.com/product/getallproductcategory";
        JsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );
                            String result = null;
                            JSONObject json= new JSONObject(response.toString());  //your response
                            try {
                                result = json.getString("result");    //result is key for which you need to retrieve data
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<ProductCategoryModel>>() {
                            }.getType();

                            final List<ProductCategoryModel> productCategoryList = gson.fromJson(result.toString(), type);
                            AsyncTask.execute( new Runnable() {
                                @Override
                                public void run() {
                                    ProductCategory productCategory=new ProductCategory( getApplicationContext() );
                                    productCategory.insertProductCategory(productCategoryList);
                                }
                            });



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "Response", error.toString() );

            }
        } );

        AppController.getInstance().submitServerRequest( req, "submitShipmet" );
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
