package com.android.yijiang.kzx.bean;

import java.io.Serializable;

/**
 * 版本更新
 * @title com.education.book.bean
 * @date 2014-1-7
 * @author tanke
 */
public class VersionsUpdate implements Serializable {
	private String vu_id;
	private String vu_name;
	private String URL;
	private String versionCode;
	private String versionName;
	private String vu_content;
	private String update_date;
	private String ispublish;
	private String remark_one;
	private String remark_two;

	/**
	 * @return the vu_id
	 */
	public String getVu_id() {
		return vu_id;
	}

	/**
	 * @param vu_id
	 *            the vu_id to set
	 */
	public void setVu_id(String vu_id) {
		this.vu_id = vu_id;
	}

	/**
	 * @return the vu_name
	 */
	public String getVu_name() {
		return vu_name;
	}

	/**
	 * @param vu_name
	 *            the vu_name to set
	 */
	public void setVu_name(String vu_name) {
		this.vu_name = vu_name;
	}

	/**
	 * @return the uRL
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * @param uRL
	 *            the uRL to set
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * @return the versionCode
	 */
	public String getVersionCode() {
		return versionCode;
	}

	/**
	 * @param versionCode
	 *            the versionCode to set
	 */
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	/**
	 * @return the versionName
	 */
	public String getVersionName() {
		return versionName;
	}

	/**
	 * @param versionName
	 *            the versionName to set
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/**
	 * @return the vu_content
	 */
	public String getVu_content() {
		return vu_content;
	}

	/**
	 * @param vu_content
	 *            the vu_content to set
	 */
	public void setVu_content(String vu_content) {
		this.vu_content = vu_content;
	}

	/**
	 * @return the update_date
	 */
	public String getUpdate_date() {
		return update_date;
	}

	/**
	 * @param update_date
	 *            the update_date to set
	 */
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	/**
	 * @return the ispublish
	 */
	public String getIspublish() {
		return ispublish;
	}

	/**
	 * @param ispublish
	 *            the ispublish to set
	 */
	public void setIspublish(String ispublish) {
		this.ispublish = ispublish;
	}

	/**
	 * @return the remark_one
	 */
	public String getRemark_one() {
		return remark_one;
	}

	/**
	 * @param remark_one
	 *            the remark_one to set
	 */
	public void setRemark_one(String remark_one) {
		this.remark_one = remark_one;
	}

	/**
	 * @return the remark_two
	 */
	public String getRemark_two() {
		return remark_two;
	}

	/**
	 * @param remark_two
	 *            the remark_two to set
	 */
	public void setRemark_two(String remark_two) {
		this.remark_two = remark_two;
	}

}
