package com.sdhz.dao.impl;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sdhz.dao.AccountDao;
import com.sdhz.dao.TalkInfoDao;
import com.sdhz.dao.support.DaoSupport;
import com.sdhz.domain.Account;
import com.sdhz.domain.group.TalkInfo;

public class TalkInfoDaoImpl  implements TalkInfoDao{
	private DaoSupport support;
	private SQLiteDatabase db;
	private static TalkInfoDaoImpl instance = null;
	public TalkInfoDaoImpl(Context context) {
//		support = new DaoSupport(context, DaoSupport.DATABASE_NAME);
//		db = support.getWritableDatabase();
        support = DaoSupport.getInstance(context);
	}
	synchronized public static TalkInfoDao getInstance(Context context) {
		if(instance == null) {
			instance = new TalkInfoDaoImpl(context);
		}
		return instance;
	}
	@Override
	public List<TalkInfo> getListTaskInfo(String accountId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void addListTalkInfo(List<TalkInfo> list) {
		// TODO Auto-generated method stub
		if(list!=null&&list.size()>0){
			db.beginTransaction();
			db = support.getWritableDatabase();
		for(TalkInfo info:list){
			ContentValues value = new ContentValues();
//			value.put("account", account.getAccount());
//			value.put("password", account.getPassword());
//			value.put("remember", account.getRemember());
//			db.replace("t_account", null, value);
		}
		
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}

		
	}
	@Override
	public void deleteTalkInfo(String accountId) {
		// TODO Auto-generated method stub
		
	}

}
