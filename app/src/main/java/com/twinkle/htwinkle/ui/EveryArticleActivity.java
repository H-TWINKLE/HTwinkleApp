package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;
import com.twinkle.htwinkle.dialog.MyDialog;
import com.twinkle.htwinkle.entity.EveryArticle;
import com.twinkle.htwinkle.init.Utils;
import com.twinkle.htwinkle.net.Bmob;
import com.twinkle.htwinkle.net.Twinkle;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

public class EveryArticleActivity extends BaseActivity implements Twinkle.JwglListener {

    private MyDialog myDialog;

    @ViewInject(R.id.one_article_nsv)
    private NestedScrollView one_article_nsv;

    @ViewInject(R.id.one_article_tb)
    private Toolbar one_article_tb;

    @ViewInject(R.id.one_article_title_tv)
    private TextView one_article_title_tv;

    @ViewInject(R.id.one_article_author_tv)
    private TextView one_article_author_tv;

    @ViewInject(R.id.one_article_content_tv)
    private TextView one_article_content_tv;

    @Event(R.id.one_article_float)
    private void onFloatClick(View view) {
        readArticle();

        one_article_nsv.scrollTo(0, 0);
    }

    private EveryArticle everyArticle;


    @Override
    public int setLayout() {
        return R.layout.activity_every_article;
    }

    @Override
    public Activity setActivity() {
        return EveryArticleActivity.this;
    }

    @Override
    public void initData() {

        Twinkle.INSTANCE.setJwglListener(this);

        one_article_tb.setTitle(R.string.every_article);

        myDialog = new MyDialog(EveryArticleActivity.this, R.style.AlertDialog, true);

        readArticle();

    }

    private void readArticle() {

        Bmob.INSTANCE.updateUserLv(20);

        everyArticle = Twinkle.INSTANCE.readOneArticle();

        if (everyArticle == null) {
            myDialog.show();
            Twinkle.INSTANCE.getEveryOneArticle(10);
        } else {
            bindData();
        }

    }


    private void bindData() {

        one_article_author_tv.setText(String.format("%s", everyArticle.getAuthor()));

        one_article_title_tv.setText(String.format("%s", everyArticle.getTitle()));

        one_article_content_tv.setText(Utils.INSTANCE.convertHtmlText(everyArticle.getContent()));


    }

    @Override
    public void initView() {
        one_article_tb.setNavigationOnClickListener(view -> EveryArticleActivity.this.finish());
    }

    @Override
    public void onJwglListenerSuccess(Object t) {
        everyArticle = (EveryArticle) t;

        myDialog.dismiss();

        if (t != null) {
            everyArticle = (EveryArticle) t;
            bindData();

        } else {
            Toast.makeText(setActivity(), R.string.get_content_failure, Toast.LENGTH_SHORT).show();
            EveryArticleActivity.this.finish();

        }

    }

    @Override
    public void onJwglListenerFailure(String text) {
        myDialog.dismiss();
        Toast.makeText(setActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
