package com.twinkle.htwinkle.entity;

import cn.bmob.v3.BmobObject;

public class Focus extends BmobObject {

    private User focusUser;  //主动关注

    private User onFocusUser;   //被关注

    public Focus() {
    }

    public Focus(User focusUser, User onFocusUser) {
        this.focusUser = focusUser;
        this.onFocusUser = onFocusUser;
    }

    public User getFocusUser() {
        return focusUser;
    }

    public void setFocusUser(User focusUser) {
        this.focusUser = focusUser;
    }

    public User getOnFocusUser() {
        return onFocusUser;
    }

    public void setOnFocusUser(User onFocusUser) {
        this.onFocusUser = onFocusUser;
    }


}
