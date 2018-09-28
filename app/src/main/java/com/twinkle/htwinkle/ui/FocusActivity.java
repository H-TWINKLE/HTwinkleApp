package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.FocusAdapter;
import com.twinkle.htwinkle.base.BaseActivity;
import com.twinkle.htwinkle.net.Bmob;
import com.twinkle.htwinkle.entity.Focus;
import com.twinkle.htwinkle.view.IndexLoadMoreView;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

public class FocusActivity extends BaseActivity implements Bmob.BmobGetListUserFocusListener, Bmob.BmobDisableUserFocusListener {

    @ViewInject(value = R.id.base_empty_line_view)
    private LinearLayout base_empty_line_view;

    @ViewInject(value = R.id.focus_srf)
    private SwipeRefreshLayout focus_srf;

    @ViewInject(value = R.id.focus_rv)
    private RecyclerView focus_rv;


    private FocusAdapter adapter;

    private List<Focus> list;

    private Boolean isrefresh = false;

    private int current = 0;

    private String focus;

    private int flag;


    @Override
    public int setLayout() {
        return R.layout.activity_focus;
    }

    @Override
    public Activity setActivity() {
        return FocusActivity.this;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

        setToolBarFlag(true);

        flag = getIntent().getIntExtra("flag", 1);

        if (flag == 1) {
            setToolBarTitle(R.string.my_focus);
            focus = "onFocusUser";
        } else if (flag == 2) {
            setToolBarTitle(R.string.my_fans);
            focus = "focusUser";
        }

        focus_rv.setLayoutManager(new LinearLayoutManager(FocusActivity.this));
        adapter = new FocusAdapter(R.layout.item_focus_content, list, flag);
        adapter.setEnableLoadMore(false);
        adapter.setLoadMoreView(new IndexLoadMoreView());
        adapter.setOnLoadMoreListener(this::setLoadMoreData, focus_rv);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {

            if (flag == 1) {
                showDialog(R.string.sure_cancel_focus, position);
            } else if (flag == 2) {
                showDialog(R.string.sure_cancel_onfocus, position);
            }

        });

        focus_srf.setRefreshing(true);
        focus_srf.setOnRefreshListener(this::refresh);

        focus_rv.setAdapter(adapter);

        Bmob.INSTANCE.setBmobGetListUserFocusListener(this);
        Bmob.INSTANCE.BmobGetListUserFocus(current, focus);

    }


    private void setLoadMoreData() {
        focus_srf.setRefreshing(true);
        adapter.setEnableLoadMore(false);
        Bmob.INSTANCE.BmobGetListUserFocus(current, focus);

    }


    private void refresh() {
        adapter.setEnableLoadMore(false);
        focus_srf.setRefreshing(true);
        current = 0;
        isrefresh = true;
        Bmob.INSTANCE.BmobGetListUserFocus(current, focus);
    }


    @Override
    public void onBmobGetListUserFocusSuccess(List<Focus> list) {

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

        focus_srf.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        current++;

        if (adapter.getData().size() == 0) {
            base_empty_line_view.setVisibility(View.VISIBLE);
        } else {
            base_empty_line_view.setVisibility(View.GONE);
        }

        if (list.size() == 0) {
            adapter.loadMoreEnd();
        } else {
            adapter.loadMoreComplete();
        }

    }

    @Override
    public void onBmobGetListUserFocusFailure(String text) {
        focus_srf.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreFail();
        Toast.makeText(FocusActivity.this, R.string.get_content_failure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBmobDisableUserFocusSuccess() {
        refresh();
    }

    @Override
    public void onBmobDisableUserFocusFailure(String text) {
        Toast.makeText(FocusActivity.this, text, Toast.LENGTH_SHORT).show();

    }


    private void showDialog(int text, int position) {
        new AlertDialog.Builder(FocusActivity.this).setMessage(text).setPositiveButton(R.string.ok, (dialog1, which) -> {
            Bmob.INSTANCE.setBmobDisableUserFocusListener(this);
            Bmob.INSTANCE.BmobDisableUserFocus(list.get(position));
        }).show();
    }


}
