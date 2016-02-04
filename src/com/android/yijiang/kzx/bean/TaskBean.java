package com.android.yijiang.kzx.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class TaskBean implements Serializable{
	private String relateTarget;//关联目标(0代表没有关联目标)
	private long createTime;//创建时间
	private String executor;//执行人
	private String state;//状态(0-草稿,1-执行中,2-已完成[待验收],3-已验收[待评价],4-已评价,5-已取消)
	private String isUrgency;//是否紧急
	private String sponsor;//发起人
	private long endTime;//结束时间
	private String isDanger;//是否危险
	private String id;
	private int isOrder;//是否关注(1:关注)
	private String content;//任务内容
	private long startTime;//开始时间
	private String attachement;//附件集合
	private String title;//标题
	private String schedule;//完成进度(0-100)
	private String lastProcess;//最新反馈
	private String lastProcessCreaterName;
	private String sponsorName;//发起人
	private String executorName;//执行人
	private String executorPhone;//执行人号码
	private String executorIcon;//执行人头像
	private String companyId;
	private String sponsorIcon;//发起人头像
	private int clientEndIsGood;//领导好评
	private String relateClient;//关联客户
	private long completeTime;//任务结束时间
	private boolean isSelf;//是否是发起人
	private boolean isGuo=true;//是否是过
	
	
	public long getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(long completeTime) {
		this.completeTime = completeTime;
	}
	public String getRelateClient() {
		return relateClient;
	}
	public void setRelateClient(String relateClient) {
		this.relateClient = relateClient;
	}
	public int getIsOrder() {
		return isOrder;
	}
	public void setIsOrder(int isOrder) {
		this.isOrder = isOrder;
	}
	public int getClientEndIsGood() {
		return clientEndIsGood;
	}
	public void setClientEndIsGood(int clientEndIsGood) {
		this.clientEndIsGood = clientEndIsGood;
	}
	public String getExecutorPhone() {
		return executorPhone;
	}
	public void setExecutorPhone(String executorPhone) {
		this.executorPhone = executorPhone;
	}
	public boolean isGuo() {
		return isGuo;
	}
	public void setGuo(boolean isGuo) {
		this.isGuo = isGuo;
	}
	public boolean isSelf() {
		return isSelf;
	}
	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}
	public String getExecutorIcon() {
		return executorIcon;
	}
	public void setExecutorIcon(String executorIcon) {
		this.executorIcon = executorIcon;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getRelateTarget() {
		return relateTarget;
	}
	public void setRelateTarget(String relateTarget) {
		this.relateTarget = relateTarget;
	}
	public String getExecutor() {
		return executor;
	}
	public void setExecutor(String executor) {
		this.executor = executor;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIsUrgency() {
		return isUrgency;
	}
	public void setIsUrgency(String isUrgency) {
		this.isUrgency = isUrgency;
	}
	public String getIsDanger() {
		return isDanger;
	}
	public void setIsDanger(String isDanger) {
		this.isDanger = isDanger;
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
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public String getLastProcess() {
		return lastProcess;
	}
	public void setLastProcess(String lastProcess) {
		this.lastProcess = lastProcess;
	}
	public String getSponsorName() {
		return sponsorName;
	}
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}
	public String getExecutorName() {
		return executorName;
	}
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	public String getSponsorIcon() {
		return sponsorIcon;
	}
	public void setSponsorIcon(String sponsorIcon) {
		this.sponsorIcon = sponsorIcon;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public String getLastProcessCreaterName() {
		return lastProcessCreaterName;
	}
	public void setLastProcessCreaterName(String lastProcessCreaterName) {
		this.lastProcessCreaterName = lastProcessCreaterName;
	}
	
}
