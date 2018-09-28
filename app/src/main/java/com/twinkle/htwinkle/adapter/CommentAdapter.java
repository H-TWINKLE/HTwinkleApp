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

public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {


    public CommentAdapter(int layoutResId, @Nullable List<Comment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {

        SmartImageView smartImageView = helper.getView(R.id.news_comment_item_siv_header);

        x.image().bind(smartImageView, item.getAuthor().getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());

        helper.setText(R.id.news_comment_item_tv_post_content, Utils.INSTANCE.convertHtmlText(item.getPost().getContent()));

        helper.setText(R.id.news_comment_item_tv_username, item.getAuthor().getNickName());

        helper.setText(R.id.news_comment_item_tv_date, item.getCreatedAt());

        helper.setText(R.id.news_comment_item_tv_post_comment_my, Utils.INSTANCE.convertHtmlText(item.getContent()));


    }


}
