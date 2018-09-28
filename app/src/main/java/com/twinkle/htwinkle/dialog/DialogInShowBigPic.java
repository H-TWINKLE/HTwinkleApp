package com.twinkle.htwinkle.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.view.SmoothImageView;

public class DialogInShowBigPic {


    private String url;

    private AlertDialog alertDialog;

    private SmoothImageView smoothImageView;

    public DialogInShowBigPic(Context context) {

        smoothImageView = new SmoothImageView(context);
        smoothImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        smoothImageView.setOnClickListener((e) -> {
            alertDialog.dismiss();
        });

        alertDialog = new AlertDialog.Builder(context, R.style.AlertDialog)
                .setView(smoothImageView)
                .create();

    }


    public void setUrl(String url) {
        this.url = url;
        smoothImageView.setImageUrl(url);

    }

    public void onShow() {

        alertDialog.show();
    }


    public void onDismiss() {

        alertDialog.dismiss();
    }


}
