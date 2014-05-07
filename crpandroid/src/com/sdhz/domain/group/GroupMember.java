package com.sdhz.domain.group;

import java.io.Serializable;

/**
 * »∫≥…‘±
 *
 */
public class GroupMember implements Serializable
{
	private String gm_id;
	private String g_id;
	private String u_id;
	public String getGm_id()
	{
		return gm_id;
	}
	public void setGm_id(String gm_id)
	{
		this.gm_id = gm_id;
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
	public void setU_id(String u_id)
	{
		this.u_id = u_id;
	}
}
