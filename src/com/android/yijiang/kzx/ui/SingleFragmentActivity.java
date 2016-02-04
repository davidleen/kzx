package com.android.yijiang.kzx.ui;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.R.anim;
import com.android.yijiang.kzx.R.color;
import com.android.yijiang.kzx.R.drawable;
import com.android.yijiang.kzx.R.id;
import com.android.yijiang.kzx.R.layout;
import com.android.yijiang.kzx.R.string;
import com.android.yijiang.kzx.bean.ServicerBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.bean.VersionsUpdate;
import com.android.yijiang.kzx.fragment.KzxAboutFragment;
import com.android.yijiang.kzx.fragment.KzxAddTaskFragment;
import com.android.yijiang.kzx.fragment.KzxCreateTeamFragment;
import com.android.yijiang.kzx.fragment.KzxCycleDoneFragment;
import com.android.yijiang.kzx.fragment.KzxGetPwdFragment;
import com.android.yijiang.kzx.fragment.KzxPartnerBatchFragment;
import com.android.yijiang.kzx.fragment.KzxRegisterFragment;
import com.android.yijiang.kzx.fragment.KzxServicerBenchResultFragment;
import com.android.yijiang.kzx.fragment.KzxSettingsFragment;
import com.android.yijiang.kzx.fragment.KzxTaskDetailInfoFragment;
import com.android.yijiang.kzx.fragment.KzxTaskProcessFragment;
import com.android.yijiang.kzx.fragment.KzxTermSettingsFragment;
import com.android.yijiang.kzx.fragment.TestFragment;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.ShareToSNS;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.sdk.SystemBarTintManager;
import com.android.yijiang.kzx.transformer.CardTransformer;
import com.android.yijiang.kzx.transformer.DepthPageTransformer;
import com.android.yijiang.kzx.transformer.ZoomOutPageTransformer;
import com.android.yijiang.kzx.widget.MsgTools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * 单例
 * 
 * @title com.android.yijiang.kzx.ui
 * @date 2014年6月12日
 * @author tanke
 */
public class SingleFragmentActivity extends FragmentActivity {

	private RelativeLayout actionBar;
	private ImageButton backBtn;
	private TextView titleTv;
	private FragmentManager mFragmentManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_frame);

		AppManager.getAppManager().addActivity(this);

		actionBar = (RelativeLayout) findViewById(R.id.actionBar);
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		titleTv = (TextView) findViewById(R.id.titleTv);

		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mFragmentManager!=null){
					if (mFragmentManager.getBackStackEntryCount() > 1) {
						removeFragment();
						return;
					}
				}
				finish();
			}
		});
		if ("about".equals(getIntent().getStringExtra("action"))) {
			actionBar.setBackgroundResource(R.drawable.action_bar_bg_settings);
			titleTv.setText(getString(R.string.about_title));
			addFragmentContent(KzxAboutFragment.newInstance());
		} else if ("term_settings".equals(getIntent().getStringExtra("action"))) {
			actionBar.setBackgroundResource(R.drawable.action_bar_bg_settings);
			titleTv.setText(getString(R.string.term_settings_title));
			addFragmentContent(KzxTermSettingsFragment.newInstance());
		} 
	}

	// 添加新的视图
	private void addFragmentContent(Fragment fragment) {
		if (null == mFragmentManager) {
			mFragmentManager = getSupportFragmentManager();
		}
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.container, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	// 移除视图
	private void removeFragment() {
		if (null == mFragmentManager) {
			mFragmentManager = getSupportFragmentManager();
		}
		mFragmentManager.popBackStack();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

	@Override
	public void onBackPressed() {
		if (mFragmentManager.getBackStackEntryCount() > 1) {
			removeFragment();
			return;
		}
		AppManager.getAppManager().finishActivity(this);
	}
}
