package com.twinkle.htwinkle.ui;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.IndexAdapter;
import com.twinkle.htwinkle.net.Bmob;
import com.twinkle.htwinkle.entity.Post;

import com.twinkle.htwinkle.listener.HidingScrollListener;
import com.twinkle.htwinkle.view.IndexLoadMoreView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Objects;


@ContentView(value = R.layout.fragment_news_praise)
public class FragmentNewsPraise extends Fragment implements Bmob.BmobGetListPraiseSendByUserListener {

    private Integer current = 0;

    private boolean isrefresh = false;

    private IndexAdapter adapter;

    private List<Post> list;

    private BottomNavigationView main_bnv;

    @ViewInject(value = R.id.fragment_news_p_srl)
    private SwipeRefreshLayout fragment_news_p_srl;

    @ViewInject(value = R.id.fragment_news_p_rv)
    private RecyclerView fragment_news_p_rv;


    public FragmentNewsPraise() {

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
        initParentView();
    }

    private void initParentView() {
        main_bnv = Objects.requireNonNull(getActivity()).findViewById(R.id.main_bnv);
    }


    private void initData() {

        Bmob.INSTANCE.setBmobGetListPraiseSendByUserListener(this);
        Bmob.INSTANCE.BmobGetListPraiseSendByUser(current);

    }

    private void initView() {

        fragment_news_p_srl.setRefreshing(true);
        fragment_news_p_srl.setColorSchemeColors(Color.rgb(47, 223, 189));
        fragment_news_p_srl.setOnRefreshListener(this::refreshData);

        adapter = new IndexAdapter(getContext(), list);
        adapter.setLoadMoreView(new IndexLoadMoreView());
        adapter.setEnableLoadMore(false);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            onItemClick(position);
        });

        fragment_news_p_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        fragment_news_p_rv.setAdapter(adapter);
        fragment_news_p_rv.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });


    }

    private void onItemClick(int position) {

        Intent intent = new Intent(getActivity(), DetailPostActivity.class);
        intent.putExtra("post", list.get(position));
        Objects.requireNonNull(getContext()).startActivity(intent);


    }

    private void refreshData() {
        fragment_news_p_srl.setRefreshing(true);
        adapter.setEnableLoadMore(false);
        isrefresh = true;
        current = 0;

        Bmob.INSTANCE.BmobGetListPraiseSendByUser(current);


    }


    private void hideViews() {
        main_bnv.animate().translationY(main_bnv.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

    }

    private void showViews() {
        main_bnv.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    @Override
    public void onBmobGetListPraiseSendByUserSuccess(List<Post> posts) {
        setData(posts);
    }

    @Override
    public void onBmobGetListPraiseSendByUserFailure(String text) {
        fragment_news_p_srl.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreFail();
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void setData(List<Post> list) {

        if (this.list == null || this.list.size() == 0) {
            this.list = list;
            adapter.setNewData(list);
        } else {
            if (isrefresh) {
                adapter.replaceData(list);
                isrefresh = false;
            } else {
                adapter.addData(list);
            }

        }

        current++;
        adapter.setEnableLoadMore(true);
        fragment_news_p_srl.setRefreshing(false);

        if (list.size() == 0) {
            adapter.loadMoreEnd();
        } else {
            adapter.loadMoreComplete();
        }
    }
}
