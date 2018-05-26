package com.twinkle.htwinkle.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.xutils.x;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(setLayout());

        x.view().inject(setActivity());

        initView();

        initData();
    }

    public abstract int setLayout();

    public abstract Activity setActivity();

    public abstract void initView();

    public abstract void initData();


}
