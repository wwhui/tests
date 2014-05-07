package com.sdhz.domain;

import java.io.Serializable;
/** 推送消息 */
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private int messageId;//自增长
	private int vid;//
	private int status;//状态（已读1，未读0）
	private String title;//标题
	private String content;//内容
	private String time;//时间
	private int type;//类型（1代表新闻、2代表用户、3代表日程）
	private String phone;//手机号码
	private int ru_id ;
	private int important;//重要
	private int urgency;//紧急
	private String user_options_1;
	private String user_options_2;
	private String user_options_3;//按钮
	private int user_checkbox_4;//文本框
	public String getUser_options_1() {
		return user_options_1;
	}
	public void setUser_options_1(String user_options_1) {
		this.user_options_1 = user_options_1;
	}
	public String getUser_options_2() {
		return user_options_2;
	}
	public void setUser_options_2(String user_options_2) {
		this.user_options_2 = user_options_2;
	}
	public String getUser_options_3() {
		return user_options_3;
	}
	public void setUser_options_3(String user_options_3) {
		this.user_options_3 = user_options_3;
	}
	public int getUser_checkbox_4() {
		return user_checkbox_4;
	}
	public void setUser_checkbox_4(int user_checkbox_4) {
		this.user_checkbox_4 = user_checkbox_4;
	}
	public int getImportant() {
		return important;
	}
	public void setImportant(int important) {
		this.important = important;
	}
	public int getUrgency() {
		return urgency;
	}
	public void setUrgency(int urgency) {
		this.urgency = urgency;
	}

	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public int getVid() {
		return vid;
	}
	public void setVid(int vid) {
		this.vid = vid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getRu_id() {
		return ru_id;
	}
	public void setRu_id(int ru_id) {
		this.ru_id = ru_id;
	}
}
