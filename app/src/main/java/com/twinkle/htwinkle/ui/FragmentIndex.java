package com.twinkle.htwinkle.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.twinkle.htwinkle.adapter.GlideImageLoader;
import com.twinkle.htwinkle.dialog.DialogInShowBigPic;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.listener.HidingScrollListener;
import com.twinkle.htwinkle.adapter.IndexAdapter;
import com.twinkle.htwinkle.adapter.IndexTypesAdapter;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.net.Bmob;
import com.twinkle.htwinkle.entity.IndexTypes;
import com.twinkle.htwinkle.entity.Post;
import com.twinkle.htwinkle.init.Constant;
import com.twinkle.htwinkle.view.IndexLoadMoreView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@ContentView(R.layout.fragment_index)
public class FragmentIndex extends Fragment implements Bmob.BmobGetPostListener {

    private DialogInShowBigPic dialogInShowBigPic;

    private boolean isrefresh = false;

    @ViewInject(value = R.id.main_index_srl)
    private SwipeRefreshLayout main_index_srl;

    private int currentPages = 0;

    private BottomNavigationView main_bnv;

    private IndexAdapter indexAdapter;

    private List<Post> list;

    @ViewInject(value = R.id.main_index_rv)
    private RecyclerView main_index_rv;

    private Banner main_bn_header;

    public FragmentIndex() {

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
        initParentView();
        initPv();
        initRv();
    }

    private void initParentView() {

        FloatingActionButton main_index_fab = Objects.requireNonNull(getActivity()).findViewById(R.id.main_index_fab);
        main_index_fab.setOnClickListener(e -> startActivity(new Intent(getActivity(), WriteMessActivity.class)));

        main_bnv = Objects.requireNonNull(getActivity()).findViewById(R.id.main_bnv);
    }

    private void initRv() {

        dialogInShowBigPic = new DialogInShowBigPic(getActivity());

        main_index_srl.setRefreshing(true);
        main_index_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        indexAdapter = new IndexAdapter(getContext(), list);
        indexAdapter.addHeaderView(initBannerHeader1());
        indexAdapter.addHeaderView(initRvHeader2());
        indexAdapter.isFirstOnly(false);
        indexAdapter.openLoadAnimation(1);
        indexAdapter.setLoadMoreView(new IndexLoadMoreView());
        indexAdapter.setEnableLoadMore(false);
        indexAdapter.setOnLoadMoreListener(this::setLoadMoreData, main_index_rv);
        indexAdapter.setOnItemClickListener((adapter, view, position) -> {
            openOnePost(list.get(position));
        });

        indexAdapter.setOnItemChildClickListener((adapter, view, position) -> {
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

        main_index_rv.setAdapter(indexAdapter);
        main_index_rv.addOnScrollListener(new HidingScrollListener() {
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

    private void onStartUserRoom(User user) {

        Intent intent = new Intent(getActivity(), UserRoomActivity.class);
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

    private void openOnePost(Post post) {
        Intent i = new Intent(getContext(), DetailPostActivity.class);
        i.putExtra("post", post);
        Objects.requireNonNull(getContext()).startActivity(i);
    }

    private void initPv() {

        main_index_srl.setColorSchemeColors(Color.rgb(47, 223, 189));
        main_index_srl.setOnRefreshListener(this::refreshData);

    }

    private View initRvHeader2() {

        String array[] = getResources().getStringArray(R.array.topics);

        List<IndexTypes> list = new ArrayList<>();
        for (int s = 0; s < Constant.my_types_icon.length; s++) {
            list.add(new IndexTypes(Constant.my_types_icon[s], array[s]));
        }

        View view = LayoutInflater.from(main_index_rv.getContext()).inflate(R.layout.header_index_types, main_index_rv, false);

        RecyclerView recyclerView = view.findViewById(R.id.main_rv_header);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        IndexTypesAdapter indexTypesAdapter = new IndexTypesAdapter(R.layout.item_index_content_header, list);
        indexTypesAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            toStartActivity(position, array);
        });

        recyclerView.setAdapter(indexTypesAdapter);

        return view;

    }

    private void toStartActivity(int position, String[] array) {
        Intent intent = new Intent(getActivity(), TopicPostActivity.class);
        intent.putExtra("topic", array[position]);

        Objects.requireNonNull(getActivity()).startActivity(intent);
    }


    private void initData() {
        Bmob.INSTANCE.setBmobGetPostListener(this);

        Bmob.INSTANCE.BmobGetPost(currentPages);

    }


    private View initBannerHeader1() {

        View view = getLayoutInflater().inflate(R.layout.header_index_banner, main_index_rv, false);

        main_bn_header = view.findViewById(R.id.main_bn_header);
        main_bn_header.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);//设置banner样式
        main_bn_header.setImageLoader(new GlideImageLoader());//设置图片加载器
        main_bn_header.setImages(Arrays.asList(Constant.my_pic_url)); //设置图片集合
        main_bn_header.setBannerAnimation(Transformer.DepthPage);//设置banner动画效果
        main_bn_header.setBannerTitles(Arrays.asList(Constant.my_pic_title));//设置标题集合（当banner样式有显示title时）
        main_bn_header.isAutoPlay(true);//设置自动轮播，默认为true
        main_bn_header.setDelayTime(3000); //设置轮播时间
        main_bn_header.setIndicatorGravity(BannerConfig.CENTER);//设置指示器位置（当banner模式中有指示器时）
        main_bn_header.start();  //banner设置方法全部调用完毕时最后调用

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        main_bn_header.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        main_bn_header.stopAutoPlay();
    }

    private void hideViews() {
        main_bnv.animate().translationY(main_bnv.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

    }

    private void showViews() {
        main_bnv.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2)).start();
    }


    @Override
    public void onGetPostSuccess(List<Post> list) {

        if (this.list == null || this.list.size() == 0) {
            this.list = list;
            indexAdapter.setNewData(list);
        } else {
            if (isrefresh) {
                indexAdapter.replaceData(list);
                isrefresh = false;
            } else {
                indexAdapter.addData(list);
            }

        }

        main_index_srl.setRefreshing(false);
        indexAdapter.setEnableLoadMore(true);
        currentPages++;

        if (list.size() == 0) {
            indexAdapter.loadMoreEnd();
        } else {
            indexAdapter.loadMoreComplete();
        }


    }

    @Override
    public void onGetPostFailure(String text) {
        main_index_srl.setRefreshing(false);
        indexAdapter.setEnableLoadMore(true);
        indexAdapter.loadMoreFail();
        Toast.makeText(getContext(), R.string.get_content_failure, Toast.LENGTH_SHORT).show();
    }


    private void setLoadMoreData() {
        main_index_srl.setRefreshing(true);
        indexAdapter.setEnableLoadMore(false);
        Bmob.INSTANCE.BmobGetPost(currentPages);

    }

    private void refreshData() {
        main_index_srl.setRefreshing(true);
        indexAdapter.setEnableLoadMore(false);
        isrefresh = true;
        currentPages = 0;
        Bmob.INSTANCE.BmobGetPost(currentPages);
    }


}
