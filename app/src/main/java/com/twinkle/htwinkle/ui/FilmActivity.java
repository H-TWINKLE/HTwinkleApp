package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.FilmAdapter;
import com.twinkle.htwinkle.base.BaseRefreshActivity;
import com.twinkle.htwinkle.entity.Film;
import com.twinkle.htwinkle.net.Twinkle;

import org.xutils.common.Callback;

public class FilmActivity extends BaseRefreshActivity<Film.SubjectsBean, FilmAdapter> implements Callback.CommonCallback<String> {


    @Override
    public void getBmobMethod() {

        Twinkle.INSTANCE.getListFilm(current, this);

    }

    @Override
    public int setLayout() {
        return R.layout.activity_film;
    }

    @Override
    public Activity setActivity() {
        return FilmActivity.this;
    }

    @Override
    public void initData() {

        setToolBarFlag(true);
        setToolBarTitles("电影推荐");

        layoutManager = new GridLayoutManager(this, 3);
        adapter = new FilmAdapter(R.layout.item_film, list);
        adapter.setOnItemClickListener((adapter, view, position) -> {

            Uri uri = Uri.parse(this.adapter.getData().get(position).getAlt() + "");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        });

        getBmobMethod();

    }

    @Override
    public void onSuccess(String result) {
        Film film = JSONObject.parseObject(result, Film.class);

        onSuccessGetList(film.getSubjects());
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        onFailureGetList(ex.getLocalizedMessage());
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
