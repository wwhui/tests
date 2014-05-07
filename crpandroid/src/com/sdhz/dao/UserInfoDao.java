package com.sdhz.dao;

import java.util.List;

import com.sdhz.domain.Account;
import com.sdhz.domain.group.UserInfo;

public interface UserInfoDao {

	/** �����ʺ� */
	public void addUserInfo(UserInfo UserInfo, String userid);

	/** ��ȡ�����ʺ��б� */
	public List<UserInfo> getUserInfoList(String user);
	public void addUserInfo(List<UserInfo>  userList, String userid);
	public void deleteUserInfo(String userid);
}
