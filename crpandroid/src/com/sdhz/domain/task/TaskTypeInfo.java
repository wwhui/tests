package com.sdhz.domain.task;

import java.io.Serializable;

public class TaskTypeInfo implements Serializable {
	private int id;
	private String  typeName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
