package com.android.yijiang.kzx.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 邀请成员
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class MemberAddBean implements Serializable {

	private String department;
	private String icon;
	private String id;
	private String name;
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
