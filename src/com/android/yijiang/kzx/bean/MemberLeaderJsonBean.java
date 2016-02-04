package com.android.yijiang.kzx.bean;

import java.io.Serializable;
/**
 * 领导好评
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class MemberLeaderJsonBean implements Serializable{
	
	private String appraiser;
	private String content;
	private long createtime;
	public String getAppraiser() {
		return appraiser;
	}
	public void setAppraiser(String appraiser) {
		this.appraiser = appraiser;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	
	
}
