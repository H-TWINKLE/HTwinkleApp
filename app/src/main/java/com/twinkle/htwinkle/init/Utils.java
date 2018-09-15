package com.twinkle.htwinkle.init;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.bean.User;
import com.twinkle.htwinkle.bean.ViewTypes;

import org.xutils.image.ImageOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;


public enum Utils {
    INSTANCE;

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

    public ImageOptions ImageOptionsInCir() {
        return new ImageOptions.Builder().setLoadingDrawableId(R.drawable.logo).setIgnoreGif(false).setCircular(true).build();
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
                if (x == 1) {
                    viewTypes.setNewTip(true);
                    viewTypes.setType(1);
                    viewTypes.setMenuTitle(str[x]);
                    viewTypes.setMenuIcon(Constant.my_menu_eol_icon[x]);
                } else {
                    viewTypes.setType(1);
                    viewTypes.setMenuTitle(str[x]);
                    viewTypes.setMenuIcon(Constant.my_menu_eol_icon[x]);
                }

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
