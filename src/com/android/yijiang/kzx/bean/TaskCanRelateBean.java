package com.android.yijiang.kzx.bean;

import java.io.Serializable;
/**
 * 关联任务
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class TaskCanRelateBean implements Serializable{
	private String id;
	private Long createtime;
	private String title;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
