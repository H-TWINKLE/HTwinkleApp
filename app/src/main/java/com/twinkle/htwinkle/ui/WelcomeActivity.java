package com.twinkle.htwinkle.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;
import com.twinkle.htwinkle.entity.EveryMusic;
import com.twinkle.htwinkle.init.Utils;
import com.twinkle.htwinkle.net.Twinkle;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Locale;

import cn.bmob.v3.BmobUser;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class WelcomeActivity extends BaseActivity implements Twinkle.JwglListener {


    private EveryMusic everyMusic;

    @ViewInject(R.id.wel_background)
    private SmartImageView wel_background;

    @ViewInject(R.id.wel_head)
    private SmartImageView wel_head;

    @ViewInject(R.id.wel_name)
    private TextView wel_name;

    @ViewInject(R.id.wel_comment)
    private TextView wel_comment;

    @ViewInject(R.id.wel_comment_author)
    private TextView wel_comment_author;

    @ViewInject(value = R.id.wel_cl)
    private CoordinatorLayout wel_cl;

    @ViewInject(value = R.id.wel_bt)
    private Button wel_bt;

    @Event(value = R.id.wel_bt)
    private void onClick(View view) {
        count.onFinish();
    }

    private void onUiThread(final int text) {
        runOnUiThread(() -> wel_bt.setText(String.format(Locale.CHINA, "跳过  %ds", text / 1000)));
    }


    private final CountDownTimer count = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            onUiThread((int) millisUntilFinished);
        }

        @Override
        public void onFinish() {
            count.cancel();
            checkUser();
        }
    };

    private final Handler handler = new Handler();

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            wel_cl.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        }
    };

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        handler.postDelayed(runnable, 500);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    public Activity setActivity() {
        return WelcomeActivity.this;
    }

    @Override
    public void initView() {
        count.start();
    }

    @Override
    public void initData() {


        Twinkle.INSTANCE.setJwglListener(this);
        everyMusic = Twinkle.INSTANCE.readOneMusic();

        if (everyMusic == null) {
            Twinkle.INSTANCE.getEveryOneMusic(10);
        } else {
            bindData();
        }


    }

    private void bindData() {

        runOnUiThread(() -> {
            x.image().bind(wel_head, everyMusic.getNetpic(), Utils.INSTANCE.ImageOptionsInCir());

            x.image().bind(wel_background, everyMusic.getNetpic(), Utils.INSTANCE.ImageOptionsInWelBackGround());

            wel_name.setText(String.format("%s", everyMusic.getNetname()));

            wel_comment.setText(String.format("%s", everyMusic.getNetcomm()));

            wel_comment_author.setText(String.format("%s", everyMusic.getNettauthor()));
        });

    }

    private void checkUser() {
        if (BmobUser.getCurrentUser() != null) {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class)); // 允许用户使用应用
        } else {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class)); //缓存用户对象为空时， 可打开用户注册界面…
        }
        finish();
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onRun() {
    }


    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onRunShow() {
    }


    @Override
    public void onJwglListenerSuccess(Object t) {

        if (t != null) {
            everyMusic = (EveryMusic) t;
            bindData();

        }

    }

    @Override
    public void onJwglListenerFailure(String text) {

    }
}
