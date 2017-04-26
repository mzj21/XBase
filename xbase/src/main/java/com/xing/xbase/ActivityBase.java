package com.xing.xbase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xing.xbase.widget.TextAndImageView;
import com.xing.xbase.widget.TitleBar;

public class ActivityBase extends AppCompatActivity {
    private WindowManager windowManager;
    private TitleBar titlebar;
    private RelativeLayout rootview;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        initBaseView();
        initView();
        initLinster();
        initDatas();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initBaseView() {
        rootview = getViewById(R.id.rootview);
        titlebar = getViewById(R.id.titlebar);
        titlebar.getleft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickTitleBarLeft(titlebar.getleft());
            }
        });
        progressDialog = new ProgressDialog(this);
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

    /**
     * 返回键点击事件，可重写
     */
    protected void OnClickTitleBarLeft(TextAndImageView textAndImageView) {
        finish();
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.BELOW, R.id.titlebar);
        if (rootview != null)
            rootview.addView(view, lp);
    }

    /**
     * TitleBar是否显示，默认显示
     *
     * @param isVisible 是否显示
     */
    protected void toggleTitleBarVisible(boolean isVisible) {
        titlebar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    /**
     * TitleBar左上是否显示，默认显示
     *
     * @param isVisible 是否显示
     */
    protected void toggleTitleBarLeftVisible(boolean isVisible) {
        titlebar.getleft().setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 获取TitleBar实例
     *
     * @return TitleBar
     */
    public TitleBar getTitleBar() {
        return titlebar;
    }

    /**
     * 设置标题
     *
     * @param text 文本
     */
    public void setTitle(CharSequence text) {
        titlebar.getmid().setText(text);
    }

    /**
     * 设置标题
     *
     * @param resid 文本ID
     */
    public void setTitle(int resid) {
        titlebar.getmid().setText(resid);
    }

    public final <E extends View> E getViewById(int id) {
        return (E) findViewById(id);
    }

    /**
     * 简化Toast
     *
     * @param text 文本
     */
    protected void toast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 简化Toast
     *
     * @param resId 文本ID
     */
    protected void toast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取屏幕高度
     *
     * @return int
     */
    protected int getScreenHeight() {
        return windowManager.getDefaultDisplay().getHeight();
    }

    /**
     * 获取屏幕宽度
     *
     * @return int
     */
    protected int getScreenWidth() {
        return windowManager.getDefaultDisplay().getWidth();
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
     * 简化跳转
     *
     * @param context 上下文
     * @param cls     class
     */
    protected void startActivity(Context context, Class<?> cls) {
        startActivity(new Intent(context, cls));
    }
}
