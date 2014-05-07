package com.sdhz.crpandroid;

import android.content.res.Configuration;

import com.baidu.frontia.FrontiaApplication;
import com.sdhz.domain.Account;

public class CrpaApplication  extends FrontiaApplication{

	private  Account account;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public Account getAccount() {
		return account;
	}
	public String getAccountString(){
		return account.getAccount();
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
