package com.android.yijiang.kzx.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 我的消息
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class MessageContentBean implements Serializable {

	private String action;
	private String actionParam;
	private String actionLabel;
	private String text;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getActionParam() {
		return actionParam;
	}
	public void setActionParam(String actionParam) {
		this.actionParam = actionParam;
	}
	public String getActionLabel() {
		return actionLabel;
	}
	public void setActionLabel(String actionLabel) {
		this.actionLabel = actionLabel;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
