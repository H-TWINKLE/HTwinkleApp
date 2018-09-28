package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.view.View;
import com.twinkle.htwinkle.R;

import com.twinkle.htwinkle.adapter.JwglScoreAdapter;
import com.twinkle.htwinkle.base.BaseRefreshActivity;
import com.twinkle.htwinkle.dialog.MyDialog;
import com.twinkle.htwinkle.entity.Jwgl;

import com.twinkle.htwinkle.entity.JwglScore;
import com.twinkle.htwinkle.entity.User;

import com.twinkle.htwinkle.net.Twinkle;


import java.util.List;

import cn.bmob.v3.BmobUser;

public class JwglScoreActivity extends BaseRefreshActivity<JwglScore, JwglScoreAdapter> implements Twinkle.JwglListener {

    private MyDialog dialog;

    @Override
    public void getBmobMethod() {
        dialog.show();
        Twinkle.INSTANCE.getJwgl(BmobUser.getCurrentUser(User.class), true);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_jwgl_score;
    }

    @Override
    public Activity setActivity() {
        return JwglScoreActivity.this;
    }

    @Override
    public void initData() {
        setToolBarFlag(true);
        setToolBarTitle(R.string.score);

        dialog = new MyDialog(setActivity(), R.style.AlertDialog, true);

        adapter = new JwglScoreAdapter(R.layout.item_jwgl_score_content, list);
        adapter.setOnItemClickListener((adapter, view, position) -> {

            View view1 = view.findViewById(R.id.item_jwgl_score_llayout);

            if (view1.getVisibility() == View.GONE) {
                view1.setVisibility(View.VISIBLE);
            } else {
                view1.setVisibility(View.GONE);
            }

        });

        isRefresh = false;

        Twinkle.INSTANCE.setJwglListener(this);

        List<JwglScore> list = Twinkle.INSTANCE.getJwglScore(BmobUser.getCurrentUser(User.class));

        if (list == null || list.size() == 0) {
            getBmobMethod();
        } else {
            adapter.setNewData(list);
        }

    }


    @Override
    public void onJwglListenerSuccess(Object t) {

        Jwgl j = (Jwgl)t;

        dialog.dismiss();


        onSuccessGetList(j.getJwglscore());

        adapter.setEnableLoadMore(false);
    }

    @Override
    public void onJwglListenerFailure(String text) {
        dialog.dismiss();
        onFailureGetList(text);
    }
}
