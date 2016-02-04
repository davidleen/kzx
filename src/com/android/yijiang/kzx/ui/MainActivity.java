package com.android.yijiang.kzx.ui;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.R.anim;
import com.android.yijiang.kzx.R.drawable;
import com.android.yijiang.kzx.R.id;
import com.android.yijiang.kzx.R.layout;
import com.android.yijiang.kzx.R.string;
import com.android.yijiang.kzx.bean.VersionsUpdate;
import com.android.yijiang.kzx.fragment.KzxBenchResultBatchFragment;
import com.android.yijiang.kzx.fragment.KzxIncentiveFragment;
import com.android.yijiang.kzx.fragment.KzxMemberResultFragment;
import com.android.yijiang.kzx.fragment.KzxNoticeFragment;
import com.android.yijiang.kzx.fragment.KzxPartnerFragment;
import com.android.yijiang.kzx.fragment.KzxServiceResultFragment;
import com.android.yijiang.kzx.fragment.KzxSettingsFragment;
import com.android.yijiang.kzx.fragment.KzxStrategicFragment;
import com.android.yijiang.kzx.fragment.TestFragment;
import com.android.yijiang.kzx.fragment.KzxDrawerFragment;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.ApkVersionHelper;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.RegexUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.sdk.SystemBarTintManager;
import com.android.yijiang.kzx.transformer.CardTransformer;
import com.android.yijiang.kzx.transformer.DepthPageTransformer;
import com.android.yijiang.kzx.transformer.ZoomOutPageTransformer;
import com.android.yijiang.kzx.widget.HorizontalListView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
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
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

/**
 * 首页
 * 
 * @title com.android.tanke.bus
 * @date 2014-3-19
 * @author tanke
 */
@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {

	private FragmentManager mFragmentManager = null;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private MessageReceiver mMessageReceiver;

	private String action;// 接收推送来的通知

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kzx_home);
		AppManager.getAppManager().addActivity(this);

		action = getIntent().getStringExtra("action");

		// 设置侧滑菜单信息
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerLayout.setScrimColor(Color.argb(100, 0, 0, 0));
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer_white, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				Intent mIntent=new Intent(getPackageName()+".DRAWER_RECEIVED_ACTION");
				mIntent.putExtra("action", "clear_focus");
				sendBroadcast(mIntent);
			}
			public void onDrawerOpened(View drawerView) {
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// 设置页面
			if ("task".equals(action)) {
				addFragmentContent(KzxBenchResultBatchFragment.newInstance(), "workbench");
			} else if ("medal".equals(action)) {
				addFragmentContent(KzxIncentiveFragment.newInstance(), "incentive");
			} else if ("notice".equals(action)) {
				addFragmentContent(KzxNoticeFragment.newInstance(), "notice");
			} else if ("target".equals(action)) {
				addFragmentContent(KzxStrategicFragment.newInstance(), "strategic");
			} else {
				addFragmentContent(KzxBenchResultBatchFragment.newInstance(), "workbench");
			}
			// 设置菜单
			addFragmentContent(KzxDrawerFragment.newInstance(), "drawer", R.id.left_drawer);
		}
		// 注册回调广播
		registerMessageReceiver();
		// 初始化推送(避免未进入主界面就接受到推送)
		initJpush();
	}

	// 添加新的视图
	private void addFragmentContent(Fragment fragment, String tag) {
		if (null == mFragmentManager) {
			mFragmentManager = getSupportFragmentManager();
			mFragmentManager.addOnBackStackChangedListener(new OnBackStackChangedListener() {
				public void onBackStackChanged() {
					Fragment fragment = mFragmentManager.findFragmentById(R.id.container);
					Intent mIntent = new Intent(getPackageName() + ".DRAWER_RECEIVED_ACTION");
					if (fragment instanceof KzxBenchResultBatchFragment) {
						mIntent.putExtra("action", "workbench");
					} else if (fragment instanceof KzxPartnerFragment) {
						mIntent.putExtra("action", "partner");
					} else if (fragment instanceof KzxStrategicFragment) {
						mIntent.putExtra("action", "strategic");
					} else if (fragment instanceof KzxServiceResultFragment) {
						mIntent.putExtra("action", "service");
					} else if (fragment instanceof KzxMemberResultFragment) {
						mIntent.putExtra("action", "member");
					} else if (fragment instanceof KzxIncentiveFragment) {
						mIntent.putExtra("action", "incentive");
					} else if (fragment instanceof KzxNoticeFragment) {
						mIntent.putExtra("action", "notice");
					}else if (fragment instanceof KzxSettingsFragment) {
						mIntent.putExtra("action", "settings");
					}
					sendBroadcast(mIntent);
				}
			});
		}
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.container, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commitAllowingStateLoss();
	}

	private void addFragmentContent(Fragment fragment, String tag, int layout) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(layout, fragment, tag);
		fragmentTransaction.commitAllowingStateLoss();
	}

	// 移除视图
	private void removeFragment() {
		if (null == mFragmentManager) {
			mFragmentManager = getSupportFragmentManager();
		}
		mFragmentManager.popBackStack();
	}

	// 注册广播
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(getPackageName() + ".HOME_RECEIVED_ACTION");
		registerReceiver(mMessageReceiver, filter);
	}

	// 初始化推送+新版本更新
	private void initJpush(){
		if(getSharedPreferences("push_info", 0).getBoolean("push", true)){
        	JPushInterface.resumePush(this);
        }
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ApkVersionHelper apkVersionHelper=new ApkVersionHelper(MainActivity.this);
				apkVersionHelper.checkInstalledApkVersion(false);
			}
		}, 1000);
	}
	
	// 团队创建成功引导
	private void inviteTipMessage(){
		new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setIcon(null)
		.setMessage(R.string.create_team_success)
		.setTitle(R.string.prompt)
		.setPositiveButton(R.string.create_team_invite_success, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent mIntent = new Intent(MainActivity.this, ContentFragmentActivity.class);
				mIntent.putExtra("action", "invite");
				startActivity(mIntent);
				dialog.dismiss();
			}
		}).create().show();
	}

	// 回调广播
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ((getPackageName() + ".HOME_RECEIVED_ACTION").equals(intent.getAction())) {
				String drawerAction = intent.getStringExtra("action");
				String drawerTitle = intent.getStringExtra("title");
				// 侧滑菜单开关
				if ("close_drawer".equals(drawerAction)) {
					if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
						mDrawerLayout.closeDrawer(Gravity.LEFT);
					} else {
						mDrawerLayout.openDrawer(Gravity.LEFT);
					}
					return;
				}
				if ("partner".equals(drawerAction)) {
					// 我的伙伴
					addFragmentContent(KzxPartnerFragment.newInstance(), "partner");
				} else if ("workbench".equals(drawerAction)) {
					// 工作台
					addFragmentContent(KzxBenchResultBatchFragment.newInstance(), "workbench");
				} else if ("strategic".equals(drawerAction)) {
					// 战略目标
					addFragmentContent(KzxStrategicFragment.newInstance(), "strategic");
				} else if ("service".equals(drawerAction)) {
					// 服务商
					addFragmentContent(KzxServiceResultFragment.newInstance(), "service");
				} else if ("member".equals(drawerAction)) {
					// 全体成员
					addFragmentContent(KzxMemberResultFragment.newInstance(), "member");
				} else if ("incentive".equals(drawerAction)) {
					// 激励认可
					addFragmentContent(KzxIncentiveFragment.newInstance(), "incentive");
				} else if ("notice".equals(drawerAction)) {
					// 团队公告
					addFragmentContent(KzxNoticeFragment.newInstance(), "notice");
				} else if("settings".equals(drawerAction)){
					addFragmentContent(KzxSettingsFragment.newInstance(),"settings");
				}
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			}
		}
	}

	public DrawerLayout getmDrawerLayout() {
		return mDrawerLayout;
	}

	public void setmDrawerLayout(DrawerLayout mDrawerLayout) {
		this.mDrawerLayout = mDrawerLayout;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Fragment fragment = (Fragment) mFragmentManager.findFragmentById(R.id.container);
		if (fragment != null) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
		final SharedPreferences sp = getSharedPreferences("invite_tip_info", 0);
		if("invite".equals(sp.getString("isInviteTip", ""))){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					inviteTipMessage();
					sp.edit().putString("isInviteTip", "isInvite").commit();
				}
			}, 500);
		}
	}

	private Fragment myFragment;
	
	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		super.onRestoreInstanceState(inState);
//		myFragment = mFragmentManager.getFragment(inState,"myfragment");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
//		mFragmentManager.putFragment(outState,"myfragment",myFragment);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	public ApplicationController getContext() {
		return ((ApplicationController) getApplicationContext());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mMessageReceiver != null)
			unregisterReceiver(mMessageReceiver);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mFragmentManager!=null&&mFragmentManager.getBackStackEntryCount() > 1) {
				removeFragment();
				return false;
			}
			new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(R.string.alert_dialog_exit).setTitle(R.string.prompt).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					AppManager.AppExit(MainActivity.this);
					dialog.dismiss();
				}
			}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			}).create().show();
		}
		return super.onKeyDown(keyCode, event);
	}

}
