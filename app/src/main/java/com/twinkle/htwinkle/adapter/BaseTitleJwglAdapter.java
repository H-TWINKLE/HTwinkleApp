package com.twinkle.htwinkle.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.entity.Title;

import java.util.List;

public class BaseTitleJwglAdapter extends BaseQuickAdapter<Title, BaseViewHolder> {

    public BaseTitleJwglAdapter(int layoutResId, @Nullable List<Title> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Title item) {

        helper.setText(R.id.base_title_tv_title, item.getTitle() + "");

        helper.setText(R.id.base_title_tv_content, item.getContent() + "");


    }
}
