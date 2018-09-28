package com.twinkle.htwinkle.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

@Table(name = "Eol")
public class Eol {

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

    @Column(name = "loginfre")
    private String loginfre;

    @Column(name = "name")
    private String name;

    @Column(name = "onlinetime")
    private String onlinetime;

    @Column(name = "pass")
    private String pass;

    @Column(name = "tip")
    private String tip;

    private List<List<com.twinkle.htwinkle.entity.List>> list;

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

    public String getLoginfre() {
        return loginfre;
    }

    public void setLoginfre(String loginfre) {
        this.loginfre = loginfre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnlinetime() {
        return onlinetime;
    }

    public void setOnlinetime(String onlinetime) {
        this.onlinetime = onlinetime;
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

    public List<List<com.twinkle.htwinkle.entity.List>> getList() {
        return list;
    }

    public void setList(List<List<com.twinkle.htwinkle.entity.List>> list) {
        this.list = list;
    }
}
