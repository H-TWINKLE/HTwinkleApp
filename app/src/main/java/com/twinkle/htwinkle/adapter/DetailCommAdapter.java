package com.twinkle.htwinkle.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.entity.Comment;
import com.twinkle.htwinkle.init.Utils;

import org.xutils.x;

import java.util.List;

public class DetailCommAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {
    public DetailCommAdapter(int layoutResId, @Nullable List<Comment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {

        SmartImageView header = helper.getView(R.id.index_cr_iv_header);
        x.image().bind(header, item.getAuthor().getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());

        helper.setText(R.id.index_cr_tv_username, item.getAuthor().getNickName());
        helper.setText(R.id.index_cr_tv_time, item.getCreatedAt());
        helper.setText(R.id.index_cr_tv_lv, Utils.INSTANCE.setUserLvByString(item.getAuthor()));

        helper.setText(R.id.item_detail_comment_tv, item.getContent());

    }
}
