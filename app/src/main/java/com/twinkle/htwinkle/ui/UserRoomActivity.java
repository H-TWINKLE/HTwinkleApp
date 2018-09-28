package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.IndexAdapter;
import com.twinkle.htwinkle.base.BaseRefreshActivity;
import com.twinkle.htwinkle.net.Bmob;
import com.twinkle.htwinkle.entity.Post;
import com.twinkle.htwinkle.entity.User;

import java.util.List;

public class UserRoomActivity extends BaseRefreshActivity<Post, IndexAdapter> implements Bmob.BmobFindAllListPostByUserIdListener {


    private User user;


    @Override
    public int setLayout() {
        return R.layout.activity_user_room;
    }

    @Override
    public Activity setActivity() {
        return UserRoomActivity.this;
    }

    @Override
    public void initData() {

        user = (User) getIntent().getSerializableExtra("user");

        setToolBarFlag(true);
        setToolBarTitles(user.getNickName());

        adapter = new IndexAdapter(UserRoomActivity.this, list);
        adapter.setHeaderView(setHeader());
        adapter.setOnItemClickListener((adapter, view, position) -> toStartActivity(list.get(position)));

        Bmob.INSTANCE.setBmobFindAllListPostByUserIdListener(this);

        getBmobMethod();


    }


    private View setHeader() {

        View view = View.inflate(UserRoomActivity.this, R.layout.content_user_room, null);

        SmartImageView user_room_siv_header = view.findViewById(R.id.user_room_siv_header);

        user_room_siv_header.setImageUrl(user.getHeaderPic(), R.drawable.logo);
        user_room_siv_header.setAlpha(0.3f);

        TextView user_room_tv_username = view.findViewById(R.id.user_room_tv_username);

        user_room_tv_username.setText(user.getNickName());

        TextView user_room_tv_say = view.findViewById(R.id.user_room_tv_say);

        user_room_tv_say.setText(user.getAuto());

        return view;


    }


    @Override
    public void onBmobFindAllListPostByUserIdListenerSuccess(List<Post> list) {
        onSuccessGetList(list);
    }

    @Override
    public void onBBmobFindAllListPostByUserIdListenerFailure(String text) {
        onFailureGetList(text);
    }

    @Override
    public void getBmobMethod() {
        Bmob.INSTANCE.BmobFindAllListPostByUserId(user, current);
    }
}
