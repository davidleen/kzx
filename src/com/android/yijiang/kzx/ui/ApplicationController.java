package com.android.yijiang.kzx.ui;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;

import com.android.yijiang.kzx.BuildConfig;
import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.AmountInfoBean;
import com.android.yijiang.kzx.bean.KzxTokenBean;
import com.android.yijiang.kzx.bean.TaskIdsBean;
import com.android.yijiang.kzx.sdk.CustomException;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.StrictMode;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class ApplicationController extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

	public static String TAG = "ApplicationController";
	private SharedPreferences mPreferences;
	private static ApplicationController sInstance;
	public static int mCount=0;//用于判断调用次数
	public static TaskIdsBean taskIdsBean;//用于存放临时的task
	private static DisplayImageOptions options;
	
	public static TaskIdsBean getTaskIdsBean() {
		return taskIdsBean;
	}

	public static void setTaskIdsBean(TaskIdsBean taskIdsBean) {
		ApplicationController.taskIdsBean = taskIdsBean;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (Config.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		sInstance = this;
		initImageLoader(getApplicationContext());
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mPreferences.registerOnSharedPreferenceChangeListener(this);
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
		}
		//全局异常捕获
//		CustomException customException = CustomException.getInstance();
//		customException.init(getApplicationContext());
		initJpush();
	}
	
	// 初始化推送
	private void initJpush(){
		JPushInterface.setDebugMode(BuildConfig.DEBUG); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        JPushInterface.setLatestNotifactionNumber(this, 3);//设置保留最近通知条数 API
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.default_bg)
			.showImageForEmptyUri(R.drawable.default_bg)
			.showImageOnFail(R.drawable.default_bg)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
	}
	
	
	public static DisplayImageOptions getOptions() {
		return options;
	}

	public static void setOptions(DisplayImageOptions options) {
		ApplicationController.options = options;
	}

	
	public String getLoginUserCookie(){
		StringBuffer SESSIONID = new StringBuffer();
		SharedPreferences sp = getInstance().getSharedPreferences("token_info", 0);
		if(!StringUtils.isEmpty(sp.getString("ACCESS_TOKEN", ""))){
			SESSIONID.append("ACCESS_TOKEN="+sp.getString("ACCESS_TOKEN", ""));
		}
		SESSIONID.append(";");
		if(!StringUtils.isEmpty(sp.getString("ACCESS_KEY", ""))){
			SESSIONID.append("ACCESS_KEY="+sp.getString("ACCESS_KEY", ""));
		}
		return SESSIONID.toString();
	}
	
	public String getACCESS_KEY(){
		StringBuffer SESSIONID = new StringBuffer();
		SharedPreferences sp = getInstance().getSharedPreferences("token_info", 0);
		if(!StringUtils.isEmpty(sp.getString("ACCESS_KEY", ""))){
			SESSIONID.append(sp.getString("ACCESS_KEY", ""));
		}
		return SESSIONID.toString();
	}

	public static synchronized ApplicationController getInstance() {
		return sInstance;
	}
	
	public KzxTokenBean getKzxTokenBean(){
		KzxTokenBean kzxTokenBean=new KzxTokenBean();
		SharedPreferences sp = getInstance().getSharedPreferences("kzx_token_info", 0);
		if(!StringUtils.isEmpty(sp.getString("memberId",""))){
			kzxTokenBean.setAccountName(sp.getString("accountName",""));
			kzxTokenBean.setCompanyName(sp.getString("companyName",""));
			kzxTokenBean.setGrowUp(sp.getInt("growUp",0));
			kzxTokenBean.setMedalCount(sp.getInt("medalCount",0));
			kzxTokenBean.setMemberIcon(sp.getString("memberIcon",""));
			kzxTokenBean.setMemberId(sp.getString("memberId",""));
			kzxTokenBean.setEncryptMemberId(sp.getString("encryptMemberId",""));
			kzxTokenBean.setMemberName(sp.getString("memberName",""));
			kzxTokenBean.setPhone(sp.getString("phone",""));
			kzxTokenBean.setEmail(sp.getString("email",""));
			kzxTokenBean.setDepartment(sp.getString("department",""));
			kzxTokenBean.setLeader(sp.getString("leader",""));
			kzxTokenBean.setLeaderName(sp.getString("leaderName",""));
			kzxTokenBean.setClientIds(sp.getString("clientIds","[]"));
			kzxTokenBean.setAccountId(sp.getString("accountId", ""));
			kzxTokenBean.setEncryptClientIds(sp.getString("encryptClientIds","[]"));
			kzxTokenBean.setNoReadMessageNum(sp.getString("noReadMessageNum", "0"));
			kzxTokenBean.setIsSendSms(sp.getInt("isSendSms", 1));
		}
		return kzxTokenBean;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String key) {
		Log.d(TAG, "shared prefs " + key + " has got changed!");
	}

	public SharedPreferences getPreferences() {
		return mPreferences;
	}
	

}