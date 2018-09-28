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
import com.twinkle.htwinkle.adapter.CommentAdapter;
import com.twinkle.htwinkle.net.Bmob;
import com.twinkle.htwinkle.entity.Comment;
import com.twinkle.htwinkle.listener.HidingScrollListener;
import com.twinkle.htwinkle.view.IndexLoadMoreView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Objects;


@ContentView(R.layout.fragment_news_comment)
public class FragmentNewsComment extends Fragment implements
        Bmob.BmobGetListCommentSendByUserListener, Bmob.BmobGetListPostCommentReceiveByUserListener {

    private int flag = 1;  //1为收到的评论，2为发出的评论

    private Integer current = 0;

    private boolean isrefresh = false;

    private CommentAdapter adapter;

    private List<Comment> list;

    private BottomNavigationView main_bnv;

    @ViewInject(value = R.id.fragment_news_c_srl)
    private SwipeRefreshLayout fragment_news_c_srl;

    @ViewInject(value = R.id.fragment_news_c_rv)
    private RecyclerView fragment_news_c_rv;


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

    public FragmentNewsComment() {

    }


    private void initData() {

        if (getArguments() != null) {
            flag = getArguments().getInt("flag");
        }

        switch (flag) {
            case 1:
                Bmob.INSTANCE.setBmobGetListPostCommentReceiveByUserListener(this);
                Bmob.INSTANCE.BmobGetListPostCommentReceiveByUser(current);
                break;
            case 2:
                Bmob.INSTANCE.setBmobGetListCommentSendByUserListener(this);
                Bmob.INSTANCE.BmobGetListCommentSendByUser(current);
                break;
        }

    }

    private void initView() {

        fragment_news_c_srl.setRefreshing(true);
        fragment_news_c_srl.setColorSchemeColors(Color.rgb(47, 223, 189));
        fragment_news_c_srl.setOnRefreshListener(this::refreshData);

        adapter = new CommentAdapter(R.layout.item_news_comment, list);
        adapter.setLoadMoreView(new IndexLoadMoreView());
        adapter.setEnableLoadMore(false);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            onItemClick(position);
        });

        fragment_news_c_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        fragment_news_c_rv.setAdapter(adapter);
        fragment_news_c_rv.addOnScrollListener(new HidingScrollListener() {
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
        intent.putExtra("post", list.get(position).getPost());
        Objects.requireNonNull(getContext()).startActivity(intent);


    }


    private void hideViews() {
        main_bnv.animate().translationY(main_bnv.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

    }

    private void showViews() {
        main_bnv.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void refreshData() {
        fragment_news_c_srl.setRefreshing(true);
        adapter.setEnableLoadMore(false);
        isrefresh = true;
        current = 0;

        if (flag == 1) {
            Bmob.INSTANCE.BmobGetListPostCommentReceiveByUser(current);
        } else if (flag == 2) {
            Bmob.INSTANCE.BmobGetListCommentSendByUser(current);
        }

    }


    @Override
    public void onBmobGetListPostCommentReceiveByUserSuccess(List<Comment> object) {

        setData(object);
    }

    @Override
    public void onBmobGetListPostCommentReceiveByUserFailure(String text) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreFail();
        fragment_news_c_srl.setRefreshing(false);
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBmobGetListCommentSendByUserSuccess(List<Comment> object) {
        setData(object);
    }

    @Override
    public void onBmobGetListCommentSendByUserFailure(String text) {
        fragment_news_c_srl.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreFail();
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }


    private void setData(List<Comment> list) {

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
        fragment_news_c_srl.setRefreshing(false);

        if (list.size() == 0) {
            adapter.loadMoreEnd();
        } else {
            adapter.loadMoreComplete();
        }
    }


}
