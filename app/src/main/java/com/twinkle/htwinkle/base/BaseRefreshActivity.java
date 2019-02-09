package com.twinkle.htwinkle.base;


import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.entity.Post;
import com.twinkle.htwinkle.ui.DetailPostActivity;
import com.twinkle.htwinkle.view.IndexLoadMoreView;

import org.xutils.view.annotation.ViewInject;

import java.util.List;


public abstract class BaseRefreshActivity<K, V extends BaseQuickAdapter<K, BaseViewHolder>> extends BaseActivity {


    public boolean isRefresh = true;

    public boolean isNeedLoadMore = true;

    public boolean refresh = false;

    public int current = 0;

    public List<K> list;

    public V adapter;

    public RecyclerView.LayoutManager layoutManager;

    @ViewInject(value = R.id.base_activity_refresh_layout)
    public SwipeRefreshLayout base_activity_refresh_layout;

    @ViewInject(value = R.id.base_activity_recycler_view)
    public RecyclerView base_activity_recycler_view;

    public SwipeRefreshLayout getBase_activity_refresh_layout() {
        return base_activity_refresh_layout;
    }

    public RecyclerView getBase_activity_recycler_view() {
        return base_activity_recycler_view;
    }


    public void initView() {

        setInitView();

    }

    public void setInitView() {


        base_activity_refresh_layout.setColorSchemeColors(Color.rgb(47, 223, 189));
        base_activity_refresh_layout.setOnRefreshListener(this::refresh);

        if (layoutManager == null) {
            base_activity_recycler_view.setLayoutManager(new LinearLayoutManager(setActivity()));
        } else {
            base_activity_recycler_view.setLayoutManager(layoutManager);
        }

        base_activity_recycler_view.setAdapter(adapter);

        if (isRefresh) {
            base_activity_refresh_layout.setRefreshing(true);
        }

        if (isNeedLoadMore) {
            adapter.setOnLoadMoreListener(this::loadMoreData, base_activity_recycler_view);
        }
        adapter.setLoadMoreView(new IndexLoadMoreView());
        adapter.setEnableLoadMore(false);

    }

    public abstract void getBmobMethod();

    public void toStartActivity(Post post) {
        Intent intent = new Intent(setActivity(), DetailPostActivity.class);
        intent.putExtra("post", post);
        startActivity(intent);

    }

    public void refresh() {

        current = 0;
        refresh = true;
        base_activity_refresh_layout.setRefreshing(true);
        adapter.setEnableLoadMore(false);

        getBmobMethod();


    }

    public void loadMoreData() {

        base_activity_refresh_layout.setRefreshing(true);
        adapter.setEnableLoadMore(false);

        getBmobMethod();
    }


    public void onSuccessGetList(List<K> list) {

        if (this.list == null || this.list.size() == 0) {
            this.list = list;
            adapter.setNewData(list);
        } else {
            if (refresh) {
                adapter.replaceData(list);
                refresh = false;
            } else {
                adapter.addData(list);
            }

        }

        base_activity_refresh_layout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        current++;

        if (list == null || list.size() == 0) {
            adapter.loadMoreEnd();
        } else {
            adapter.loadMoreComplete();
        }

    }

    public void onFailureGetList(String text) {
        base_activity_refresh_layout.setRefreshing(false);
        Toast.makeText(setActivity(), text, Toast.LENGTH_SHORT).show();
    }


}
