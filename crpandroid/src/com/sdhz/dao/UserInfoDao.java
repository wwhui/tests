package com.sdhz.dao;

import java.util.List;

import com.sdhz.domain.Account;
import com.sdhz.domain.group.UserInfo;

public interface UserInfoDao {

	/** 保存帐号 */
	public void addUserInfo(UserInfo UserInfo, String userid);

	/** 获取所有帐号列表 */
	public List<UserInfo> getUserInfoList(String user);
	public void addUserInfo(List<UserInfo>  userList, String userid);
	public void deleteUserInfo(String userid);
}
