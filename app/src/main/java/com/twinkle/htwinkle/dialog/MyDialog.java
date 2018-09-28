package com.twinkle.htwinkle.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.twinkle.htwinkle.R;


public class MyDialog extends AlertDialog {

    private TextView textView;

    private String tip;

    private boolean isShowTip = false;

    public MyDialog(Context context) {
        super(context);
        this.setView(setProgressView(context));
        this.setCancelable(false);
    }

    public MyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.setView(setProgressView(context));
        this.setCancelable(false);
    }

    public MyDialog(@NonNull Context context, int themeResId, boolean isShowTip) {
        super(context, themeResId);
        this.isShowTip = isShowTip;
        this.setView(setProgressView(context));
        this.setCancelable(false);
    }

    private View setProgressView(Context context) {

        View view = View.inflate(context, R.layout.base_progress, null);
        textView = view.findViewById(R.id.base_process_text);

        isShowTip();

        return view;

    }

    private void isShowTip() {
        if (isShowTip) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    public Boolean getShowTip() {
        return isShowTip;
    }

    public void setShowTip(Boolean showTip) {
        isShowTip = showTip;

        isShowTip();
    }

    private String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;

        if (!TextUtils.isEmpty(getTip())) {
            textView.setText(tip);
        }

    }


}
