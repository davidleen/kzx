package com.android.yijiang.kzx.sdk;

import java.math.BigDecimal;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.UpdateInfoBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.ui.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;

/**
 * 获取apk版本信息
 * 
 * @title com.android.tanke.bus.http.autoupdate
 * @date 2014-3-19
 * @author tanke
 */
@SuppressLint("NewApi") public class ApkVersionHelper {

	public static ProgressDialog dialog;
	public static AsyncHttpClient asyncHttpClient;
	public static Context context;

	public ApkVersionHelper(Context context) {
		this.context = context;
	}

	/**
	 * Returns the version string of the installed APK.
	 * 
	 * @param context
	 *            The activity context to access the Package Manager
	 * @param packageNamespace
	 *            The APK's namespace
	 * @return Version string
	 * @throws PackageManager.NameNotFoundException
	 */
	public static String getInstalledApkVersion(String packageNamespace){

		String installedVersion = "";
		try {
			PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(packageNamespace, 0);

			if (pInfo != null)
				installedVersion = pInfo.versionName;

		} catch (PackageManager.NameNotFoundException e) {
			Log.w("ApkUpdater", "[WARNING #900] Package name '" + packageNamespace + "'was not found.");
		} catch (Exception e) {
			Log.e("ApkUpdater", "[ERROR #900] getInstalledApkVersion() failed.");
		}

		return installedVersion;
	}

	public static void checkInstalledApkVersion(final boolean isCheckByOnclick){
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		asyncHttpClient.post(getContext(), Constants.versionAPI, rp, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				if (isCheckByOnclick) {
					dialog = new ProgressDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT);
					dialog.setMessage("检查新版本");
					dialog.setIndeterminate(true);
					dialog.setCancelable(true);
					dialog.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							asyncHttpClient.cancelRequests(context, true);
						}
					});
					dialog.show();
				}
				super.onStart();
			}
			@Override
			public void onFinish() {
				super.onFinish();
				if (isCheckByOnclick) {
					dialog.dismiss();
				}
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String content=new String(responseBody);
				Gson gson = new Gson();
				try {
					final UpdateInfoBean updateInfoBean = gson.fromJson(new JSONObject(content).optString("updateInfo"), UpdateInfoBean.class);
					String installedApkVersion = getInstalledApkVersion(getContext().getPackageName());
					if (!StringUtils.isEmpty(updateInfoBean.getVersionName()) 
							&& installedApkVersion != null 
							&& !installedApkVersion.isEmpty() 
							&& new BigDecimal(installedApkVersion).compareTo(new BigDecimal(updateInfoBean.getVersionName()))==0) {
						//installedApkVersion.equalsIgnoreCase(updateInfoBean.getVersionName())
						if (isCheckByOnclick) {
							new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage("已经是最新版本").setTitle("提示").setCancelable(false)
							.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									dialog.dismiss();
								}
							}).create().show();
						}
					} else if(!StringUtils.isEmpty(updateInfoBean.getVersionName()) 
							&& installedApkVersion != null 
							&& !installedApkVersion.isEmpty() 
							&& new BigDecimal(installedApkVersion).compareTo(new BigDecimal(updateInfoBean.getVersionName()))==-1){
						new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(updateInfoBean.getAppDescription()).setTitle("有新版本:"+updateInfoBean.getVersionName()).setCancelable(false)
								.setPositiveButton(R.string.alert_dialog_update, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										Uri uri = Uri.parse(updateInfoBean.getApkUrl());
										Intent intent = new Intent(Intent.ACTION_VIEW, uri);
										getContext().startActivity(intent);
										dialog.dismiss();
									}
								}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										dialog.dismiss();
									}
								}).create().show();
					}else{
						if (isCheckByOnclick) {
							new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage("已经是最新版本").setTitle("提示").setCancelable(false)
							.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									dialog.dismiss();
								}
							}).create().show();
						}
					}
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		ApkVersionHelper.context = context;
	}
}
