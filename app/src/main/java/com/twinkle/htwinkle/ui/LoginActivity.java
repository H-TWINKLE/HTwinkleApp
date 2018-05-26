package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

public class LoginActivity extends BaseActivity {


    @ViewInject(value = R.id.login_iv_header)
    private ImageView login_iv_header;

    @ViewInject(value = R.id.login_et_tel)
    private EditText login_et_tel;

    @ViewInject(value = R.id.login_et_pass)
    private EditText login_et_pass;

    @ViewInject(value = R.id.login_bt)
    private Button login_bt;


    @Event(value = R.id.login_tv_fPass)
    private void fPassClick(View view) {
        startActivity(10002);
    }

    @Event(value = R.id.login_tv_reg)
    private void regClick(View view) {
        startActivity(10001);
    }


    private void startActivity(int code) {

        Intent intent = new Intent(LoginActivity.this, RegOrForActivity.class);
        intent.putExtra("flag", code);
        startActivity(intent);

    }


    @Override
    public int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    public Activity setActivity() {
        return LoginActivity.this;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
