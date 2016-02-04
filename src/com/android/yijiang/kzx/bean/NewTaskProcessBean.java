package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 最新反馈
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class NewTaskProcessBean implements Serializable {

	private String createrName;
	private String content;
	private String creater;
	private String createrIcon;
	private String type;//'99-"默认",1-"遇到问题",2-"需要延期",3-“顺利进行”,4-"抓紧处理",5-"进度如何"
	private long createTime;
	private String attachement;
	
	public String getAttachement() {
		return attachement;
	}
	public void setAttachement(String attachement) {
		this.attachement = attachement;
	}
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreaterIcon() {
		return createrIcon;
	}
	public void setCreaterIcon(String createrIcon) {
		this.createrIcon = createrIcon;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
}
