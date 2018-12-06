package com.twinkle.htwinkle.ui;

import android.app.Activity;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.BaseTitleJwglAdapter;
import com.twinkle.htwinkle.base.BaseRefreshActivity;
import com.twinkle.htwinkle.dialog.MyDialog;
import com.twinkle.htwinkle.entity.Jwgl;
import com.twinkle.htwinkle.entity.JwglInfo;
import com.twinkle.htwinkle.entity.Title;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.init.Utils;
import com.twinkle.htwinkle.net.Twinkle;

import cn.bmob.v3.BmobUser;

public class JwglInfoActivity extends BaseRefreshActivity<Title, BaseTitleJwglAdapter> implements Twinkle.JwglListener {

    private MyDialog dialog;

    @Override
    public void getBmobMethod() {
        dialog.show();
        Twinkle.INSTANCE.getJwgl(BmobUser.getCurrentUser(User.class), true);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_jwgl_info;
    }

    @Override
    public Activity setActivity() {
        return JwglInfoActivity.this;
    }

    @Override
    public void initData() {

        setToolBarFlag(true);
        setToolBarTitle(R.string.info);

        dialog = new MyDialog(JwglInfoActivity.this, R.style.AlertDialog, true);

        adapter = new BaseTitleJwglAdapter(R.layout.item_base_title_content, list);
        //adapter.addHeaderView(setHeader1());

        isRefresh = false;

        Twinkle.INSTANCE.setJwglListener(this);

        JwglInfo jwglInfo = Twinkle.INSTANCE.getJwglInfo(BmobUser.getCurrentUser(User.class));


        list = Utils.INSTANCE.ReflexByClass(JwglInfoActivity.this, jwglInfo);

        if (list == null || list.size() == 0) {
            getBmobMethod();
        } else {
            adapter.setNewData(list);
        }

    }

    /*private View setHeader1() {

        SmartImageView smartImageView = new SmartImageView(JwglInfoActivity.this);

        return smartImageView;
    }*/


    @Override
    public void onJwglListenerSuccess(Object t) {

        Jwgl j = (Jwgl) t;

        list = Utils.INSTANCE.ReflexByClass(JwglInfoActivity.this, j.getJwglinfo());

        dialog.dismiss();
        onSuccessGetList(list);

        adapter.setEnableLoadMore(false);

        if (adapter.getData().size() == 0) {
            adapter.setEmptyView(R.layout.base_content_empty);
        }
    }

    @Override
    public void onJwglListenerFailure(String text) {
        dialog.dismiss();
        onFailureGetList(text);
        this.finish();
    }
}
