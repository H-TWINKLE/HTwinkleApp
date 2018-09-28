package com.twinkle.htwinkle.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "JwglTtb")
public class JwglTtb {

    /**
     * dates : 1537844038446
     * id : 400079
     * userid : 100104
     * week1 : [*数字摄影与摄像技术 选修 周一第1,2节{第2-16周|双周} 游君臣 1教楼B-305*, *平面媒体设计 选修 周一第3,4节{第1-15周|单周} 杜菲菲 1教楼B-303*, *数字摄影与摄像技术 选修 周一第5,6节{第1-17周|单周} 游君臣 1教楼B-305 平面媒体设计 选修 周一第5,6节{第2-16周|双周} 杜菲菲 1教楼B-303*, *数字摄影与摄像技术 选修 周一第7,8节{第1-17周|单周} 游君臣 1教楼B-305 平面媒体设计 选修 周一第7,8节{第2-16周|双周} 杜菲菲 1教楼B-303*]
     * week2 : [*市场应用项目实践 选修 周二第3,4节{第1-17周} 李丹 1教楼B-308*, *数字摄影与摄像技术 选修 周二第5,6节{第17-17周|单周} 游君臣 1教楼B-305 操作系统 必修 周二第5,6节{第1-16周} 汪晓飞 1教楼C-308*, *操作系统 必修 周二第7,8节{第1-15周|单周} 汪晓飞 1教楼B-308*]
     * week3 : [*Web移动应用程序设计 必修 周三第1,2节{第1-16周} 余科军 1教楼B-308*]
     * week4 : [*Android的应用软件开发 必修 周四第1,2节{第1-15周|单周} 李又玲 1教楼B-306*, *市场应用项目实践 选修 周四第3,4节{第17-17周|单周} 李丹 1教楼B-305 Android的应用软件开发 必修 周四第3,4节{第1-15周|单周} 李又玲 1教楼B-306*, *Android的应用软件开发 必修 周四第5,6节{第2-16周|双周} 李又玲 1教楼B-306*, *人文科学概论 必修 周四第7,8节{第1-1周|单周} 赵双叶/董春林 1教楼D-106 (换0062)、(换0061) 人文科学概论 必修 周四第7,8节{第2-16周} 赵双叶/董春林 1教楼D-105 (换0062)、(换0061)*]
     * week5 : [*Web移动应用程序设计 必修 周五第1,2节{第1-16周} 余科军 1教楼B-308*]
     */

    @Column(name = "dates")
    private long dates;

    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    @Column(name = "userid")
    private int userid;

    @Column(name = "week1")
    private String week1;

    @Column(name = "week2")
    private String week2;

    @Column(name = "week3")
    private String week3;

    @Column(name = "week4")
    private String week4;

    @Column(name = "week5")
    private String week5;

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

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getWeek1() {
        return week1;
    }

    public void setWeek1(String week1) {
        this.week1 = week1;
    }

    public String getWeek2() {
        return week2;
    }

    public void setWeek2(String week2) {
        this.week2 = week2;
    }

    public String getWeek3() {
        return week3;
    }

    public void setWeek3(String week3) {
        this.week3 = week3;
    }

    public String getWeek4() {
        return week4;
    }

    public void setWeek4(String week4) {
        this.week4 = week4;
    }

    public String getWeek5() {
        return week5;
    }

    public void setWeek5(String week5) {
        this.week5 = week5;
    }
}