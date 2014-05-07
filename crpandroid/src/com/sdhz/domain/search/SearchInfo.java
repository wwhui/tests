package com.sdhz.domain.search;

import java.io.Serializable;

/**
 * 存储搜索信息
 *
 */
public class SearchInfo implements Serializable
{
	private String flow_name;//流程名
	private String flow_id;//流程号
	private String project_name;//项目名
	private String begin_code;//发起人姓名
	private String  begin_date;//发起时间
	private String  flow_state;//流程状态
	
	public String getFlow_name()
	{
		return flow_name;
	}
	public void setFlow_name(String flow_name)
	{
		this.flow_name = flow_name;
	}
	public String getFlow_id()
	{
		return flow_id;
	}
	public void setFlow_id(String flow_id)
	{
		this.flow_id = flow_id;
	}
	public String getProject_name()
	{
		return project_name;
	}
	public void setProject_name(String project_name)
	{
		this.project_name = project_name;
	}
	public String getBegin_code()
	{
		return begin_code;
	}
	public void setBegin_code(String begin_code)
	{
		this.begin_code = begin_code;
	}
	public String getBegin_date()
	{
		return begin_date;
	}
	public void setBegin_date(String begin_date)
	{
		this.begin_date = begin_date;
	}
	public String getFlow_state()
	{
		return flow_state;
	}
	public void setFlow_state(String flow_state)
	{
		this.flow_state = flow_state;
	}
	
}
