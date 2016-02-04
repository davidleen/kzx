package com.android.yijiang.kzx.widget;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.view.*;

public class TextViewHeightPlus extends TextView {

	private static final String TAG = "TextView";
	private Context context;
	private int actualHeight = 0;

	public int getActualHeight() {
		return actualHeight;
	}

	public TextViewHeightPlus(Context context) {
		super(context);
		this.context = context;
	}

	public TextViewHeightPlus(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public TextViewHeightPlus(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		actualHeight = 0;

		actualHeight = (int) ((getLineCount() - 1) * getTextSize());

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
		float screenW = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
		float paddingLeft = ((LinearLayout) this.getParent()).getPaddingLeft();
		float paddingReft = ((LinearLayout) this.getParent()).getPaddingRight();
		// 这里具体this.getPaint()要注意使用，要看你的TextView在什么位置，这个是拿TextView父控件的Padding的，为了更准确的算出换行
		int line = (int) Math.ceil((this.getPaint().measureText(str) / (screenW - paddingLeft - paddingReft)));
		height = (this.getPaint().getFontMetrics().descent - this.getPaint().getFontMetrics().ascent) * line;
		return height;
	}
}
