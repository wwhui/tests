package com.sdhz.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sdhz.dao.AccountDao;
import com.sdhz.dao.support.DaoSupport;
import com.sdhz.domain.Account;

public class AccountDaoImpl implements AccountDao {
	private DaoSupport support;
	private SQLiteDatabase db;
	private static AccountDaoImpl instance = null;
	private String mAccount;
	private List<Account> mAccountList;
	
	public AccountDaoImpl(Context context) {
//		support = new DaoSupport(context, DaoSupport.DATABASE_NAME);
//		db = support.getWritableDatabase();
        support = DaoSupport.getInstance(context);
	}
	synchronized public static AccountDao getInstance(Context context) {
		if(instance == null) {
			instance = new AccountDaoImpl(context);
		}
		return instance;
	}

	@Override
	public void addAccount(Account account) {
		//保存的时候不能同名
		db = support.getWritableDatabase();
		//始终保存一个账号
		ContentValues value = new ContentValues();
		value.put("account", account.getAccount());
		value.put("password", account.getPassword());
		value.put("remember", account.getRemember());
		db.replace("t_account", null, value);
		db.close();
	    mAccount = null;
	    refreshAccount();
	}
	
	@Override
	public void modAccount(Account obj, String account) {
		db = support.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put("account", obj.getAccount());
		value.put("password", obj.getPassword());
		value.put("remember", obj.getRemember());
		db.update("t_account", value, "account=?", new String[]{account});
        refreshAccount();
	}

	@Override
	public List<Account> getAccountList() {
	    if(mAccountList != null && mAccountList.size() > 0)
	    {
	        return mAccountList;
	    }
		List<Account> list = new ArrayList<Account>();
		db = support.getReadableDatabase();
		Cursor cursor = db.query("t_account", null, null, null, null, null, null);
		if(cursor != null) {
			if(cursor.getCount() > 0) {
				while(cursor.moveToNext()) {
					String accountStr = cursor.getString(cursor.getColumnIndex("account"));
					String passwordStr = cursor.getString(cursor.getColumnIndex("password"));
					int remember = cursor.getInt(cursor.getColumnIndex("remember"));
					Account account = new Account();
					account.setAccount(accountStr);
					account.setPassword(passwordStr);
					account.setRemember(remember);
					list.add(account);
				}
			}
		}
		cursor.close();
		db.close();
		return mAccountList = list;
	}
	
	@Override
	public String getAccount()
	{
	    if(mAccount == null || mAccount.length() == 0)
	    {
	        if(this.getAccountList().size() > 0)
	        {
	            mAccount = this.getAccountList().get(0).getAccount().toString();
	        }
	        else
	        {
	            mAccount = null;
	        }
	    }
	    
	    return mAccount;
	}
	
	@Override
	public int isExistsAccount(String account) {
		db = support.getWritableDatabase();
		Cursor cursor = db.query("t_account", null, "account=?", new String[]{account}, null, null, null);
		if(cursor != null) {
			return cursor.getCount();
		}
		return 0;
	}
	@Override
	public void deleteAccount() {
		db = support.getWritableDatabase();
		db.execSQL("delete from t_account");
		db.close();
		
		refreshAccount();
	}
	
	private void refreshAccount()
	{
		if(mAccountList!=null){
			mAccountList.clear();
		}
        mAccountList = getAccountList();
        
        mAccount = null;
        mAccount = getAccount();
	}
}
