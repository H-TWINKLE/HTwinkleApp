package com.twinkle.htwinkle.Adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jph.takephoto.model.TImage;
import com.twinkle.htwinkle.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

public class WMessIvAdapter extends BaseQuickAdapter<TImage, BaseViewHolder> {

    public WMessIvAdapter(int layoutResId, @Nullable List<TImage> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TImage item) {

        ImageView view = helper.getView(R.id.wMess_rv_base_iv);

        x.image().bind(view,item.getOriginalPath(),new ImageOptions.Builder().setIgnoreGif(false).build());

        helper.addOnClickListener(R.id.wMess_rv_base_iv_del);

    }
}
