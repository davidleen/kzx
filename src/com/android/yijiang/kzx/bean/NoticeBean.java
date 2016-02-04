package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 团队公告
 * 
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class NoticeBean implements Serializable {

	private String id;
	private String content;
	private String searchContent;
	private String attachement;
	private String memberId;
	private String companyId;
	private String title;
	private String creater;
	private String createrName;
	private String createrIcon;
	private long createTime;
	
	
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	public String getCreaterIcon() {
		return createrIcon;
	}
	public void setCreaterIcon(String createrIcon) {
		this.createrIcon = createrIcon;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSearchContent() {
		return searchContent;
	}
	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}
	public String getAttachement() {
		return attachement;
	}
	public void setAttachement(String attachement) {
		this.attachement = attachement;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
}
