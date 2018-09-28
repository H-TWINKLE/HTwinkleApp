package com.twinkle.htwinkle.entity;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {

    private String jwgl;

    private String eol;

    private String nickName;

    private String sex;

    private String auto;

    private String passWord;

    private String headerPic;

    private String schoolId;

    private List<User> focus;

    private List<User> fans;

    private String lv;

    private User(Builder builder) {
        setObjectId(builder.objectId);
        setCreatedAt(builder.createdAt);
        setUpdatedAt(builder.updatedAt);
        setUsername(builder.username);
        setPassword(builder.password);
        setEmail(builder.email);
        setEmailVerified(builder.emailVerified);
        setSessionToken(builder.sessionToken);
        setMobilePhoneNumber(builder.mobilePhoneNumber);
        setMobilePhoneNumberVerified(builder.mobilePhoneNumberVerified);
        setJwgl(builder.jwgl);
        setEol(builder.eol);
        setNickName(builder.nickName);
        setSex(builder.sex);
        setAuto(builder.auto);
        setPassWord(builder.passWord);
        setHeaderPic(builder.headerPic);
        setSchoolId(builder.schoolId);
        setFocus(builder.focus);
        setFans(builder.fans);
        setLv(builder.lv);
    }


    public String getJwgl() {
        return jwgl;
    }

    public void setJwgl(String jwgl) {
        this.jwgl = jwgl;
    }

    public String getEol() {
        return eol;
    }

    public void setEol(String eol) {
        this.eol = eol;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getHeaderPic() {
        return headerPic;
    }

    public void setHeaderPic(String headerPic) {
        this.headerPic = headerPic;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public List<User> getFocus() {
        return focus;
    }

    public void setFocus(List<User> focus) {
        this.focus = focus;
    }

    public List<User> getFans() {
        return fans;
    }

    public void setFans(List<User> fans) {
        this.fans = fans;
    }

    public String getLv() {
        return lv;
    }

    public void setLv(String lv) {
        this.lv = lv;
    }

    public static final class Builder {
        private String objectId;
        private String createdAt;
        private String updatedAt;
        private String username;
        private String password;
        private String email;
        private Boolean emailVerified;
        private String sessionToken;
        private String mobilePhoneNumber;
        private Boolean mobilePhoneNumberVerified;
        private String jwgl;
        private String eol;
        private String nickName;
        private String sex;
        private String auto;
        private String passWord;
        private String headerPic;
        private String schoolId;
        private List<User> focus;
        private List<User> fans;
        private String lv;

        public Builder() {
        }

        public Builder objectId(String val) {
            objectId = val;
            return this;
        }

        public Builder createdAt(String val) {
            createdAt = val;
            return this;
        }

        public Builder updatedAt(String val) {
            updatedAt = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder emailVerified(Boolean val) {
            emailVerified = val;
            return this;
        }

        public Builder sessionToken(String val) {
            sessionToken = val;
            return this;
        }

        public Builder mobilePhoneNumber(String val) {
            mobilePhoneNumber = val;
            return this;
        }

        public Builder mobilePhoneNumberVerified(Boolean val) {
            mobilePhoneNumberVerified = val;
            return this;
        }

        public Builder jwgl(String val) {
            jwgl = val;
            return this;
        }

        public Builder eol(String val) {
            eol = val;
            return this;
        }

        public Builder nickName(String val) {
            nickName = val;
            return this;
        }

        public Builder sex(String val) {
            sex = val;
            return this;
        }

        public Builder auto(String val) {
            auto = val;
            return this;
        }

        public Builder passWord(String val) {
            passWord = val;
            return this;
        }

        public Builder headerPic(String val) {
            headerPic = val;
            return this;
        }

        public Builder schoolId(String val) {
            schoolId = val;
            return this;
        }

        public Builder focus(List<User> val) {
            focus = val;
            return this;
        }

        public Builder fans(List<User> val) {
            fans = val;
            return this;
        }

        public Builder lv(String val) {
            lv = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
