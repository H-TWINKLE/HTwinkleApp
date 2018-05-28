package com.twinkle.htwinkle.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twinkle.htwinkle.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;


@ContentView(R.layout.fragment_news)
public class NewsFragment extends Fragment {

    public NewsFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);

    }




}