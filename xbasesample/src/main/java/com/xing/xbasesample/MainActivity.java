package com.xing.xbasesample;

import android.view.View;
import android.widget.RelativeLayout;

import com.xing.xbase.ActivityBase;

public class MainActivity extends ActivityBase {
    Fragment1 fragment1;
    Fragment2 fragment2;
    RelativeLayout main;
    View bottom;

    @Override
    protected void initView() {
        toggleTitleBarLeftVisible(false);
        setContentView(R.layout.activity_main);
        main = getViewById(R.id.main);
        bottom = getLayoutInflater().inflate(R.layout.bottomview, null);
        initBottomView(bottom, getResources().getDimensionPixelOffset(R.dimen._40dp));
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        addFragment(fragment1);
    }

    @Override
    protected void initLinster() {
        bottom.findViewById(R.id.f1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(fragment1);
            }
        });
        bottom.findViewById(R.id.f2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(fragment2);
            }
        });
    }
}
