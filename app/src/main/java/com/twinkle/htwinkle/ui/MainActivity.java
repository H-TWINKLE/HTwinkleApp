package com.twinkle.htwinkle.ui;

import android.app.Activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.twinkle.htwinkle.Adapter.FragAdapter;
import com.twinkle.htwinkle.Adapter.HeaderAndFooterAdapter;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;
import com.twinkle.htwinkle.bean.ViewTypes;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private List<Fragment> list;

    private HeaderAndFooterAdapter headerAndFooterAdapter;

    @ViewInject(value = R.id.main_tb)
    private Toolbar main_tb;

    @ViewInject(value = R.id.main_bnv)
    private BottomNavigationView main_bnv;

    @ViewInject(value = R.id.main_dl)
    private DrawerLayout main_dl;


    @ViewInject(value = R.id.main_vp)
    private ViewPager main_vp;

    @ViewInject(value = R.id.main_side_rv)
    private RecyclerView main_side_rv;


    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public Activity setActivity() {
        return this;
    }

    @Override
    public void initView() {
        main_tb.setNavigationOnClickListener(e -> main_dl.openDrawer(Gravity.START, true));

        main_vp.addOnPageChangeListener(this);

        initBnv();
        initFragment();

        initRv();

    }

    @Override
    public void initData() {
        list = new ArrayList<>();
        list.add(new IndexFragment());
        list.add(new NewsFragment());
    }

    private void initRv() {

        main_side_rv.setLayoutManager(new LinearLayoutManager(this));

        List<ViewTypes> list = new ArrayList<>();

        ViewTypes viewTypes;
        for (int x = 0; x < 20; x++) {
            viewTypes = new ViewTypes();
            if (x == 8 || x == 12 || x == 15) {
                viewTypes.type = 2;
                list.add(viewTypes);
            } else {
                viewTypes.type = 1;
                viewTypes.setTitle("this is a number :" + x);
            }
            list.add(viewTypes);
        }

        headerAndFooterAdapter = new HeaderAndFooterAdapter(list);
        headerAndFooterAdapter.isFirstOnly(false);
        headerAndFooterAdapter.openLoadAnimation(2);
        headerAndFooterAdapter.addHeaderView(Header1());
        headerAndFooterAdapter.setOnItemClickListener(
                (a, v, p) -> Toast.makeText(this, p + "  :title", Toast.LENGTH_SHORT).show());

        main_side_rv.setAdapter(headerAndFooterAdapter);

    }

    private View Header1() {
        return getLayoutInflater().inflate(R.layout.header_user_info, (ViewGroup) main_side_rv.getParent(), false);
    }

    private void initBnv() {
        main_bnv.setItemIconTintList(null);
        main_bnv.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initFragment() {
        main_vp.setAdapter(new FragAdapter(getSupportFragmentManager(), list));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bnv_index:
                    main_vp.setCurrentItem(0);
                    return true;
                case R.id.bnv_news:
                    main_vp.setCurrentItem(1);
                    return true;
            }
            return false;
        }
    };


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                main_bnv.setSelectedItemId(R.id.bnv_index);
                break;
            case 1:
                main_bnv.setSelectedItemId(R.id.bnv_news);
                break;
            default:
                main_bnv.setSelectedItemId(R.id.bnv_index);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
