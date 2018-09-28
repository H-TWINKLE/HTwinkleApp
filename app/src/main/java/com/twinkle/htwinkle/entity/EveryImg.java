package com.twinkle.htwinkle.entity;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "EveryImg")
public class EveryImg {

    @Column(name = "date")
    private String date;

    @Column(name = "types")
    private String types;

    @Column(name = "host")
    private String host;

    @Column(name = "name")
    private String name;

    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    @Column(name = "pic")
    private String pic;

    @Column(name = "read")       //1 为已读
    private Integer read;

    public Integer getRead() {
        return read;
    }

    public void setRead(Integer read) {
        this.read = read;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
