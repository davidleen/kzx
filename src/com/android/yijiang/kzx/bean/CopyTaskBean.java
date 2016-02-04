package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 抄送信息
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class CopyTaskBean implements Serializable {
	
	private String memberId;
	private String memberName;
	private String taskId;
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
}
