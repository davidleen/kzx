package com.android.yijiang.kzx.bean;

import java.io.Serializable;

public class updateTips implements Serializable {
	private String en;
	private String zh;
	private String zh_CN;
	private String zh_TW;
	private String zh_HK;

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getZh() {
		return zh;
	}

	public void setZh(String zh) {
		this.zh = zh;
	}

	public String getZh_CN() {
		return zh_CN;
	}

	public void setZh_CN(String zh_CN) {
		this.zh_CN = zh_CN;
	}

	public String getZh_TW() {
		return zh_TW;
	}

	public void setZh_TW(String zh_TW) {
		this.zh_TW = zh_TW;
	}

	public String getZh_HK() {
		return zh_HK;
	}

	public void setZh_HK(String zh_HK) {
		this.zh_HK = zh_HK;
	}

}
