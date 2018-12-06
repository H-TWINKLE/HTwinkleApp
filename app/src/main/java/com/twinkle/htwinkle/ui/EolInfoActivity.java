package com.twinkle.htwinkle.ui;

import android.app.Activity;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.BaseTitleJwglAdapter;
import com.twinkle.htwinkle.base.BaseRefreshActivity;
import com.twinkle.htwinkle.dialog.MyDialog;
import com.twinkle.htwinkle.entity.Eol;
import com.twinkle.htwinkle.entity.Title;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.init.Utils;
import com.twinkle.htwinkle.net.Twinkle;

import cn.bmob.v3.BmobUser;

public class EolInfoActivity extends BaseRefreshActivity<Title, BaseTitleJwglAdapter> implements Twinkle.JwglListener {


    private MyDialog dialog;


    @Override
    public int setLayout() {
        return R.layout.activity_eol_info;
    }

    @Override
    public Activity setActivity() {
        return EolInfoActivity.this;
    }

    @Override
    public void initData() {

        setToolBarFlag(true);
        setToolBarTitle(R.string.info);

        dialog = new MyDialog(EolInfoActivity.this, R.style.AlertDialog, true);

        adapter = new BaseTitleJwglAdapter(R.layout.item_base_title_content, list);

        isRefresh = false;

        Twinkle.INSTANCE.setJwglListener(this);

        Eol eol = Twinkle.INSTANCE.getEolInfo(BmobUser.getCurrentUser(User.class));

        list = Utils.INSTANCE.ReflexByClass(EolInfoActivity.this, eol);

        if (list == null || list.size() == 0) {
            getBmobMethod();
        } else {
            adapter.setNewData(list);
        }
    }

    @Override
    public void getBmobMethod() {
        dialog.show();
        Twinkle.INSTANCE.getEol(BmobUser.getCurrentUser(User.class));
    }

    @Override
    public void onJwglListenerSuccess(Object t) {

        Eol j = (Eol) t;

        list = Utils.INSTANCE.ReflexByClass(EolInfoActivity.this, j);
        dialog.dismiss();
        onSuccessGetList(list);

        adapter.setEnableLoadMore(false);
    }

    @Override
    public void onJwglListenerFailure(String text) {
        dialog.dismiss();
        onFailureGetList(text);
        this.finish();
    }
}
