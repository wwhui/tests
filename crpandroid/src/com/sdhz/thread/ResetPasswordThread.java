/*package com.sdhz.thread;

import android.os.Message;

import com.hzsoft.dao.AccountDao;
import com.hzsoft.dao.impl.AccountDaoImpl;
import com.hzsoft.domain.Account;
import com.hzsoft.web.RemoteService;
import com.hzsoft.wxty.config.AccountActivity;

public class ResetPasswordThread extends Thread {
	private RemoteService remoteService;
	private AccountDao accountDao;
	private String account;
	private String oldpassword;
	private String newpassword;
	private AccountActivity a;

	public ResetPasswordThread(String account, String oldpass, String newpass, AccountActivity a) {
		this.account = account;
		this.oldpassword = oldpass;
		this.newpassword = newpass;
		this.a = a;
	}
	@Override
	public void run() {
		remoteService = RemoteService.getInstance();
		Message msg = Message.obtain();
		String result = remoteService.modPassword(account, oldpassword, newpassword);
		if(result.equals("success")) {
			modAccount(account, newpassword);
			msg.what = 1;
		} else if (result.equals("overdue")) {
			msg.what = 2;
		} else {
			msg.what = 3;
		}
		a.handler.sendEmptyMessage(msg.what);
	}
	private void modAccount(String account, String password) {
		accountDao = AccountDaoImpl.getInstance(a);
		Account obj = new Account();
		obj.setAccount(account);
		obj.setPassword(password);
		obj.setRemember(1);
		accountDao.modAccount(obj, account);
	}
}
*/