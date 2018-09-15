package com.twinkle.htwinkle.bmob;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jph.takephoto.model.TImage;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.bean.Post;
import com.twinkle.htwinkle.bean.User;
import com.twinkle.htwinkle.init.Constant;
import com.twinkle.htwinkle.init.Utils;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

public enum Bmob {

    INSTANCE;

    private static final String TAG = "Bmob";

    //检查手机号是否存在

    public void BmobCheckUser(String tel) {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("username", tel);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (checkUserListener != null) {
                        checkUserListener.CheckUserSuccess(object);
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
        void CheckUserSuccess(List<User> object);

        void CheckUserFailure(String text);
    }

    private CheckUserListener checkUserListener;

    public void setCheckUserListener(CheckUserListener checkUserListener) {
        this.checkUserListener = checkUserListener;
    }

    //登录

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

    //注册

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

    //修改密码没有登录
    public void BmobModifyPassWithoutLogin(User user, String pass) {

        RequestParams r = new RequestParams(Constant.UPDATEUSERPASSINNOTLOGIN + user.getObjectId());

        r.addHeader("Content-Type", "application/json");
        r.addHeader("X-Bmob-REST-API-Key", Constant.RESTKEY);
        r.addHeader("X-Bmob-Master-Key", Constant.MASTERKEY);
        r.addHeader("X-Bmob-Application-Id", Constant.BOMBKEY);

        JSONObject js = new JSONObject();

        js.put("oldPassword", user.getPassWord());
        js.put("newPassword", pass);

        r.setBodyContent(js.toJSONString());

        x.http().request(HttpMethod.PUT, r, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                if (Utils.INSTANCE.verificationBmobMsgIsOk(result)) {
                    BmobModifyPassWithoutLoginByPass2(user, pass);
                } else {
                    if (modifyPassWithoutLoginListener != null) {
                        modifyPassWithoutLoginListener.ModifyPassWithoutLoginFailure("修改失败");
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (modifyPassWithoutLoginListener != null) {
                    modifyPassWithoutLoginListener.ModifyPassWithoutLoginFailure(ex.getLocalizedMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void BmobModifyPassWithoutLoginByPass2(User user, String pass) {

        RequestParams r = new RequestParams(Constant.UPDATEUSERPASSINNOTLOGINBYPASS2 + user.getObjectId());

        r.addHeader("Content-Type", "application/json");
        r.addHeader("X-Bmob-REST-API-Key", Constant.RESTKEY);
        r.addHeader("X-Bmob-Master-Key", Constant.MASTERKEY);
        r.addHeader("X-Bmob-Application-Id", Constant.BOMBKEY);

        User u = new User.Builder().password(pass).passWord(pass).build();

        r.setBodyContent(JSON.toJSONString(u));

        x.http().request(HttpMethod.PUT, r, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                JSONObject js = JSONObject.parseObject(result);

                if (js.get("updatedAt") != null) {
                    if (modifyPassWithoutLoginListener != null) {
                        modifyPassWithoutLoginListener.ModifyPassWithoutLoginSuccess();
                    }
                } else {
                    if (modifyPassWithoutLoginListener != null) {
                        modifyPassWithoutLoginListener.ModifyPassWithoutLoginFailure("修改失败");
                    }
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (modifyPassWithoutLoginListener != null) {
                    modifyPassWithoutLoginListener.ModifyPassWithoutLoginFailure(ex.getLocalizedMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }


    public interface ModifyPassWithoutLoginListener {
        void ModifyPassWithoutLoginSuccess();

        void ModifyPassWithoutLoginFailure(String text);
    }

    private ModifyPassWithoutLoginListener modifyPassWithoutLoginListener;

    public void setModifyPassWithoutLoginListener(ModifyPassWithoutLoginListener modifyPassWithoutLoginListener) {
        this.modifyPassWithoutLoginListener = modifyPassWithoutLoginListener;
    }

    //修改密码登录成功

    public void BmobModifyPass(String pass) {
        new User.Builder().passWord(pass).password(pass).build().update(BmobUser.getCurrentUser(User.class).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (modifyPassListener != null) {
                        modifyPassListener.ModifyPassSuccess();
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
        void ModifyPassSuccess();

        void ModifyPassFailure(String text);
    }

    private ModifyPassListener modifyPassListener;

    public void setModifyPassListener(ModifyPassListener modifyPassListener) {
        this.modifyPassListener = modifyPassListener;
    }


    public void BmobModifyUserInfo(User u) {
        u.update(BmobUser.getCurrentUser(User.class).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (modifyUserInfoListener != null) {
                        modifyUserInfoListener.ModifyUserInfoSuccess();
                    }
                } else {
                    if (modifyUserInfoListener != null) {
                        modifyUserInfoListener.ModifyUserInfoFailure(e.getLocalizedMessage());
                    }
                }
            }
        });
    }

    public interface ModifyUserInfoListener {
        void ModifyUserInfoSuccess();

        void ModifyUserInfoFailure(String text);
    }

    private ModifyUserInfoListener modifyUserInfoListener;

    public void setModifyUserInfoListener(ModifyUserInfoListener modifyUserInfoListener) {
        this.modifyUserInfoListener = modifyUserInfoListener;
    }


    public void BmobFetchUserInfo(Context context) {
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(context, context.getString(R.string.fetchUserInfoSuccess), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getString(R.string.fetchUserInfoSuccess), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void BmobUploadPic(File file) {
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (bmobUploadPicListener != null) {
                        bmobUploadPicListener.onUploadPicSuccess(bmobFile);
                    }
                } else {

                    if (bmobUploadPicListener != null) {
                        bmobUploadPicListener.onUploadPicFailure(e.getMessage());
                    }

                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }


    public interface BmobUploadPicListener {
        void onUploadPicSuccess(BmobFile bmobFile);

        void onUploadPicFailure(String tip);
    }

    private BmobUploadPicListener bmobUploadPicListener;

    public void setBmobUploadPicListener(BmobUploadPicListener bmobUploadPicListener) {
        this.bmobUploadPicListener = bmobUploadPicListener;
    }

    public void BmobUpdatePic(String text) {

        new User.Builder().headerPic(text).build().update(BmobUser.getCurrentUser(User.class).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {

                    if (bmobPicUpdateListener != null) {
                        bmobPicUpdateListener.onPicUpdateSuccess();
                    }
                } else {
                    if (bmobPicUpdateListener != null) {
                        bmobPicUpdateListener.onPicUpdateFailure(e.getMessage());
                    }
                }
            }
        });
    }


    public interface BmobPicUpdateListener {
        void onPicUpdateSuccess();

        void onPicUpdateFailure(String tip);
    }

    private BmobPicUpdateListener bmobPicUpdateListener;

    public void setBmobPicUpdateListener(BmobPicUpdateListener bmobPicUpdateListener) {
        this.bmobPicUpdateListener = bmobPicUpdateListener;
    }


    public void BmobUploadPostPic(List<TImage> list) {

        if (list == null || list.size() == 0) return;

        final String[] filePaths = new String[list.size()];

        for (int x = 0; x < list.size(); x++) {
            filePaths[x] = list.get(x).getOriginalPath();
        }

        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    if(bmobUploadPostPicListener!=null){
                        bmobUploadPostPicListener.onBmobUploadPostPicSuccess(files);
                    }
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                if(bmobUploadPostPicListener!=null){
                    bmobUploadPostPicListener.onBmobUploadPostPicFailure(errormsg);
                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });

    }

    public  interface BmobUploadPostPicListener{
        void onBmobUploadPostPicSuccess(List<BmobFile> files);
        void onBmobUploadPostPicFailure(String text);
    }

    private BmobUploadPostPicListener bmobUploadPostPicListener;

    public void setBmobUploadPostPicListener(BmobUploadPostPicListener bmobUploadPostPicListener) {
        this.bmobUploadPostPicListener = bmobUploadPostPicListener;
    }




    public void BmobAddPost(Post post){
        post.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    if (bmobAddPostListener != null) {
                        bmobAddPostListener.onBmobAddPostSuccess();
                    }
                } else {
                    if (bmobAddPostListener != null) {
                        bmobAddPostListener.onBmobAddPostFailure(e.getLocalizedMessage());
                    }
                }
            }
        });
    }


    public interface BmobAddPostListener{
        void onBmobAddPostSuccess();
        void onBmobAddPostFailure(String text);
    }

    private BmobAddPostListener bmobAddPostListener;

    public void setBmobAddPostListener(BmobAddPostListener bmobAddPostListener) {
        this.bmobAddPostListener = bmobAddPostListener;
    }
}