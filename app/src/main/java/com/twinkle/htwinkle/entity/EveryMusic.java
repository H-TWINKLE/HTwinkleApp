package com.twinkle.htwinkle.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "EveryMusic")
public class EveryMusic {

    @Column(name = "netsinger")
    private String netsinger;

    @Column(name = "date")
    private String date;

    @Column(name = "netcomm")
    private String netcomm;

    @Column(name = "netcommid")
    private String netcommid;

    @Column(name = "nettauthor")
    private String nettauthor;

    @Column(name = "netname")
    private String netname;

    @Column(name = "nettime")
    private String nettime;

    @Column(name = "netpic")
    private String netpic;

    @Column(name = "netmusicurl")
    private String netmusicurl;

    @Column(name = "read")
    private Integer read;   //1 为已读

    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    public Integer getRead() {
        return read;
    }

    public void setRead(Integer read) {
        this.read = read;
    }


    public String getNetsinger() {
        return netsinger;
    }

    public void setNetsinger(String netsinger) {
        this.netsinger = netsinger;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNetcomm() {
        return netcomm;
    }

    public void setNetcomm(String netcomm) {
        this.netcomm = netcomm;
    }

    public String getNetcommid() {
        return netcommid;
    }

    public void setNetcommid(String netcommid) {
        this.netcommid = netcommid;
    }

    public String getNettauthor() {
        return nettauthor;
    }

    public void setNettauthor(String nettauthor) {
        this.nettauthor = nettauthor;
    }

    public String getNetname() {
        return netname;
    }

    public void setNetname(String netname) {
        this.netname = netname;
    }

    public String getNettime() {
        return nettime;
    }

    public void setNettime(String nettime) {
        this.nettime = nettime;
    }

    public String getNetpic() {
        return netpic;
    }

    public void setNetpic(String netpic) {
        this.netpic = netpic;
    }

    public String getNetmusicurl() {
        return netmusicurl;
    }

    public void setNetmusicurl(String netmusicurl) {
        this.netmusicurl = netmusicurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
