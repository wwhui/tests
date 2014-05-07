package com.sdhz.dao;

import java.util.List;

import com.sdhz.domain.Account;

public interface AccountDao {

	/** 保存帐号 */
	public void addAccount(Account account);
	
	/** 更新帐号信息 */
	public void modAccount(Account obj, String account);

	/** 获取所有帐号列表 */
	public List<Account> getAccountList();
	
	/** 帐号是否存在,返回存在的数量 */
	public int isExistsAccount(String accountStr);
	
	public void deleteAccount();
	
	//获取当前用户帐号
	public String getAccount();
}
