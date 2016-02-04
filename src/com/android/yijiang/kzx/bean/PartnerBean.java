package com.android.yijiang.kzx.bean;

import java.io.Serializable;
import java.util.List;
/**
 * 我的伙伴
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class PartnerBean implements Serializable{
	
	private String partnerName;
	private String icon;
	private List<DangerTaskBean> dangerJsonList;
	private int state3Count;
	private String partnerId;
	private int state1Count;
	private int state2Count;
	private String phone;
	private String targetId;
	
	
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<DangerTaskBean> getDangerJsonList() {
		return dangerJsonList;
	}
	public void setDangerJsonList(List<DangerTaskBean> dangerJsonList) {
		this.dangerJsonList = dangerJsonList;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getState3Count() {
		return state3Count;
	}
	public void setState3Count(int state3Count) {
		this.state3Count = state3Count;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public int getState1Count() {
		return state1Count;
	}
	public void setState1Count(int state1Count) {
		this.state1Count = state1Count;
	}
	public int getState2Count() {
		return state2Count;
	}
	public void setState2Count(int state2Count) {
		this.state2Count = state2Count;
	}
	
}
