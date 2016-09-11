package com.o2cinemas.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 9/9/16.
 */
public class SharedPreferenceUtil {
    private static final String PRE = "O2Vidz_Pre";
    private static final String PRE_DOWNLOAD_ITEM = "O2Vidz_Pre_DOWNLOAD_ITEM";
    private static final String TASK_NUM_KEY = "TASK_NUM_KEY";
    private static final String DOWNLOAD_PATH_KEY = "DOWNLOAD_PATH_KEY";
    private static final String AUTO_RESUME_KEY = "AUTO_RESUME_KEY";
    private static final String DOWNLOAD_WIFI_KEY = "DOWNLOAD_WIFI_KEY";
    private static final String DOWNLOAD_ITEMS_KEY = "DOWNLOAD_ITEMS_KEY";

    public static void setDownloadTaskNum(Context context, int num) {
        SharedPreferences.Editor pre = context.getSharedPreferences(PRE, Context.MODE_PRIVATE).edit();
        pre.putInt(TASK_NUM_KEY, num);
        pre.commit();
    }

    public static int getDownloadTaskNum(Context context) {
        SharedPreferences pre = context.getSharedPreferences(PRE, Context.MODE_PRIVATE);
        return pre.getInt(TASK_NUM_KEY, 1);
    }

    public static void setDownloadPath(Context context, String path) {
        SharedPreferences.Editor pre = context.getSharedPreferences(PRE, Context.MODE_PRIVATE).edit();
        pre.putString(DOWNLOAD_PATH_KEY, path);
        pre.commit();
    }

    public static String getDownloadPath(Context context) {
        SharedPreferences pre = context.getSharedPreferences(PRE, Context.MODE_PRIVATE);
        return pre.getString(DOWNLOAD_PATH_KEY, "");
    }

    public static void setAutoResume(Context context, boolean isAutoResume) {
        SharedPreferences.Editor pre = context.getSharedPreferences(PRE, Context.MODE_PRIVATE).edit();
        pre.putBoolean(AUTO_RESUME_KEY, isAutoResume);
        pre.commit();
    }

    public static boolean getAutoResume(Context context) {
        SharedPreferences pre = context.getSharedPreferences(PRE, Context.MODE_PRIVATE);
        return pre.getBoolean(AUTO_RESUME_KEY, false);
    }

    public static void setDownLoadWifi(Context context, boolean isDownloadWifi) {
        SharedPreferences.Editor pre = context.getSharedPreferences(PRE, Context.MODE_PRIVATE).edit();
        pre.putBoolean(DOWNLOAD_WIFI_KEY, isDownloadWifi);
        pre.commit();
    }

    public static boolean getDownloadWifi(Context context) {
        SharedPreferences pre = context.getSharedPreferences(PRE, Context.MODE_PRIVATE);
        return pre.getBoolean(DOWNLOAD_WIFI_KEY, false);
    }


}
