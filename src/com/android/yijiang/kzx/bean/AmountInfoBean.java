package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 账户信息
 * 
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class AmountInfoBean implements Serializable {
	
	private String accountId;
	private String accountKey;
	private String companyId;
	private String companyName;
	private String domain;
	private String growUp;
	private String leaderId;
	private String medalCount;
	private String memberIcon;
	private String memberId;
	private String memberName;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(String accountKey) {
		this.accountKey = accountKey;
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

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getGrowUp() {
		return growUp;
	}

	public void setGrowUp(String growUp) {
		this.growUp = growUp;
	}

	public String getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}

	public String getMedalCount() {
		return medalCount;
	}

	public void setMedalCount(String medalCount) {
		this.medalCount = medalCount;
	}

	public String getMemberIcon() {
		return memberIcon;
	}

	public void setMemberIcon(String memberIcon) {
		this.memberIcon = memberIcon;
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

}
