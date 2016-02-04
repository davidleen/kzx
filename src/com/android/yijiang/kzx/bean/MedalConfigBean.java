package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 勋章信息
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class MedalConfigBean implements Serializable {
	
	private String icon;
	private int id;
	private String name;
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
