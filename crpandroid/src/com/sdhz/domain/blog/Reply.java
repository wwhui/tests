package com.sdhz.domain.blog;

import java.io.Serializable;

public class Reply implements Serializable{
	private String reply_id;//����  �ظ�id
	private String reply_operator;//�ظ�����
	private String reply_username;//�ظ�������
	private String reply_content;//�ظ�����
	private String reply_date;//�ظ�����
	private String soure_operator;//��Դ������id
	private String blog_id;//��Դ����id
	
	public String getReply_id() {
		return reply_id;
	}
	public void setReply_id(String reply_id) {
		this.reply_id = reply_id;
	}
	public String getReply_operator() {
		return reply_operator;
	}
	public void setReply_operator(String reply_operator) {
		this.reply_operator = reply_operator;
	}
	public String getReply_content() {
		return reply_content;
	}
	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}
	public String getReply_date() {
		return reply_date;
	}
	public void setReply_date(String reply_date) {
		this.reply_date = reply_date;
	}
	public String getSoure_operator() {
		if(!"null".equals(soure_operator)&&soure_operator!=null){
			return soure_operator;
		}
		return "";
	}
	public void setSoure_operator(String soure_operator) {
		this.soure_operator = soure_operator;
	}
	public String getBlog_id() {
		return blog_id;
	}
	public void setBlog_id(String blog_id) {
		this.blog_id = blog_id;
	}
	
	
	public String getReply_username() {
		return reply_username;
	}
	public void setReply_username(String reply_username) {
		this.reply_username = reply_username;
	}
	@Override
	public String toString() {
		return "Reply ��Ϣ[reply_id=" + reply_id + ", reply_operator="
				+ reply_operator + ", reply_content=" + reply_content
				+ ", reply_date=" + reply_date + ", soure_operator="
				+ soure_operator + ", blog_id=" + blog_id + "]";
	}
	
	
	
	
	
}
