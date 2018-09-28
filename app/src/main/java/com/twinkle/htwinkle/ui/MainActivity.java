package com.twinkle.htwinkle.ui;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.adapter.FragAdapter;
import com.twinkle.htwinkle.adapter.MenuHeaderAndFooterAdapter;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.entity.ViewTypes;
import com.twinkle.htwinkle.init.Utils;
import com.twinkle.htwinkle.receiver.UserInfoReceiver;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, UserInfoReceiver.onUserInfoUpdate {

    private List<Fragment> lists;

    private MenuHeaderAndFooterAdapter headerAndFooterAdapter;

    private long mExitTime;

    private boolean isOpenLeft = false;

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

    private User user;

    private UserInfoReceiver userInfoReceiver;

    public SmartImageView main_header_iv_head;
    public TextView main_header_tv_name;
    public TextView main_header_tv_lv;
    public TextView main_header_tv_auto;

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

        userInfoReceiver = new UserInfoReceiver();
        registerReceiver(userInfoReceiver, new IntentFilter("User_Info_Update"));
        userInfoReceiver.setInfoUpdate(this);

        main_dl.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isOpenLeft = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isOpenLeft = false;
            }
        });

        main_tb.setNavigationOnClickListener(e -> main_dl.openDrawer(Gravity.START, true));
        main_vp.addOnPageChangeListener(this);

        initBnv();
        initFragment();

        initRv();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(userInfoReceiver);
    }

    @Override
    public void initData() {
        lists = new ArrayList<>();
        lists.add(new FragmentIndex());
        lists.add(new FragmentNews());

        user = BmobUser.getCurrentUser(User.class);

    }

    private void initRv() {

        main_side_rv.setLayoutManager(new LinearLayoutManager(this));
        setRvAdapter(Utils.INSTANCE.getMenuList(this));

    }

    private void setRvAdapter(List<ViewTypes> list) {
        headerAndFooterAdapter = new MenuHeaderAndFooterAdapter(list);
        headerAndFooterAdapter.isFirstOnly(false);
        headerAndFooterAdapter.openLoadAnimation(2);
        headerAndFooterAdapter.addHeaderView(setHeader1());
        headerAndFooterAdapter.addHeaderView(setHeader2());
        headerAndFooterAdapter.setOnItemClickListener(
                (a, v, p) -> {
                    if (list.size() - 1 == p) {
                        showDialog();
                    } else if (p == 1) {
                        startActivity(new Intent(this, InfoActivity.class));
                    } else if (p == 2) {
                        startActivity(new Intent(this, CollectActivity.class));
                    } else if (list.size() - 2 == p) {
                        startActivity(new Intent(this, SettingsActivity.class));
                    } else {
                        switch (list.get(p).getMenuTitle()) {
                            case "个人信息":
                                toJwglActivity(JwglInfoActivity.class);
                                break;
                            case "我的成绩":
                                toJwglActivity(JwglScoreActivity.class);
                                break;
                            case "我的课表":
                                toJwglActivity(JwglTtbActivity.class);
                                break;
                            case "我的信息":
                                toEolActivity(EolInfoActivity.class);
                                break;
                            case "我的作业":
                                toEolActivity(EolSubjectActivity.class);
                                break;
                            case "电脑义务维修":
                                joinQQGroup(getString(R.string.qingzhi));
                                break;
                            case "智能小华":
                                startActivity(new Intent(MainActivity.this, RobotActivity.class));
                                break;
                            case "每日一文":
                                startActivity(new Intent(MainActivity.this, EveryArticleActivity.class));
                                break;
                            case "每日一曲":
                                startActivity(new Intent(MainActivity.this, EveryInOneMusicActivity.class));
                                break;
                        }


                    }
                });

        main_side_rv.setAdapter(headerAndFooterAdapter);
    }

    @Override
    public void onBackPressed() {

        if (isOpenLeft) {
            main_dl.closeDrawer(Gravity.START);
            isOpenLeft = false;
        } else {

            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(MainActivity.this, getString(R.string.confirm_to_quit), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        }
    }


    private void toJwglActivity(Class c) {

        if (TextUtils.isEmpty(user.getSchoolId())) {
            Toast.makeText(MainActivity.this, R.string.please_input_school_id, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, InfoActivity.class));
            return;
        }

        if (TextUtils.isEmpty(user.getJwgl())) {
            Toast.makeText(MainActivity.this, R.string.please_input_jwgl, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, InfoActivity.class));
            return;
        }
        startActivity(new Intent(MainActivity.this, c));

    }

    private void toEolActivity(Class c) {

        if (TextUtils.isEmpty(user.getSchoolId())) {
            Toast.makeText(MainActivity.this, R.string.please_input_school_id, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, InfoActivity.class));
            return;
        }

        if (TextUtils.isEmpty(user.getEol())) {
            Toast.makeText(MainActivity.this, R.string.please_input_eol, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, InfoActivity.class));
            return;
        }
        startActivity(new Intent(MainActivity.this, c));

    }


    private View setHeader1() {
        View v = getLayoutInflater().inflate(R.layout.header_user_info, (ViewGroup) main_side_rv.getParent(), false);
        setHeader1View(v);
        return v;

    }

    private void setHeader1View(View v) {
        main_header_iv_head = v.findViewById(R.id.main_header_iv_head);
        if (!TextUtils.isEmpty(user.getHeaderPic()))
            x.image().bind(main_header_iv_head, user.getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());

        main_header_tv_name = v.findViewById(R.id.main_header_tv_name);
        if (!TextUtils.isEmpty(user.getNickName()))
            main_header_tv_name.setText(user.getNickName());


        main_header_tv_lv = v.findViewById(R.id.main_header_tv_lv);

        main_header_tv_lv.setText(Utils.INSTANCE.setUserLvByString(user));


        main_header_tv_auto = v.findViewById(R.id.main_header_tv_auto);
        if (!TextUtils.isEmpty(user.getAuto()))
            main_header_tv_auto.setText(user.getAuto());

        LinearLayout linearLayout = v.findViewById(R.id.line_header_user_info);
        linearLayout.setOnClickListener((e) -> {
            Intent intent = new Intent(MainActivity.this, UserRoomActivity.class);
            intent.putExtra("user", BmobUser.getCurrentUser(User.class));
            startActivity(intent);
        });

    }


    private View setHeader2() {
        View view = getLayoutInflater().inflate(R.layout.header_user_count, (ViewGroup) main_side_rv.getParent(), false);
        LinearLayout focus = view.findViewById(R.id.item_main_focus);

        focus.setOnClickListener(v -> startActivity(1));

        LinearLayout onfocus = view.findViewById(R.id.item_main_on_focus);

        onfocus.setOnClickListener((e) -> startActivity(2));
        return view;
    }

    private void startActivity(int flag) {
        Intent i = new Intent(MainActivity.this, FocusActivity.class);
        i.putExtra("flag", flag);
        startActivity(i);
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
                    Utils.INSTANCE.UserLoginOut(this);
                    finish();
                    startActivity(new Intent(this, LoginActivity.class));
                })
                .create()
                .show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (headerAndFooterAdapter != null) {
            headerAndFooterAdapter.replaceData(Utils.INSTANCE.getMenuList(this));
            headerAndFooterAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onReceiveToUpdate() {

        user = BmobUser.getCurrentUser(User.class);

        if (!TextUtils.isEmpty(user.getHeaderPic())) {
            x.image().bind(main_header_iv_head, user.getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());
        }


        if (!TextUtils.isEmpty(user.getNickName()))
            main_header_tv_name.setText(user.getNickName());
        if (!TextUtils.isEmpty(user.getLv())) {
            main_header_tv_lv.setText(Utils.INSTANCE.setUserLvByString(user));
        }
        if (!TextUtils.isEmpty(user.getAuto()))
            main_header_tv_auto.setText(user.getAuto());
    }


    public void joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "您好像没有安装QQ喔", Toast.LENGTH_SHORT).show();

        }
    }


}
