package com.twinkle.htwinkle.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.twinkle.htwinkle.R;

import java.util.List;

public class BaseTitleEolAdapter extends BaseQuickAdapter<com.twinkle.htwinkle.entity.List, BaseViewHolder> {

    public BaseTitleEolAdapter(int layoutResId, @Nullable List<com.twinkle.htwinkle.entity.List> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, com.twinkle.htwinkle.entity.List item) {

        helper.setText(R.id.subject_title, item.getTitle() + "");

        helper.setText(R.id.subject_dates, item.getAbort() + "");

        helper.setText(R.id.subject_name, item.getSubject() + "");

    }
}
