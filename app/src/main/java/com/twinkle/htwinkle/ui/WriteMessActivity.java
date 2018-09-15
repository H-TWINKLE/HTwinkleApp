package com.twinkle.htwinkle.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.loopj.android.image.SmartImageView;
import com.twinkle.htwinkle.adapter.WMessIvAdapter;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.bean.Post;
import com.twinkle.htwinkle.bean.User;
import com.twinkle.htwinkle.bmob.Bmob;
import com.twinkle.htwinkle.dialog.MyDialog;
import com.twinkle.htwinkle.init.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

import static com.twinkle.htwinkle.init.Constant.REQUEST_CODE;

@ContentView(R.layout.activity_write_mess)
public class WriteMessActivity extends TakePhotoActivity implements Bmob.BmobUploadPostPicListener, Bmob.BmobAddPostListener {

    private static final String TAG = "WriteMessActivity";

    private MyDialog myDialog;

    private Post post;

    private List<TImage> list;

    private List<String> listtopic;

    @ViewInject(value = R.id.wMess_siv_header)
    private SmartImageView wMess_siv_header;

    @ViewInject(value = R.id.wMess_iv_send)
    private ImageView wMess_iv_send;

    @ViewInject(value = R.id.wMess_et_mess)
    private EditText wMess_et_mess;

    @ViewInject(value = R.id.wMess_tv_local)
    private TextView wMess_tv_local;

    @ViewInject(value = R.id.wMess_rv_img)
    private RecyclerView wMess_rv_img;

    @Event(value = R.id.wMess_tv_local)
    private void onLocalClick(View view) {
        wMess_tv_local.setText(null);
        post.setPlace(null);
    }


    @Event(value = R.id.wMess_iv_send)
    private void onSendClick(View view) {

        //发送信息

        myDialog = new MyDialog(WriteMessActivity.this, R.style.AlertDialog);
        myDialog.show();

        Bmob.INSTANCE.setBmobAddPostListener(this);

        if (list != null && list.size() != 0) {
            Bmob.INSTANCE.setBmobUploadPostPicListener(this);
            Bmob.INSTANCE.BmobUploadPostPic(list);
        } else {
            sendPost();
        }
    }


    @Event(value = R.id.wMess_iv_back)
    private void onBackClick(View view) {
        finish();
    }

    @Event(value = R.id.wMess_iv_cPic)
    private void onCPicClick(View view) {
        getTakePhoto().onPickMultipleWithCrop(9, new CropOptions.Builder().setAspectX(800).setAspectY(800).setWithOwnCrop(true).create());
    }

    @Event(value = R.id.wMess_iv_cTopic)
    private void onCTopicClick(View view) {
        createTopicDialog();
    }

    @Event(value = R.id.wMess_iv_cLocal)
    private void onCLocalClick(View view) {
        Intent i = new Intent(this, TopicActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE) {
            wMess_tv_local.setText(data.getStringExtra("local"));
            post.setPlace(data.getStringExtra("local"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
        initView();
    }


    private void sendPost() {
        post.setTopic(listtopic);
        post.setContent( String.valueOf(wMess_et_mess.getText()) );
        post.setAuthor(BmobUser.getCurrentUser(User.class));
        Log.i(TAG, "sendPost: "+ JSON.toJSONString(post));

        Bmob.INSTANCE.BmobAddPost(post);
    }


    private void initView() {

        wMess_iv_send.setVisibility(View.INVISIBLE);
        wMess_et_mess.addTextChangedListener(textWatcher);
        x.image().bind(wMess_siv_header, BmobUser.getCurrentUser(User.class).getHeaderPic(), Utils.INSTANCE.ImageOptionsInCir());


    }

    private void initData() {
        post = new Post();
        listtopic = new ArrayList<>();

    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(wMess_et_mess.getText().toString())) {
                wMess_iv_send.setVisibility(View.VISIBLE);
            } else {
                wMess_iv_send.setVisibility(View.INVISIBLE);
            }
        }
    };


    @Override
    public void takeSuccess(TResult result) {
        initRv(result.getImages());
    }

    private void initRv(List<TImage> list) {
        this.list = list;

        wMess_rv_img.setLayoutManager(new GridLayoutManager(WriteMessActivity.this, 3));

        WMessIvAdapter adapter = new WMessIvAdapter(R.layout.base_imageview, list);
        wMess_rv_img.setAdapter(adapter);

        adapter.setOnItemChildClickListener((a, v, p) -> {
            switch (v.getId()) {
                case R.id.wMess_rv_base_iv_del:
                    list.remove(p);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        });

    }

    private void deleteCacheFile() {
        if (list == null) return;
        for (TImage path : list) {
            boolean flag = new File(path.getOriginalPath()).delete();
            if (!flag) return;
        }
    }

    private void createTopicDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.choose_topic)
                .setCancelable(true)
                .setItems(R.array.topics, (d, p) ->
                        addText(getResources().getStringArray(R.array.topics)[p])).setNegativeButton(R.string.cancel, null)
                .create();

        dialog.show();

    }

    private void addText(String text) {
        listtopic.add(text);
        wMess_et_mess.getText().append(colorText(text));
    }

    private Spanned colorText(String text) {
        return Html.fromHtml(String.format("<font color='#1f6394'> #%1$s# </font>", text));
    }


    @Override
    public void onBmobUploadPostPicSuccess(List<BmobFile> files) {

        List<String> list = new ArrayList<>();

        for (BmobFile file : files) {
            list.add(file.getFileUrl());
        }

        post.setPic(list);
        sendPost();

    }

    @Override
    public void onBmobUploadPostPicFailure(String text) {
        Toast.makeText(WriteMessActivity.this, text, Toast.LENGTH_SHORT).show();
        myDialog.dismiss();
    }

    @Override
    public void onBmobAddPostSuccess() {
        Toast.makeText(WriteMessActivity.this, R.string.send_post_success, Toast.LENGTH_SHORT).show();
        deleteCacheFile();
        myDialog.dismiss();
        WriteMessActivity.this.finish();
    }

    @Override
    public void onBmobAddPostFailure(String text) {
        Toast.makeText(WriteMessActivity.this, R.string.send_post_failure, Toast.LENGTH_SHORT).show();
        myDialog.dismiss();
    }
}
