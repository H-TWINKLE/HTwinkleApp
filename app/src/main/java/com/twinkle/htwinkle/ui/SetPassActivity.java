package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;
import com.twinkle.htwinkle.bean.User;
import com.twinkle.htwinkle.bmob.Bmob;

import static com.twinkle.htwinkle.init.InitString.*;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


public class SetPassActivity extends BaseActivity implements Bmob.RegisterListener, Bmob.ModifyPassListener {

    private int code;

    private String tel;

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
                case Modify_Pass:
                    modifyListener(pass2);
                    break;
                case Register_:
                    registerListener(pass2);
                    break;
                default:
                    break;
            }
        }
    }

    private void registerListener(String pass) {
        Bmob.INSTANCE.setRegisterListener(this);
        Bmob.INSTANCE.BmobRegister(tel, pass);
    }

    private void modifyListener(String pass) {
        Bmob.INSTANCE.setModifyPassListener(this);
        Bmob.INSTANCE.BmobModifyPass(pass, SetPassActivity.this);
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
    }

    @Override
    public void initData() {
        code = getIntent().getIntExtra("flag", Default_Int);
        tel = getIntent().getStringExtra("tel");

        switch (code) {
            case Register_:
                setToolBarTitle(R.string.create_pass);
                break;
            case Modify_Pass:
                setToolBarTitle(R.string.revise_pass);
                break;
            default:
                setToolBarTitle(R.string.tests);
                break;
        }
    }

    @Override
    public void RegisterSuccess(User user) {
        finish();
        startActivity(new Intent(SetPassActivity.this, LoginActivity.class));

    }

    @Override
    public void RegisterFailure(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ModifyPassSuccess(String text) {
        finish();
        startActivity(new Intent(SetPassActivity.this, LoginActivity.class));
    }

    @Override
    public void ModifyPassFailure(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
