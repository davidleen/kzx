package com.android.yijiang.kzx.bean;

import java.io.Serializable;
/**
 * 验收标准
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class AcceptanceBean implements Serializable{
	
	private String name;
	private String complete;
	
	public AcceptanceBean(String name,String complete){
		this.name=name;
		this.complete=complete;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComplete() {
		return complete;
	}
	public void setComplete(String complete) {
		this.complete = complete;
	}
	
}
