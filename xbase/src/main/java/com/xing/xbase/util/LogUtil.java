package com.xing.xbase.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Log
 * Created by mzj on 2017/3/30.
 */

public class LogUtil {
    private static boolean isDebug;
    private static SimpleDateFormat df = null;

    private static SimpleDateFormat getDf() {
        if (df != null) {
            return df;
        }
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df;
    }

    public static void init(boolean is) {
        isDebug = is;
    }

    private static String getTag() {
        SimpleDateFormat df = getDf();
        return "[" + df.format(new Date()) + "] -> ";
    }

    public static int d(String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.d(getTag(), msg);
    }

    public static int d(String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.d(getTag(), msg, tr);
    }

    public static int e(String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.e(getTag(), msg);
    }

    public static int e(String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.e(getTag(), msg, tr);
    }

    public static int i(String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.i(getTag(), msg);
    }

    public static int i(String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.i(getTag(), msg, tr);
    }
}
