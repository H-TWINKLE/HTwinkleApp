package com.twinkle.htwinkle.entity;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "List")
public class List {


    @Column(name = "abort")
    private String abort;

    @Column(name = "dates")
    private long dates;

    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "title")
    private String title;

    @Column(name = "url")
    private String url;

    public String getAbort() {
        return abort;
    }

    public void setAbort(String abort) {
        this.abort = abort;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
