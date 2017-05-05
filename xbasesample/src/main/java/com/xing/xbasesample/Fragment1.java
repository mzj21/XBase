package com.xing.xbasesample;

import android.view.View;

import com.xing.xbase.FragmentBase;

/**
 * Created by mzj on 2017/5/4.
 */

public class Fragment1 extends FragmentBase {

    @Override
    protected void initView() {
        setContentView(R.layout.fragment1);
    }

    @Override
    protected void initLinster() {
        getViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getActivity(), Acvitity_1.class);
            }
        });
    }
}
