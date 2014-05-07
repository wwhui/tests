package com.sdhz.domain.blog;

import java.io.Serializable;
import java.util.List;

public class Blog  implements Serializable  {
	private String blog_id;//博文id
	private String send_operator;//发表人工号
	private String send_username;//发表人姓名
	private String send_date;//发表日期
	private String send_content;//内容
	private String flow_id;//流程ID
	private String route_id;//路由id
	private List<Reply> replyList;//评论
	private int type;//类型 博文类型  1、路由博文  2、流程博文 3、其他博文
	private int is_redict;//是否转发 
	private String fileName;
	
	public String getFileName() {
		if(fileName!=null&&!"null".equals(fileName))
				return fileName;
		return "";
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getBlog_id() {
		return blog_id;
	}
	public void setBlog_id(String blog_id) {
		this.blog_id = blog_id;
	}
	public String getSend_operator() {
		return send_operator;
	}
	public void setSend_operator(String send_operator) {
		this.send_operator = send_operator;
	}
	public String getSend_username() {
		return send_username;
	}
	public void setSend_username(String send_username) {
		this.send_username = send_username;
	}
	public String getSend_date() {
		return send_date;
	}
	public void setSend_date(String send_date) {
		this.send_date = send_date;
	}
	public String getSend_content() {
		if(!"null".equals(send_content)&&send_content!=null&&!"".equals(send_content)){
			return send_content;
		}
		return "";
	}
	public void setSend_content(String send_content) {
		this.send_content = send_content;
	}
	public String getFlow_id() {
		if(flow_id!=null&&!"".equals(flow_id)&&!"null".equals(flow_id)){
			return flow_id;
		}
		return "";
	}
	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}
	public String getRoute_id() {
		if(route_id!=null&&!"".equals(route_id)){
			return route_id;
		}
		return "";
	}
	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}
	public List getReplyList() {
		return replyList;
	}
	public void setReplyList(List replyList) {
		this.replyList = replyList;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIs_redict() {
		return is_redict;
	}
	public void setIs_redict(int is_redict) {
		this.is_redict = is_redict;
	}
	@Override
	public String toString() {
		return "Blog信息 [blog_id=" + blog_id + ", send_operator=" + send_operator
				+ ", send_username=" + send_username + ", send_date="
				+ send_date + ", send_content=" + send_content + ", flow_id="
				+ flow_id + ", route_id=" + route_id + ", replyList="
				+ replyList + ", type=" + type + ", is_redict=" + is_redict
				+ "]";
	}
	
	
	
	
}
