package com.android.yijiang.kzx.widget;

//继承重写seekbar代码以获取thumb的对象
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class MySeekBar extends SeekBar {
	Drawable mThumb;

	public MySeekBar(Context context) {
		super(context);
	}

	public MySeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MySeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void setThumb(Drawable thumb) {
		super.setThumb(thumb);
		mThumb = thumb;
	}

	public Drawable getSeekBarThumb() {
		return mThumb;
	}
	
}