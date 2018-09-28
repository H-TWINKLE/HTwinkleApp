package com.twinkle.htwinkle.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.entity.Focus;
import com.twinkle.htwinkle.init.Utils;

import org.xutils.x;

import java.util.List;

public class FocusAdapter extends BaseQuickAdapter<Focus, BaseViewHolder> {


    private int flag;   //0 w为我的关注  1 为关注我的

    public FocusAdapter(int layoutResId, @Nullable List<Focus> data, int flag) {
        super(layoutResId, data);
        this.flag = flag;
    }

    @Override
    protected void convert(BaseViewHolder helper, Focus item) {

        SmartImageView smartImageView = helper.getView(R.id.focus_iv_header);

        switch (flag) {
            case 2:
                x.image().bind(smartImageView, item.getOnFocusUser().getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());
                helper.setText(R.id.focus_tv_username, item.getOnFocusUser().getNickName());
                helper.setText(R.id.focus_tv_say, item.getOnFocusUser().getAuto());
                helper.setText(R.id.focus_tv_tool, "移除粉丝");
                break;
            case 1:
                x.image().bind(smartImageView, item.getFocusUser().getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());
                helper.setText(R.id.focus_tv_username, item.getFocusUser().getNickName());
                helper.setText(R.id.focus_tv_say, item.getFocusUser().getAuto());
                helper.setText(R.id.focus_tv_tool, "取消关注");
                break;
        }

        helper.addOnClickListener(R.id.focus_tv_tool);

    }
}
