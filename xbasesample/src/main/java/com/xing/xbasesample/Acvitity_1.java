package com.xing.xbasesample;

import com.xing.xbase.ActivityBase;
import com.xing.xbase.net.NetBase;
import com.xing.xbase.net.NetBaseSync;
import com.xing.xbase.util.FileUtil;

import okhttp3.Call;

/**
 * Created by mzj on 2017/5/5.
 */

public class Acvitity_1 extends ActivityBase {

    @Override
    protected void initView() {
        toggleTitleBarVisible(false);
        setImmersive();
        setContentView(R.layout.activity_1);
    }

    @Override
    protected void initDatas() {
        new NetThread().start();
    }

    private class NetThread extends Thread {

        @Override
        public void run() {
            super.run();
            NetBaseSync.download("http://phonesprite.img-cn-shanghai.aliyuncs.com/content/20170107/9f81fd6c41924777a0c58e3867ce13bb.jpg"
                    , 0, FileUtil.Path_Temp, "1.jpg", new NetBase.ProgressCall() {
                        @Override
                        public void onProgress(long l, long l1, long l2, boolean b) {
                            log("" + l2);
                        }

                        @Override
                        public void onSuccess(int i, NetBase.NetData netData) {

                        }

                        @Override
                        public void onFailure(Call call, String s) {

                        }
                    });
        }
    }
}
