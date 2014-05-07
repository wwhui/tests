package com.sdhz.domain.group;

import java.io.Serializable;

public class FriendInfo implements Serializable
{
	private String f_id;
	private String operator_id;
	private String u_id;
	public String getF_id()
	{
		return f_id;
	}
	public void setF_id(String f_id)
	{
		this.f_id = f_id;
	}
	public String getOperator_id()
	{
		return operator_id;
	}
	public void setOperator_id(String operator_id)
	{
		this.operator_id = operator_id;
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
