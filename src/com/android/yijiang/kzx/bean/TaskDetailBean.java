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
public class TaskDetailBean implements Serializable{
	private String acceptStandard;//验收标准
	private String attachement;//附件集合
	private int clientEndIsGood;//客户结束好评
	private String clientEndRemark;//客户结束备注
	private String companyId;//公司ID
	private String content;//任务内容
	private String copyto;//
	private long createTime;//创建时间
	private String endIsGood;//
	private String endRemark;//
	private long endTime;//结束时间
	private long completeTime;//任务结束时间
	private String executor;//执行人
	private String executorName;//执行人
	private String id;
	private String isLeaderTask;//是否上级任务
	private String isOrder;//是否标记(0:没有;1已标记)
	private String isUrgency;//是否紧急
	private String lastProcess;//最新反馈
	private String lastProcessType;//最新反馈类型
	private String passids;//过(集合)
	private String relateClient;//关联客户(0代表没有关联客户)
	private String relateClientName;//	
	private String relateClientPhone;//	
	private String relateTarget;//关联目标(0代表没有关联目标)
	private String relateTasks;//关联目标
	private String schedule;//完成进度(0-100)
	private String searchContent;//搜索内容
	private String sponsor;//发起人
	private String sponsorName;//发起人
	private long startTime;//开始时间
	private String state;//状态(0-草稿,1-执行中,2-已完成[待验收],3-已验收[待评价],4-已评价,5-已取消)
	private String subscribe;
	private String taskIds;//
	private String title;//标题
	private boolean isSelf;
	private String executorPhone;//执行人号码
	private String isDanger;//是否危险
	
	
	public long getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(long completeTime) {
		this.completeTime = completeTime;
	}
	public String getIsDanger() {
		return isDanger;
	}
	public void setIsDanger(String isDanger) {
		this.isDanger = isDanger;
	}
	public String getExecutorPhone() {
		return executorPhone;
	}
	public void setExecutorPhone(String executorPhone) {
		this.executorPhone = executorPhone;
	}
	public boolean isSelf() {
		return isSelf;
	}
	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}
	public String getIsOrder() {
		return isOrder;
	}
	public void setIsOrder(String isOrder) {
		this.isOrder = isOrder;
	}
	public String getAcceptStandard() {
		return acceptStandard;
	}
	public void setAcceptStandard(String acceptStandard) {
		this.acceptStandard = acceptStandard;
	}
	public int getClientEndIsGood() {
		return clientEndIsGood;
	}
	public void setClientEndIsGood(int clientEndIsGood) {
		this.clientEndIsGood = clientEndIsGood;
	}
	public String getClientEndRemark() {
		return clientEndRemark;
	}
	public void setClientEndRemark(String clientEndRemark) {
		this.clientEndRemark = clientEndRemark;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCopyto() {
		return copyto;
	}
	public void setCopyto(String copyto) {
		this.copyto = copyto;
	}
	public String getEndIsGood() {
		return endIsGood;
	}
	public void setEndIsGood(String endIsGood) {
		this.endIsGood = endIsGood;
	}
	public String getEndRemark() {
		return endRemark;
	}
	public void setEndRemark(String endRemark) {
		this.endRemark = endRemark;
	}
	public String getIsLeaderTask() {
		return isLeaderTask;
	}
	public void setIsLeaderTask(String isLeaderTask) {
		this.isLeaderTask = isLeaderTask;
	}
	public String getLastProcessType() {
		return lastProcessType;
	}
	public void setLastProcessType(String lastProcessType) {
		this.lastProcessType = lastProcessType;
	}
	public String getPassids() {
		return passids;
	}
	public void setPassids(String passids) {
		this.passids = passids;
	}
	public String getRelateClient() {
		return relateClient;
	}
	public void setRelateClient(String relateClient) {
		this.relateClient = relateClient;
	}
	public String getRelateClientName() {
		return relateClientName;
	}
	public void setRelateClientName(String relateClientName) {
		this.relateClientName = relateClientName;
	}
	public String getRelateClientPhone() {
		return relateClientPhone;
	}
	public void setRelateClientPhone(String relateClientPhone) {
		this.relateClientPhone = relateClientPhone;
	}
	public String getRelateTasks() {
		return relateTasks;
	}
	public void setRelateTasks(String relateTasks) {
		this.relateTasks = relateTasks;
	}
	public String getSearchContent() {
		return searchContent;
	}
	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}
	public String getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}
	public String getTaskIds() {
		return taskIds;
	}
	public void setTaskIds(String taskIds) {
		this.taskIds = taskIds;
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
	
}
