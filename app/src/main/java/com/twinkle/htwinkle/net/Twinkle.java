package com.twinkle.htwinkle.net;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.htwinkle.entity.Eol;
import com.twinkle.htwinkle.entity.EveryArticle;
import com.twinkle.htwinkle.entity.EveryImg;
import com.twinkle.htwinkle.entity.EveryMusic;
import com.twinkle.htwinkle.entity.Jwgl;
import com.twinkle.htwinkle.entity.JwglInfo;
import com.twinkle.htwinkle.entity.JwglScore;
import com.twinkle.htwinkle.entity.JwglTtb;
import com.twinkle.htwinkle.entity.Robot;
import com.twinkle.htwinkle.entity.Talk;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.init.Constant;
import com.twinkle.htwinkle.init.Utils;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Date;
import java.util.List;


public enum Twinkle {
    INSTANCE;

    private DbManager getDb() {
        return x.getDb(Utils.INSTANCE.daoConfig);
    }

    public void getJwgl(User user, boolean isNew) {

        if (checkUserByJwgl(user)) {

            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure("请填写学号或者密码");
                return;
            }
        }

        RequestParams params = new RequestParams(Constant.JWGLURL);

        params.addParameter("admin", user.getSchoolId());
        params.addParameter("pass", user.getJwgl());

        if (isNew) {
            params.addParameter("news", "yes");
        }

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (jwglListener != null) {

                    Jwgl jwgl = JSONObject.parseObject(result, Jwgl.class);

                    if (jwgl.getCode() == 10000) {

                        DbManager db = getDb();

                        dropTableByJwgl(db);
                        saveDataBase(jwgl, db);
                        saveDataBase(jwgl.getJwglinfo(), db);
                        saveDataBase(jwgl.getJwglscore(), db);
                        saveDataBase(jwgl.getJwglttb(), db);
                        dataBaseClose(db);

                        jwglListener.onJwglListenerSuccess(jwgl);

                    } else {
                        jwglListener.onJwglListenerFailure(jwgl.getTip());
                    }

                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (jwglListener != null) {
                    jwglListener.onJwglListenerFailure(ex.getLocalizedMessage());
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


    public interface JwglListener {
        void onJwglListenerSuccess(Object t);

        void onJwglListenerFailure(String text);
    }

    private JwglListener jwglListener;

    public void setJwglListener(JwglListener jwglListener) {
        this.jwglListener = jwglListener;
    }

    private void dataBaseClose(DbManager dbManager) {
        try {
            dbManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getEol(User user) {

        if (checkUserByEol(user)) {

            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure("请填写学号或者密码");
                return;
            }
        }

        RequestParams params = new RequestParams(Constant.EOLURL);

        params.addParameter("admin", user.getSchoolId());
        params.addParameter("pass", user.getEol());

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (jwglListener != null) {

                    Eol eol = JSONObject.parseObject(result, Eol.class);

                    if (eol.getCode() == 10000) {

                        DbManager db = getDb();

                        dropTableByEol(db);

                        saveDataBase(eol, db);
                        analEolList(eol.getList(), db);

                        dataBaseClose(db);

                        jwglListener.onJwglListenerSuccess(eol);

                    } else {
                        jwglListener.onJwglListenerFailure(eol.getTip());
                    }

                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (jwglListener != null) {
                    jwglListener.onJwglListenerFailure(ex.getLocalizedMessage());
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


    private void analEolList(List<List<com.twinkle.htwinkle.entity.List>> list, DbManager db) {

        if (list == null) return;

        for (List<com.twinkle.htwinkle.entity.List> aList : list) {
            for (com.twinkle.htwinkle.entity.List anAList : aList) {
                saveDataBase(anAList, db);
            }
        }
    }


    private <T> void saveDataBase(T t, DbManager db) {
        if (t == null) return;
        try {
            db.save(t);
        } catch (DbException e) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure(e.getLocalizedMessage());
            }
        }
    }

    private boolean checkUserByJwgl(User user) {
        return TextUtils.isEmpty(user.getJwgl()) || TextUtils.isEmpty(user.getSchoolId());
    }

    private boolean checkUserByEol(User user) {
        return TextUtils.isEmpty(user.getJwgl()) || TextUtils.isEmpty(user.getSchoolId());
    }


    public JwglInfo getJwglInfo(User user) {

        DbManager db = getDb();

        if (checkUserByJwgl(user)) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure("请填写学号或者密码");
                return null;
            }
        }

        try {
            return db.selector(JwglInfo.class).orderBy("dates", true).findFirst();
        } catch (DbException e) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure(e.getLocalizedMessage());
            }
        } finally {
            dataBaseClose(db);
        }

        return null;
    }

    public Eol getEolInfo(User user) {

        DbManager db = getDb();

        if (checkUserByEol(user)) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure("请填写学号或者密码");
                return null;
            }
        }

        try {
            return db.selector(Eol.class).orderBy("dates", true).findFirst();
        } catch (DbException e) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure(e.getLocalizedMessage());
            }
        } finally {
            dataBaseClose(db);
        }

        return null;
    }

    public List<com.twinkle.htwinkle.entity.List> getEolSubject(User user) {

        DbManager db = getDb();

        if (checkUserByEol(user)) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure("请填写学号或者密码");
                return null;
            }
        }

        try {
            return db.selector(com.twinkle.htwinkle.entity.List.class).orderBy("dates", true).findAll();
        } catch (DbException e) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure(e.getLocalizedMessage());
            }
        } finally {
            dataBaseClose(db);
        }

        return null;
    }

    public void sendMessageToRobot(String message) {

        RequestParams params = new RequestParams(Constant.ROBOTURL);

        params.addParameter("info", message);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (jwglListener != null) {

                    Talk talk = new Talk(2, "智能小华", new Date());

                    talk.setRobot(JSONObject.parseObject(result, Robot.class));

                    jwglListener.onJwglListenerSuccess(talk);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (jwglListener != null) {
                    jwglListener.onJwglListenerFailure(ex.getLocalizedMessage());
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


    public List<JwglScore> getJwglScore(User user) {

        DbManager db = getDb();

        if (checkUserByJwgl(user)) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure("请填写学号或者密码");
                return null;
            }
        }

        try {
            return db.selector(JwglScore.class).orderBy("dates", true).findAll();
        } catch (DbException e) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure(e.getLocalizedMessage());
            }
        } finally {
            dataBaseClose(db);
        }

        return null;
    }


    private void dropTableByJwgl(DbManager db) {

        if (db == null) return;

        try {
            db.dropTable(Jwgl.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            db.dropTable(JwglScore.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            db.dropTable(JwglInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            db.dropTable(JwglTtb.class);
        } catch (DbException e) {
            e.printStackTrace();
        }


    }

    private void dropTableByEol(DbManager db) {

        if (db == null) return;

        try {
            db.dropTable(Eol.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            db.dropTable(com.twinkle.htwinkle.entity.List.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    private void dropTableByT(DbManager db, Class c) {

        if (db == null) return;

        try {
            db.dropTable(c);
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            db.dropTable(com.twinkle.htwinkle.entity.List.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }


    public JwglTtb getJwglTtb(User user) {

        DbManager db = getDb();

        if (checkUserByJwgl(user)) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure("请填写学号或者密码");
                return null;
            }
        }

        try {
            return db.selector(JwglTtb.class).orderBy("dates", true).findFirst();
        } catch (DbException e) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure(e.getLocalizedMessage());
            }
        } finally {
            dataBaseClose(db);
        }

        return null;


    }


    public void getEveryOneArticle(int num) {

        RequestParams params = new RequestParams(Constant.EVERYONEART);

        params.addParameter("num", num);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                if (jwglListener != null) {

                    DbManager db = getDb();

                    List<EveryArticle> list = JSONObject.parseArray(result, EveryArticle.class);
                    for (EveryArticle img : list) {
                        img.setId(null);
                        img.setRead(0);
                        saveDataBase(img, db);
                    }

                    if (list.size() > 0) {
                        jwglListener.onJwglListenerSuccess(list.get(0));
                    } else {
                        jwglListener.onJwglListenerFailure("数据错误");
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (jwglListener != null) {
                    jwglListener.onJwglListenerFailure(ex.getLocalizedMessage());
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

    public void getEveryOneMusic(int num) {

        RequestParams params = new RequestParams(Constant.EVERYONEMUSIC);

        params.addParameter("num", num);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                if (jwglListener != null) {

                    DbManager db = getDb();
                    List<EveryMusic> list = JSONObject.parseArray(result, EveryMusic.class);
                    for (EveryMusic img : list) {
                        img.setRead(0);
                        saveDataBase(img, db);
                    }

                    if (list.size() > 0) {
                        jwglListener.onJwglListenerSuccess(list.get(0));
                    } else {
                        jwglListener.onJwglListenerFailure("数据错误");
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (jwglListener != null) {
                    jwglListener.onJwglListenerFailure(ex.getLocalizedMessage());
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

    public void getEveryOneImg(int num) {

        RequestParams params = new RequestParams(Constant.EVERYONEPIC);

        params.addParameter("num", num);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                if (jwglListener != null) {

                    DbManager db = getDb();
                    List<EveryImg> list = JSONObject.parseArray(result, EveryImg.class);
                    for (EveryImg img : list) {
                        saveDataBase(img, db);
                    }

                    if (list.size() > 0) {
                        jwglListener.onJwglListenerSuccess(list.get(0));
                    } else {
                        jwglListener.onJwglListenerFailure("数据错误");
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (jwglListener != null) {
                    jwglListener.onJwglListenerFailure(ex.getLocalizedMessage());
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

    public EveryImg readOneImg() {

        DbManager db = getDb();

        try {
            EveryImg everyImg = db.selector(EveryImg.class).where("read", "!=", 1).findFirst();

            everyImg.setRead(1);

            dataBaeUpdate(everyImg, db);

            return everyImg;

        } catch (Exception e) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure(e.getLocalizedMessage());
            }
        } finally {
            dataBaseClose(db);
        }
        return null;
    }

    public EveryArticle readOneArticle() {

        DbManager db = getDb();

        try {
            EveryArticle everyArticle = db.selector(EveryArticle.class).where("read", "!=", 1).findFirst();

            everyArticle.setRead(1);

            dataBaeUpdate(everyArticle, db);
            return everyArticle;

        } catch (Exception e) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure(e.getLocalizedMessage());
            }
        } finally {
            dataBaseClose(db);
        }
        return null;
    }

    public EveryMusic readOneMusic() {

        DbManager db = getDb();

        try {
            EveryMusic everyMusic = db.selector(EveryMusic.class).where("read", "!=", 1).findFirst();
            everyMusic.setRead(1);

            dataBaeUpdate(everyMusic, db);

            return everyMusic;

        } catch (Exception e) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure(e.getLocalizedMessage());
            }
        } finally {
            dataBaseClose(db);
        }
        return null;
    }


    public <T> void dataBaeUpdate(T t, DbManager db) {

        if (t == null) return;

        try {
            db.update(t, "read");
        } catch (DbException e) {
            if (jwglListener != null) {
                jwglListener.onJwglListenerFailure(e.getLocalizedMessage());
            }
        }

    }


}
