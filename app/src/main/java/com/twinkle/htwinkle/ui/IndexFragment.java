package com.twinkle.htwinkle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.twinkle.htwinkle.Adapter.GlideImageLoader;
import com.twinkle.htwinkle.Adapter.HidingScrollListener;
import com.twinkle.htwinkle.Adapter.IndexAdapter;
import com.twinkle.htwinkle.Adapter.IndexTypesAdapter;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.bean.IndexTypes;
import com.twinkle.htwinkle.init.InitString;
import com.twinkle.htwinkle.yalantis.OwnPullToRefreshView;
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
public class IndexFragment extends Fragment {

    @ViewInject(value = R.id.main_index_pv)
    private OwnPullToRefreshView main_index_pv;


    private BottomNavigationView main_bnv;

    private IndexAdapter indexAdapter;

    private List<String> list;

    @ViewInject(value = R.id.main_index_rv)
    private RecyclerView main_index_rv;

    private Banner main_bn_header;

    public IndexFragment() {

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

        FloatingActionButton  main_index_fab = Objects.requireNonNull(getActivity()).findViewById(R.id.main_index_fab);
        main_index_fab.setOnClickListener(e -> startActivity(new Intent(getActivity(), WriteMessActivity.class)));

        main_bnv = Objects.requireNonNull(getActivity()).findViewById(R.id.main_bnv);
    }

    private void initRv() {
        main_index_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        indexAdapter = new IndexAdapter(R.layout.index_content, list);
        indexAdapter.addHeaderView(initBannerHeader1());
        indexAdapter.addHeaderView(initRvHeader2());
        indexAdapter.isFirstOnly(false);
        indexAdapter.openLoadAnimation(1);

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

    private void initPv(){

        main_index_pv.setOnRefreshListener(()->{
            main_index_pv.postDelayed(()->{
                main_index_pv.setRefreshing(false);
            },3000);
        });

    }

    private View initRvHeader2() {

        List<IndexTypes> list = new ArrayList<>();
        for (int s = 0; s < InitString.my_types_title.length; s++) {
            list.add(new IndexTypes(InitString.my_types_icon[s], InitString.my_types_title[s]));
        }

        View view = LayoutInflater.from(main_index_rv.getContext()).inflate(R.layout.header_index_types, main_index_rv, false);


        RecyclerView recyclerView = view.findViewById(R.id.main_rv_header);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView.setAdapter(new IndexTypesAdapter(R.layout.index_content_rv, list));


        return view;

    }


    private void initData() {
        list = new ArrayList<>();

        for (int x = 0; x < 10; x++) {
            list.add("this is a :" + x);
        }

    }


    private View initBannerHeader1() {

        View view = getLayoutInflater().inflate(R.layout.header_index_banner, main_index_rv, false);

        main_bn_header = view.findViewById(R.id.main_bn_header);
        main_bn_header.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);//设置banner样式
        main_bn_header.setImageLoader(new GlideImageLoader());//设置图片加载器
        main_bn_header.setImages(Arrays.asList(InitString.my_pic_url)); //设置图片集合
        main_bn_header.setBannerAnimation(Transformer.DepthPage);//设置banner动画效果
        main_bn_header.setBannerTitles(Arrays.asList(InitString.my_pic_title));//设置标题集合（当banner样式有显示title时）
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


}
