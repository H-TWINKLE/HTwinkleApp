package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.content.Intent;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.IndexAdapter;
import com.twinkle.htwinkle.base.BaseRefreshActivity;
import com.twinkle.htwinkle.dialog.DialogInShowBigPic;
import com.twinkle.htwinkle.entity.Post;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.net.Bmob;

import java.util.List;

public class TopicPostActivity extends BaseRefreshActivity<Post, IndexAdapter> implements Bmob.BmobFindPostByTopicListener {


    private String[] topic;

    private DialogInShowBigPic dialogInShowBigPic;

    @Override
    public int setLayout() {
        return R.layout.activity_topic_post;
    }

    @Override
    public Activity setActivity() {
        return TopicPostActivity.this;
    }

    @Override
    public void initData() {

        topic = new String[1];
        topic[0] = getIntent().getStringExtra("topic");

        setToolBarFlag(true);
        setToolBarTitles(topic[0]);

        dialogInShowBigPic = new DialogInShowBigPic(TopicPostActivity.this);

        adapter = new IndexAdapter(TopicPostActivity.this, list);
        adapter.setOnItemClickListener((adapter, view, position) -> toStartActivity(list.get(position)));
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.index_cr_iv_one_img:
                    if (list.get(position).getPic() != null && list.get(position).getPic().size() > 0) {
                        onShowDialog(list.get(position).getPic().get(0));
                    }
                    break;

                case R.id.index_cr_tv_username:
                    onStartUserRoom(list.get(position).getAuthor());
                    break;
                case R.id.index_cr_iv_header:
                    onStartUserRoom(list.get(position).getAuthor());
                    break;

            }
        });

        Bmob.INSTANCE.setBmobFindPostByTopicListener(this);

        getBmobMethod();

    }

    private void onStartUserRoom(User user) {

        Intent intent = new Intent(TopicPostActivity.this, UserRoomActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);


    }


    private void onShowDialog(String url) {
        if (url == null) {
            return;
        }
        dialogInShowBigPic.setUrl(url);
        dialogInShowBigPic.onShow();
    }

    @Override
    public void getBmobMethod() {
        Bmob.INSTANCE.BmobFindPostByTopic(topic, current);
    }


    @Override
    public void onBmobFindPostByTopicListenerSuccess(List<Post> list) {
        onSuccessGetList(list);

        if (adapter.getData().size() == 0) {
            adapter.setEmptyView(R.layout.base_content_empty);
        }
    }

    @Override
    public void onBmobFindPostByTopicListenerFailure(String text) {
        onFailureGetList(text);
    }
}
