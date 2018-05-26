package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Locale;

public class WelcomeActivity extends BaseActivity {

    @ViewInject(value = R.id.wel_cl)
    private ConstraintLayout wel_cl;

    @ViewInject(value = R.id.wel_bt)
    private Button wel_bt;

    @Event(value = R.id.wel_bt)
    private void onClick(View view) {
        count.onFinish();
    }
    private void onUiThread(final int text) {
        runOnUiThread(()-> wel_bt.setText(String.format(Locale.CHINA, "跳过  %ds", text / 1000)));
    }


    private final CountDownTimer count = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            onUiThread((int) millisUntilFinished);
        }

        @Override
        public void onFinish() {
            count.cancel();
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
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

    }
}
