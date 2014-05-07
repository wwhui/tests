package com.sdhz.domain.group;

import java.io.Serializable;

public class TalkInfo implements Serializable
{
	private String t_id;//��������id
	private String g_id;//Ⱥ��id
	private String u_id;//�û�id
	private String talk_content;//��������
	private String name;//�û���
	private String create_date;//�����¼�
	public String getT_id()
	{
		return t_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setT_id(String t_id)
	{
		this.t_id = t_id;
	}
	public String getG_id()
	{
		return g_id;
	}
	public void setG_id(String g_id)
	{
		this.g_id = g_id;
	}
	public String getU_id()
	{
		return u_id;
	}
	
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public void setU_id(String u_id)
	{
		this.u_id = u_id;
	}
	public String getTalk_content()
	{
		if(!"null".equals(talk_content)&&!"".equals(talk_content)&&talk_content!=null){
			return talk_content;
		}
		return "";
	}
	public void setTalk_content(String talk_content)
	{
		this.talk_content = talk_content;
	}
}
