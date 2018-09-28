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

import com.twinkle.htwinkle.adapter.PraiseMeAdapter;
import com.twinkle.htwinkle.net.Bmob;

import com.twinkle.htwinkle.entity.Praise;

import com.twinkle.htwinkle.listener.HidingScrollListener;
import com.twinkle.htwinkle.view.IndexLoadMoreView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Objects;


@ContentView(value = R.layout.fragment_news_praise_m)
public class FragmentNewsPraiseMe extends Fragment implements Bmob.BmobGetListPraiseReceiveByUserListener {

    private Integer current = 0;

    private boolean isrefresh = false;

    private PraiseMeAdapter adapter;

    private List<Praise> list;

    private BottomNavigationView main_bnv;

    @ViewInject(value = R.id.fragment_news_p_me_srl)
    private SwipeRefreshLayout fragment_news_p_me_srl;

    @ViewInject(value = R.id.fragment_news_p_me_rv)
    private RecyclerView fragment_news_p_me_rv;


    public FragmentNewsPraiseMe() {

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
        Bmob.INSTANCE.setBmobGetListPraiseReceiveByUserListener(this);
        Bmob.INSTANCE.BmobGetListPraiseReceiveByUser(current);

    }

    private void onItemClick(int position) {

        Intent intent = new Intent(getActivity(), DetailPostActivity.class);
        intent.putExtra("post", list.get(position).getPost());
        Objects.requireNonNull(getContext()).startActivity(intent);


    }

    private void initView() {

        fragment_news_p_me_srl.setRefreshing(true);
        fragment_news_p_me_srl.setColorSchemeColors(Color.rgb(47, 223, 189));
        fragment_news_p_me_srl.setOnRefreshListener(this::refreshData);

        adapter = new PraiseMeAdapter(R.layout.item_news_praise, list);
        adapter.setLoadMoreView(new IndexLoadMoreView());
        adapter.setEnableLoadMore(false);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            onItemClick(position);
        });

        fragment_news_p_me_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        fragment_news_p_me_rv.setAdapter(adapter);
        fragment_news_p_me_rv.addOnScrollListener(new HidingScrollListener() {
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

    private void refreshData() {
        fragment_news_p_me_srl.setRefreshing(true);
        adapter.setEnableLoadMore(false);
        isrefresh = true;
        current = 0;

        Bmob.INSTANCE.BmobGetListPraiseReceiveByUser(current);


    }


    private void hideViews() {
        main_bnv.animate().translationY(main_bnv.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

    }

    private void showViews() {
        main_bnv.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    @Override
    public void onBmobGetListPraiseReceiveByUserSuccess(List<Praise> list) {
        setData(list);
    }

    @Override
    public void onBmobGetListPraiseReceiveByUserFailure(String text) {
        fragment_news_p_me_srl.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreFail();
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }


    private void setData(List<Praise> list) {

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
        fragment_news_p_me_srl.setRefreshing(false);

        if (list.size() == 0) {
            adapter.loadMoreEnd();
        } else {
            adapter.loadMoreComplete();
        }
    }
}
