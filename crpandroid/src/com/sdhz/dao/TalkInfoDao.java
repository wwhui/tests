package com.sdhz.dao;

import java.util.List;

import com.sdhz.domain.group.TalkInfo;

public interface TalkInfoDao {
	public List<TalkInfo>  getListTaskInfo(String accountId);
	public void  addListTalkInfo(List<TalkInfo> list);
	public void deleteTalkInfo(String accountId);
}
