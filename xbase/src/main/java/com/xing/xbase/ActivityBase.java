package com.xing.xbase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xing.xbase.util.LogUtil;
import com.xing.xbase.widget.TextAndImageView;
import com.xing.xbase.widget.TitleBar;

public class ActivityBase extends AppCompatActivity {
    private WindowManager windowManager;
    private TitleBar titlebar;
    private RelativeLayout rootview;
    private FrameLayout fragmentview;
    private RelativeLayout bottomview;
    private View addbottomview;
    private ProgressDialog progressDialog;
    private RelativeLayout.LayoutParams lp_bottom;
    private RelativeLayout.LayoutParams lp_root;
    private int bottomHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        lp_root = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        initBaseView();
        initView();
        lp_bottom = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, bottomHeight);
        if (addbottomview != null) {
            bottomview.addView(addbottomview, lp_bottom);
            fragmentview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        } else {
            fragmentview.setVisibility(View.GONE);
        }
        initLinster();
        initDatas();
    }

    private void initBaseView() {
        rootview = getViewById(R.id.rootview);
        fragmentview = getViewById(R.id.fragmentview);
        titlebar = getViewById(R.id.titlebar);
        titlebar.getleft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickTitleBarLeft(titlebar.getleft());
            }
        });
        bottomview = getViewById(R.id.bottomview);
        progressDialog = new ProgressDialog(this);
    }

    /**
     * 初始化底部View
     */
    protected void initBottomView(View view, int height) {
        addbottomview = view;
        bottomHeight = height;
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
        if (rootview != null)
            rootview.addView(view, lp_root);
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

    /**
     * LOG打印
     *
     * @param s 内容
     */
    protected void log(String s) {
        LogUtil.e(s);
    }

    protected void addFragment(Fragment targetFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentview, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    //移除fragment
    protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    //返回键返回事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
