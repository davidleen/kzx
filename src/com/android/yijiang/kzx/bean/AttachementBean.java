package com.android.yijiang.kzx.bean;

import java.io.Serializable;
/**
 * 附件
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class AttachementBean implements Serializable{
	private String id;
	private String name;
	private long createTime;
	private String type;
	private long size;
	private String object;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
}
