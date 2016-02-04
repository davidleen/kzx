package com.android.yijiang.kzx.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 我的消息
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class MessageBean implements Serializable {

	private int noReadNum;
	private String text;
	private String id;
	private long messageTime;
	private String type;
	private String icon;
	private String name;
	
	public int getNoReadNum() {
		return noReadNum;
	}
	public void setNoReadNum(int noReadNum) {
		this.noReadNum = noReadNum;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getMessageTime() {
		return messageTime;
	}
	public void setMessageTime(long messageTime) {
		this.messageTime = messageTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
