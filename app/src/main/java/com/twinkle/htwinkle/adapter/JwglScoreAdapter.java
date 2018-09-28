package com.twinkle.htwinkle.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.entity.JwglScore;

import java.util.List;

public class JwglScoreAdapter extends BaseQuickAdapter<JwglScore, BaseViewHolder> {

    public JwglScoreAdapter(int layoutResId, @Nullable List<JwglScore> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, JwglScore item) {

        helper.setText(R.id.item_score_name, item.getKechengmingcheng() + "");
        helper.setText(R.id.item_score_chenji, item.getChengji() + "");
        helper.setText(R.id.socre_xuenian, item.getXuenian() + "");
        helper.setText(R.id.score_xueqi, item.getXueqi() + "");
        helper.setText(R.id.score_daima, item.getKechengdaima() + "");
        helper.setText(R.id.xueyuan, item.getXueyuanmingchen() + "");
        helper.setText(R.id.guishu, item.getKechengguishu() + "");
        helper.setText(R.id.xingzhi, item.getKechengxingzhi() + "");
        helper.setText(R.id.xuefen, item.getXuefen() + "");
        helper.setText(R.id.jidian, item.getJidian() + "");
        helper.setText(R.id.chongxiubiaoji, item.getChongxiubiaoji() + "");
        helper.setText(R.id.chongxiuchengji, item.getChongxiuchengji() + "");
        helper.setText(R.id.fuxiubiaoji, item.getFuxiubiaoji() + "");
        helper.setText(R.id.bukaochengji, item.getBukaochengji() + "");


    }
}
