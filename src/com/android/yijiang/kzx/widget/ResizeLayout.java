package com.android.yijiang.kzx.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class ResizeLayout extends LinearLayout {
	private OnResizeListener mListener;

	private int t1;

	public interface OnResizeListener {
		void OnResize(boolean isShowed);
	}

	public void setOnResizeListener(OnResizeListener l) {
		mListener = l;
	}

	//
	// public ResizeLayout(Context context, AttributeSet attrs) {
	// super(context, attrs);
	// }
	//
	// @Override
	// protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	// super.onSizeChanged(w, h, oldw, oldh);
	//
	// if (mListener != null) {
	// mListener.OnResize(w, h, oldw, oldh);
	// }
	// }
	private static int count = 0;

	public ResizeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (this.t1 == 0) {
			this.t1 = t;
		}
		if (mListener != null) {
			mListener.OnResize(this.t1 == t ? false : true);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}
}