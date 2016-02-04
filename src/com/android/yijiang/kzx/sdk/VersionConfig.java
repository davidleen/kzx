package com.android.yijiang.kzx.sdk;


import com.android.yijiang.kzx.R;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class VersionConfig {
	private static final String TAG = "Config";

	public static String getVerCode(Context context) {
		String verCode = "-1";
		try {
			verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode+"";
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;

	}

	public static String getAppName(Context context) {
		String verName = context.getResources().getText(R.string.app_name).toString();
		return verName;
	}
}
