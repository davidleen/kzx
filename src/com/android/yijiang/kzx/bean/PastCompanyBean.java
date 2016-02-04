package com.android.yijiang.kzx.bean;

import java.io.Serializable;
/**
 * 成员过去的公司信息
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class PastCompanyBean implements Serializable{
	
	private String companyIcon;
	private String companyId;
	private String memberId;
	private String companyName;
	private String companyAmount;
	private String completeTaskAmount;
	private String completeClientTaskAmount;
	
	public PastCompanyBean(String companyIcon,String companyId,String memberId,String companyName,String companyAmount,String completeTaskAmount,String completeClientTaskAmount){
		this.companyIcon=companyIcon;
		this.companyId=companyId;
		this.memberId=memberId;
		this.companyName=companyName;
		this.companyAmount=companyAmount;
		this.completeTaskAmount=completeTaskAmount;
		this.completeClientTaskAmount=completeClientTaskAmount;
	}
	
	
	public String getCompleteTaskAmount() {
		return completeTaskAmount;
	}


	public void setCompleteTaskAmount(String completeTaskAmount) {
		this.completeTaskAmount = completeTaskAmount;
	}


	public String getCompleteClientTaskAmount() {
		return completeClientTaskAmount;
	}


	public void setCompleteClientTaskAmount(String completeClientTaskAmount) {
		this.completeClientTaskAmount = completeClientTaskAmount;
	}


	public String getMemberId() {
		return memberId;
	}


	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}


	public String getCompanyAmount() {
		return companyAmount;
	}

	public void setCompanyAmount(String companyAmount) {
		this.companyAmount = companyAmount;
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
	
}
