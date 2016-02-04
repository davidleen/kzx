package com.android.yijiang.kzx.bean;

import java.io.Serializable;
/**
 * 客户满意
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class MemberClientBean implements Serializable{
	
	private String id;
	private String clientName;
	private String clientPhone;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientPhone() {
		return clientPhone;
	}
	public void setClientPhone(String clientPhone) {
		this.clientPhone = clientPhone;
	}
	
}
