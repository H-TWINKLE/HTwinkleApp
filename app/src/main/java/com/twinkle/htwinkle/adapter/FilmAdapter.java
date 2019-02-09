package com.twinkle.htwinkle.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.entity.Film;

import java.util.List;

public class FilmAdapter extends BaseQuickAdapter<Film.SubjectsBean, BaseViewHolder> {

    public FilmAdapter(int layoutResId, @Nullable List<Film.SubjectsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Film.SubjectsBean item) {

        SmartImageView smartImageView = helper.getView(R.id.item_film_siv);

        smartImageView.setImageUrl(item.getImages().getMedium(), R.drawable.load);

        helper.setText(R.id.item_film_tv_introduce, TextUtils.join("  ", item.getGenres()));
        helper.setText(R.id.item_film_tv_name, item.getTitle() + "");
        helper.setText(R.id.item_film_tv_num, item.getRating().getAverage() + "");


    }
}
