package com.sdhz.domain.person;

import java.io.Serializable;

/**
 *存储我发起的数据
 *
 */
public class MyCreateInfo implements Serializable
{
	private String flow_id;
	private String flow_name;
	private String begin_code;
	private String begin_date;
	private String project_name;
	public String getFlow_id()
	{
		return flow_id;
	}
	public void setFlow_id(String flow_id)
	{
		this.flow_id = flow_id;
	}
	public String getFlow_name()
	{
		return flow_name;
	}
	public void setFlow_name(String flow_name)
	{
		this.flow_name = flow_name;
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
	public String getProject_name()
	{
		return project_name;
	}
	public void setProject_name(String project_name)
	{
		this.project_name = project_name;
	}
}
