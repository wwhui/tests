package com.sdhz.domain.blog;

import java.io.Serializable;
import java.util.List;

public class Blog  implements Serializable  {
	private String blog_id;//����id
	private String send_operator;//�����˹���
	private String send_username;//����������
	private String send_date;//��������
	private String send_content;//����
	private String flow_id;//����ID
	private String route_id;//·��id
	private List<Reply> replyList;//����
	private int type;//���� ��������  1��·�ɲ���  2�����̲��� 3����������
	private int is_redict;//�Ƿ�ת�� 
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
		return "Blog��Ϣ [blog_id=" + blog_id + ", send_operator=" + send_operator
				+ ", send_username=" + send_username + ", send_date="
				+ send_date + ", send_content=" + send_content + ", flow_id="
				+ flow_id + ", route_id=" + route_id + ", replyList="
				+ replyList + ", type=" + type + ", is_redict=" + is_redict
				+ "]";
	}
	
	
	
	
}
