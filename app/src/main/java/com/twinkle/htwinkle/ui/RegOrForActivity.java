package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;

public class RegOrForActivity extends BaseActivity {

    @ViewInject(value = R.id.comm_tb)
    private Toolbar comm_tb;

    @Override
    public int setLayout() {
        return R.layout.activity_reg_or_for;
    }

    @Override
    public Activity setActivity() {
        return this;
    }

    @Override
    public void initView() {

        comm_tb.setNavigationOnClickListener(tbOnclickListener);

        Intent intent = getIntent();
        int code = intent.getIntExtra("flag", 10000);

        switch (code) {
            case 10001:
                comm_tb.setTitle(R.string.register);
                break;
            case 10002:
                comm_tb.setTitle(R.string.forgetPass);
                break;
            default:
                break;
        }

    }

    @Override
    public void initData() {

    }

    private View.OnClickListener tbOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
