package com.sdhz.domain;

import java.io.Serializable;
/** 日程信息 */
public class Calendar implements Serializable {
	private long calId;
	private String account;
	private String currentDate;
	private String wholeDay;
	private String calName;
	private String calDescription;
	private String calLocation;
	private String webcalPeople;
	private String webcalPeopleId;
	private String start;
	private String end;
	private int important;

	public int getImportant() {
		return important;
	}
	public void setImportant(int important) {
		this.important = important;
	}
	public long getCalId() {
		return calId;
	}
	public void setCalId(long calId) {
		this.calId = calId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	public String getWholeDay() {
		return wholeDay;
	}
	public void setWholeDay(String wholeDay) {
		this.wholeDay = wholeDay;
	}
	public String getCalName() {
		return calName;
	}
	public void setCalName(String calName) {
		this.calName = calName;
	}
	public String getCalDescription() {
		return calDescription;
	}
	public void setCalDescription(String calDescription) {
		this.calDescription = calDescription;
	}
	public String getCalLocation() {
		return calLocation;
	}
	public void setCalLocation(String calLocation) {
		this.calLocation = calLocation;
	}
	public String getWebcalPeople() {
		return webcalPeople;
	}
	public void setWebcalPeople(String webcalPeople) {
		this.webcalPeople = webcalPeople;
	}
	public String getWebcalPeopleId() {
		return webcalPeopleId;
	}
	public void setWebcalPeopleId(String webcalPeopleId) {
		this.webcalPeopleId = webcalPeopleId;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
}
