package com.sdhz.dao;

import java.util.List;

import com.sdhz.domain.task.TaskTypeInfo;

public interface TaskTypeDao {
	/** 获取所有帐号列表 */
	public List<String> getTypeInfos();
	public void replace(TaskTypeInfo info);
	public void addList(List<TaskTypeInfo> list);
	public void delete();
}
