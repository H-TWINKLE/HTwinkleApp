package com.twinkle.htwinkle.ui;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
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
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.twinkle.htwinkle.Adapter.FragAdapter;
import com.twinkle.htwinkle.Adapter.MenuHeaderAndFooterAdapter;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;
import com.twinkle.htwinkle.bean.ViewTypes;
import com.twinkle.htwinkle.init.InitString;
import com.twinkle.htwinkle.init.InitUtils;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private List<Fragment> lists;

    private MenuHeaderAndFooterAdapter headerAndFooterAdapter;

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
        lists = new ArrayList<>();
        lists.add(new IndexFragment());
        lists.add(new NewsFragment());
    }

    private void initRv() {

        main_side_rv.setLayoutManager(new LinearLayoutManager(this));

        setRvAdapter(initRvList());

    }

    private List<ViewTypes> initRvList() {

        List<ViewTypes> list = new ArrayList<>();

        ViewTypes viewTypes;

        String[] user = getResources().getStringArray(R.array.my_menu_user);

        for (int x = 0; x < user.length; x++) {
            viewTypes = new ViewTypes();
            if (x == 0) {
                viewTypes.setType(3);
                viewTypes.setMenuTitle(user[x]);

            } else {
                viewTypes.setType(1);
                viewTypes.setMenuTitle(user[x]);
                viewTypes.setMenuIcon(InitString.my_menu_user_icon[x]);
            }

            list.add(viewTypes);
        }

        list.add(new ViewTypes(2));

        String[] jwgl = getResources().getStringArray(R.array.my_menu_jwgl);

        for (int x = 0; x < jwgl.length; x++) {
            viewTypes = new ViewTypes();
            if (x == 0) {
                viewTypes.setType(3);
                viewTypes.setMenuTitle(jwgl[x]);
            } else {
                viewTypes.setType(1);
                viewTypes.setMenuTitle(jwgl[x]);
                viewTypes.setMenuIcon(InitString.my_menu_jwgl_icon[x]);
            }

            list.add(viewTypes);
        }
        list.add(new ViewTypes(2));

        String[] eol = getResources().getStringArray(R.array.my_menu_eol);

        for (int x = 0; x < eol.length; x++) {
            viewTypes = new ViewTypes();
            if (x == 0) {
                viewTypes.setType(3);
                viewTypes.setMenuTitle(eol[x]);
            } else if (x == 2) {
                viewTypes.setNewTip(true);
                viewTypes.setType(1);
                viewTypes.setMenuTitle(eol[x]);
                viewTypes.setMenuIcon(InitString.my_menu_eol_icon[x]);
            } else {
                viewTypes.setType(1);
                viewTypes.setMenuTitle(eol[x]);
                viewTypes.setMenuIcon(InitString.my_menu_eol_icon[x]);
            }

            list.add(viewTypes);
        }
        list.add(new ViewTypes(2));

        String[] setting = getResources().getStringArray(R.array.my_menu_setting);

        for (int x = 0; x < setting.length; x++) {
            viewTypes = new ViewTypes();
            if (x == 0) {
                viewTypes.setType(3);
                viewTypes.setMenuTitle(setting[x]);
            } else {
                viewTypes.setType(1);
                viewTypes.setMenuTitle(setting[x]);
                viewTypes.setMenuIcon(InitString.my_menu_setting_icon[x]);
            }

            list.add(viewTypes);
        }

        return list;
    }

    private void setRvAdapter(List<ViewTypes> list) {
        headerAndFooterAdapter = new MenuHeaderAndFooterAdapter(list);
        headerAndFooterAdapter.isFirstOnly(false);
        headerAndFooterAdapter.openLoadAnimation(2);
        headerAndFooterAdapter.addHeaderView(setHeader1());
        headerAndFooterAdapter.addHeaderView(setHeader2());
        headerAndFooterAdapter.setOnItemClickListener(
                (a, v, p) -> {
                    // Toast.makeText(this, p + "", Toast.LENGTH_SHORT).show();
                    if (list.size() - 1 == p) {
                        showDialog();
                    }else if(list.size() - 2 == p){
                        startActivity(new Intent(this,SettingsActivity.class));
                    }
                });

        main_side_rv.setAdapter(headerAndFooterAdapter);
    }


    private View setHeader1() {
        return getLayoutInflater().inflate(R.layout.header_user_info, (ViewGroup) main_side_rv.getParent(), false);
    }

    private View setHeader2() {
        return getLayoutInflater().inflate(R.layout.header_user_count, (ViewGroup) main_side_rv.getParent(), false);
    }

    private void initBnv() {
        main_bnv.setItemIconTintList(null);
        main_bnv.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initFragment() {
        main_vp.setAdapter(new FragAdapter(getSupportFragmentManager(), lists));
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
                main_tb.setTitle(R.string.main);
                break;
            case 1:
                main_bnv.setSelectedItemId(R.id.bnv_news);
                main_tb.setTitle(R.string.news);
                break;
            default:
                main_bnv.setSelectedItemId(R.id.bnv_index);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void showDialog() {

        new AlertDialog.Builder(this)
                .setMessage(R.string.login_out_tip)
                .setNegativeButton(R.string.negative, null)
                .setPositiveButton(R.string.positive, (d, w) -> {
                    InitUtils.INSTANCE.UserLoginOut(this);
                    finish();
                    startActivity(new Intent(this, LoginActivity.class));
                })
                .create()
                .show();

    }


}
