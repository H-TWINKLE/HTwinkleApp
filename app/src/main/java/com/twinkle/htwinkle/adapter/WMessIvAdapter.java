package com.twinkle.htwinkle.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jph.takephoto.model.TImage;
import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.init.Utils;

import org.xutils.x;

import java.util.List;

public class WMessIvAdapter extends BaseQuickAdapter<TImage, BaseViewHolder> {

    public WMessIvAdapter(int layoutResId, @Nullable List<TImage> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TImage item) {

        SmartImageView view = helper.getView(R.id.wMess_rv_base_iv);

        x.image().bind(view, item.getOriginalPath(), Utils.INSTANCE.baseOptions());

        helper.addOnClickListener(R.id.wMess_rv_base_iv_del);

    }
}
