package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 账户聊天信息
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class AmountMessageBean implements Serializable {
	
	private String accountName;
	private long accountId;
	private String accountIcon;
	
	public AmountMessageBean(String accountName,long accountId,String accountIcon){
		this.accountName=accountName;
		this.accountId=accountId;
		this.accountIcon=accountIcon;
	}
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public String getAccountIcon() {
		return accountIcon;
	}
	public void setAccountIcon(String accountIcon) {
		this.accountIcon = accountIcon;
	}

	
	
}
