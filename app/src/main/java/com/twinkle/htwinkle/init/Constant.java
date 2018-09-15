package com.twinkle.htwinkle.init;

import android.app.Application;

import com.twinkle.htwinkle.R;

public class Constant {

    public static final int Modify_Pass = 10001;  // 修改密码  忘记密码页面

    public static final int Register_ = 10002;  //  创建密码   注册页面

    public static final int Default_Int = 10000; //默认页面

    public static final int CountDownInterval_ = 1000; //计时器时间跳转间隔

    public static final int REQUEST_CODE = 10003;  //回调id

    public static final int[] my_menu_user_icon = { R.drawable.user, R.drawable.coll, R.drawable.dra};

    public static final int[] my_menu_jwgl_icon = { R.drawable.info, R.drawable.jwgl_ttb, R.drawable.jwgl_score};

    public static final int[] my_menu_eol_icon = { R.drawable.info, R.drawable.eol_work};

    public static final int[] my_menu_one_icon = { R.drawable.onearticle, R.drawable.onemusic,R.drawable.onepic};

    public static final int[] my_menu_tool_icon = { R.drawable.toolxiaohua, R.drawable.tooldiannao};

    public static final int[] my_menu_setting_icon = { R.drawable.setting, R.drawable.logout};

    public static final String[] my_pic_url = {
            "http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=5e310a4ddb09b3deffb2ec2ba4d606f4/9d82d158ccbf6c81887581cdb63eb13533fa4050.jpg",
            "http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=9b867a04b299a9012f38537575fc600e/4d086e061d950a7b86bee8d400d162d9f2d3c913.jpg",
            "http://pic5.photophoto.cn/20071228/0034034901778224_b.jpg",
            "http://imgsrc.baidu.com/imgad/pic/item/34fae6cd7b899e51fab3e9c048a7d933c8950d21.jpg",
            "http://img3.3lian.com/2013/c3/80/d/2.jpg"
    };  //demoPic

    public static final String[] my_pic_title = {
            "穿过树林的鸟儿手机唯美壁纸",
            "水墨风景手机壁纸下载",
            "雪山上的湖泊很唯美的手机桌面",
            "奇幻炫彩云彩手机壁纸下载",
            "湖上的埃菲尔铁塔浪漫爱情手机壁纸"
    };  //demoTitle

    public static final String[] my_types_title = {
            "type1", "type2", "type3", "type4", "type5", "type6", "type7", "type8"
    }; //types_title

    public static final int[] my_types_icon = {
            R.drawable.type1,R.drawable.type2,R.drawable.type3,R.drawable.type4,
            R.drawable.type5,R.drawable.type6,R.drawable.type7,R.drawable.type8,
    }; //types_icon

    public final static String BOMBKEY = "efcec7fdecd3aefe199792559b33bf1b";

    public final static  String  RESTKEY = "2f6e22943cb0acd32ca6c5d0dfa07b08";

    public final static String MASTERKEY = "610b06bc1d804857ad71e8faccaebd14";

    public final static String UPDATEUSERPASSINNOTLOGIN = "https://api2.bmob.cn/1/updateUserPassword/";

    public final static String UPDATEUSERPASSINNOTLOGINBYPASS2 = "https://api2.bmob.cn/1/users/";

}
