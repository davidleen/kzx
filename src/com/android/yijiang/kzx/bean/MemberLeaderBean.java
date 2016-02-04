package com.android.yijiang.kzx.bean;

import java.io.Serializable;
/**
 * 领导好评
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class MemberLeaderBean implements Serializable{
	
	private String id;
	private int endIsGood;
	private String endIsGoodJSON;
	private String leaderIcon;
	private String leaderName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getEndIsGood() {
		return endIsGood;
	}
	public void setEndIsGood(int endIsGood) {
		this.endIsGood = endIsGood;
	}
	public String getEndIsGoodJSON() {
		return endIsGoodJSON;
	}
	public void setEndIsGoodJSON(String endIsGoodJSON) {
		this.endIsGoodJSON = endIsGoodJSON;
	}
	public String getLeaderIcon() {
		return leaderIcon;
	}
	public void setLeaderIcon(String leaderIcon) {
		this.leaderIcon = leaderIcon;
	}
	public String getLeaderName() {
		return leaderName;
	}
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	
}
