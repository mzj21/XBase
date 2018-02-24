package com.xing.xbase;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xing.xbase.util.LogUtil;
import com.xing.xbase.widget.TextAndImageView;
import com.xing.xbase.widget.TitleBar;

import java.lang.reflect.Field;
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
     * 设置状态栏颜色
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
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置沉浸状态
     */
    protected void setImmersive() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
     * 已知系统类型时，设置状态栏黑色文字、图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     */
    public void setStatusBar(boolean dark) {
        MIUISetStatusBarLightMode(dark);
        FlymeSetStatusBarLightMode(dark);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(dark ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    : View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     */
    public void FlymeSetStatusBarLightMode(boolean dark) {
        Window window = getWindow();
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
            } catch (Exception ignored) {

            }
        }
    }

    /**
     * 需要MIUIV6以上
     *
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     */
    public void MIUISetStatusBarLightMode(boolean dark) {
        Window window = getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception ignored) {

            }
        }
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

    protected void switchFragment(Fragment to) {
        if (from == to) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (!to.isAdded()) {
            if (from == null) {
                transaction.add(R.id.fragmentview, to).commit();
            } else {
                transaction.hide(from).add(R.id.fragmentview, to).commit();
            }
        } else {
            if (from == null) {
                transaction.show(to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
        from = to;
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
