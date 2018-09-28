package com.twinkle.htwinkle.entity;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "JwglInfo")
public class JwglInfo {

    @Column(name = "dates")
    private long dates;

    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    @Column(name = "lblCc")
    private String lblCc;

    @Column(name = "lblCsrq")
    private String lblCsrq;

    @Column(name = "lblDqszj")
    private String lblDqszj;

    @Column(name = "lblJtszd")
    private String lblJtszd;

    @Column(name = "lblKsh")
    private String lblKsh;

    @Column(name = "lblLxdh")
    private String lblLxdh;

    @Column(name = "lblMz")
    private String lblMz;

    @Column(name = "lblRxrq")
    private String lblRxrq;

    @Column(name = "lblSfzh")
    private String lblSfzh;

    @Column(name = "lblXb")
    private String lblXb;

    @Column(name = "lblXjzt")
    private String lblXjzt;

    @Column(name = "lblXy")
    private String lblXy;

    @Column(name = "lblXz")
    private String lblXz;

    @Column(name = "lblXzb")
    private String lblXzb;

    @Column(name = "lblYycj")
    private String lblYycj;

    @Column(name = "lblYzbm")
    private String lblYzbm;

    @Column(name = "lblZkzh")
    private String lblZkzh;

    @Column(name = "lblZymc")
    private String lblZymc;

    @Column(name = "lblZzmm")
    private String lblZzmm;

    @Column(name = "userid")
    private int userid;

    @Column(name = "xh")
    private String xh;

    @Column(name = "xm")
    private String xm;

    @Column(name = "xszp")
    private String xszp;

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

    public String getLblCc() {
        return lblCc;
    }

    public void setLblCc(String lblCc) {
        this.lblCc = lblCc;
    }

    public String getLblCsrq() {
        return lblCsrq;
    }

    public void setLblCsrq(String lblCsrq) {
        this.lblCsrq = lblCsrq;
    }

    public String getLblDqszj() {
        return lblDqszj;
    }

    public void setLblDqszj(String lblDqszj) {
        this.lblDqszj = lblDqszj;
    }

    public String getLblJtszd() {
        return lblJtszd;
    }

    public void setLblJtszd(String lblJtszd) {
        this.lblJtszd = lblJtszd;
    }

    public String getLblKsh() {
        return lblKsh;
    }

    public void setLblKsh(String lblKsh) {
        this.lblKsh = lblKsh;
    }

    public String getLblLxdh() {
        return lblLxdh;
    }

    public void setLblLxdh(String lblLxdh) {
        this.lblLxdh = lblLxdh;
    }

    public String getLblMz() {
        return lblMz;
    }

    public void setLblMz(String lblMz) {
        this.lblMz = lblMz;
    }

    public String getLblRxrq() {
        return lblRxrq;
    }

    public void setLblRxrq(String lblRxrq) {
        this.lblRxrq = lblRxrq;
    }

    public String getLblSfzh() {
        return lblSfzh;
    }

    public void setLblSfzh(String lblSfzh) {
        this.lblSfzh = lblSfzh;
    }

    public String getLblXb() {
        return lblXb;
    }

    public void setLblXb(String lblXb) {
        this.lblXb = lblXb;
    }

    public String getLblXjzt() {
        return lblXjzt;
    }

    public void setLblXjzt(String lblXjzt) {
        this.lblXjzt = lblXjzt;
    }

    public String getLblXy() {
        return lblXy;
    }

    public void setLblXy(String lblXy) {
        this.lblXy = lblXy;
    }

    public String getLblXz() {
        return lblXz;
    }

    public void setLblXz(String lblXz) {
        this.lblXz = lblXz;
    }

    public String getLblXzb() {
        return lblXzb;
    }

    public void setLblXzb(String lblXzb) {
        this.lblXzb = lblXzb;
    }

    public String getLblYycj() {
        return lblYycj;
    }

    public void setLblYycj(String lblYycj) {
        this.lblYycj = lblYycj;
    }

    public String getLblYzbm() {
        return lblYzbm;
    }

    public void setLblYzbm(String lblYzbm) {
        this.lblYzbm = lblYzbm;
    }

    public String getLblZkzh() {
        return lblZkzh;
    }

    public void setLblZkzh(String lblZkzh) {
        this.lblZkzh = lblZkzh;
    }

    public String getLblZymc() {
        return lblZymc;
    }

    public void setLblZymc(String lblZymc) {
        this.lblZymc = lblZymc;
    }

    public String getLblZzmm() {
        return lblZzmm;
    }

    public void setLblZzmm(String lblZzmm) {
        this.lblZzmm = lblZzmm;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXszp() {
        return xszp;
    }

    public void setXszp(String xszp) {
        this.xszp = xszp;
    }
}