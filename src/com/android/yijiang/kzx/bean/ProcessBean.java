package com.android.yijiang.kzx.bean;

import java.io.Serializable;

public class ProcessBean implements Serializable{
	
	private String createrName;
	private String content;
	private String creater;
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}

	

}
