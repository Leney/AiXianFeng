package com.bjzcht.lovebeequick.gui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jude.rollviewpager.RollPagerView;

import com.bjzcht.lovebeequick.R;

/**
 * 自定义添加左上角标签的RollPagerView控件
 * Created by lilijun on 2016/11/15.
 */
public class RollPagerViewLab extends RollPagerView {
    public RollPagerViewLab(Context context) {
        super(context);
    }

    public RollPagerViewLab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RollPagerViewLab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void showLab(){
        ImageView lab = new ImageView(getContext());
        lab.setImageResource(R.drawable.vip_lab);
        addView(lab);
    }
}
