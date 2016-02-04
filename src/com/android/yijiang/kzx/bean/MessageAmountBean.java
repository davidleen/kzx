package com.android.yijiang.kzx.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 我的消息
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class MessageAmountBean implements Serializable {

	private MessageContentBean content;
	private long id;
	private long createTime;
	private int toAccountId;
	private String identify;
	private boolean state;
	private long fromAccountId;
	
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getToAccountId() {
		return toAccountId;
	}
	public void setToAccountId(int toAccountId) {
		this.toAccountId = toAccountId;
	}
	public String getIdentify() {
		return identify;
	}
	public void setIdentify(String identify) {
		this.identify = identify;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public MessageContentBean getContent() {
		return content;
	}
	public void setContent(MessageContentBean content) {
		this.content = content;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getFromAccountId() {
		return fromAccountId;
	}
	public void setFromAccountId(long fromAccountId) {
		this.fromAccountId = fromAccountId;
	}
	
}
