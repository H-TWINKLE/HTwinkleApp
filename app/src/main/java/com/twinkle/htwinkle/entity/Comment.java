package com.twinkle.htwinkle.entity;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {

    private String content;
    private User author;
    private Post post;
    private User replyuser;
    private String replycontent;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getReplyuser() {
        return replyuser;
    }

    public void setReplyuser(User replyuser) {
        this.replyuser = replyuser;
    }

    public String getReplycontent() {
        return replycontent;
    }

    public void setReplycontent(String replycontent) {
        this.replycontent = replycontent;
    }
}
