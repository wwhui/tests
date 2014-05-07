package com.sdhz.dao;

import java.util.List;

import com.sdhz.domain.Account;

public interface AccountDao {

	/** �����ʺ� */
	public void addAccount(Account account);
	
	/** �����ʺ���Ϣ */
	public void modAccount(Account obj, String account);

	/** ��ȡ�����ʺ��б� */
	public List<Account> getAccountList();
	
	/** �ʺ��Ƿ����,���ش��ڵ����� */
	public int isExistsAccount(String accountStr);
	
	public void deleteAccount();
	
	//��ȡ��ǰ�û��ʺ�
	public String getAccount();
}
