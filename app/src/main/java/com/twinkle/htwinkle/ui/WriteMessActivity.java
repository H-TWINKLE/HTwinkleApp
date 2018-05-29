package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.twinkle.htwinkle.Adapter.WMessIvAdapter;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Arrays;
import java.util.List;

@ContentView(R.layout.activity_write_mess)
public class WriteMessActivity extends TakePhotoActivity {
    private static final String TAG = "WriteMessActivity";

    @ViewInject(value = R.id.wMess_iv_back)
    private ImageView wMess_iv_back;

    @ViewInject(value = R.id.wMess_iv_header)
    private ImageView wMess_iv_header;

    @ViewInject(value = R.id.wMess_iv_send)
    private ImageView wMess_iv_send;

    @ViewInject(value = R.id.wMess_et_mess)
    private EditText wMess_et_mess;

    @ViewInject(value = R.id.wMess_tv_local)
    private TextView wMess_tv_local;

    @ViewInject(value = R.id.wMess_rv_img)
    private RecyclerView wMess_rv_img;

    @ViewInject(value = R.id.wMess_iv_cPic)
    private ImageView wMess_iv_cPic;

    @ViewInject(value = R.id.wMess_iv_cLocal)
    private ImageView wMess_iv_cLocal;

    @Event(value = R.id.wMess_iv_back)
    private void onBackClick(View view) {
        finish();
    }

    @Event(value = R.id.wMess_iv_cPic)
    private void onCPicClick(View view) {
        getTakePhoto().onPickMultipleWithCrop(9, new CropOptions.Builder().setAspectX(800).setAspectY(800).setWithOwnCrop(true).create());
    }

    @Event(value = R.id.wMess_iv_cLocal)
    private void onCLocalClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        initData();

        initView();
    }


    private void initView() {
        wMess_iv_send.setVisibility(View.INVISIBLE);

        wMess_et_mess.addTextChangedListener(textWatcher);
    }

    private void initData() {

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


}
