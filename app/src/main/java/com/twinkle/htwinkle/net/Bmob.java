package com.twinkle.htwinkle.net;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jph.takephoto.model.TImage;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.entity.Collection;
import com.twinkle.htwinkle.entity.Comment;
import com.twinkle.htwinkle.entity.Focus;
import com.twinkle.htwinkle.entity.Post;
import com.twinkle.htwinkle.entity.Praise;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.init.Constant;
import com.twinkle.htwinkle.init.Utils;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

public enum Bmob {

    INSTANCE;

    //检查手机号是否存在

    public void BmobCheckUser(String tel) {
        BmobQuery<User> query = new BmobQuery<>();
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

    //修改密码没有登录，旧密码新密码
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


    //修改个人信息通过masterkey
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

    //修改用户个人信息 通过登录
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

    //同步个人信息
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

    //上传单张图片
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

    //上传用户头像
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


    //上传图片集合
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
                    if (bmobUploadPostPicListener != null) {
                        bmobUploadPostPicListener.onBmobUploadPostPicSuccess(files);
                    }
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                if (bmobUploadPostPicListener != null) {
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

    public interface BmobUploadPostPicListener {
        void onBmobUploadPostPicSuccess(List<BmobFile> files);

        void onBmobUploadPostPicFailure(String text);
    }

    private BmobUploadPostPicListener bmobUploadPostPicListener;

    public void setBmobUploadPostPicListener(BmobUploadPostPicListener bmobUploadPostPicListener) {
        this.bmobUploadPostPicListener = bmobUploadPostPicListener;
    }

    //添加帖子
    public void BmobAddPost(Post post) {
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


    public interface BmobAddPostListener {
        void onBmobAddPostSuccess();

        void onBmobAddPostFailure(String text);
    }

    private BmobAddPostListener bmobAddPostListener;

    public void setBmobAddPostListener(BmobAddPostListener bmobAddPostListener) {
        this.bmobAddPostListener = bmobAddPostListener;
    }

    //获取帖子，单位10
    public void BmobGetPost(int currentPages) {
        BmobQuery<Post> query = new BmobQuery<>();
        query.include("author");
        query.order("-createdAt");
        query.setLimit(10);
        query.setSkip(currentPages * 10);
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    if (bmobGetPostListener != null) {
                        bmobGetPostListener.onGetPostSuccess(object);
                    }

                } else {
                    if (bmobGetPostListener != null) {
                        bmobGetPostListener.onGetPostFailure(e.getMessage());
                    }
                }
            }
        });
    }


    public interface BmobGetPostListener {
        void onGetPostSuccess(List<Post> list);

        void onGetPostFailure(String text);
    }

    private BmobGetPostListener bmobGetPostListener;


    public void setBmobGetPostListener(BmobGetPostListener bmobGetPostListener) {
        this.bmobGetPostListener = bmobGetPostListener;
    }

    //添加帖子评论
    public void BmobAddPostComment(Comment comment) {

        comment.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    if (bmobAddPostCommentListener != null) {
                        bmobAddPostCommentListener.onAddPostCommentListenerSuccess();
                    }
                } else {
                    if (bmobAddPostCommentListener != null) {
                        bmobAddPostCommentListener.onAddPostCommentListenerFailure(e.getMessage());
                    }
                }
            }

        });
    }

    public interface BmobAddPostCommentListener {
        void onAddPostCommentListenerSuccess();

        void onAddPostCommentListenerFailure(String text);
    }

    private BmobAddPostCommentListener bmobAddPostCommentListener;

    public void setBmobAddPostCommentListener(BmobAddPostCommentListener bmobAddPostCommentListener) {
        this.bmobAddPostCommentListener = bmobAddPostCommentListener;
    }

    //帖子评论数增加
    public void BmobPostCommentIncrement(Post post) {
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (bmobPostCommentIncrementListener != null) {
                        bmobPostCommentIncrementListener.onBmobPostCommentIncrementSuccess();
                    }
                } else {
                    if (bmobPostCommentIncrementListener != null) {
                        bmobPostCommentIncrementListener.onBmobPostCommentIncrementFailure(e.getLocalizedMessage());
                    }
                }
            }
        });
    }


    public interface BmobPostCommentIncrementListener {
        void onBmobPostCommentIncrementSuccess();

        void onBmobPostCommentIncrementFailure(String text);
    }

    private BmobPostCommentIncrementListener bmobPostCommentIncrementListener;

    public void setBmobPostCommentIncrementListener(BmobPostCommentIncrementListener bmobPostCommentIncrementListener) {
        this.bmobPostCommentIncrementListener = bmobPostCommentIncrementListener;
    }

    //获取单一帖子
    public void BmobGetOnePost(Post post) {
        BmobQuery<Post> query = new BmobQuery<>();
        query.include("author");
        query.getObject(post.getObjectId(), new QueryListener<Post>() {

            @Override
            public void done(Post object, BmobException e) {
                if (e == null) {
                    if (bmobGetOnePostListener != null) {
                        bmobGetOnePostListener.onBmobGetOnePostSuccess(object);
                    }
                } else {
                    if (bmobGetOnePostListener != null) {
                        bmobGetOnePostListener.onBmobGetOnePostFailure(e.getLocalizedMessage());
                    }
                }
            }

        });

    }


    public interface BmobGetOnePostListener {
        void onBmobGetOnePostSuccess(Post post);

        void onBmobGetOnePostFailure(String text);
    }

    private BmobGetOnePostListener bmobGetOnePostListener;

    public void setBmobGetOnePostListener(BmobGetOnePostListener bmobGetOnePostListener) {
        this.bmobGetOnePostListener = bmobGetOnePostListener;
    }

    //添加点赞 点赞数加一
    public void BmobAddPraise(Praise praise) {

        BmobRelation relation = new BmobRelation();//将当前用户添加到Post表中的likes字段值中，表明当前用户喜欢该帖子
        relation.add(praise.getUser());//将当前用户添加到多对多关联中
        praise.getPost().setUserlike(relation);//多对多关联指向`post`的`likes`字段
        praise.getPost().increment("likenum");
        praise.getPost().update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (bmobAddPraiseListener != null) {
                        //bmobAddPraiseListener.onBmobAddPraiseSuccess();

                        /*Post post2 = new Post(praise.getPost().getObjectId());
                        post2.increment("likenum");
                        BmobPostPraiseIncrement(post2);*/
                        BmobAddPraiseToPraiseTable(praise);

                    }
                } else {
                    if (bmobAddPraiseListener != null) {
                        bmobAddPraiseListener.onBmobAddPraiseFailure(e.getLocalizedMessage());
                    }
                }
            }
        });
    }

    //添加点赞到表

    public void BmobAddPraiseToPraiseTable(Praise praise) {
        praise.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    if (bmobAddPraiseListener != null) {
                        bmobAddPraiseListener.onBmobAddPraiseSuccess();
                    }
                } else {
                    if (bmobAddPraiseListener != null) {
                        bmobAddPraiseListener.onBmobAddPraiseFailure(e.getLocalizedMessage());
                    }
                }
            }
        });
    }

    public interface BmobAddPraiseListener {
        void onBmobAddPraiseSuccess();

        void onBmobAddPraiseFailure(String text);
    }

    private BmobAddPraiseListener bmobAddPraiseListener;

    public void setBmobAddPraiseListener(BmobAddPraiseListener bmobAddPraiseListener) {
        this.bmobAddPraiseListener = bmobAddPraiseListener;
    }

    /*//添加点赞数加一
    public void BmobPostPraiseIncrement(Post post) {
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (bmobAddPraiseListener != null) {
                        bmobAddPraiseListener.onBmobAddPraiseSuccess();
                    }
                } else {
                    if (bmobAddPraiseListener != null) {
                        bmobAddPraiseListener.onBmobAddPraiseFailure(e.getLocalizedMessage());
                    }
                }
            }
        });
    }*/


    //取消点赞 点赞数减一

    public void BmobDisablePraise(Praise praise) {

        BmobRelation relation = new BmobRelation();
        relation.remove(praise.getUser());
        praise.getPost().setUserlike(relation);
        praise.getPost().increment("likenum", -1);
        praise.getPost().update(new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    BmobSearchUserCollect(praise);
                } else {
                    if (bmobDisablePraiseListener != null) {
                        bmobDisablePraiseListener.onBmobDisablePraiseFailure(e.getLocalizedMessage());
                    }
                }
            }

        });
    }

    public void BmobSearchUserCollect(Praise praise) {

        BmobQuery<Praise> query = new BmobQuery<>();
        query.addWhereEqualTo("post", praise.getPost());
        query.addWhereEqualTo("user", praise.getUser());

        query.findObjects(new FindListener<Praise>() {
            @Override
            public void done(List<Praise> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        BmobDeletePraise(list.get(0));
                    } else {
                        if (bmobDisablePraiseListener != null) {
                            bmobDisablePraiseListener.onBmobDisablePraiseFailure("您已经取消点赞了");
                        }
                    }

                } else {
                    if (bmobDisablePraiseListener != null) {
                        bmobDisablePraiseListener.onBmobDisablePraiseFailure(e.getLocalizedMessage());
                    }
                }
            }
        });

    }


    private void BmobDeletePraise(Praise praise) {
        praise.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (bmobDisablePraiseListener != null) {
                        bmobDisablePraiseListener.onBmobDisablePraiseSuccess();
                    }
                } else {
                    if (bmobDisablePraiseListener != null) {
                        bmobDisablePraiseListener.onBmobDisablePraiseFailure(e.getLocalizedMessage());
                    }
                }
            }
        });
    }


    public interface BmobDisablePraiseListener {
        void onBmobDisablePraiseSuccess();

        void onBmobDisablePraiseFailure(String text);
    }

    private BmobDisablePraiseListener bmobDisablePraiseListener;

    public void setBmobDisablePraiseListener(BmobDisablePraiseListener bmobDisablePraiseListener) {
        this.bmobDisablePraiseListener = bmobDisablePraiseListener;
    }


    //增加收藏

    public void BmobUserCollection(Collection collection) {

        collection.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    if (bmobUserCollectionListener != null) {
                        bmobUserCollectionListener.onBmobUserCollectionSuccess(collection);
                    }
                } else {
                    if (bmobUserCollectionListener != null) {
                        bmobUserCollectionListener.onBmobUserCollectionFailure(e.getLocalizedMessage());
                    }
                }
            }
        });

    }

    public interface BmobUserCollectionListener {
        void onBmobUserCollectionSuccess(Collection collection);

        void onBmobUserCollectionFailure(String text);
    }

    private BmobUserCollectionListener bmobUserCollectionListener;

    public void setBmobUserCollectionListener(BmobUserCollectionListener bmobUserCollectionListener) {
        this.bmobUserCollectionListener = bmobUserCollectionListener;
    }

    //取消收藏

    public void BmobUserDisableCollection(Collection collection) {
        collection.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (bmobUserDisableCollectionListener != null) {
                        bmobUserDisableCollectionListener.onBmobUserDisableCollectionSuccess();
                    }
                } else {
                    if (bmobUserDisableCollectionListener != null) {
                        bmobUserDisableCollectionListener.onBmobUserDisableCollectionFailure(e.getLocalizedMessage());
                    }
                }
            }
        });
    }

    public interface BmobUserDisableCollectionListener {
        void onBmobUserDisableCollectionSuccess();

        void onBmobUserDisableCollectionFailure(String text);
    }

    private BmobUserDisableCollectionListener bmobUserDisableCollectionListener;

    public void setBmobUserDisableCollectionListener(BmobUserDisableCollectionListener bmobUserDisableCollectionListener) {
        this.bmobUserDisableCollectionListener = bmobUserDisableCollectionListener;
    }

    //增加关注

    public void BmobUserFocus(Focus focus) {

        if (focus.getFocusUser().getObjectId().equals(focus.getOnFocusUser().getObjectId())) {
            if (bmobUserFocusListener != null) {
                bmobUserFocusListener.onBmobUserFocusFaiure("您不能够关注自己！");
                return;
            }
        }

        focus.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    if (bmobUserFocusListener != null) {
                        bmobUserFocusListener.onBmobUserFocusSuccess(focus);
                    }
                } else {
                    if (bmobUserFocusListener != null) {
                        bmobUserFocusListener.onBmobUserFocusFaiure(e.getLocalizedMessage());
                    }
                }
            }
        });
    }

    public interface BmobUserFocusListener {
        void onBmobUserFocusSuccess(Focus focus);

        void onBmobUserFocusFaiure(String text);
    }

    private BmobUserFocusListener bmobUserFocusListener;

    public void setBmobUserFocusListener(BmobUserFocusListener bmobUserFocusListener) {
        this.bmobUserFocusListener = bmobUserFocusListener;
    }

    //取消关注

    public void BmobDisableUserFocus(Focus focus) {

        focus.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (bmobDisableUserFocusListener != null) {
                        bmobDisableUserFocusListener.onBmobDisableUserFocusSuccess();
                    }
                } else {
                    if (bmobDisableUserFocusListener != null) {
                        bmobDisableUserFocusListener.onBmobDisableUserFocusFailure(e.getLocalizedMessage());
                    }
                }
            }
        });
    }

    public interface BmobDisableUserFocusListener {
        void onBmobDisableUserFocusSuccess();

        void onBmobDisableUserFocusFailure(String text);
    }

    private BmobDisableUserFocusListener bmobDisableUserFocusListener;

    public void setBmobDisableUserFocusListener(BmobDisableUserFocusListener bmobDisableUserFocusListener) {
        this.bmobDisableUserFocusListener = bmobDisableUserFocusListener;
    }

    //搜索指定用户是否点赞
    public void BmobSearchUserPraise(Praise praise) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereRelatedTo("userlike", new BmobPointer(praise.getPost()));
        query.addWhereEqualTo("username", praise.getUser().getUsername());
        query.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (bmobSearchUserPraiseListener != null) {
                        bmobSearchUserPraiseListener.onBmobSearchUserPraiseSuccess(object.size());
                    }
                } else {
                    if (bmobSearchUserPraiseListener != null) {
                        bmobSearchUserPraiseListener.onBmobSearchUserPraiseFailure(e.getLocalizedMessage());
                    }
                }
            }

        });

    }

    public interface BmobSearchUserPraiseListener {
        void onBmobSearchUserPraiseSuccess(int size);

        void onBmobSearchUserPraiseFailure(String text);
    }

    private BmobSearchUserPraiseListener bmobSearchUserPraiseListener;

    public void setBmobSearchUserPraiseListener(BmobSearchUserPraiseListener bmobSearchUserPraiseListener) {
        this.bmobSearchUserPraiseListener = bmobSearchUserPraiseListener;
    }

    //搜索用户是否收藏

    public void BmobSearchUserCollect(Collection collection) {

        BmobQuery<Collection> query = new BmobQuery<>();
        query.addWhereEqualTo("post", collection.getPost());
        query.addWhereEqualTo("user", collection.getUser());

        query.findObjects(new FindListener<Collection>() {
            @Override
            public void done(List<Collection> list, BmobException e) {
                if (e == null) {
                    if (bmobSearchUserAboutTListener != null) {
                        bmobSearchUserAboutTListener.onBmobSearchUserAboutTSuccess(collection, list);
                    }
                } else {
                    if (bmobSearchUserAboutTListener != null) {
                        bmobSearchUserAboutTListener.onBmobSearchUserAboutTFailure(collection, e.getLocalizedMessage());
                    }
                }
            }
        });

    }

    //搜索用户是否关注

    public void BmobSearchUserFocus(Focus focus) {

        BmobQuery<Focus> query = new BmobQuery<>();
        query.addWhereEqualTo("focusUser", focus.getFocusUser());
        query.addWhereEqualTo("onFocusUser", focus.getOnFocusUser());

        query.findObjects(new FindListener<Focus>() {
            @Override
            public void done(List<Focus> list, BmobException e) {
                if (e == null) {
                    if (bmobSearchUserAboutTListener != null) {
                        bmobSearchUserAboutTListener.onBmobSearchUserAboutTSuccess(focus, list);
                    }
                } else {
                    if (bmobSearchUserAboutTListener != null) {
                        bmobSearchUserAboutTListener.onBmobSearchUserAboutTFailure(focus, e.getLocalizedMessage());
                    }
                }
            }
        });

    }

    public interface BmobSearchUserAboutTListener {
        <T extends BmobObject> void onBmobSearchUserAboutTSuccess(T t, List<T> list);

        <T extends BmobObject> void onBmobSearchUserAboutTFailure(T t, String text);
    }

    private BmobSearchUserAboutTListener bmobSearchUserAboutTListener;

    public void setBmobSearchUserAboutTListener(BmobSearchUserAboutTListener bmobSearchUserAboutTListener) {
        this.bmobSearchUserAboutTListener = bmobSearchUserAboutTListener;
    }

    //搜索用户是否关注


    //获取当前用户收到的所有评论
    public void BmobGetListPostCommentReceiveByUser(int current) {
        BmobQuery<Comment> query = new BmobQuery<>();
        BmobQuery<Post> innerQuery = new BmobQuery<>();
        innerQuery.addWhereEqualTo("author", BmobUser.getCurrentUser(User.class));
        query.addWhereMatchesQuery("post", "Post", innerQuery);
        query.include("author,post");
        query.order("-createdAt");
        query.setLimit(10);
        query.setSkip(current * 10);
        query.findObjects(new FindListener<Comment>() {

            @Override
            public void done(List<Comment> objects, BmobException e) {
                if (e == null) {
                    if (bmobGetListPostCommentReceiveByUserListener != null) {
                        bmobGetListPostCommentReceiveByUserListener.onBmobGetListPostCommentReceiveByUserSuccess(objects);
                    }
                } else {
                    if (bmobGetListPostCommentReceiveByUserListener != null) {
                        bmobGetListPostCommentReceiveByUserListener.onBmobGetListPostCommentReceiveByUserFailure(e.getLocalizedMessage());
                    }
                }
            }
        });
    }


    public interface BmobGetListPostCommentReceiveByUserListener {
        void onBmobGetListPostCommentReceiveByUserSuccess(List<Comment> object);

        void onBmobGetListPostCommentReceiveByUserFailure(String text);
    }


    private BmobGetListPostCommentReceiveByUserListener bmobGetListPostCommentReceiveByUserListener;

    public void setBmobGetListPostCommentReceiveByUserListener(BmobGetListPostCommentReceiveByUserListener bmobGetListPostCommentReceiveByUserListener) {
        this.bmobGetListPostCommentReceiveByUserListener = bmobGetListPostCommentReceiveByUserListener;
    }


    //获取到我当前用户发出的所有评论

    public void BmobGetListCommentSendByUser(int current) {
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("author", new BmobPointer(BmobUser.getCurrentUser(User.class)));
        query.include("author,post.author");
        query.setLimit(10);
        query.order("-createdAt");
        query.setSkip(current * 10);
        query.findObjects(new FindListener<Comment>() {

            @Override
            public void done(List<Comment> objects, BmobException e) {
                if (e == null) {
                    if (bmobGetListCommentSendByUserListener != null) {
                        bmobGetListCommentSendByUserListener.onBmobGetListCommentSendByUserSuccess(objects);
                    }
                } else {
                    if (bmobGetListCommentSendByUserListener != null) {
                        bmobGetListCommentSendByUserListener.onBmobGetListCommentSendByUserFailure(e.getMessage());
                    }
                }
            }
        });

    }

    public interface BmobGetListCommentSendByUserListener {
        void onBmobGetListCommentSendByUserSuccess(List<Comment> object);

        void onBmobGetListCommentSendByUserFailure(String text);
    }

    private BmobGetListCommentSendByUserListener bmobGetListCommentSendByUserListener;


    public void setBmobGetListCommentSendByUserListener(BmobGetListCommentSendByUserListener bmobGetListCommentSendByUserListener) {
        this.bmobGetListCommentSendByUserListener = bmobGetListCommentSendByUserListener;
    }


    //获取当前用户收到的所有赞
    public void BmobGetListPraiseReceiveByUser(int current) {

        BmobQuery<Praise> query = new BmobQuery<>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(User.class));
        query.include("post,user");
        query.order("-createdAt");
        query.setLimit(10);
        query.setSkip(current * 10);
        query.findObjects(new FindListener<Praise>() {

            @Override
            public void done(List<Praise> objects, BmobException e) {
                if (e == null) {
                    if (bmobGetListPraiseReceiveByUserListener != null) {
                        bmobGetListPraiseReceiveByUserListener.onBmobGetListPraiseReceiveByUserSuccess(objects);
                    }
                } else {
                    if (bmobGetListPraiseReceiveByUserListener != null) {
                        bmobGetListPraiseReceiveByUserListener.onBmobGetListPraiseReceiveByUserFailure(e.getLocalizedMessage());
                    }
                }
            }
        });
    }

    public interface BmobGetListPraiseReceiveByUserListener {
        void onBmobGetListPraiseReceiveByUserSuccess(List<Praise> objects);

        void onBmobGetListPraiseReceiveByUserFailure(String text);
    }

    private BmobGetListPraiseReceiveByUserListener bmobGetListPraiseReceiveByUserListener;

    public void setBmobGetListPraiseReceiveByUserListener(BmobGetListPraiseReceiveByUserListener bmobGetListPraiseReceiveByUserListener) {
        this.bmobGetListPraiseReceiveByUserListener = bmobGetListPraiseReceiveByUserListener;
    }

    //获取当前用户发出的所有赞
    public void BmobGetListPraiseSendByUser(int current) {
        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereEqualTo("userlike", new BmobPointer(BmobUser.getCurrentUser(User.class)));
        query.include("author");
        query.setLimit(10);
        query.order("-createdAt");
        query.setSkip(current * 10);
        query.findObjects(new FindListener<Post>() {

            @Override
            public void done(List<Post> objects, BmobException e) {
                if (e == null) {
                    if (bmobGetListPraiseSendByUserListener != null) {
                        bmobGetListPraiseSendByUserListener.onBmobGetListPraiseSendByUserSuccess(objects);
                    }
                } else {
                    if (bmobGetListPraiseSendByUserListener != null) {
                        bmobGetListPraiseSendByUserListener.onBmobGetListPraiseSendByUserFailure(e.getMessage());
                    }
                }
            }
        });

    }

    public interface BmobGetListPraiseSendByUserListener {
        void onBmobGetListPraiseSendByUserSuccess(List<Post> posts);

        void onBmobGetListPraiseSendByUserFailure(String text);
    }

    private BmobGetListPraiseSendByUserListener bmobGetListPraiseSendByUserListener;

    public void setBmobGetListPraiseSendByUserListener(BmobGetListPraiseSendByUserListener bmobGetListPraiseSendByUserListener) {
        this.bmobGetListPraiseSendByUserListener = bmobGetListPraiseSendByUserListener;
    }


    public void BmobGetCommListByPost(Post post, int current) {

        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("post", new BmobPointer(post));
        query.include("author,post.author");
        query.setLimit(10);
        query.setSkip(10 * current);
        query.order("-createdAt");
        query.findObjects(new FindListener<Comment>() {

            @Override
            public void done(List<Comment> objects, BmobException e) {
                if (e == null) {
                    if (bmobGetCommListByPostListener != null) {
                        bmobGetCommListByPostListener.onBmobGetCommListByPostSuccess(objects);
                    }
                } else {
                    if (bmobGetCommListByPostListener != null) {
                        bmobGetCommListByPostListener.onBmobGetCommListByPostFailure(e.getLocalizedMessage());
                    }
                }
            }
        });


    }

    public interface BmobGetCommListByPostListener {
        void onBmobGetCommListByPostSuccess(List<Comment> list);

        void onBmobGetCommListByPostFailure(String text);
    }

    private BmobGetCommListByPostListener bmobGetCommListByPostListener;

    public void setBmobGetCommListByPostListener(BmobGetCommListByPostListener bmobGetCommListByPostListener) {
        this.bmobGetCommListByPostListener = bmobGetCommListByPostListener;
    }

    //通用Save
    public <T extends BmobObject> void BmobCommSave(T t) {

        t.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    if (bmobCommListener != null) {
                        bmobCommListener.onBmobCommSuccess(1, t);
                    }
                } else {
                    if (bmobCommListener != null) {
                        bmobCommListener.onBmobCommFailure(1, t, e.getLocalizedMessage());
                    }
                }
            }
        });

    }


    //通用Update

    public <T extends BmobObject> void BmobCommUpdate(T t) {
        t.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (bmobCommListener != null) {
                        bmobCommListener.onBmobCommSuccess(2, t);
                    }
                } else {
                    if (bmobCommListener != null) {
                        bmobCommListener.onBmobCommFailure(2, t, e.getLocalizedMessage());
                    }
                }
            }
        });
    }


    //通用删除

    public <T extends BmobObject> void BmobCommDelete(T t) {
        t.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (bmobCommListener != null) {
                        bmobCommListener.onBmobCommSuccess(3, t);
                    }
                } else {
                    if (bmobCommListener != null) {
                        bmobCommListener.onBmobCommFailure(3, t, e.getLocalizedMessage());
                    }
                }
            }
        });
    }

    // 1为save 2为update 3为delete

    public interface BmobCommListener {
        void onBmobCommSuccess(int flag, Object o);

        void onBmobCommFailure(int flag, Object o, String text);
    }

    private BmobCommListener bmobCommListener;

    public void setBmobCommListener(BmobCommListener bmobCommListener) {
        this.bmobCommListener = bmobCommListener;
    }


    //查询用户关注
    public void BmobGetListUserFocus(int current, String equal) {

        BmobQuery<Focus> focusBmobQuery = new BmobQuery<>();
        focusBmobQuery.addWhereEqualTo(equal, BmobUser.getCurrentUser(User.class));
        focusBmobQuery.setLimit(10);
        focusBmobQuery.setSkip(10 * current);
        focusBmobQuery.include("focusUser,onFocusUser");
        focusBmobQuery.order("-createdAt");
        focusBmobQuery.findObjects(new FindListener<Focus>() {
            @Override
            public void done(List<Focus> list, BmobException e) {
                if (e == null) {
                    if (bmobGetListUserFocusListener != null) {
                        bmobGetListUserFocusListener.onBmobGetListUserFocusSuccess(list);
                    }
                } else {
                    if (bmobGetListUserFocusListener != null) {
                        bmobGetListUserFocusListener.onBmobGetListUserFocusFailure(e.getLocalizedMessage());
                    }
                }
            }
        });


    }


    public interface BmobGetListUserFocusListener {
        void onBmobGetListUserFocusSuccess(List<Focus> list);

        void onBmobGetListUserFocusFailure(String text);
    }

    private BmobGetListUserFocusListener bmobGetListUserFocusListener;

    public void setBmobGetListUserFocusListener(BmobGetListUserFocusListener bmobGetListUserFocusListener) {
        this.bmobGetListUserFocusListener = bmobGetListUserFocusListener;
    }


    public void BmobGetListUserCollect(int current) {

        BmobQuery<Collection> query = new BmobQuery<>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(User.class));
        query.setLimit(10);
        query.setSkip(10 * current);
        query.include("post,user,post.author");
        query.order("-createdAt");
        query.findObjects(new FindListener<Collection>() {
            @Override
            public void done(List<Collection> list, BmobException e) {
                if (e == null) {
                    if (bmobGetListUserCollectListener != null) {
                        bmobGetListUserCollectListener.onBmobGetListUserCollectSuccess(list);
                    }
                } else {
                    if (bmobGetListUserCollectListener != null) {
                        bmobGetListUserCollectListener.onBmobGetListUserCollectFailure(e.getLocalizedMessage());
                    }
                }
            }
        });
    }


    public interface BmobGetListUserCollectListener {

        void onBmobGetListUserCollectSuccess(List<Collection> list);

        void onBmobGetListUserCollectFailure(String text);
    }


    public BmobGetListUserCollectListener bmobGetListUserCollectListener;

    public void setBmobGetListUserCollectListener(BmobGetListUserCollectListener bmobGetListUserCollectListener) {
        this.bmobGetListUserCollectListener = bmobGetListUserCollectListener;
    }


    //查询用户的所有帖子

    public void BmobFindAllListPostByUserId(BmobUser user, int current) {

        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereEqualTo("author", user);  // 查询当前用户的所有帖子
        query.setLimit(10);
        query.setSkip(10 * current);
        query.order("-updatedAt");
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(new FindListener<Post>() {

            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    if (bmobFindAllListPostByUserIdListener != null) {
                        bmobFindAllListPostByUserIdListener.onBmobFindAllListPostByUserIdListenerSuccess(object);
                    }
                } else {
                    if (bmobFindAllListPostByUserIdListener != null) {
                        bmobFindAllListPostByUserIdListener.onBBmobFindAllListPostByUserIdListenerFailure(e.getLocalizedMessage());
                    }
                }
            }

        });


    }

    public interface BmobFindAllListPostByUserIdListener {
        void onBmobFindAllListPostByUserIdListenerSuccess(List<Post> list);

        void onBBmobFindAllListPostByUserIdListenerFailure(String text);

    }

    private BmobFindAllListPostByUserIdListener bmobFindAllListPostByUserIdListener;

    public void setBmobFindAllListPostByUserIdListener(BmobFindAllListPostByUserIdListener bmobFindAllListPostByUserIdListener) {
        this.bmobFindAllListPostByUserIdListener = bmobFindAllListPostByUserIdListener;
    }

    //查询指定类型的帖子

    public void BmobFindPostByTopic(String[] topic, int current) {

        BmobQuery<Post> query = new BmobQuery<>();
        query.setLimit(10);
        query.setSkip(10 * current);
        query.include("author");
        query.order("-updatedAt");
        query.addWhereContainsAll("topic", Arrays.asList(topic));
        query.findObjects(new FindListener<Post>() {

            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    if (bmobFindPostByTopicListener != null) {
                        bmobFindPostByTopicListener.onBmobFindPostByTopicListenerSuccess(object);
                    }
                } else {
                    if (bmobFindPostByTopicListener != null) {
                        bmobFindPostByTopicListener.onBmobFindPostByTopicListenerFailure(e.getLocalizedMessage());
                    }
                }
            }

        });
    }


    public interface BmobFindPostByTopicListener {
        void onBmobFindPostByTopicListenerSuccess(List<Post> list);

        void onBmobFindPostByTopicListenerFailure(String text);
    }


    private BmobFindPostByTopicListener bmobFindPostByTopicListener;

    public void setBmobFindPostByTopicListener(BmobFindPostByTopicListener bmobFindPostByTopicListener) {
        this.bmobFindPostByTopicListener = bmobFindPostByTopicListener;
    }

    public void updateUserLv(Integer lv) {

        User user = BmobUser.getCurrentUser(User.class);

        Integer oldLv = (user.getLv() == null) ? 0 : Integer.parseInt(user.getLv());

        new User.Builder().lv(String.valueOf(oldLv + lv)).build().update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });

        onlyFetchUserJsonInfo();

    }

    public void onlyFetchUserJsonInfo() {
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
    }

    //查询所有关注 不分页
    public void BmobGetListUserFocus(int num, FindListener<Post> findListener) {

        BmobQuery<Focus> focusBmobQuery = new BmobQuery<>();
        focusBmobQuery.addWhereEqualTo("onFocusUser", BmobUser.getCurrentUser(User.class));
        focusBmobQuery.setLimit(1000);
        focusBmobQuery.order("-createdAt");
        focusBmobQuery.findObjects(new FindListener<Focus>() {
            @Override
            public void done(List<Focus> list, BmobException e) {

                List<String> list1 = new ArrayList<>();

                for (Focus focus : list) {
                    list1.add(focus.getFocusUser().getObjectId());
                }

                if (e == null) {
                    getUserMoment(num, list1, findListener);
                }
            }
        });


    }

    private void getUserMoment(int current, List<String> friendIds, FindListener<Post> findListener) {

        BmobQuery<User> innerQuery = new BmobQuery<User>();
        innerQuery.addWhereContainedIn("objectId", friendIds);

        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereMatchesQuery("author", "_User", innerQuery);
        query.setLimit(10);
        query.setSkip(10 * current);
        query.order("-updatedAt");
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(findListener);


    }


}