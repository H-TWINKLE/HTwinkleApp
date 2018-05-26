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
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.base.BaseActivity;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Locale;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegOrForActivity extends BaseActivity {

    private int code;

    private boolean isSendTel;

    private String tel;

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
                sendCode(text);
            }
        }
    }

    @Event(value = R.id.rf_tv_smsTip)
    private void onSmsTip(View view) {
        sendCode(tel);
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

        setToolBarFlag(true);

        switch (code) {
            case 10001:
                setToolBarTitle(R.string.register);
                break;
            case 10002:
                setToolBarTitle(R.string.forgetPass);
                break;
            default:
                setToolBarTitle(R.string.tests);
                break;
        }
    }

    @Override
    public void initData() {
        code = getIntent().getIntExtra("flag", 10000);
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
                    tel = phone;

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

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            runOnUiThread(() ->
                    rf_tv_smsTip.setText(String.format(Locale.CHINA, "您的短信应该在 %d 秒内到达", millisUntilFinished / 1000))
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
        startActivity(intent);
        this.finish();
    }


}
