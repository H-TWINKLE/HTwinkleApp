package com.twinkle.htwinkle.ui;

import android.app.Activity;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.IndexAdapter;
import com.twinkle.htwinkle.base.BaseRefreshActivity;
import com.twinkle.htwinkle.entity.Collection;
import com.twinkle.htwinkle.entity.Post;
import com.twinkle.htwinkle.net.Bmob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollectActivity extends BaseRefreshActivity<Post, IndexAdapter> implements Bmob.BmobGetListUserCollectListener {

    @Override
    public Activity setActivity() {
        return CollectActivity.this;
    }

    @Override
    public void initData() {

        setToolBarFlag(true);
        setToolBarTitle(R.string.my_coll);

        adapter = new IndexAdapter(CollectActivity.this, list);
        adapter.setOnItemClickListener((adapter, view, position) -> toStartActivity(list.get(position)));

        Bmob.INSTANCE.setBmobGetListUserCollectListener(this);

        getBmobMethod();
    }


    public void getBmobMethod() {
        Bmob.INSTANCE.BmobGetListUserCollect(current);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_collect;
    }

    @Override
    public void onBmobGetListUserCollectSuccess(List<Collection> list) {

        Iterator<Collection> iterator = list.iterator();

        List<Post> posts = new ArrayList<>();

        while (iterator.hasNext()) {
            posts.add(iterator.next().getPost());
        }

        onSuccessGetList(posts);

        if (list.size() == 0) {
            adapter.setEmptyView(R.layout.base_content_empty);
        }

    }

    @Override
    public void onBmobGetListUserCollectFailure(String text) {
        onFailureGetList(text);
    }
}
