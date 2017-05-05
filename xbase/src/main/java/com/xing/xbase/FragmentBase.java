package com.xing.xbase;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xing.xbase.util.LogUtil;

/**
 * Created by mzj on 2017/5/4.
 */

public class FragmentBase extends Fragment {
    private ProgressDialog progressDialog;
    private View ContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getContext());
        initView();
        return ContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLinster();
        initDatas();
    }

    public void setContentView(int layoutResID) {
        ContentView = getActivity().getLayoutInflater().inflate(layoutResID, null);
    }

    /**
     * 初始化View
     */
    protected void initView() {
    }

    /**
     * 初始化监听事件
     */
    protected void initLinster() {
    }

    /**
     * 初始化数据
     */
    protected void initDatas() {
    }

    public final <E extends View> E getViewById(int id) {
        return (E) ContentView.findViewById(id);
    }

    /**
     * 简化Toast
     *
     * @param text 文本
     */
    protected void toast(CharSequence text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 简化Toast
     *
     * @param resId 文本ID
     */
    protected void toast(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示ProgressDialog
     *
     * @param text 文本
     */
    protected void showProgressDialog(CharSequence text) {
        progressDialog.setMessage(text);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    /**
     * 关闭ProgressDialog
     */
    protected void closeProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * LOG打印
     *
     * @param s 内容
     */
    protected void log(String s) {
        LogUtil.e(s);
    }
}
