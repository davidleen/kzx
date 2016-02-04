package com.android.yijiang.kzx.bean;

import java.io.Serializable;
/**
 * 验证信息
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class KzxTokenBean implements Serializable{
	
	private int medalCount;//勋章数
	private String accountName;//账号
	private String accountId;//账号ID
	private String memberIcon;//头像
	private int growUp;//等级
	private String memberId;
	private String encryptMemberId;
	private String encryptClientIds;
	private String memberName;
	private String companyName;
	private String phone;//电话
	private String email;//邮箱
	private String department;//部门
	private String leader;//领导
	private String leaderName;//领导	
	private String messageBox;
	private String clientIds;
	private String noReadMessageNum;
	private int isSendSms;//0:未打开,1:打开
	
	public int getIsSendSms() {
		return isSendSms;
	}
	public void setIsSendSms(int isSendSms) {
		this.isSendSms = isSendSms;
	}
	public String getNoReadMessageNum() {
		return noReadMessageNum;
	}
	public void setNoReadMessageNum(String noReadMessageNum) {
		this.noReadMessageNum = noReadMessageNum;
	}
	public String getEncryptClientIds() {
		return encryptClientIds;
	}
	public void setEncryptClientIds(String encryptClientIds) {
		this.encryptClientIds = encryptClientIds;
	}
	public String getEncryptMemberId() {
		return encryptMemberId;
	}
	public void setEncryptMemberId(String encryptMemberId) {
		this.encryptMemberId = encryptMemberId;
	}
	public String getClientIds() {
		return clientIds;
	}
	public void setClientIds(String clientIds) {
		this.clientIds = clientIds;
	}
	public String getMessageBox() {
		return messageBox;
	}
	public void setMessageBox(String messageBox) {
		this.messageBox = messageBox;
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
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public String getLeaderName() {
		return leaderName;
	}
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public int getMedalCount() {
		return medalCount;
	}
	public void setMedalCount(int medalCount) {
		this.medalCount = medalCount;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getMemberIcon() {
		return memberIcon;
	}
	public void setMemberIcon(String memberIcon) {
		this.memberIcon = memberIcon;
	}
	public int getGrowUp() {
		return growUp;
	}
	public void setGrowUp(int growUp) {
		this.growUp = growUp;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
}
