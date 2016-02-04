package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 关联目标
 * 
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class TargetCanRelateBean implements Serializable {
	private String id;
	private String title;
	private Long createtime;
	private String description;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}
