package com.plannet.apps.diarybook;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.plannet.apps.diarybook.activity.PendingDiaryFragment;
import com.plannet.apps.diarybook.forms.CreateProductsActivity;
import com.plannet.apps.diarybook.forms.UserCreationActivity;

public class MainActivity extends AppCompatActivity {
    Fragment[]  PAGES;
    String []  PAGE_TITLES;
    public PendingDiaryFragment pendingDiaryFragment=new PendingDiaryFragment();
    public PendingDiaryFragment currentDiaryFragment=new PendingDiaryFragment();
    public MyPagerAdapter myPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
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
                    case R.id.action_view:
                        Toast.makeText(MainActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
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
