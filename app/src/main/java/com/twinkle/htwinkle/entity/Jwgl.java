package com.twinkle.htwinkle.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;


@Table(name = "Jwgl")
public class Jwgl {

    @Column(name = "admin")
    private String admin;

    @Column(name = "code")
    private int code;

    @Column(name = "cookies")
    private String cookies;

    @Column(name = "dates")
    private long dates;

    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;


    private JwglInfo jwglinfo;


    private JwglTtb jwglttb;

    @Column(name = "name")
    private String name;

    @Column(name = "pass")
    private String pass;

    @Column(name = "tip")
    private String tip;


    private List<JwglScore> jwglscore;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public long getDates() {
        return dates;
    }

    public void setDates(long dates) {
        this.dates = dates;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JwglInfo getJwglinfo() {
        return jwglinfo;
    }

    public void setJwglinfo(JwglInfo jwglinfo) {
        this.jwglinfo = jwglinfo;
    }

    public JwglTtb getJwglttb() {
        return jwglttb;
    }

    public void setJwglttb(JwglTtb jwglttb) {
        this.jwglttb = jwglttb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public List<JwglScore> getJwglscore() {
        return jwglscore;
    }

    public void setJwglscore(List<JwglScore> jwglscore) {
        this.jwglscore = jwglscore;
    }
}
