package com.xing.xbase.net;

/**
 * 文件接口
 * Created by mzj on 2017/3/16.
 */


public interface ProgressCallBase<T> {
    void onFileFailure();

    void onProgress(long current, long total, long progress, boolean isfinish);

    void onSuccess(T t);
}