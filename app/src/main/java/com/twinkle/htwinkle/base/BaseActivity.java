package com.twinkle.htwinkle.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.twinkle.htwinkle.R;
import org.xutils.x;

public abstract class BaseActivity extends AppCompatActivity {

    private int toolBarTitle;

    private boolean toolBarFlag = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(setLayout());

        x.view().inject(setActivity());

        initData();

        initView();

        setToolBar();
    }

    private void setToolBar(){
        if(toolBarFlag){
         Toolbar comm_tb = findViewById(R.id.comm_tb);
         comm_tb.setTitle(toolBarTitle);
         comm_tb.setNavigationOnClickListener(e-> finish());
        }
    }


    public abstract int setLayout();

    public abstract Activity setActivity();

    public abstract void initView();

    public abstract void initData();

    public void setToolBarTitle(int toolBarTitle) {
        this.toolBarTitle = toolBarTitle;
    }

    public void setToolBarFlag(boolean toolBarFlag) {
        this.toolBarFlag = toolBarFlag;
    }
}
