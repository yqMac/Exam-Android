package com.yqmac.it.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharedPreferences操作类
 */
public class SharedPreferencesHandler {

    //静态??
    private static SharedPreferences prefs = null;
    private static SharedPreferences.Editor editor = null;

    /**
     * 得到实例
     *
     * @param context 项目资源
     * @return
     */
    public static SharedPreferences getInstance(Context context) {
        if (prefs == null) {
            //PreferenceManager 类  操作 Preference
            //getDefaultSharedPreferences 得到 Preference
            //参数 项目资源
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return prefs;
    }

    /**
     * 设置String类型的数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setDataToPref(Context context, String key, String value) {
        //上面定义的变量
        if (prefs == null) {
            //得到 Preference
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        //上面定义的变量
        if (editor == null) {
            editor = prefs.edit(); // 编辑文件
        }

        //保存字符串
        editor.putString(key, value);
        //提交
        editor.commit();
    }

    public static void setDataToPref(Context context, String key, int value) {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        if (editor == null) {
            editor = prefs.edit();
        }

        editor.putInt(key, value);
        editor.commit();
    }

    public static void setDataToPref(Context context, String key, boolean value) {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        if (editor == null) {
            editor = prefs.edit();
        }

        editor.putBoolean(key, value);
        editor.commit();
    }

    // 读数据

    /**
     * @param context  系统资源
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static String getDataFromPref(Context context, String key, String defValue) {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return prefs.getString(key, defValue);
    }

    public static int getDataFromPref(Context context, String key, int defValue) {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return prefs.getInt(key, defValue);
    }

    public static boolean getDataFromPref(Context context, String key, boolean defValue) {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return prefs.getBoolean(key, defValue);
    }


    // 删除数据
    public static void deleteDataToPref(Context context, String key) {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        if (editor == null) {
            editor = prefs.edit();
        }

        editor.remove(key);
        editor.commit();
    }
}