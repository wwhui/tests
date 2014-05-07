package com.sdhz.domain.task;

import java.io.Serializable;

public class TaskRoute implements Serializable {
	private String flow_id; // 流程ID
	private String route_id;// 路由IDID
	private String receive_date;// 接收时间
	private String cur_task;// 节点名称
	private String cur_task_state;//节点处理状态0,未处理,1,已受理,2,已处理,3,驳回,4,驳回受理 5:延时
	private String cur_role;// 处理人角色
	private String cur_code;// 处理人工号
	private String commit_date;// 处理时间
	private String audit_content;// 处理意见
	private String cur_name; //处理人姓名
	private String zfNum="";//转发数
	private String plNum="";//评论数

	
	public String getZfNum() {
		if(zfNum!=null&&!"".equals(zfNum)){
			return zfNum;
		}
		return "0";
	}

	public void setZfNum(String zfNum) {
		this.zfNum = zfNum;
	}

	public String getPlNum() {
		if(plNum!=null&&!"".equals(plNum)){
			return plNum;
		}
		return "0";
	}

	public void setPlNum(String plNum) {
		this.plNum = plNum;
	}

	public String getFlow_id() {
		return flow_id;
	}

	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}

	public String getRoute_id() {
		return route_id;
	}

	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}

	public String getReceive_date() {
		if(receive_date!=null&&!"".equals(receive_date)&&!"null".equals(receive_date)){
			return receive_date;
		}
		return "";
	}

	public void setReceive_date(String receive_date) {
		this.receive_date = receive_date;
	}

	public String getCur_task() {
		return cur_task;
	}

	public void setCur_task(String cur_task) {
		this.cur_task = cur_task;
	}

	public String getCur_task_state()
	{
		if(cur_task_state!=null&&!"".equals(cur_task_state))
		{
			return cur_task_state;
		}
		return "0";
	}

	public void setCur_task_state(String cur_task_state)
	{
		this.cur_task_state = cur_task_state;
	}

	public String getCur_role() {
		return cur_role;
	}

	public void setCur_role(String cur_role) {
		this.cur_role = cur_role;
	}

	public String getCur_code() {
		return cur_code;
	}

	public void setCur_code(String cur_code) {
		this.cur_code = cur_code;
	}

	public String getCommit_date() {
		if(commit_date!=null&&!"".equals(commit_date)&&!"null".equals(commit_date)){
			return commit_date;
		}
		return "";
	}

	public void setCommit_date(String commit_date) {
		this.commit_date = commit_date;
	}

	public String getAudit_content() {
		if(audit_content!=null&&!"".equals(audit_content)&&!"null".equals(audit_content)){
			return audit_content;
		}
		return "";
	}

	public void setAudit_content(String audit_content) {
		this.audit_content = audit_content;
	}

	public String getCur_name() {
		return cur_name;
	}

	public void setCur_name(String cur_name) {
		this.cur_name = cur_name;
	}
	
	
}
