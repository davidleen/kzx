package com.android.yijiang.kzx.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 账户信息
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class AmountMemberBean implements Serializable {
	
    private String medalCount;
	private String phone;
	private String email;
	private String icon;
	private String department;
	private String growUp;
	private String name;
	private String taskClientGoodCount;
	private String taskLeaderGoodCount;
	private String completeClientTaskCount;
	private String completeLeaderTaskCount;
	private String leaderName;
	private String memberId;//null:说明可以创建团队 else 离开团队
	private String companyIcon;
	private String companyId;
	private String companyName;
	private List<PastMemberListBean> pastMemberList=new ArrayList<PastMemberListBean>();
	
	
	public String getCompleteClientTaskCount() {
		return completeClientTaskCount;
	}
	public void setCompleteClientTaskCount(String completeClientTaskCount) {
		this.completeClientTaskCount = completeClientTaskCount;
	}
	public String getCompleteLeaderTaskCount() {
		return completeLeaderTaskCount;
	}
	public void setCompleteLeaderTaskCount(String completeLeaderTaskCount) {
		this.completeLeaderTaskCount = completeLeaderTaskCount;
	}
	public String getMedalCount() {
		return medalCount;
	}
	public void setMedalCount(String medalCount) {
		this.medalCount = medalCount;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getGrowUp() {
		return growUp;
	}
	public void setGrowUp(String growUp) {
		this.growUp = growUp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTaskClientGoodCount() {
		return taskClientGoodCount;
	}
	public void setTaskClientGoodCount(String taskClientGoodCount) {
		this.taskClientGoodCount = taskClientGoodCount;
	}
	public String getTaskLeaderGoodCount() {
		return taskLeaderGoodCount;
	}
	public void setTaskLeaderGoodCount(String taskLeaderGoodCount) {
		this.taskLeaderGoodCount = taskLeaderGoodCount;
	}
	public String getLeaderName() {
		return leaderName;
	}
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getCompanyIcon() {
		return companyIcon;
	}
	public void setCompanyIcon(String companyIcon) {
		this.companyIcon = companyIcon;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public List<PastMemberListBean> getPastMemberList() {
		return pastMemberList;
	}
	public void setPastMemberList(List<PastMemberListBean> pastMemberList) {
		this.pastMemberList = pastMemberList;
	}
	
}
