package com.sdhz.dao;

import java.util.List;

import com.sdhz.domain.task.TaskTypeInfo;

public interface TaskTypeDao {
	/** ��ȡ�����ʺ��б� */
	public List<String> getTypeInfos();
	public void replace(TaskTypeInfo info);
	public void addList(List<TaskTypeInfo> list);
	public void delete();
}
