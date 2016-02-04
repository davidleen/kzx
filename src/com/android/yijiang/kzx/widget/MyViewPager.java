package com.android.yijiang.kzx.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onInterceptTouchEvent(arg0);

	}
}