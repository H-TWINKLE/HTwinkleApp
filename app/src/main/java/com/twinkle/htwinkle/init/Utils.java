package com.twinkle.htwinkle.init;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.entity.Post;
import com.twinkle.htwinkle.entity.Title;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.entity.ViewTypes;

import org.xutils.DbManager;
import org.xutils.image.ImageOptions;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;


public enum Utils {
    INSTANCE;


    private static final String TAG = "Utils";

    public void saveUser(Context context, String tel, String pass) {

        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.User), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("tel", tel);
        editor.putString("pass", pass);
        editor.putBoolean("isLoginOut", true);
        editor.apply();

    }


    public void UserLoginOut(Context context) {
        BmobUser.logOut();
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.User), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLoginOut", false);
        editor.apply();

    }


    public User remUser(Context context) {

        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.User), Context.MODE_PRIVATE);

        return new User.Builder()
                .username(sp.getString("tel", ""))
                .passWord(sp.getString("pass", ""))
                .build();

    }

    public ImageOptions baseOptions() {
        return new ImageOptions.Builder().setLoadingDrawableId(R.drawable.load).setIgnoreGif(true).build();
    }

    public ImageOptions IndexOptions() {
        return new ImageOptions.Builder().setLoadingDrawableId(R.drawable.load).setIgnoreGif(true).setSize(800, 800).build();
    }

   /* public Bitmap blurBitmap(Context context, Bitmap image, float blurRadius) { // 计算图片缩小后的长宽
        int width = Math.round(image.getWidth() * 0.4f);
        int height = Math.round(image.getHeight() * 0.4f); // 将缩小后的图片做为预渲染的图片
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false); // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap); // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context); // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs)); // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap); // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurRadius); // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn); // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut); // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }*/

    public <T> List<Title> ReflexByClass(Context context, T t) {

        if (t == null) return null;

        List<Title> list = new ArrayList<>();

        Field[] fields = t.getClass().getDeclaredFields();

        for (Field f : fields) {

            f.setAccessible(true);

            try {
                switch (f.getName()) {
                    case "dates":
                        list.add(new Title("更新时间", DateUtils.formatDateTime(context, Long.valueOf(f.get(t).toString()),
                                DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME)));
                        break;
                    case "lblCsrq":
                        list.add(new Title("出生日期", String.valueOf(f.get(t))));
                        break;
                    case "lblCc":
                        list.add(new Title("学历层次", String.valueOf(f.get(t))));
                        break;
                    case "lblDqszj":
                        list.add(new Title("当前所在级", String.valueOf(f.get(t))));
                        break;
                    case "lblJtszd":
                        list.add(new Title("家庭所在地", String.valueOf(f.get(t))));
                        break;
                    case "lblKsh":
                        list.add(new Title("考生号", String.valueOf(f.get(t))));
                        break;
                    case "lblMz":
                        list.add(new Title("民族", String.valueOf(f.get(t))));
                        break;
                    case "lblRxrq":
                        list.add(new Title("入学日期", String.valueOf(f.get(t))));
                        break;
                    case "lblSfzh":
                        list.add(new Title("身份证号码", String.valueOf(f.get(t))));
                        break;
                    case "lblXb":
                        list.add(new Title("性别", String.valueOf(f.get(t))));
                        break;
                    case "lblXjzt":
                        list.add(new Title("学籍状态", String.valueOf(f.get(t))));
                        break;
                    case "lblXy":
                        list.add(new Title("学院", String.valueOf(f.get(t))));
                        break;
                    case "lblXz":
                        list.add(new Title("学制", String.valueOf(f.get(t))));
                        break;
                    case "lblXzb":
                        list.add(new Title("行政班", String.valueOf(f.get(t))));
                        break;
                    case "lblYycj":
                        list.add(new Title("高考英语成绩", String.valueOf(f.get(t))));
                        break;
                    case "lblYzbm":
                        list.add(new Title("邮政编码", String.valueOf(f.get(t))));
                        break;
                    case "lblZymc":
                        list.add(new Title("专业名称", String.valueOf(f.get(t))));
                        break;
                    case "lblZzmm":
                        list.add(new Title("政治面貌", String.valueOf(f.get(t))));
                        break;
                    case "xm":
                        list.add(new Title("姓名", String.valueOf(f.get(t))));
                        break;
                    case "admin":
                        list.add(new Title("学号", String.valueOf(f.get(t))));
                        break;
                    case "loginfre":
                        list.add(new Title("登录次数", String.valueOf(f.get(t))));
                        break;
                    case "name":
                        list.add(new Title("姓名", String.valueOf(f.get(t))));
                        break;
                    case "onlinetime":
                        list.add(new Title("在线时长", String.valueOf(f.get(t))));
                        break;
                    case "xh":
                        list.add(new Title("学号", String.valueOf(f.get(t))));
                        break;

                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }
        return list;
    }


    public DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            //.setDbDir(new File(Environment.getExternalStorageDirectory().getPath()+"/How/"))
            .setDbName("htwinkle.db")
            //设置数据库路径，默认存储在app的私有目录
            .setDbVersion(1)
            .setAllowTransaction(true)    //设置是否允许事务，默认true
            //设置数据库打开的监听
            .setDbOpenListener(db -> db.getDatabase().enableWriteAheadLogging())
            //设置数据库更新的监听
            .setDbUpgradeListener((db, oldVersion, newVersion) -> {
            })
            //设置表创建的监听
            .setTableCreateListener((db, table) -> {
            });


    public ImageOptions ImageOptionsInCir() {
        return new ImageOptions.Builder().setLoadingDrawableId(R.drawable.logo).setIgnoreGif(false).setCircular(true).build();
    }

    public ImageOptions ImageOptionsInWelBackGround() {
        return new ImageOptions.Builder().setLoadingDrawableId(R.drawable.logo).setIgnoreGif(false).setLoadingDrawableId(R.drawable.wel).setFailureDrawableId(R.drawable.wel).build();
    }

    public String getPostTopicInString(Post post) {

        if (post == null || post.getTopic() == null) return "";

        StringBuilder stringBuffer = new StringBuilder();
        for (String li : post.getTopic()) {
            stringBuffer.append(li);
            stringBuffer.append(" ");
        }

        return stringBuffer.toString();

    }

    public String setPraiseFromIntToString(TextView textView, int value) {

        if ("".equals(textView.getText().toString())) {
            return String.valueOf(value);
        }
        return String.valueOf(Integer.parseInt(textView.getText().toString()) + value);
    }



 /*   public static String getTotalCacheSize(Context context) throws Exception {        //显示缓存大小
        long cacheSize = Utils.INSTANCE.getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            cacheSize += Utils.INSTANCE.getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }*/

    public void clearAllCache(Context context) {           //删除缓存
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            int size;
            if (children != null) {
                size = children.length;
                for (int i = 0; i < size; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }

        }

        return dir == null || dir.delete();
    }


    public List<ViewTypes> getMenuList(Context context) {

        Resources resources = context.getResources();

        List<ViewTypes> list = new ArrayList<>();

        ViewTypes viewTypes;

        list.add(new ViewTypes(3, resources.getString(R.string.my_menu_user_title)));      //我的

        String[] str = resources.getStringArray(R.array.my_menu_user);

        for (int x = 0; x < str.length; x++) {
            viewTypes = new ViewTypes();
            viewTypes.setType(1);
            viewTypes.setMenuTitle(str[x]);
            viewTypes.setMenuIcon(Constant.my_menu_user_icon[x]);

            list.add(viewTypes);
        }

        list.add(new ViewTypes(2));          //分割线

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        if (sp.getBoolean("setting_jwgl", false)) {

            list.add(new ViewTypes(3, resources.getString(R.string.my_menu_jwgl_title)));     //教务管理系统title

            str = resources.getStringArray(R.array.my_menu_jwgl);
            for (int x = 0; x < str.length; x++) {
                viewTypes = new ViewTypes();
                viewTypes.setType(1);
                viewTypes.setMenuTitle(str[x]);
                viewTypes.setMenuIcon(Constant.my_menu_jwgl_icon[x]);
                list.add(viewTypes);
            }

            list.add(new ViewTypes(2));         //分割线
        }


        if (sp.getBoolean("setting_eol", false)) {

            list.add(new ViewTypes(3, resources.getString(R.string.my_menu_eol_title)));     //教务管理系统title

            str = resources.getStringArray(R.array.my_menu_eol);

            for (int x = 0; x < str.length; x++) {
                viewTypes = new ViewTypes();
                viewTypes.setType(1);
                viewTypes.setMenuTitle(str[x]);
                viewTypes.setMenuIcon(Constant.my_menu_eol_icon[x]);


                list.add(viewTypes);
            }

            list.add(new ViewTypes(2));   //分割线
        }

        if (sp.getBoolean("setting_one_day", false)) {

            list.add(new ViewTypes(3, resources.getString(R.string.my_menu_one_day_title)));     //每日推荐title

            str = resources.getStringArray(R.array.my_menu_one);

            for (int x = 0; x < str.length; x++) {
                viewTypes = new ViewTypes();
                viewTypes.setType(1);
                viewTypes.setMenuTitle(str[x]);
                viewTypes.setMenuIcon(Constant.my_menu_one_icon[x]);

                list.add(viewTypes);
            }

            list.add(new ViewTypes(2));   //分割线
        }

        if (sp.getBoolean("setting_tools", false)) {

            list.add(new ViewTypes(3, resources.getString(R.string.my_menu_tools_title)));     //小助手title

            str = resources.getStringArray(R.array.my_menu_tool);

            for (int x = 0; x < str.length; x++) {
                viewTypes = new ViewTypes();
                viewTypes.setType(1);
                viewTypes.setMenuTitle(str[x]);
                viewTypes.setMenuIcon(Constant.my_menu_tool_icon[x]);

                list.add(viewTypes);
            }

            list.add(new ViewTypes(2));   //分割线
        }

        list.add(new ViewTypes(3, resources.getString(R.string.my_menu_seeting_title)));     //设置title

        str = resources.getStringArray(R.array.my_menu_setting);

        for (int x = 0; x < str.length; x++) {
            viewTypes = new ViewTypes();
            viewTypes.setType(1);
            viewTypes.setMenuTitle(str[x]);
            viewTypes.setMenuIcon(Constant.my_menu_setting_icon[x]);

            list.add(viewTypes);
        }

        return list;


    }

    public boolean verificationBmobMsgIsOk(String result) {

        if (result == null) return false;

        JSONObject js = JSONObject.parseObject(result);
        String msg = js.getString("msg");

        return msg != null && !"".equals(msg) && "ok".equals(msg);
    }

    public String setUserLvByString(User u) {

        if (u.getLv() == null) return "LV0";
        String lv = u.getLv();
        switch (Integer.parseInt(lv) / 1000) {
            case 0:
                return "LV0";
            case 1:
                return "LV1";

            case 2:
                return "LV2";

            case 3:
                return "LV3";

            case 4:
                return "LV4";

            case 5:
                return "LV5";

            case 6:
                return "LV6";

            case 7:
                return "LV7";

            case 8:
                return "LV8";

            default:
                return "LV0";
        }
    }


    public Spanned convertHtmlText(String text) {

        if (TextUtils.isEmpty(text)) {
            return Html.fromHtml("");
        }
        return Html.fromHtml(text);
    }


    // 获取文件
    // Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
    // 目录，一般放一些长时间保存的数据
    // Context.getExternalCacheDir() -->
    // SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
   /* private  long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            int size2;
            if (fileList != null) {
                size2 = fileList.length;
                for (int i = 0; i < size2; i++) {
                    // 如果下面还有文件
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }*/


    /*private  String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            // return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }*/


}
