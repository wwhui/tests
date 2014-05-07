package com.sdhz.domain.group;

import java.util.List;
/**
 * Èº×é Ãû³Æ
 * 
 * */
public class GroupUser {
	private String  groupName;
	private List<UserInfo> userInfoList;
	
	public GroupUser(String groupName, List<UserInfo> userInfoList) {
		super();
		this.groupName = groupName;
		this.userInfoList = userInfoList;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<UserInfo> getUserInfoList() {
		return userInfoList;
	}
	public void setUserInfoList(List<UserInfo> userInfoList) {
		this.userInfoList = userInfoList;
	}
	
	
}
