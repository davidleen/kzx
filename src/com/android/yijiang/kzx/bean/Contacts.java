package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 通讯录
 * 
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class Contacts implements Serializable {
	private String contactId;
	private String name;
	private String phoneNumber;
	private String email;

	public Contacts(String contactId,String name,String phoneNumber,String email){
		this.contactId=contactId;
		this.name=name;
		this.phoneNumber=phoneNumber;
		this.email=email;
	}
	
	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
