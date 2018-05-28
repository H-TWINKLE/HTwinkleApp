package com.twinkle.htwinkle.bean;

public class ViewTypes {

    public static final int Title_TYPE = 1;
    public static final int Div_TYPE = 2;
    public static final int DivHeader_TYPE = 3;


    private Integer type;
    private String menuTitle;
    private int menuIcon;

    public ViewTypes(Integer type) {
        this.type = type;
    }

    public ViewTypes() {
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public int getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(int menuIcon) {
        this.menuIcon = menuIcon;
    }
}


