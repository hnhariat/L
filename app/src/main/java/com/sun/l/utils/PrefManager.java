package com.sun.l.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sunje on 2016-03-07.
 */
public class PrefManager {
    public static PrefManager sInstance;


    public static PrefManager getInstance() {
        if (sInstance == null) {
            synchronized (PrefManager.class) {
                sInstance = new PrefManager();
            }
        }
        return sInstance;
    }

    public void putString(Context c, String key, String value) {
        SharedPreferences pref = c.getSharedPreferences("l.pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public String getString(Context c, String key) {
        SharedPreferences pref = c.getSharedPreferences("l.pref", Activity.MODE_PRIVATE);
        return pref.getString(key, "");
    }
}
