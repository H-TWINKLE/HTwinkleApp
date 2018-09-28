package com.twinkle.htwinkle.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.view.IndexLoadMoreView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;


public abstract class BaseRefreshFragment<K, V extends BaseQuickAdapter<K, BaseViewHolder>> extends Fragment {

    public boolean refresh = false;

    public int current = 0;

    public List<K> list;

    public V adapter;

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


    public void setInitView() {

        base_activity_refresh_layout.setRefreshing(true);
        base_activity_refresh_layout.setColorSchemeColors(Color.rgb(47, 223, 189));
        base_activity_refresh_layout.setOnRefreshListener(this::refresh);

        base_activity_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        base_activity_recycler_view.setAdapter(adapter);

        adapter.setOnLoadMoreListener(this::loadMoreData, base_activity_recycler_view);
        adapter.setLoadMoreView(new IndexLoadMoreView());
        adapter.setEnableLoadMore(false);

    }


    public void refresh() {

        current = 0;
        refresh = true;
        base_activity_refresh_layout.setRefreshing(true);
        adapter.setEnableLoadMore(false);


    }

    public void loadMoreData() {

        base_activity_refresh_layout.setRefreshing(true);
        adapter.setEnableLoadMore(false);
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
            Toast.makeText(getActivity(), R.string.get_content_empty, Toast.LENGTH_SHORT).show();
        } else {
            adapter.loadMoreComplete();
        }

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initView();
        setInitView();

    }


    public abstract void initView();

    public abstract void initData();


}
