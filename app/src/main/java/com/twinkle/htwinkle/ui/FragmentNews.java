package com.twinkle.htwinkle.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.FragAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@ContentView(R.layout.fragment_news)
public class FragmentNews extends Fragment {

    @ViewInject(value = R.id.news_comment_vp)
    private ViewPager news_comment_vp;


    @ViewInject(value = R.id.news_tabl)
    private TabLayout news_tabl;

    private List<Fragment> fragmentList;

    public FragmentNews() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setFragmentList();
        vp();

    }


    private void setFragmentList() {
        fragmentList = new ArrayList<>();

        FragmentNewsComment fragmentNewsComment1 = new FragmentNewsComment();
        fragmentNewsComment1.setArguments(sendBundle(1));
        fragmentList.add(fragmentNewsComment1);

        FragmentNewsComment fragmentNewsComment2 = new FragmentNewsComment();
        fragmentNewsComment1.setArguments(sendBundle(2));
        fragmentList.add(fragmentNewsComment2);

        fragmentList.add(new FragmentNewsPraiseMe());

        fragmentList.add(new FragmentNewsPraise());


    }

    private void vp() {

        news_comment_vp.setAdapter(new FragAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), fragmentList));
        news_tabl.setupWithViewPager(news_comment_vp);
        setTapName();

    }


    private Bundle sendBundle(int flag) {
        Bundle bundle = new Bundle();
        bundle.putInt("flag", flag);
        return bundle;
    }

    private void setTapName() {

        Objects.requireNonNull(news_tabl.getTabAt(0)).setText(R.string.send_comm);
        Objects.requireNonNull(news_tabl.getTabAt(1)).setText(R.string.received_comm);
        Objects.requireNonNull(news_tabl.getTabAt(2)).setText(R.string.received_praise);
        Objects.requireNonNull(news_tabl.getTabAt(3)).setText(R.string.send_praise);

    }


}
