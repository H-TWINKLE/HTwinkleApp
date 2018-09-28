package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;
import com.twinkle.htwinkle.dialog.MyDialog;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.net.Bmob;
import com.twinkle.htwinkle.init.Utils;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static com.twinkle.htwinkle.init.Constant.*;

public class LoginActivity extends BaseActivity implements Bmob.LoginListener {

    private MyDialog myDialog;

    @ViewInject(value = R.id.login_et_tel)
    private EditText login_et_tel;

    @ViewInject(value = R.id.login_et_pass)
    private EditText login_et_pass;


    @Event(value = R.id.login_bt)
    private void btClick(View view) {
        String tel = login_et_tel.getText().toString();
        String pass = login_et_pass.getText().toString();

        if (checkLogin(tel, pass)) {

            myDialog = new MyDialog(this,R.style.AlertDialog);
            myDialog.show();

            Bmob.INSTANCE.setLoginListener(this);
            Bmob.INSTANCE.BmobLogin(tel, pass);
        }
    }


    @Event(value = R.id.login_tv_fPass)
    private void fPassClick(View view) {
        startActivity(Modify_Pass);
    }

    @Event(value = R.id.login_tv_reg)
    private void regClick(View view) {
        startActivity(Register_);
    }


    private boolean checkLogin(String tel, String pass) {

        if (TextUtils.isEmpty(tel)) {
            login_et_tel.setFocusable(true);
            login_et_tel.setError(getString(R.string.inputTel));
            return false;
        } else if (TextUtils.isEmpty(pass)) {
            login_et_pass.setFocusable(true);
            login_et_pass.setError(getString(R.string.inputPass));
            return false;
        }
        return true;
    }


    private void startActivity(int code) {

        Intent intent = new Intent(LoginActivity.this, RegOrForActivity.class);
        intent.putExtra("flag", code);
        startActivity(intent);

    }


    @Override
    public int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    public Activity setActivity() {
        return LoginActivity.this;
    }

    @Override
    public void initView() {
      getUser();
    }

    @Override
    public void initData() {

    }

    private void getUser() {

        User user = Utils.INSTANCE.remUser(this);
        login_et_tel.setText(user.getUsername());
        login_et_pass.setText(user.getPassWord());

    }

    @Override
    public void LoginSuccess(User user) {
        myDialog.dismiss();
        Utils.INSTANCE.saveUser(this, user.getUsername(), user.getPassWord());
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    @Override
    public void LoginFailure(String text) {
        myDialog.dismiss();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
