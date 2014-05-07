package com.sdhz.domain.group;

import java.io.Serializable;

public class GroupInfo implements Serializable
{
	private String groupname;//组名
	private String state;//组状态
	private String lable;//标签
	public String getLable()
	{
		return lable;
	}

	public void setLable(String lable)
	{
		this.lable = lable;
	}

	private String g_id;
	
	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getG_id()
	{
		return g_id;
	}

	public void setG_id(String g_id)
	{
		this.g_id = g_id;
	}

	public String getGroupname()
	{
		return groupname;
	}

	public void setGroupname(String groupname)
	{
		this.groupname = groupname;
	}
}
