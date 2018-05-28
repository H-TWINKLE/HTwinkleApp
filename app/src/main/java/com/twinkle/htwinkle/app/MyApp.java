package com.twinkle.htwinkle.app;

import android.app.Application;

import org.xutils.x;

import cn.bmob.v3.Bmob;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);

        x.Ext.setDebug(false);

        Bmob.initialize(this, "efcec7fdecd3aefe199792559b33bf1b");

    }
}
