package com.android.yijiang.kzx.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 激励认可
 * 
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class MedalBean implements Serializable {

	private String account;
	private long companyId;
	private String companyName;
	private int completeClientTaskCount;
	private int completeLeaderTaskCount;
	private int completeTaskCount;
	private String createTime;
	private String department;
	private String email;
	private String icon;
	private String id;
	private int isLeader;
	private String leader;
	private String leaderAmount;
	private String leaderName;
	private int medalCount;
	private List<MemberMedalBean> memberMedalList;
	private String name;
	private String phone;
	private String powerId;
	private String searchContent;
	private String sortType;
	private int state;
	private int taskClientGoodCount;
	private int taskLeaderGoodCount;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public int getCompleteClientTaskCount() {
		return completeClientTaskCount;
	}
	public void setCompleteClientTaskCount(int completeClientTaskCount) {
		this.completeClientTaskCount = completeClientTaskCount;
	}
	public int getCompleteLeaderTaskCount() {
		return completeLeaderTaskCount;
	}
	public void setCompleteLeaderTaskCount(int completeLeaderTaskCount) {
		this.completeLeaderTaskCount = completeLeaderTaskCount;
	}
	public int getCompleteTaskCount() {
		return completeTaskCount;
	}
	public void setCompleteTaskCount(int completeTaskCount) {
		this.completeTaskCount = completeTaskCount;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
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
	public int getIsLeader() {
		return isLeader;
	}
	public void setIsLeader(int isLeader) {
		this.isLeader = isLeader;
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public String getLeaderAmount() {
		return leaderAmount;
	}
	public void setLeaderAmount(String leaderAmount) {
		this.leaderAmount = leaderAmount;
	}
	public String getLeaderName() {
		return leaderName;
	}
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	public int getMedalCount() {
		return medalCount;
	}
	public void setMedalCount(int medalCount) {
		this.medalCount = medalCount;
	}
	public List<MemberMedalBean> getMemberMedalList() {
		return memberMedalList;
	}
	public void setMemberMedalList(List<MemberMedalBean> memberMedalList) {
		this.memberMedalList = memberMedalList;
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
	public String getPowerId() {
		return powerId;
	}
	public void setPowerId(String powerId) {
		this.powerId = powerId;
	}
	public String getSearchContent() {
		return searchContent;
	}
	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getTaskClientGoodCount() {
		return taskClientGoodCount;
	}
	public void setTaskClientGoodCount(int taskClientGoodCount) {
		this.taskClientGoodCount = taskClientGoodCount;
	}
	public int getTaskLeaderGoodCount() {
		return taskLeaderGoodCount;
	}
	public void setTaskLeaderGoodCount(int taskLeaderGoodCount) {
		this.taskLeaderGoodCount = taskLeaderGoodCount;
	}
	
	
}
