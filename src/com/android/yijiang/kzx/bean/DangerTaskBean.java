package com.android.yijiang.kzx.bean;

import java.io.Serializable;
/**
 * 我的伙伴危险任务
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class DangerTaskBean implements Serializable{
	
	private String sponsorName;
	private String sponsor;
	private String id;
	private long endTime;
	private long createTime;
	private String title;
	
	public String getSponsorName() {
		return sponsorName;
	}
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	
}
