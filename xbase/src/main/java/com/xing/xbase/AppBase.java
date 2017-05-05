package com.xing.xbase;

import android.app.Application;
import android.content.Context;

import com.xing.xbase.net.NetBase;
import com.xing.xbase.util.LogUtil;

/**
 * Created by mzj on 2017/4/24.
 */

public class AppBase extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        NetBase.init(); //网络基类初始化
        LogUtil.init(true);//LogUtil初始化
        init();
    }

    protected void init(){

    }
}
