package com.twinkle.htwinkle.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.dialog.DialogInShowBigPic;
import com.twinkle.htwinkle.entity.Post;
import com.twinkle.htwinkle.init.Utils;

import org.xutils.x;

import java.util.List;

public class IndexAdapter extends BaseMultiItemQuickAdapter<Post, BaseViewHolder> {


    private Context context;

    private DialogInShowBigPic dialogInShowBigPic;


    public IndexAdapter(Context context, List<Post> list) {
        super(list);
        this.context = context;
        addItemType(Post.TEXT, R.layout.item_index_content__text);
        addItemType(Post.ONE_ING_TEXT, R.layout.item_index_content_one_img_text);
        addItemType(Post.IMG_TEXT, R.layout.item_index_content_img_text);
    }

    @Override
    protected void convert(BaseViewHolder helper, Post item) {

        SmartImageView header = helper.getView(R.id.index_cr_iv_header);
        helper.addOnClickListener(R.id.index_cr_iv_header);
        x.image().bind(header, item.getAuthor().getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());

        helper.setText(R.id.index_cr_tv_username, item.getAuthor().getNickName());
        helper.addOnClickListener(R.id.index_cr_tv_username);

        helper.setText(R.id.index_cr_tv_time, item.getCreatedAt());

        helper.setText(R.id.index_cr_tv_lv, Utils.INSTANCE.setUserLvByString(item.getAuthor()));

        List<String> list = item.getTopic();

        if (list != null && list.size() > 0) {
            helper.setText(R.id.index_cr_iv_types, Utils.INSTANCE.getPostTopicInString(item));
        }

        if (item.getLikenum() != null && item.getLikenum() > 0) {
            helper.setText(R.id.index_item_tv_praise, String.valueOf(item.getLikenum()));
        }

        if (item.getCommentnum() != null && item.getCommentnum() > 0) {
            helper.setText(R.id.index_item_tv_comment, String.valueOf(item.getCommentnum()));
        }

        switch (helper.getItemViewType()) {
            case Post.TEXT:
                helper.setText(R.id.index_cr_iv_content_text, Utils.INSTANCE.convertHtmlText(item.getContent()));
                break;
            case Post.ONE_ING_TEXT:
                helper.setText(R.id.index_cr_iv_content_one_img_text, Utils.INSTANCE.convertHtmlText(item.getContent()));

                SmartImageView siv = helper.getView(R.id.index_cr_iv_one_img);

                if (item.getPic() != null && item.getPic().size() == 1) {
                    siv.setImageUrl(item.getPic().get(0), R.drawable.logo);
                    helper.addOnClickListener(R.id.index_cr_iv_one_img);
                }
                break;
            case Post.IMG_TEXT:
                helper.setText(R.id.index_cr_iv_content_img_text, Utils.INSTANCE.convertHtmlText(item.getContent()));
                if (item.getPic() != null && item.getPic().size() > 1) {
                    RecyclerView recyclerView = helper.getView(R.id.index_rv_img_text);
                    recyclerView.setLayoutManager(new GridLayoutManager(context, 3));

                    dialogInShowBigPic = new DialogInShowBigPic(context);

                    BaseRVOnlyWithPicAdapter adapter = new BaseRVOnlyWithPicAdapter(R.layout.base_imageview, item.getPic());
                    adapter.setOnItemChildClickListener((adapter1, view, position) -> onShowDialog(item.getPic().get(position)));
                    recyclerView.setAdapter(adapter);
                }
                break;
        }

    }

    private void onShowDialog(String url) {
        if (url == null) {
            return;
        }
        dialogInShowBigPic.setUrl(url);
        dialogInShowBigPic.onShow();
    }


}
