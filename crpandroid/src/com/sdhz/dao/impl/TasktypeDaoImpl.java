package com.sdhz.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sdhz.dao.TaskTypeDao;
import com.sdhz.dao.support.DaoSupport;
import com.sdhz.domain.group.UserInfo;
import com.sdhz.domain.task.TaskTypeInfo;

public class TasktypeDaoImpl  implements TaskTypeDao{
	private DaoSupport support;
	private SQLiteDatabase db;
	private static TasktypeDaoImpl instance = null;
	public TasktypeDaoImpl(Context context) {
        support = DaoSupport.getInstance(context);
	}
	synchronized public static TaskTypeDao getInstance(Context context) {
		if(instance == null) {
			instance = new TasktypeDaoImpl(context);
		}
		return instance;
	}
	
	@Override
	public List<String> getTypeInfos() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		db = support.getReadableDatabase();
		Cursor cursor = db.query("t_task_type", null, null, null, null, null, " typename desc");
		list.add("È«²¿");
		if(cursor != null) {
			if(cursor.getCount() > 0) {
				while(cursor.moveToNext()) {
					String accountStr = cursor.getString(cursor.getColumnIndex("typename"));
					list.add(accountStr);
				}
			}
		}
		cursor.close();
		db.close();
		return list;
	}

	@Override
	public void replace(TaskTypeInfo info) {
		// TODO Auto-generated method stub
		db = support.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put("typeName", info.getTypeName());
		String sql=" repalce into  t_task_type ( typename) values (?) ";
		db.replace("t_task_type", null, value);
		db.close();
	}
	@Override
	public void addList(List<TaskTypeInfo> list) {
			db = support.getWritableDatabase();
			db.beginTransaction();
			if(list!=null&&list.size()>0){
				for(TaskTypeInfo info:list){
					ContentValues value = new ContentValues();
					value.put("typeName", info.getTypeName());
					db.insert("t_task_type", null, value);
				}
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			db.close();
	}
	@Override
	public void delete() {
		// TODO Auto-generated method stub
		db = support.getWritableDatabase();
		db.execSQL("delete from t_task_type");
		db.close();
	}

}
