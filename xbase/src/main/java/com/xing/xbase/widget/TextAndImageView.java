package com.xing.xbase.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xing.xbase.R;

/**
 * Created by mzj on 2016/12/23.
 */

public class TextAndImageView extends FrameLayout {
    private TextView tv;
    private ImageView iv;

    public TextAndImageView(Context context) {
        this(context, null, 0);
    }

    public TextAndImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextAndImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TextAndImageView);
        Drawable d = ta.getDrawable(R.styleable.TextAndImageView_taiv_src);
        LayoutInflater.from(context).inflate(R.layout.textandimageview, this);
        tv = findViewById(R.id.tv);
        iv = findViewById(R.id.iv);
        if (d != null) {
            iv.setImageDrawable(d);
        }
        ta.recycle();
    }

    public void setText(CharSequence text) {
        tv.setText(text);
    }

    public void setText(int resid) {
        tv.setText(resid);
    }

    public void setImageResource(int resId) {
        iv.setImageResource(resId);
    }

    public TextView getTextView() {
        return tv;
    }

    public ImageView getImageView() {
        return iv;
    }
}
