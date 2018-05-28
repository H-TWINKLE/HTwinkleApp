package com.twinkle.htwinkle.bmob;

import android.content.Context;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.bean.User;

import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public enum Bmob {
    INSTANCE;

    public void BmobCheckUser(String tel) {
        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("username", tel);
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> object, BmobException e) {
                if (e == null) {
                    if (checkUserListener != null) {
                        checkUserListener.CheckUserSuccess(object.size());
                    }
                } else {
                    if (checkUserListener != null) {
                        checkUserListener.CheckUserFailure(e.getLocalizedMessage());
                    }
                }

            }
        });
    }


    public interface CheckUserListener {
        void CheckUserSuccess(int size);

        void CheckUserFailure(String text);
    }

    private CheckUserListener checkUserListener;


    public void setCheckUserListener(CheckUserListener checkUserListener) {
        this.checkUserListener = checkUserListener;
    }

    public void BmobLogin(String tel, String pass) {

        new User.Builder().username(tel).password(pass).build()
                .login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            if (loginListener != null) {
                                loginListener.LoginSuccess(user);
                            }
                        } else {
                            if (loginListener != null) {
                                loginListener.LoginFailure(e.getLocalizedMessage());
                            }
                        }
                    }
                });
    }

    public interface LoginListener {
        void LoginSuccess(User user);

        void LoginFailure(String text);
    }

    private LoginListener loginListener;

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void BmobRegister(String tel, String pass) {
        new User.Builder().username(tel).nickName(String.format(Locale.CHINA, "新用户%s", tel.substring(tel.length() / 2))).password(pass).passWord(pass).mobilePhoneNumber(tel).mobilePhoneNumberVerified(true).build()
                .signUp(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            if (registerListener != null) {
                                registerListener.RegisterSuccess(user);
                            }
                        } else {
                            if (registerListener != null) {
                                registerListener.RegisterFailure(e.getLocalizedMessage());
                            }
                        }
                    }
                });
    }

    public interface RegisterListener {
        void RegisterSuccess(User user);

        void RegisterFailure(String text);
    }

    private RegisterListener registerListener;

    public void setRegisterListener(RegisterListener registerListener) {
        this.registerListener = registerListener;
    }

    public void BmobModifyPass(String pass, Context context) {
        new User.Builder().passWord(pass).password(pass).build().update(BmobUser.getCurrentUser(User.class).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (modifyPassListener != null) {
                        modifyPassListener.ModifyPassSuccess(context.getString(R.string.modify_pass_success));
                    }
                } else {
                    if (modifyPassListener != null) {
                        modifyPassListener.ModifyPassFailure(e.getLocalizedMessage());
                    }
                }
            }
        });

    }

    public interface ModifyPassListener {
        void ModifyPassSuccess(String text);

        void ModifyPassFailure(String text);
    }

    private ModifyPassListener modifyPassListener;

    public void setModifyPassListener(ModifyPassListener modifyPassListener) {
        this.modifyPassListener = modifyPassListener;
    }
}