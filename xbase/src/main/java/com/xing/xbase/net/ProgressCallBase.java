package com.xing.xbase.net;

/**
 * 文件接口
 * Created by mzj on 2017/3/16.
 */
public interface ProgressCallBase<T> extends NetCallBackBase<T> {
    /**
     * 文件监听
     *
     * @param current  当前传输大小
     * @param total    总大小
     * @param progress 已完成百分比
     * @param isfinish 是否完成
     */
    void onProgress(long current, long total, long progress, boolean isfinish);
}