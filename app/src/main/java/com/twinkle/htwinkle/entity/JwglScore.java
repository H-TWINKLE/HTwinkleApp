package com.twinkle.htwinkle.entity;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "JwglScore")
public class JwglScore {

    @Column(name = "bukaochengji")
    private String bukaochengji;

    @Column(name = "chengji")
    private String chengji;

    @Column(name = "chongxiubiaoji")
    private String chongxiubiaoji;

    @Column(name = "chongxiuchengji")
    private String chongxiuchengji;

    @Column(name = "dates")
    private long dates;

    @Column(name = "fuxiubiaoji")
    private String fuxiubiaoji;

    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    @Column(name = "jidian")
    private String jidian;

    @Column(name = "kechengdaima")
    private String kechengdaima;

    @Column(name = "kechengguishu")
    private String kechengguishu;

    @Column(name = "kechengmingcheng")
    private String kechengmingcheng;

    @Column(name = "kechengxingzhi")
    private String kechengxingzhi;

    @Column(name = "userid")
    private int userid;

    @Column(name = "xuefen")
    private String xuefen;

    @Column(name = "xuenian")
    private String xuenian;

    @Column(name = "xueqi")
    private String xueqi;

    @Column(name = "xueyuanmingchen")
    private String xueyuanmingchen;

    public String getBukaochengji() {
        return bukaochengji;
    }

    public void setBukaochengji(String bukaochengji) {
        this.bukaochengji = bukaochengji;
    }

    public String getChengji() {
        return chengji;
    }

    public void setChengji(String chengji) {
        this.chengji = chengji;
    }

    public String getChongxiubiaoji() {
        return chongxiubiaoji;
    }

    public void setChongxiubiaoji(String chongxiubiaoji) {
        this.chongxiubiaoji = chongxiubiaoji;
    }

    public String getChongxiuchengji() {
        return chongxiuchengji;
    }

    public void setChongxiuchengji(String chongxiuchengji) {
        this.chongxiuchengji = chongxiuchengji;
    }

    public long getDates() {
        return dates;
    }

    public void setDates(long dates) {
        this.dates = dates;
    }

    public String getFuxiubiaoji() {
        return fuxiubiaoji;
    }

    public void setFuxiubiaoji(String fuxiubiaoji) {
        this.fuxiubiaoji = fuxiubiaoji;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJidian() {
        return jidian;
    }

    public void setJidian(String jidian) {
        this.jidian = jidian;
    }

    public String getKechengdaima() {
        return kechengdaima;
    }

    public void setKechengdaima(String kechengdaima) {
        this.kechengdaima = kechengdaima;
    }

    public String getKechengguishu() {
        return kechengguishu;
    }

    public void setKechengguishu(String kechengguishu) {
        this.kechengguishu = kechengguishu;
    }

    public String getKechengmingcheng() {
        return kechengmingcheng;
    }

    public void setKechengmingcheng(String kechengmingcheng) {
        this.kechengmingcheng = kechengmingcheng;
    }

    public String getKechengxingzhi() {
        return kechengxingzhi;
    }

    public void setKechengxingzhi(String kechengxingzhi) {
        this.kechengxingzhi = kechengxingzhi;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getXuefen() {
        return xuefen;
    }

    public void setXuefen(String xuefen) {
        this.xuefen = xuefen;
    }

    public String getXuenian() {
        return xuenian;
    }

    public void setXuenian(String xuenian) {
        this.xuenian = xuenian;
    }

    public String getXueqi() {
        return xueqi;
    }

    public void setXueqi(String xueqi) {
        this.xueqi = xueqi;
    }

    public String getXueyuanmingchen() {
        return xueyuanmingchen;
    }

    public void setXueyuanmingchen(String xueyuanmingchen) {
        this.xueyuanmingchen = xueyuanmingchen;
    }
}


