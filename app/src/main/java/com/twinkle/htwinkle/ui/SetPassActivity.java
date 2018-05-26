package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

public class SetPassActivity extends BaseActivity {

    private int code;

    @ViewInject(value = R.id.sp_et_pass1)
    private EditText sp_et_pass1;

    @ViewInject(value = R.id.sp_et_pass2)
    private EditText sp_et_pass2;

    @ViewInject(value = R.id.sp_bt_ok)
    private Button sp_bt_ok;


    @Event(value = R.id.sp_bt_ok)
    private void btClick(View view) {

        String pass1 = sp_et_pass1.getText().toString();
        String pass2 = sp_et_pass2.getText().toString();

        if (checkPass(pass1, pass2)) {
            switch (code) {
                case 10001:
                    break;
                case 10002:
                    break;
                default:
                    break;
            }
        }
    }


    private boolean checkPass(String pass1, String pass2) {

        if (TextUtils.isEmpty(pass1)) {
            sp_et_pass1.setFocusable(true);
            sp_et_pass1.setError(getString(R.string.pass1));
            return false;
        } else if (TextUtils.isEmpty(pass2)) {
            sp_et_pass2.setFocusable(true);
            sp_et_pass2.setError(getString(R.string.pass2));
            return false;
        } else if (!pass1.equals(pass2)) {
            sp_et_pass2.setFocusable(true);
            sp_et_pass2.setError(getString(R.string.pass_not_equal));
            return false;
        }
        return true;

    }


    @Override
    public int setLayout() {
        return R.layout.activity_set_pass;
    }

    @Override
    public Activity setActivity() {
        return this;
    }

    @Override
    public void initView() {
        setToolBarFlag(true);
        setToolBarTitle(R.string.BMUpdateTitle);
    }

    @Override
    public void initData() {
        code = getIntent().getIntExtra("flag", 10000);
        switch (code) {
            case 10001:
                setToolBarTitle(R.string.create_pass);
                break;
            case 10002:
                setToolBarTitle(R.string.revise_pass);
                break;
            default:
                setToolBarTitle(R.string.tests);
                break;
        }
    }
}
