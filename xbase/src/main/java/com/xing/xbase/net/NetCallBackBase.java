package com.xing.xbase.net;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 普通监听
 * Created by mzj on 2017/2/10.
 */
public interface NetCallBackBase<T> {
    void onSuccess(int Code, NetBase.NetData<T> netData);

    void onFailure(Call call, String msg);
}
