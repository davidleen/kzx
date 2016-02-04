package com.android.yijiang.kzx.bean;

import java.io.Serializable;

public class UpdateInfoBean implements Serializable{
	private String appName;
	private String appDescription;
	private String packageName;
	private String versionCode;
	private String versionName;
	private String forceUpdate;
	private String autoUpdate;
	private String apkUrl;
	private updateTips updateTips;
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppDescription() {
		return appDescription;
	}
	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getForceUpdate() {
		return forceUpdate;
	}
	public void setForceUpdate(String forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
	public String getAutoUpdate() {
		return autoUpdate;
	}
	public void setAutoUpdate(String autoUpdate) {
		this.autoUpdate = autoUpdate;
	}
	public String getApkUrl() {
		return apkUrl;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	public updateTips getUpdateTips() {
		return updateTips;
	}
	public void setUpdateTips(updateTips updateTips) {
		this.updateTips = updateTips;
	}
	
}
