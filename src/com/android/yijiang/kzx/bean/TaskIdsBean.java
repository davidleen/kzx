package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 用于存放一个临时的进度信息
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class TaskIdsBean implements Serializable {

	private String taskId;
	private String state;
	private String schedule;
	private String typeStr;
	
	public TaskIdsBean(String taskId,String schedule,String state,String typeStr){
		this.taskId=taskId;
		this.schedule=schedule;
		this.state=state;
		this.typeStr=typeStr;
	}
	
	
	public String getTypeStr() {
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	

}
