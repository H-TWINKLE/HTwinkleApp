package com.twinkle.htwinkle.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.Date;

public class Talk implements MultiItemEntity {

    public static final int sendByUser = 1;   //用户发送

    public static final int sendByRobot = 2;  //机器人发送

    private User user;

    private int types;

    private String name;

    private Date date;

    private Robot robot;

    public Talk() {
    }

    public Talk(int types, String name, Date date) {
        this.types = types;
        this.name = name;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    @Override
    public int getItemType() {

        return types;
    }
}
