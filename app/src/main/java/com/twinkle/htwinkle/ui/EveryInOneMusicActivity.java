package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;
import com.twinkle.htwinkle.dialog.MyDialog;
import com.twinkle.htwinkle.entity.EveryMusic;
import com.twinkle.htwinkle.init.Constant;
import com.twinkle.htwinkle.init.Utils;
import com.twinkle.htwinkle.net.Twinkle;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class EveryInOneMusicActivity extends BaseActivity implements Twinkle.JwglListener {

    @ViewInject(R.id.one_music_toolbar)
    private Toolbar one_music_toolbar;

    @ViewInject(R.id.one_music_siv_background)
    private SmartImageView one_music_siv_background;

    private MyDialog myDialog;

    private EveryMusic everyMusic;

    @ViewInject(R.id.one_music_siv)
    private SmartImageView one_music_siv;

    @ViewInject(R.id.one_music_author)
    private TextView one_music_author;

    @ViewInject(R.id.one_music_comment)
    private TextView one_music_comment;

    @ViewInject(R.id.one_music_comment_author)
    private TextView one_music_comment_author;

    @Event(R.id.one_music_open_url)
    private void onUrlOpen(View view) {

        if (TextUtils.isEmpty(everyMusic.getNetmusicurl()))
            return;

        Uri uri = Uri.parse(Constant.NetMusicUrl + everyMusic.getNetmusicurl());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }


    @Override
    public int setLayout() {
        return R.layout.activity_every_in_one_music;
    }

    @Override
    public Activity setActivity() {
        return EveryInOneMusicActivity.this;
    }

    @Override
    public void initData() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        one_music_toolbar.setTitle(R.string.every_music);

        Twinkle.INSTANCE.setJwglListener(this);

        myDialog = new MyDialog(EveryInOneMusicActivity.this, R.style.AlertDialog, true);

        everyMusic = Twinkle.INSTANCE.readOneMusic();

        if (everyMusic == null) {
            myDialog.show();
            Twinkle.INSTANCE.getEveryOneMusic(10);
        } else {
            one_music_toolbar.setTitle(String.format("%s", everyMusic.getNetname()));
            bindData();
        }


    }

    private void bindData() {

        x.image().bind(one_music_siv, everyMusic.getNetpic(), Utils.INSTANCE.ImageOptionsInCir());

        x.image().bind(one_music_siv_background, everyMusic.getNetpic());

        one_music_author.setText(String.format("%s", everyMusic.getNetsinger()));

        one_music_comment.setText(String.format("%s", everyMusic.getNetcomm()));

        one_music_comment_author.setText(String.format("%s", everyMusic.getNettauthor()));

        one_music_toolbar.setTitle(String.format("%s", everyMusic.getNetname()));

    }

    @Override
    public void initView() {
        one_music_toolbar.setNavigationOnClickListener(view -> {
            this.finish();
        });
    }

    @Override
    public void onJwglListenerSuccess(Object t) {

        myDialog.dismiss();

        if (t != null) {
            everyMusic = (EveryMusic) t;
            bindData();

        } else {
            Toast.makeText(setActivity(), R.string.get_content_failure, Toast.LENGTH_SHORT).show();
            EveryInOneMusicActivity.this.finish();

        }


    }

    @Override
    public void onJwglListenerFailure(String text) {
        myDialog.dismiss();
        Toast.makeText(setActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
