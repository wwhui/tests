package com.sdhz.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sdhz.dao.AccountDao;
import com.sdhz.dao.UserInfoDao;
import com.sdhz.dao.support.DaoSupport;
import com.sdhz.domain.Account;
import com.sdhz.domain.group.UserInfo;

public class UserDaoImpl implements UserInfoDao {
	private DaoSupport support;
	private SQLiteDatabase db;
	private static UserDaoImpl instance = null;
	private String mAccount;
	private List<Account> mAccountList;
	
	public UserDaoImpl(Context context) {
        support = DaoSupport.getInstance(context);
	}
	synchronized public static UserInfoDao getInstance(Context context) {
		if(instance == null) {
			instance = new UserDaoImpl(context);
		}
		return instance;
	}
	@Override
	public void addUserInfo(UserInfo userInfo,String userid) {
		// TODO Auto-generated method stub
		db = support.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put("operator_id", userInfo.getOperator_id());
		value.put("name", userInfo.getName());
		value.put("long_phone", userInfo.getLong_phone());
		value.put("userid", userid);//当前登录人的opertaor_id
		db.replace("t_user_info", null, value);
		db.close();
	}
	public void addUserInfo(List<UserInfo>  userList,String person){
		db = support.getWritableDatabase();
		db.beginTransaction();
		if(userList!=null&&userList.size()>0){
			for(UserInfo userInfo:userList){
				ContentValues value = new ContentValues();
				value.put("operator_id", userInfo.getOperator_id());
				value.put("name", userInfo.getName());
				value.put("long_phone", userInfo.getLong_phone());
				value.put("userid", person);//当前登录人的opertaor_id
				db.replace("t_user_info", null, value);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
	}
	
	@Override
	public List<UserInfo> getUserInfoList(String user) {
		List<UserInfo> list = new ArrayList<UserInfo>();
		db = support.getReadableDatabase();
		if(user==null||"".equals(user)){
			return list;
		}
		Cursor cursor = db.query("t_user_info", null, "userid=?", new String[]{user}, null, null,null);
		if(cursor != null) {
			if(cursor.getCount() > 0) {
				while(cursor.moveToNext()) {
					String operator_id = cursor.getString(cursor.getColumnIndex("operator_id"));
					String name = cursor.getString(cursor.getColumnIndex("name"));
					String long_phone = cursor.getString(cursor.getColumnIndex("long_phone"));
					UserInfo info = new UserInfo();
					info.setOperator_id(operator_id);
					info.setName(name);
					info.setLong_phone(long_phone);
					list.add(info);
				}
			}
		}
		cursor.close();
		db.close();
		return list;
	}
	@Override
	public void deleteUserInfo(String userid) {
		db = support.getReadableDatabase();
		db.delete("t_user_info", null, null);
		db.close();
	}

}
