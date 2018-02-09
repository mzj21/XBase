package com.xing.xbase;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xing.xbase.util.LogUtil;
import com.xing.xbase.widget.TextAndImageView;
import com.xing.xbase.widget.TitleBar;

import java.lang.reflect.Method;

public class ActivityBase extends AppCompatActivity {
    private WindowManager windowManager;
    private TitleBar titlebar;
    private RelativeLayout baseview;
    private RelativeLayout rootview;
    private FrameLayout fragmentview;
    private RelativeLayout bottomview;
    private View navigationbarview;
    private View addbottomview;
    private ProgressDialog progressDialog;
    private RelativeLayout.LayoutParams lp_bottom;
    private RelativeLayout.LayoutParams lp_root;
    private int bottomHeight;
    private FragmentTransaction transaction;
    private Fragment from;

    @SuppressLint("ClickableViewAccessibility")
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
        baseview = getViewById(R.id.baseview);
        rootview = getViewById(R.id.rootview);
        fragmentview = getViewById(R.id.fragmentview);
        titlebar = getViewById(R.id.titlebar);
        titlebar.getleft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickTitleBarLeft(titlebar.getleft());
            }
        });
        titlebar.getmid().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickTitleBarMid(titlebar.getmid());
            }
        });
        titlebar.getright().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickTitleBarRight(titlebar.getright());
            }
        });
        bottomview = getViewById(R.id.bottomview);
        navigationbarview = getViewById(R.id.navigationbarview);
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
     * TitleBar中键点击事件，可重写
     */
    protected void OnClickTitleBarMid(TextAndImageView textAndImageView) {

    }

    /**
     * TitleBar左键点击事件，可重写
     */
    protected void OnClickTitleBarLeft(TextAndImageView textAndImageView) {
        finish();
    }

    /**
     * TitleBar右键点击事件，可重写
     */
    protected void OnClickTitleBarRight(TextAndImageView textAndImageView) {

    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        if (rootview != null)
            rootview.addView(view, lp_root);
    }

    /**
     * 设置状态栏沉浸颜色
     */
    protected void setStatusBarColor(int resid) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(resid));
        }
    }

    /**
     * 设置图片沉浸状态
     */
    protected void setImmersive() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            if (checkDeviceHasNavigationBar()) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) navigationbarview.getLayoutParams();
                params.height = getVirtualBarHeigh();
                navigationbarview.setLayoutParams(params);
                navigationbarview.setBackgroundColor(getResources().getColor(R.color.black));
            }
        }
    }

    /**
     * 检测是否有虚拟按键
     *
     * @return 是否
     */
    protected boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception ignored) {
        }
        return hasNavigationBar;
    }

    /**
     * 虚拟按键高度
     *
     * @return 高度
     */
    protected int getVirtualBarHeigh() {
        int vh = 0;
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            Class c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
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
    protected void toast(final CharSequence text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 简化Toast
     *
     * @param resId 文本ID
     */
    protected void toast(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
            }
        });
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

    protected void setFragmentTransaction(Fragment to, FragmentTransaction transaction) {
        this.transaction = transaction;
    }

    protected void switchFragment(Fragment fragment) {
        if (from == fragment) {
            return;
        }
        if (!fragment.isAdded()) {
            if (from == null) {
                transaction.add(R.id.fragmentview, fragment).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).add(R.id.fragmentview, fragment).commit(); // 隐藏当前的fragment，add下一个到Activity中
            }
        } else {
            if (from == null) {
                transaction.show(fragment).commit();
            } else {
                transaction.hide(from).show(fragment).commit();
            }
        }
        from = fragment;
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
