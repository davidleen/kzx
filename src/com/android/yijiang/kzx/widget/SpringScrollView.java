package com.android.yijiang.kzx.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;


/**
 * 增加ScrollView控件的弹簧效果
 * @title com.aishang.group.buy.ui
 * @date 2014-2-27
 * @author tanke
 */
public class SpringScrollView extends ScrollView {
	private final String TAG = getClass().getSimpleName();
	
	private Context mContext;
	private View mChild = null;
	
	private int mInitTop = 0;	// 第一个动画View初始top位置
	private int mInitBottom;	// 第一个动画View初始bottom位置
	private int mCurrentTop;	// 第一个动画View当前top位置
	private int mCurrentBottom;	// 第一个动画View当前bottom位置
	
	private int mInitTop2 = 0;
	private int mInitBottom2 = 0;
	private int mCurrentTop2;
	private int mCurrentBottom2;
	
	private float mLastDownY = 0f;	// 触摸DOWN时记录Y坐标
	
	/** 
	 * 标记第一次进入OnTouchEvent，为了处理ScrollView手指滑动的时候
	 * 并不进入ACTION_DOWN，为了获取ACTION_MOVE之前手指按下时的坐标
	 */
	private boolean isFirst = true;			// 标记：ScrollView是否处于最顶部
	private boolean mIsTop = false;			// 标记：ScrollView是否在最顶端
	private boolean mIsDownSlide = false;	// 标记：向下滑动手势
	private final int MOVE_FACTOR = 6;		// 影响滑动速度的自定义除数因子(为了用户体验)
	
	// 移动的View
	private View mNeedMoveUpView;	//第一个移动的View
	private View mNeedMoveDownView;	// 第二个移动的View，第二个View的移动速度是第一个View速度的2倍(用户体验)
	
	public SpringScrollView(Context context) {
		super(context);
		mContext = context;
	}
	
	public SpringScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	
	public SpringScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}
	
	/**
     * Finalize inflating a view from XML. This is called as the last phase of 
     * inflation, after all child views have been added.
     */
	@Override
	public void onFinishInflate() {
		init();
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 这是因为ACTION_DOWN和子View的OnClick有冲突，ACTION_DOWN进入不了
		if (getScrollY() == 0 && isFirst) {	// 当前滚动条在最顶部且是首次进入TouchEvent事件循环
			mInitTop = mNeedMoveUpView.getTop();
			mInitBottom = mNeedMoveUpView.getBottom();
			
			mLastDownY = event.getY();	// 获得手指按下的Y坐标
			
			if (mNeedMoveDownView != null) {
				mInitTop2 = mNeedMoveDownView.getTop();
				mInitBottom2 = mNeedMoveDownView.getBottom();
			}
		}
		
		int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mIsTop = false;
			if (getScrollY() == 0) {	// 滑动到顶部了
				mLastDownY = event.getY();
				/*mLastDownX = event.getX();*/
				mInitTop = mNeedMoveUpView.getTop();
				mInitBottom = mNeedMoveUpView.getBottom();
				mIsTop = true;
				
				if (mNeedMoveDownView != null) {
					mInitTop2 = mNeedMoveDownView.getTop();
					mInitBottom2 = mNeedMoveDownView.getBottom();
				}
			}
			
			isFirst = false;
			break;
		
		case MotionEvent.ACTION_MOVE:
			int distanceY = (int) (event.getY() - mLastDownY);
			
			if (distanceY > 0 && mIsTop) {
				mIsDownSlide = true;	// 标记是向下滑动手势
				
				mCurrentTop = mInitTop + distanceY / MOVE_FACTOR;
				mCurrentBottom = mInitBottom + distanceY / MOVE_FACTOR;
				mNeedMoveUpView.layout(mNeedMoveUpView.getLeft(), mCurrentTop, 
						mNeedMoveUpView.getRight(), mCurrentBottom);
				
				mCurrentTop2 = mInitTop2 + 2 * distanceY / MOVE_FACTOR;
				mCurrentBottom2 = mInitBottom2 + 2 * distanceY / MOVE_FACTOR;
				mNeedMoveDownView.layout(mNeedMoveDownView.getLeft(), mCurrentTop2, 
						mNeedMoveDownView.getRight(), mCurrentBottom2);
			} else if (distanceY > 0 && !mIsTop) {
				mIsTop = true;
				mIsDownSlide = true;	// 标记是向下滑动手势
				
				mCurrentTop = mInitTop + distanceY / MOVE_FACTOR;
				mCurrentBottom = mInitBottom + distanceY / MOVE_FACTOR;
				mNeedMoveUpView.layout(mNeedMoveUpView.getLeft(), mCurrentTop, 
						mNeedMoveUpView.getRight(), mCurrentBottom);
				
				mCurrentTop2 = mInitTop2 + 2 * distanceY / MOVE_FACTOR;
				mCurrentBottom2 = mInitBottom2 + 2 * distanceY / MOVE_FACTOR;
				mNeedMoveDownView.layout(mNeedMoveDownView.getLeft(), mCurrentTop2, 
						mNeedMoveDownView.getRight(), mCurrentBottom2);
			} else if (distanceY <= 0 && !canScrollToUp()) {
				/**
				 * 处理由于第1个View隐藏的部分，导致ScrollView滚动。
				 * 手指向上滑动，且如果不能向上滑动，因为弹簧效果的原理是，让第一个View的顶部坐标
				 * (android:marginTop是负值)超出屏幕的顶部，且让第一个View的底部坐标(
				 * android:layout_marginBottom为负值)超过第二个View。因此有可能导致在界面上看起来
				 * 并没有超过一屏幕的View，但是却可以向上滚动，这里就是为了处理这个的，重写了判断滚动的
				 * 函数让其不滚动。
				 */
				isFirst = false;
				mIsDownSlide = false;
				return true;	//
			}
			
			isFirst = false;
			break;
		
		/*case MotionEvent.ACTION_CANCEL:*/
		case MotionEvent.ACTION_UP:
			if (mIsTop && mIsDownSlide) {
				mIsTop = false;
				mIsDownSlide = false;
				bounceToBack();
			} else if (mIsTop && !mIsDownSlide) {	// 处理超出滑动
				mIsTop = false;
				mIsDownSlide = false;
				isFirst = true;
				return true;
			}
			
			isFirst = true;
			break;
		
		default:
			break;
		}
		
		return super.onTouchEvent(event);
	}
	
	/**
	 * 获得唯一的子View，因为是继承ScrollView(ScrollView只能有一个子View或ViewGroup)
	 */
	private void init() {
		if (getChildCount() > 0) {
			mChild = getChildAt(0);	// 获得唯一的子View(一般是一个XxxLayout)
		}
	}
	
	/**
	 * 指定第二个动画View
	 */
	public void setMoveDownView(View v) {
		mNeedMoveDownView = v;
	}
	
	/**
	 * 指定第一个动画View
	 */
	public void setMoveUpView(View v) {
		mNeedMoveUpView = v;
	}
	
	/**
	 * 判断是否可以向上滚动
	 * 只有在当前View的高度大于屏幕高度的时候，才允许滚动。
	 */
	private boolean canScrollToUp() {
		if (mChild != null) {
			if (mChild.getHeight() > getResources().getDisplayMetrics().heightPixels) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 回弹动画
	 */
	private void bounceToBack() {
		/**
		 * TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
		 * float fromXDelta，这个参数表示动画开始的点离当前View X坐标上的差值；
		 * float toXDelta，这个参数表示动画结束的点离当前View X坐标上的差值；
		 * float fromYDelta，这个参数表示动画开始的点离当前View Y坐标上的差值；
		 * float toYDelta，这个参数表示动画结束的点离当前View Y坐标上的差值；
		 */
		TranslateAnimation animation = new TranslateAnimation(0, 0, mCurrentTop - mInitTop, 0);
		animation.setInterpolator(mContext, android.R.anim.decelerate_interpolator);
		animation.setDuration(200);
		
		TranslateAnimation animation2 = new TranslateAnimation(0, 0, mCurrentTop2 - mInitTop2, 0);
		animation2.setInterpolator(mContext, android.R.anim.decelerate_interpolator);
		animation2.setDuration(200);
		
		mNeedMoveUpView.startAnimation(animation);
		
		// 这是因为Animation的动画并不真正的改变View的实际坐标，仅仅是一个动画，所以需要最后设定下布局
		mNeedMoveUpView.layout(mNeedMoveUpView.getLeft(), mInitTop, mNeedMoveUpView.getRight(), mInitBottom);
		
		mNeedMoveDownView.startAnimation(animation2);
		mNeedMoveDownView.layout(mNeedMoveDownView.getLeft(), mInitTop2, mNeedMoveDownView.getRight(),
				mInitBottom2);
	}
}
