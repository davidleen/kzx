package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 任务反馈
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class TaskProcessBean implements Serializable {

	private String content;
	private String taskId;
	private long createTime;
	private String schedule;
	private String createrName;
	private String createrIcon;
	private String attachement;
	private String creater;
	private String type;//'99-"默认",1-"遇到问题",2-"需要延期",3-“顺利进行”,4-"抓紧处理",5-"进度如何"
	private int viewType;
	private boolean isSelf;
	
	
	public boolean isSelf() {
		return isSelf;
	}
	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
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
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAttachement() {
		return attachement;
	}
	public void setAttachement(String attachement) {
		this.attachement = attachement;
	}
	public int getViewType() {
		return viewType;
	}
	public void setViewType(int viewType) {
		this.viewType = viewType;
	}
	
}
