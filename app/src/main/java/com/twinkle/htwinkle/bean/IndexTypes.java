package com.twinkle.htwinkle.bean;

public class IndexTypes {
    private Integer icon;
    private String title;

    public IndexTypes() {
    }

    public IndexTypes(Integer icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
