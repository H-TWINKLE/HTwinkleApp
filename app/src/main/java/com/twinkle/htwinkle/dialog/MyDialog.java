package com.twinkle.htwinkle.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;

import com.twinkle.htwinkle.R;


public class MyDialog extends AlertDialog {


    public MyDialog( Context context) {
        super(context);
        this.setView(setProgressView(context));
        this.setCancelable(false);
    }

    public MyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.setView(setProgressView(context));
        this.setCancelable(false);
    }

    private View setProgressView(Context context){

       return  View.inflate(context, R.layout.base_progress,null);

    }

}
