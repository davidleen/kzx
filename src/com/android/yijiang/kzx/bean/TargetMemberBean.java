package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 战略目标
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class TargetMemberBean implements Serializable {
	
	private int dutyersCount;
	private long createTime;
	private int state;//'目标状态(1-执行中,2-取消,3-已完成)'
	private String creater;
	private long endTime;
	private String createName;
	private int dangerCount;
	private String id;
	private long startTime;
	private String attachement;
	private String title;
	private int taskCount;
	private String description;
	private int execCount;
	private String completeStandard;
	private String companyId;
	private boolean isSelf;//是否是发起人
	
	
	public boolean isSelf() {
		return isSelf;
	}
	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}
	public int getDutyersCount() {
		return dutyersCount;
	}
	public void setDutyersCount(int dutyersCount) {
		this.dutyersCount = dutyersCount;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public int getDangerCount() {
		return dangerCount;
	}
	public void setDangerCount(int dangerCount) {
		this.dangerCount = dangerCount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public String getAttachement() {
		return attachement;
	}
	public void setAttachement(String attachement) {
		this.attachement = attachement;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getExecCount() {
		return execCount;
	}
	public void setExecCount(int execCount) {
		this.execCount = execCount;
	}
	public String getCompleteStandard() {
		return completeStandard;
	}
	public void setCompleteStandard(String completeStandard) {
		this.completeStandard = completeStandard;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
}
