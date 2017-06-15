package com.xing.xbase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
    protected void toast(final CharSequence text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 简化Toast
     *
     * @param resId 文本ID
     */
    protected void toast(final int resId) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext().getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
            }
        });
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

    /**
     * 简化跳转
     *
     * @param context 上下文
     * @param cls     class
     */
    protected void startActivity(Context context, Class<?> cls) {
        startActivity(new Intent(context, cls));
    }
}
