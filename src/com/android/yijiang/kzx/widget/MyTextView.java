package com.android.yijiang.kzx.widget;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.widget.TextView;
import android.app.Activity;
import android.view.*;

public class MyTextView extends TextView {

	public MyTextView(Context context) {
		super(context);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		Layout layout = getLayout();
		if (layout != null) {
			int height = (int) FloatMath.ceil(getMaxLineHeight(this.getText().toString())) + getCompoundPaddingTop() + getCompoundPaddingBottom();
			int width = getMeasuredWidth();
			setMeasuredDimension(width, height);
		}
	}

	private float getMaxLineHeight(String str) {
		float height = 0.0f;
		float screenW = getContext().getResources().getDisplayMetrics().widthPixels;
//		float paddingLeft = ((LinearLayout) this.getParent()).getPaddingLeft();
//		float paddingReft = ((LinearLayout) this.getParent()).getPaddingRight();
		float paddingLeft=0;
		float paddingReft=0;
		// 这里具体this.getPaint()要注意使用，要看你的TextView在什么位置，这个是拿TextView父控件的Padding的，为了更准确的算出换行
		int line = (int) Math.ceil((this.getPaint().measureText(str) / (screenW - paddingLeft - paddingReft)));
		height = (this.getPaint().getFontMetrics().descent - this.getPaint().getFontMetrics().ascent) * line;
		return height;
	}
}
