package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.widget.Toast;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.BaseTitleEolAdapter;
import com.twinkle.htwinkle.base.BaseRefreshActivity;
import com.twinkle.htwinkle.dialog.MyDialog;
import com.twinkle.htwinkle.entity.Eol;
import com.twinkle.htwinkle.entity.List;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.net.Bmob;
import com.twinkle.htwinkle.net.Twinkle;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;

public class EolSubjectActivity extends BaseRefreshActivity<List, BaseTitleEolAdapter> implements Twinkle.JwglListener {

    private MyDialog dialog;

    @Override
    public int setLayout() {
        return R.layout.activity_eol_subject;
    }

    @Override
    public Activity setActivity() {
        return EolSubjectActivity.this;
    }

    @Override
    public void initData() {
        setToolBarFlag(true);
        setToolBarTitle(R.string.work);

        dialog = new MyDialog(EolSubjectActivity.this, R.style.AlertDialog, true);

        adapter = new BaseTitleEolAdapter(R.layout.item_eol_subject_content, list);

        isRefresh = false;

        Twinkle.INSTANCE.setJwglListener(this);

        Bmob.INSTANCE.updateUserLv(50);

        list = Twinkle.INSTANCE.getEolSubject(BmobUser.getCurrentUser(User.class));

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

        dialog.dismiss();

        Eol j = (Eol) t;
        java.util.List<List> list = new ArrayList<>();

        if (j.getList() == null) {
            Toast.makeText(EolSubjectActivity.this, R.string.work_is_finish, Toast.LENGTH_SHORT).show();
            adapter.setEmptyView(R.layout.base_content_empty);
            return;
        }

        for (java.util.List<List> aList : j.getList()) {
            list.addAll(aList);
        }

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
