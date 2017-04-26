package com.xing.xbase.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.xing.xbase.R;

/**
 * Created by mzj on 2016/12/23.
 */

public class TitleBar extends RelativeLayout {
    TextAndImageView left, mid, right;

    public TitleBar(Context context) {
        this(context, null, 0);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.titlebar, this);
        left = (TextAndImageView) findViewById(R.id.titlebar_left);
        mid = (TextAndImageView) findViewById(R.id.titlebar_mid);
        right = (TextAndImageView) findViewById(R.id.titlebar_right);
    }

    public TextAndImageView getleft() {
        return left;
    }

    public TextAndImageView getmid() {
        return mid;
    }

    public TextAndImageView getright() {
        return right;
    }
}
