package com.sdhz.domain.group;

import java.io.Serializable;

public class UserInfo  implements Serializable{
	private  String  operator_id;
	private String name;
	private String long_phone;
	public String getOperator_id() {
		return operator_id;
	}
	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLong_phone() {
		if(!"null".equals(long_phone))
		{
			return long_phone;
		}
		return "亲，他很懒没有留下号码哦";
	}
	public void setLong_phone(String long_phone) {
		this.long_phone = long_phone;
	};
	
}
