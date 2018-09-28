package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.adapter.JwglTtbAdapter;
import com.twinkle.htwinkle.base.BaseActivity;
import com.twinkle.htwinkle.dialog.MyDialog;
import com.twinkle.htwinkle.entity.Jwgl;
import com.twinkle.htwinkle.entity.JwglTtb;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.net.Twinkle;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;

public class JwglTtbActivity extends BaseActivity implements Twinkle.JwglListener {

    @ViewInject(R.id.jwgl_score_gv)
    private GridView jwgl_score_gv;

    @ViewInject(R.id.base_empty_line_view)
    private LinearLayout base_empty_line_view;

    private JwglTtb jwglTtb;

    private MyDialog myDialog;

    private JwglTtbAdapter adapter;

    private List<String> list;


    @Override
    public int setLayout() {
        return R.layout.activity_jwgl_ttb;
    }

    @Override
    public Activity setActivity() {
        return JwglTtbActivity.this;
    }

    @Override
    public void initData() {

        setToolBarFlag(true);

        setToolBarTitle(R.string.ttb);

        myDialog = new MyDialog(setActivity(), R.style.AlertDialog, true);

        Twinkle.INSTANCE.setJwglListener(this);

        jwglTtb = Twinkle.INSTANCE.getJwglTtb(BmobUser.getCurrentUser(User.class));

        list = new ArrayList<>();

        if (jwglTtb == null) {
            getData();
        } else {
            analList();
        }


    }


    private void analList() {

        String[][] str = new String[6][5];

        String[] week1 = jwglTtb.getWeek1().split("\\*,");
        str = addList(0, str, week1, "一");

        String[] week2 = jwglTtb.getWeek2().split("\\*,");
        str = addList(1, str, week2, "二");

        String[] week3 = jwglTtb.getWeek3().split("\\*,");
        str = addList(2, str, week3, "三");

        String[] week4 = jwglTtb.getWeek4().split("\\*,");
        str = addList(3, str, week4, "四");

        String[] week5 = jwglTtb.getWeek5().split("\\*,");
        str = addList(4, str, week5, "五");

        list.addAll(Arrays.asList(str[0]));
        list.addAll(Arrays.asList(str[1]));
        list.addAll(Arrays.asList(str[2]));
        list.addAll(Arrays.asList(str[3]));
        list.addAll(Arrays.asList(str[4]));


    }

    private String[][] addList(int x, String[][] str, String[] week, String weeks) {

        for (String string : week) {

            string = string.replace("*", "").replace("[", "").replace("]", "");

            if (Pattern.compile("周" + weeks + "第1").matcher(string).find()) {
                str[0][x] = string;
            } else if (Pattern.compile("周" + weeks + "第3").matcher(string).find()) {
                str[1][x] = string;
            } else if (Pattern.compile("周" + weeks + "第5").matcher(string).find()) {
                str[2][x] = string;
            } else if (Pattern.compile("周" + weeks + "第7").matcher(string).find()) {
                str[3][x] = string;
            } else if (Pattern.compile("周" + weeks + "第9").matcher(string).find()) {
                str[4][x] = string;
            } else if (Pattern.compile("周" + weeks + "第11").matcher(string).find()) {
                str[5][x] = string;
            }

        }

        return str;


    }


    @Override
    public void initView() {

        adapter = new JwglTtbAdapter(setActivity(), list);

        jwgl_score_gv.setAdapter(adapter);

    }

    private void getData() {

        myDialog.show();
        Twinkle.INSTANCE.getJwgl(BmobUser.getCurrentUser(User.class), true);
    }


    @Override
    public void onJwglListenerSuccess(Object t) {

        Jwgl j = (Jwgl)t;

        myDialog.dismiss();

        jwglTtb = j.getJwglttb();

        if (jwglTtb == null) {
            base_empty_line_view.setVisibility(View.VISIBLE);
            Toast.makeText(setActivity(), R.string.ttb_is_null, Toast.LENGTH_SHORT).show();
        } else {
            base_empty_line_view.setVisibility(View.GONE);
            list.clear();
            analList();
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onJwglListenerFailure(String text) {

        myDialog.dismiss();
        Toast.makeText(setActivity(), text, Toast.LENGTH_SHORT).show();

    }
}
