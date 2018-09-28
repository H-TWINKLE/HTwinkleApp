package com.twinkle.htwinkle.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.init.Utils;

import org.xutils.x;

import java.util.List;

public class BaseRVOnlyWithPicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public BaseRVOnlyWithPicAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        SmartImageView smartImageView = helper.getView(R.id.wMess_rv_base_iv_base);
        x.image().bind(smartImageView, item, Utils.INSTANCE.IndexOptions());

        helper.addOnClickListener(R.id.wMess_rv_base_iv_base);


    }
}
