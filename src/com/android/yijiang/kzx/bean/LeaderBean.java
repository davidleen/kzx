package com.android.yijiang.kzx.bean;

import java.io.Serializable;
/**
 * 执行人,抄送人
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class LeaderBean implements Serializable{
	private String id;
	private String department;
	private String name;
	private String icon;
	private int execCount;
	
	
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
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getExecCount() {
		return execCount;
	}
	public void setExecCount(int execCount) {
		this.execCount = execCount;
	}
	
}
