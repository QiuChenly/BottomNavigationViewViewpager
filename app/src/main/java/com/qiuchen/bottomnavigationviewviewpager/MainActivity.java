package com.qiuchen.bottomnavigationviewviewpager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    BottomNavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.argb(33, 0, 0, 0));
//        }

        mViewPager = findViewById(R.id.mViewPager);
        mNavigationView = findViewById(R.id.mBottomNavigationView);

        initView();
    }

    List<View> viewList;
    int[] layout = {
            R.layout.viewpager_item1, R.layout.viewpager_item2, R.layout.viewpager_item3
            , R.layout.viewpager_item4
    };

    int[] navigationBtn = {
            R.id.n_View1, R.id.n_View2,
            R.id.n_View3, R.id.n_View4
    };

    int[] color = {Color.parseColor("#f44336"), Color.parseColor("#e91e63")
            , Color.parseColor("#9c27b0"), Color.parseColor("#673ab7")};

    @SuppressLint("NewApi")
    void initView() {

        viewList = new ArrayList<>();
        for (int a : layout) {
            viewList.add(LayoutInflater.from(this).inflate(a, null));
        }
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(pageChange);

        mNavigationView.setOnNavigationItemSelectedListener(navigationItem);

        BottomNavigationHelper.disable(mNavigationView);


    }

    PagerAdapter mPagerAdapter = new PagerAdapter() {


        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = viewList.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };

    ViewPager.OnPageChangeListener pageChange = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mNavigationView.setSelectedItemId(navigationBtn[position]);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    BottomNavigationView.OnNavigationItemSelectedListener navigationItem =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.n_View1:
                            mViewPager.setCurrentItem(0);
                            break;
                        case R.id.n_View2:
                            mViewPager.setCurrentItem(1);
                            break;
                        case R.id.n_View3:
                            mViewPager.setCurrentItem(2);
                            break;
                        case R.id.n_View4:
                            mViewPager.setCurrentItem(3);
                            break;
                        default:
                            return false;
                    }
                    return true;
                }
            };

    static class BottomNavigationHelper {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @SuppressLint("RestrictedApi")
        static void disable(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);

                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                    itemView.setShiftingMode(false);
                    itemView.setChecked(itemView.getItemData().isChecked());
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }
}
