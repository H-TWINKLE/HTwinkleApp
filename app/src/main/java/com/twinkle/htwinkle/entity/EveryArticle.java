package com.twinkle.htwinkle.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;


@Table(name = "EveryArticle")
public class EveryArticle {

    @Column(name = "author")
    private String author;

    @Column(name = "dates")
    private String dates;

    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "read")             //1 为已读
    private Integer read;

    public Integer getRead() {
        return read;
    }

    public void setRead(Integer read) {
        this.read = read;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
