package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 服务商
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class ServicerBean implements Serializable {

	private String relateClient;
	private long createTime;
	private int noReadNum;
	private int noAppraiseNum;
	private String companyId;
	private String companyIcon;
	private String companyName;
	private String taskNum;
	
	
	public String getCompanyIcon() {
		return companyIcon;
	}
	public void setCompanyIcon(String companyIcon) {
		this.companyIcon = companyIcon;
	}
	public String getRelateClient() {
		return relateClient;
	}
	public void setRelateClient(String relateClient) {
		this.relateClient = relateClient;
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
	public String getTaskNum() {
		return taskNum;
	}
	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getNoReadNum() {
		return noReadNum;
	}
	public void setNoReadNum(int noReadNum) {
		this.noReadNum = noReadNum;
	}
	public int getNoAppraiseNum() {
		return noAppraiseNum;
	}
	public void setNoAppraiseNum(int noAppraiseNum) {
		this.noAppraiseNum = noAppraiseNum;
	}
	
}
