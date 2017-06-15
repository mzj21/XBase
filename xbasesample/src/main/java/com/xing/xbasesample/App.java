package com.xing.xbasesample;

import com.xing.xbase.AppBase;
import com.xing.xbase.util.FileUtil;

/**
 * Created by mzj on 2017/5/5.
 */

public class App extends AppBase {
    @Override
    protected void init() {
        FileUtil.init(getPackageName());
    }
}
