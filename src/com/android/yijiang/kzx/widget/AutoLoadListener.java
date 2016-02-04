package com.android.yijiang.kzx.widget;

import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

/**
 * 原理是在AbsListView.getLastVisiblePosition() = =(AbsListView.getCount() - 1) 时，
 * 保存最后一个Item的绝对坐标，如果两次获取的绝对Y值都一样，即到底然后执行回调函数
 */
public class AutoLoadListener implements OnScrollListener {
	public interface AutoLoadCallBack {
		void execute();
	}

	private int getLastVisiblePosition = 0, lastVisiblePositionY = 0;
	private AutoLoadCallBack mCallback;

	public AutoLoadListener(AutoLoadCallBack callback) {
		this.mCallback = callback;
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			// 滚动到底部
			if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
				View v = (View) view.getChildAt(view.getChildCount() - 1);
				int[] location = new int[2];
				v.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标
				int y = location[1];

				if (view.getLastVisiblePosition() != getLastVisiblePosition && lastVisiblePositionY != y)// 第一次拖至底部
				{
					getLastVisiblePosition = view.getLastVisiblePosition();
					lastVisiblePositionY = y;
					return;
				} else if (view.getLastVisiblePosition() == getLastVisiblePosition && lastVisiblePositionY == y)// 第二次拖至底部
				{
					mCallback.execute();
				}
			}

			// 未滚动到底部，第二次拖至底部都初始化
			getLastVisiblePosition = 0;
			lastVisiblePositionY = 0;
		}
	}

	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

	}
}
