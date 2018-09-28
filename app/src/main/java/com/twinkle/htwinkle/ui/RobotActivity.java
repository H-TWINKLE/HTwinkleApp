package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.RobotAdapter;
import com.twinkle.htwinkle.base.BaseActivity;
import com.twinkle.htwinkle.entity.Robot;
import com.twinkle.htwinkle.entity.Talk;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.init.Utils;
import com.twinkle.htwinkle.net.Twinkle;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class RobotActivity extends BaseActivity implements Twinkle.JwglListener {

    @ViewInject(R.id.robot_rv)
    private RecyclerView robot_rv;

    @ViewInject(R.id.detail_user_comment_siv_logo)
    private SmartImageView detail_user_comment_siv_logo;

    @ViewInject(R.id.detail_user_comment_et_text)
    private EditText detail_user_comment_et_text;

    private RobotAdapter robotAdapter;

    private Talk talk;

    @Event(R.id.detail_user_comment_siv_logo)
    private void onLogoClick(View view) {
        sendMessage();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_robot;
    }

    @Override
    public Activity setActivity() {
        return RobotActivity.this;
    }

    @Override
    public void initData() {

        setToolBarFlag(true);

        setToolBarTitle(R.string.robot);

        Twinkle.INSTANCE.setJwglListener(this);

        detail_user_comment_et_text.addTextChangedListener(textWatcher);
        detail_user_comment_et_text.setOnClickListener(view -> {
            toScroll();
        });

        List<Talk> list = new ArrayList<>();

        talk = new Talk(1, BmobUser.getCurrentUser(User.class).getNickName(), new Date());
        talk.setUser(BmobUser.getCurrentUser(User.class));

        robotAdapter = new RobotAdapter(list);
        robotAdapter.addHeaderView(addHeader());

        robot_rv.setLayoutManager(new LinearLayoutManager(RobotActivity.this));
        robot_rv.setAdapter(robotAdapter);

    }

    private View addHeader() {

        return View.inflate(setActivity(), R.layout.item_talk_in_left, null);

    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(detail_user_comment_et_text.getText().toString())) {
                x.image().bind(detail_user_comment_siv_logo, BmobUser.getCurrentUser(User.class).getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());
            } else {
                detail_user_comment_siv_logo.setImageDrawable(getDrawable(R.drawable.ic_send_black_36dp));
            }
        }
    };


    private void sendMessage() {

        String mess = detail_user_comment_et_text.getText().toString();

        if (TextUtils.isEmpty(mess)) {
            detail_user_comment_et_text.setError(getString(R.string.please_input_content));
            detail_user_comment_et_text.setFocusable(true);
            return;
        }

        Twinkle.INSTANCE.sendMessageToRobot(mess);

        talk.setRobot(new Robot(mess));
        talk.setDate(new Date());

        robotAdapter.addData(talk);


    }

    @Override
    public void initView() {

        detail_user_comment_et_text.setHint(R.string.talk_with_me);

        x.image().bind(detail_user_comment_siv_logo, BmobUser.getCurrentUser(User.class).getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());


    }

    @Override
    public void onJwglListenerSuccess(Object t) {

        Talk talk = (Talk) t;

        detail_user_comment_et_text.getText().clear();
        robotAdapter.addData(talk);


        toScroll();
    }

    private void toScroll() {
        try {
            robot_rv.scrollToPosition(robotAdapter.getData().size());
        } catch (Exception ignored) {
        }

    }


    @Override
    public void onJwglListenerFailure(String text) {

        Toast.makeText(setActivity(), text, Toast.LENGTH_SHORT).show();


    }
}
