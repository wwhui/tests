package com.sdhz.domain.task;

import java.io.Serializable;

public class TaskRoute implements Serializable {
	private String flow_id; // ����ID
	private String route_id;// ·��IDID
	private String receive_date;// ����ʱ��
	private String cur_task;// �ڵ�����
	private String cur_task_state;//�ڵ㴦��״̬0,δ����,1,������,2,�Ѵ���,3,����,4,�������� 5:��ʱ
	private String cur_role;// �����˽�ɫ
	private String cur_code;// �����˹���
	private String commit_date;// ����ʱ��
	private String audit_content;// �������
	private String cur_name; //����������
	private String zfNum="";//ת����
	private String plNum="";//������

	
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
