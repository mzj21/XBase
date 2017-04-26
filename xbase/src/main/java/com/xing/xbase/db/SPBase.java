package com.xing.xbase.db;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.xing.xbase.AppBase;

public class SPBase {
    public static String load(String key) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(AppBase.getContext());
        return mPrefs.getString(key, "");
    }

    public static void save(String key, String value) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(AppBase.getContext());
        Editor ed = mPrefs.edit();
        ed.putString(key, value);
        ed.apply();
    }
}
