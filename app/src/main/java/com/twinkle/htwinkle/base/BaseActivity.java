package com.twinkle.htwinkle.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.twinkle.htwinkle.R;

import org.xutils.x;

public abstract class BaseActivity extends AppCompatActivity {

    private int toolBarTitle;

    private String toolBarTitles;

    private boolean toolBarFlag = false;

    public void setToolBarTitles(String toolBarTitles) {
        this.toolBarTitles = toolBarTitles;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(setLayout());

        x.view().inject(setActivity());

        initData();

        setToolBar();

        initView();

    }

    private void setToolBar() {
        if (toolBarFlag) {

            Toolbar baseToolBar = findViewById(R.id.comm_tb);

            if (!TextUtils.isEmpty(toolBarTitles)) {
                baseToolBar.setTitle(toolBarTitles);
            } else {
                baseToolBar.setTitle(toolBarTitle);
            }

            this.setSupportActionBar(baseToolBar);
            baseToolBar.setNavigationOnClickListener(e -> this.finish());
        }
    }

    public abstract int setLayout();

    public abstract Activity setActivity();

    public abstract void initData();

    public abstract void initView();


    public void setToolBarTitle(int toolBarTitle) {
        this.toolBarTitle = toolBarTitle;
    }

    public void setToolBarFlag(boolean toolBarFlag) {
        this.toolBarFlag = toolBarFlag;
    }
}
