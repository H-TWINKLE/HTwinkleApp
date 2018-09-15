package com.twinkle.htwinkle.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class Post extends BmobObject{

    private String title;
    private User author;
    private String content;
    private List<String> pic;
    private List<String> topic;
    private String place;
    private BmobRelation userlike;
    private BmobRelation usercomment;
    private Integer likenum;
    private Integer commentnum;
    private Integer hot;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public List<String> getTopic() {
        return topic;
    }

    public void setTopic(List<String> topic) {
        this.topic = topic;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public BmobRelation getUserlike() {
        return userlike;
    }

    public void setUserlike(BmobRelation userlike) {
        this.userlike = userlike;
    }

    public BmobRelation getUsercomment() {
        return usercomment;
    }

    public void setUsercomment(BmobRelation usercomment) {
        this.usercomment = usercomment;
    }

    public Integer getLikenum() {
        return likenum;
    }

    public void setLikenum(Integer likenum) {
        this.likenum = likenum;
    }

    public Integer getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(Integer commentnum) {
        this.commentnum = commentnum;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }





}
