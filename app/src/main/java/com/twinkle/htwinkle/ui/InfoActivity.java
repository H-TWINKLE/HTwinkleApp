package com.twinkle.htwinkle.ui;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.FragAdapter;
import com.twinkle.htwinkle.base.BaseActivity;
import com.twinkle.htwinkle.bean.User;
import com.twinkle.htwinkle.bmob.Bmob;
import com.twinkle.htwinkle.dialog.MyDialog;
import com.twinkle.htwinkle.listener.AppBarStateChangeListener;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;


public class InfoActivity extends BaseActivity implements ViewPager.OnPageChangeListener, TakePhoto.TakeResultListener, InvokeListener, Bmob.BmobUploadPicListener, Bmob.BmobPicUpdateListener {

    private static final String TAG = "InfoActivity";

    private MyDialog dialog;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    @ViewInject(value = R.id.info_siv_top)
    private SmartImageView info_siv_top;

    @ViewInject(value = R.id.info_vp)
    private ViewPager info_vp;

    @ViewInject(value = R.id.info_ctl)
    private CollapsingToolbarLayout info_ctl;

    @ViewInject(value = R.id.info_tb)
    private Toolbar info_tb;

    @ViewInject(value = R.id.info_al)
    private AppBarLayout info_al;

    @ViewInject(value = R.id.info_tl)
    private TabLayout info_tl;

    private List<Fragment> lists;

    private User user;

    @Event(value = R.id.info_siv_top)
    private void onHeaderClick(View view) {
        Bmob.INSTANCE.setBmobUploadPicListener(this);
        getTakePhoto().onPickMultipleWithCrop(1, new CropOptions.Builder().setAspectX(1600).setAspectY(1600).setWithOwnCrop(true).create());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public int setLayout() {
        return R.layout.activity_info;
    }

    @Override
    public Activity setActivity() {
        return this;
    }

    @Override
    public void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        info_tb.setNavigationOnClickListener((e) -> this.finish());

        initFragment();

        info_vp.addOnPageChangeListener(this);

        info_al.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED) {
                    info_tb.setTitle(R.string.info_index);
                } else if (state == State.EXPANDED) {
                    info_ctl.setTitle(BmobUser.getCurrentUser(User.class).getNickName() == null ? user.getUsername() : BmobUser.getCurrentUser(User.class).getNickName());
                } else {
                    info_ctl.setTitle("");
                }

            }
        });


    }

    @Override
    public void initData() {

        user = BmobUser.getCurrentUser(User.class);
        lists = new ArrayList<>();
        lists.add(new FragmentUserInfo());

        info_siv_top.setImageUrl(user.getHeaderPic(), R.drawable.logo);

    }

    private void initFragment() {
        info_vp.setAdapter(new FragAdapter(getSupportFragmentManager(), lists));
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.i(TAG, "onPageSelected: " + position);
        switch (position) {
            case 0:
                info_tb.setTitle(R.string.info_index);
                break;
            case 1:
                info_tb.setTitle(R.string.dynamic);
                break;
            default:
                info_tb.setTitle(R.string.info_index);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void takeSuccess(TResult result) {
        dialog = new MyDialog(InfoActivity.this, R.style.AlertDialog);
        dialog.show();
        Bmob.INSTANCE.BmobUploadPic(new File(result.getImage().getOriginalPath()));

    }

    @Override
    public void takeFail(TResult result, String msg) {
        Toast.makeText(InfoActivity.this, R.string.modify_user_info_failure, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    private TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void onUploadPicSuccess(BmobFile bmobFile) {
        info_siv_top.setImageUrl(bmobFile.getFileUrl(), R.drawable.logo);
        Bmob.INSTANCE.setBmobPicUpdateListener(this);
        Bmob.INSTANCE.BmobUpdatePic(bmobFile.getFileUrl());

    }

    @Override
    public void onUploadPicFailure(String tip) {
        Toast.makeText(InfoActivity.this, R.string.modify_user_info_failure, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onPicUpdateSuccess() {
        sendBroadcast();
        Bmob.INSTANCE.BmobFetchUserInfo(InfoActivity.this);
        Toast.makeText(InfoActivity.this, R.string.modify_user_info_success, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onPicUpdateFailure(String tip) {
        Toast.makeText(InfoActivity.this, R.string.modify_user_info_failure, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    private void sendBroadcast() {
        Intent i = new Intent();
        i.setAction("User_Info_Update");
        Objects.requireNonNull(InfoActivity.this).sendBroadcast(i);

    }


}
