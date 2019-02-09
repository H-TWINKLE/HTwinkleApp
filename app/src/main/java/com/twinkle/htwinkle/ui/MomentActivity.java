package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.content.Intent;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.IndexAdapter;
import com.twinkle.htwinkle.base.BaseRefreshActivity;
import com.twinkle.htwinkle.entity.Post;
import com.twinkle.htwinkle.net.Bmob;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MomentActivity extends BaseRefreshActivity<Post, IndexAdapter> {

    @Override
    public int setLayout() {
        return R.layout.activity_moment;
    }

    @Override
    public Activity setActivity() {
        return this;
    }

    @Override
    public void initData() {

        setToolBarFlag(true);
        setToolBarTitles("我的朋友圈");


        adapter = new IndexAdapter(this, list);
        adapter.setOnItemClickListener((adapter, view, position) -> {

            openOnePost(this.adapter.getData().get(position));

        });

        getBmobMethod();

    }

    private void openOnePost(Post post) {
        Intent i = new Intent(this, DetailPostActivity.class);
        i.putExtra("post", post);
        startActivity(i);
    }

    @Override
    public void getBmobMethod() {
        Bmob.INSTANCE.BmobGetListUserFocus(current, new findListener());
    }

    private class findListener extends FindListener<Post> {

        @Override
        public void done(List<Post> list, BmobException e) {

            if (e == null) {
                onSuccessGetList(list);
            } else {
                onFailureGetList(e.getLocalizedMessage());
            }

        }
    }

}
