package com.twinkle.htwinkle.app;

import android.app.Application;

import com.twinkle.htwinkle.init.Constant;

import org.xutils.x;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);

        x.Ext.setDebug(false);

        Bmob.initialize(this, Constant.BOMBKEY);

    }
}
