package com.twinkle.htwinkle.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.mob.MobSDK;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.net.Bmob;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Locale;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.twinkle.htwinkle.init.Constant.CountDownInterval_;
import static com.twinkle.htwinkle.init.Constant.Default_Int;
import static com.twinkle.htwinkle.init.Constant.Modify_Pass;
import static com.twinkle.htwinkle.init.Constant.Register_;

public class RegOrForActivity extends BaseActivity implements Bmob.CheckUserListener {

    private int code;

    private boolean isSendTel;

    private String tel;

    private User user;

    @ViewInject(value = R.id.rf_tv_tipIsSend)
    private TextView rf_tv_tipIsSend;

    @ViewInject(value = R.id.rf_tv_tip)
    private TextView rf_tv_tip;

    @ViewInject(value = R.id.rf_et_input)
    private EditText rf_et_input;

    @ViewInject(value = R.id.rf_tv_smsTip)
    private TextView rf_tv_smsTip;

    @ViewInject(value = R.id.rf_bt_ok)
    private Button rf_bt_ok;

    @Event(value = R.id.rf_bt_ok)
    private void onBtClick(View view) {
        String text = rf_et_input.getText().toString();

        if (isSendTel) {
            if (checkCode(text)) {
                submitCode(this.tel, text);
            }

        } else {
            if (checkTel(text)) {
                tel = text;
                Bmob.INSTANCE.setCheckUserListener(this);
                Bmob.INSTANCE.BmobCheckUser(text);
            }
        }
    }


    @Event(value = R.id.rf_tv_smsTip)
    private void onSmsTip(View view) {
        sendCode(tel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MobSDK.init(this);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_reg_or_for;
    }

    @Override
    public Activity setActivity() {
        return this;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

        setToolBarFlag(true);

        code = getIntent().getIntExtra("flag", Default_Int);

        switch (code) {
            case Register_:
                setToolBarTitle(R.string.register);
                break;
            case Modify_Pass:
                setToolBarTitle(R.string.forgetPass);
                break;
            default:
                setToolBarTitle(R.string.tests);
                break;
        }
    }

    private boolean checkTel(String tel) {
        if (TextUtils.isEmpty(tel)) {
            rf_et_input.setFocusable(true);
            rf_et_input.setError(getString(R.string.please_input_tel));
            return false;
        }
        if (tel.length() != 11) {
            rf_et_input.setFocusable(true);
            rf_et_input.setError(getString(R.string.tel_form_not_val));
            return false;
        }
        return true;
    }

    private boolean checkCode(String code) {
        if (TextUtils.isEmpty(code)) {
            rf_et_input.setFocusable(true);
            rf_et_input.setError(getString(R.string.please_input_code));
            return false;
        }
        if (code.length() != 6) {
            rf_et_input.setFocusable(true);
            rf_et_input.setError(getString(R.string.code_form_not_val));
            return false;
        }
        return true;
    }

    private void sendCode(String phone) {  // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //  Log.i(TAG, "afterEvent: " + data.toString());  // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    isSendTel = true;

                    runOnUiThread(() -> {
                        rf_et_input.getText().clear();
                        rf_et_input.setHint(R.string.please_input_code);

                        rf_bt_ok.setText(getString(R.string.check));

                        rf_tv_tip.setText(getString(R.string.please_input_code));

                        rf_tv_smsTip.setVisibility(View.VISIBLE);
                        rf_tv_smsTip.setEnabled(false);

                        rf_tv_tipIsSend.setVisibility(View.VISIBLE);
                        rf_tv_tipIsSend.setText(String.format(Locale.CHINA, "输入我们发送到%s的验证码", tel));
                        timer.start();
                    });
                } else {
                    showTip(JSONObject.parseObject(data.toString().
                            replace("java.lang.Throwable: ", "")).get("detail").toString());
                }
            }
        });
        SMSSDK.getVerificationCode("86", phone); // 触发操作
    }

    private CountDownTimer timer = new CountDownTimer(60000, CountDownInterval_) {
        @Override
        public void onTick(long millisUntilFinished) {
            runOnUiThread(() ->
                    rf_tv_smsTip.setText(String.format(Locale.CHINA, "您的短信应该在 %d 秒内到达", millisUntilFinished / CountDownInterval_))
            );
        }

        @Override
        public void onFinish() {
            rf_tv_smsTip.setText(R.string.re_get_code);
            rf_tv_smsTip.setEnabled(true);
        }
    };


    private void submitCode(String phone, String code) {  // 提交验证码，其中的code表示验证码，如“1357”
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {

                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        // showTip("验证成功！");
                        startActivity();

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {       //获取验证码成功
                        showTip("获取验证码成功！");
                    }

                } else {
                    showTip(JSONObject.parseObject(data.toString().
                            replace("java.lang.Throwable: ", "")).get("detail").toString());
                }

            }
        });
        SMSSDK.submitVerificationCode("86", phone, code);     // 触发操作
    }

    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();   //用完回调要注销掉，否则可能会出现内存泄露
    }

    private void showTip(String text) {
        runOnUiThread(() ->
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show());
    }

    private void startActivity() {
        Intent intent = new Intent(RegOrForActivity.this, SetPassActivity.class);
        intent.putExtra("flag", code);
        intent.putExtra("tel", tel);
        intent.putExtra("user", user);
        startActivity(intent);
        this.finish();
    }


    @Override
    public void CheckUserSuccess(List<User> object) {
        switch (code) {
            case Modify_Pass:
                if (object.size() != 0) {
                    sendCode(tel);
                    user = object.get(0);
                } else {
                    Toast.makeText(this, getString(R.string.user_not_reg), Toast.LENGTH_SHORT).show();
                }
                break;
            case Register_:
                if (object.size() != 0) {
                    Toast.makeText(this, getString(R.string.user_reg), Toast.LENGTH_SHORT).show();
                } else {
                    sendCode(tel);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void CheckUserFailure(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
