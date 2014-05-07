package com.sdhz.domain;

import java.io.Serializable;
import java.sql.Date;
/** µÇÂ½ÕÊºÅ */
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	private String account;
	private String name;
	private String password;
	private int remember;
	private Date lastLoginTime;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRemember() {
		return remember;
	}
	public void setRemember(int remember) {
		this.remember = remember;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

}
