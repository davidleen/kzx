package com.android.yijiang.kzx.bean;

import java.io.Serializable;
/**
 * 成员过去的公司信息
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class PastMemberListBean implements Serializable{
	
	private String companyIcon;
	private String companyId;
	private String companyName;
	private MemberAllBean member;
	
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
	public MemberAllBean getMember() {
		return member;
	}
	public void setMember(MemberAllBean member) {
		this.member = member;
	}
	
	
}
