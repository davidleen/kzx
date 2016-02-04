package com.android.yijiang.kzx.ui;


import cn.jpush.android.api.JPushInterface;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ViewFlipper;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.BinaryHttpResponseHandler;

/**
 * 启动界面
 * @title com.android.ticket.web.ui
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class SplashActivity extends FragmentActivity {

	private String TAG=SplashActivity.class.getName();

	private Animation mFadeIn;
	
	private ImageView welcome_luncher;
	private ImageView welcome_luncher_title;
	
	private Spring mScaleSpring;
	private final BaseSpringSystem mSpringSystem = SpringSystem.create();
	private final SpringListener mSpringListener = new SpringListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);

		setContentView(R.layout.kzx_splash_fragment);
		
		welcome_luncher=(ImageView) findViewById(R.id.welcome_luncher);
		welcome_luncher_title=(ImageView) findViewById(R.id.welcome_luncher_title);
		
		mScaleSpring = mSpringSystem.createSpring();
		mFadeIn = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in);
		mFadeIn.setDuration(1000);
		mFadeIn.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
				mScaleSpring.setEndValue(2);
			}
			public void onAnimationRepeat(Animation animation) {
			}
			public void onAnimationEnd(Animation animation) {
				mScaleSpring.setEndValue(0);
				welcome_luncher.postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent mIntent = null;
						if(!StringUtils.isEmpty(getContext().getACCESS_KEY())){
							mIntent = new Intent(SplashActivity.this, MainActivity.class);
							mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				        	startActivity(mIntent);
						}else{
							mIntent = new Intent(SplashActivity.this, LoginActivity.class);
							mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				        	startActivity(mIntent);
						}
					}
				}, 1000);
			}
		});
		welcome_luncher.startAnimation(mFadeIn);
		welcome_luncher_title.startAnimation(mFadeIn);
	}


	private class SpringListener extends SimpleSpringListener {
		@Override
		public void onSpringUpdate(Spring spring) {
			float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 1.0);
			float mappedValue2 = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);
			welcome_luncher.setScaleX(mappedValue);
			welcome_luncher.setScaleY(mappedValue);
			welcome_luncher_title.setScaleX(mappedValue2);
			welcome_luncher_title.setScaleY(mappedValue2);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
		mScaleSpring.addListener(mSpringListener);
	}

	@Override
	public void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
		mScaleSpring.removeListener(mSpringListener);
	}
	
	public ApplicationController getContext() {
		return ((ApplicationController) getApplicationContext());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 移除Activity
			AppManager.getAppManager().finishActivity(this);
			AppManager.AppExit(this);
		}
		return super.onKeyDown(keyCode, event);
	}

}
