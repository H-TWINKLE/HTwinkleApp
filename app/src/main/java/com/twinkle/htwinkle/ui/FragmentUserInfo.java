package com.twinkle.htwinkle.ui;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.net.Bmob;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.bmob.v3.BmobUser;

import static com.twinkle.htwinkle.init.Constant.Modify_Pass;


@ContentView(R.layout.fragment_fragment_user_info)
public class FragmentUserInfo extends Fragment implements Bmob.ModifyUserInfoListener {


    @ViewInject(value = R.id.user_info_lv)
    private ListView user_info_lv;

    private SimpleAdapter adapter;

    private User user;

    private List<Map<String, Object>> list = new ArrayList<>();

    public FragmentUserInfo() {
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

    }

    private void initData() {

        Bmob.INSTANCE.setModifyUserInfoListener(this);

        user = BmobUser.getCurrentUser(User.class);

        setListValue();

        adapter = new SimpleAdapter(getContext(), list, android.R.layout.simple_list_item_2,
                new String[]{"title", "value"}, new int[]{android.R.id.text1, android.R.id.text2});

        user_info_lv.setAdapter(adapter);
        user_info_lv.setOnItemClickListener(listener);

    }

    private void startActivity() {

        Intent intent = new Intent(getActivity(), RegOrForActivity.class);
        intent.putExtra("flag", Modify_Pass);
        startActivity(intent);

    }

    private AdapterView.OnItemClickListener listener = (parent, view, position, id) -> {
        switch (position) {
            case 0:
                break;
            case 7:
                break;
            case 2:
                showSexDialog();
                break;
            case 8:
                startActivity();
                break;
            default:
                showOtherMessage(position);

        }
    };

    private void showSexDialog() {
        new AlertDialog.Builder(getContext()).setItems(new CharSequence[]{"男", "女"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    Bmob.INSTANCE.BmobModifyUserInfo(new User.Builder().sex("男").build());
                    list.get(2).put("value", "男");
                    break;
                case 1:
                    Bmob.INSTANCE.BmobModifyUserInfo(new User.Builder().sex("女").build());
                    list.get(2).put("value", "女");
                    break;
            }
        }).setTitle("请选择性别").show();
    }

    private void showOtherMessage(int positon) {

        EditText editText = new EditText(getContext());
        editText.setText(list.get(positon).get("value") == null ? "" : list.get(positon).get("value").toString());
        new AlertDialog.Builder(getContext()).setTitle(list.get(positon).get("title").toString())
                .setView(editText).setPositiveButton("修改", (dialog, which) -> {
            if (TextUtils.isEmpty(editText.getText().toString())) return;
            switch (positon) {
                case 1:
                    Bmob.INSTANCE.BmobModifyUserInfo(new User.Builder().nickName(editText.getText().toString()).build());
                    list.get(1).put("value", editText.getText().toString());
                    break;
                case 3:
                    Bmob.INSTANCE.BmobModifyUserInfo(new User.Builder().auto(editText.getText().toString()).build());
                    list.get(3).put("value", editText.getText().toString());
                    break;
                case 4:
                    Bmob.INSTANCE.BmobModifyUserInfo(new User.Builder().schoolId(editText.getText().toString()).build());
                    list.get(4).put("value", editText.getText().toString());
                    break;
                case 5:
                    Bmob.INSTANCE.BmobModifyUserInfo(new User.Builder().eol(editText.getText().toString()).build());
                    list.get(5).put("value", editText.getText().toString());
                    break;
                case 6:
                    Bmob.INSTANCE.BmobModifyUserInfo(new User.Builder().jwgl(editText.getText().toString()).build());
                    list.get(6).put("value", editText.getText().toString());
                    break;
                default:
                    break;
            }


        }).show();
    }


    @Override
    public void ModifyUserInfoSuccess() {
        Bmob.INSTANCE.BmobFetchUserInfo(getContext());
        adapter.notifyDataSetChanged();
        sendBroadcast();

    }

    @Override
    public void ModifyUserInfoFailure(String text) {
        Toast.makeText(getContext(), R.string.modify_user_info_failure, Toast.LENGTH_SHORT).show();
    }


    private void setListValue() {

        String[] user_info = getResources().getStringArray(R.array.my_list_info);

        if (list.size() != 0) {
            list.clear();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("title", user_info[0]);
        map.put("value", user.getUsername());
        list.add(map);
        map = new HashMap<>();
        map.put("title", user_info[1]);
        map.put("value", user.getNickName());
        list.add(map);
        map = new HashMap<>();
        map.put("title", user_info[2]);
        map.put("value", user.getSex());
        list.add(map);
        map = new HashMap<>();
        map.put("title", user_info[3]);
        map.put("value", user.getAuto());
        list.add(map);
        map = new HashMap<>();
        map.put("title", user_info[4]);
        map.put("value", user.getSchoolId());
        list.add(map);
        map = new HashMap<>();
        map.put("title", user_info[5]);
        map.put("value", user.getEol());
        list.add(map);
        map = new HashMap<>();
        map.put("title", user_info[6]);
        map.put("value", user.getJwgl());
        list.add(map);
        map = new HashMap<>();
        map.put("title", user_info[7]);
        map.put("value", user.getLv() == null ? 0 : user.getLv());
        list.add(map);
        map = new HashMap<>();
        map.put("title", user_info[8]);
        map.put("value", getString(R.string.click_me_to_modify_pass));
        list.add(map);


    }


    private void sendBroadcast() {

        Intent i = new Intent();
        i.setAction("User_Info_Update");
        Objects.requireNonNull(getContext()).sendBroadcast(i);


    }


}
