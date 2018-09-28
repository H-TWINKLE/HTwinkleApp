package com.twinkle.htwinkle.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.entity.IndexTypes;

import java.util.List;

public class IndexTypesAdapter extends BaseQuickAdapter<IndexTypes, BaseViewHolder> {


    public IndexTypesAdapter(int layoutResId, @Nullable List<IndexTypes> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IndexTypes item) {
        helper.setText(R.id.main_cr_types_tv, item.getTitle());
        helper.setImageResource(R.id.main_cr_types_iv, item.getIcon());

        helper.addOnClickListener(R.id.main_cr_types_tv);
        helper.addOnClickListener(R.id.main_cr_types_iv);

    }
}
