package com.android.yijiang.kzx.bean;

import java.io.Serializable;
/**
 * 关联客户
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class ClientBean implements Serializable{
	
	private String id;
	private String memberId;
	private String phone;
	private String name;
	private String companyId;
	
	public ClientBean(String id,String phone,String name){
		this.id=id;
		this.phone=phone;
		this.name=name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
}
