package com.sdhz.domain.task;

import java.io.Serializable;

public class TaskInfo implements Serializable{
	private String project_name;
	private String flow_name;
	private String flow_id;//发
	private String begin_code;//发起人姓名
	private String name;////发起人工号
	private String  begin_date;//发起时间
	private String item_name;//
	private String  state;//
	private String zfnum="";//转发总数
	private String plnum="";//评论总数
	private String rr;
	
	public String getZfnum() {
		if(zfnum!=null&&!"".equals(zfnum)){
		 return	zfnum;
		}
		return "0";
	}
	public void setZfnum(String zfnum) {
		this.zfnum = zfnum;
	}
	public String getPlnum() {
		if(plnum!=null&&!"".equals(plnum)){
			return plnum;
		}
		return "0";
	}
	public void setPlnum(String plnum) {
		this.plnum = plnum;
	}
	public String getFlow_name() {
		if(flow_name!=null&&!"".equals(flow_name)){
			return flow_name;
		}
		return "";
	}
	public void setFlow_name(String flow_name) {
		this.flow_name = flow_name;
	}
	public String getRr() {
		return rr;
	}
	public void setRr(String rr) {
		this.rr = rr;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getFlow_id() {
		return flow_id;
	}
	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}
	public String getBegin_code() {
		return begin_code;
	}
	public void setBegin_code(String begin_code) {
		this.begin_code = begin_code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBegin_date() {
		if(!"null".equals(begin_date)){
			return begin_date;
		}
		return "";
	}
	public void setBegin_date(String begin_date) {
		this.begin_date = begin_date;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}	
