package com.android.yijiang.kzx.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 全体成员
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class MemberBean implements Serializable {

	private String department;
	private String email;
	private String icon;
	private String id;
	private String leaderName;
	private String name;
	private String phone;
	private int state;//状态(1-邀请中,2-已加入,3-已离职)
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getLeaderName() {
		return leaderName;
	}
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
}
