package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 公司信息
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class CompanyInfoBean implements Serializable {
	
	private String name;
	private String id;
	private String domain;
	private long endTime;
	private long createTime;
	private String createAccountid;
	private String icon;
	private String gemCount;
	private String smsCount;
	private boolean isBuyTime;
	private String callCount;
	private boolean isEnd;
	private boolean isPay;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getCreateAccountid() {
		return createAccountid;
	}
	public void setCreateAccountid(String createAccountid) {
		this.createAccountid = createAccountid;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getGemCount() {
		return gemCount;
	}
	public void setGemCount(String gemCount) {
		this.gemCount = gemCount;
	}
	public String getSmsCount() {
		return smsCount;
	}
	public void setSmsCount(String smsCount) {
		this.smsCount = smsCount;
	}
	public boolean isBuyTime() {
		return isBuyTime;
	}
	public void setBuyTime(boolean isBuyTime) {
		this.isBuyTime = isBuyTime;
	}
	public String getCallCount() {
		return callCount;
	}
	public void setCallCount(String callCount) {
		this.callCount = callCount;
	}
	public boolean isEnd() {
		return isEnd;
	}
	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}
	public boolean isPay() {
		return isPay;
	}
	public void setPay(boolean isPay) {
		this.isPay = isPay;
	}

	
	
}
