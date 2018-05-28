package com.twinkle.htwinkle.init;

import android.content.Context;
import android.content.SharedPreferences;

public enum InitUtils {
    INSTANCE;

    public boolean saveUser(Context context, String tel, String pass) {
        
        SharedPreferences sp = context.getSharedPreferences(InitString.User, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("tel", tel);
        editor.putString("pass", pass);
        editor.putBoolean("isLoginOut", true);
        return editor.commit();

    }
}
