package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 激励认可
 * 
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class MemberMedalBean implements Serializable {

	private String appraiseCount;
	private String awarder;
	private String awarderName;
	private String configId;
	private String configName;
	private long createTime;
	private String goodCount;
	private String icon;
	private String id;
	private String isCheck;
	private String memberId;
	private String ownMemberId;
	private String reason;
	private String sortType;
	private String tableSuffix;
	public String getAppraiseCount() {
		return appraiseCount;
	}
	public void setAppraiseCount(String appraiseCount) {
		this.appraiseCount = appraiseCount;
	}
	public String getAwarder() {
		return awarder;
	}
	public void setAwarder(String awarder) {
		this.awarder = awarder;
	}
	public String getAwarderName() {
		return awarderName;
	}
	public void setAwarderName(String awarderName) {
		this.awarderName = awarderName;
	}
	public String getConfigId() {
		return configId;
	}
	public void setConfigId(String configId) {
		this.configId = configId;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getGoodCount() {
		return goodCount;
	}
	public void setGoodCount(String goodCount) {
		this.goodCount = goodCount;
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
	public String getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getOwnMemberId() {
		return ownMemberId;
	}
	public void setOwnMemberId(String ownMemberId) {
		this.ownMemberId = ownMemberId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	public String getTableSuffix() {
		return tableSuffix;
	}
	public void setTableSuffix(String tableSuffix) {
		this.tableSuffix = tableSuffix;
	}

}
