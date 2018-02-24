package com.xing.xbase.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 公共
 * Created by mzj on 2017/2/10.
 */

public class CommonUtil {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 返回yyyy-MM-dd HH:mm:ss格式时间
     *
     * @param time long型时间
     */
    public static String getCurTime(Long time) {
        if (time == null || time <= 0L) {
            time = System.currentTimeMillis();
        }
        Date date = new Date(time);
        return sdf.format(date);
    }

    /**
     * 获取程序的版本号
     *
     * @param context  上下文
     * @param packname 包名
     * @return 版本号
     */
    public String getAppVersion(Context context, String packname) {
        try {
            PackageInfo packinfo = context.getPackageManager().getPackageInfo(packname, 0);
            return packinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return "";
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 高度单位px
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
