package com.twinkle.htwinkle.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.entity.Praise;
import com.twinkle.htwinkle.init.Utils;

import org.xutils.x;

import java.util.List;

public class PraiseMeAdapter extends BaseQuickAdapter<Praise, BaseViewHolder> {


    public PraiseMeAdapter(int layout, @Nullable List<Praise> data) {
        super(layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Praise item) {

        SmartImageView smartImageView = helper.getView(R.id.me_item_siv_header);

        x.image().bind(smartImageView, item.getUser().getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());

        helper.setText(R.id.me_item_tv_nick, item.getUser().getNickName());

        helper.setText(R.id.me_item_tv_date, item.getCreatedAt());

        helper.setText(R.id.me_item_tv_post_comment_my,Utils.INSTANCE.convertHtmlText( item.getPost().getContent()));
    }
}
