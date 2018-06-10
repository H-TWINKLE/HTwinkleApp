package com.twinkle.htwinkle.init;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.bean.User;

import java.io.File;
import java.math.BigDecimal;

import cn.bmob.v3.BmobUser;


public enum InitUtils {
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

 /*   public static String getTotalCacheSize(Context context) throws Exception {        //显示缓存大小
        long cacheSize = InitUtils.INSTANCE.getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            cacheSize += InitUtils.INSTANCE.getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }*/

    public  void clearAllCache(Context context) {           //删除缓存
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private  boolean deleteDir(File dir) {
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

        return dir ==null||dir.delete();
    }

    // 获取文件
    // Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
    // 目录，一般放一些长时间保存的数据
    // Context.getExternalCacheDir() -->
    // SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    private  long getFolderSize(File file) {
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
    }


    private static String getFormatSize(double size) {
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
    }


}
