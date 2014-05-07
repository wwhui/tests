package com.sdhz.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sdhz.dao.ConfigDao;
import com.sdhz.dao.support.DaoSupport;
import com.sdhz.domain.Config;

public class ConfigDaoImpl implements ConfigDao {
	private DaoSupport support;
	private SQLiteDatabase db;
	private static ConfigDaoImpl instance = null;

	public ConfigDaoImpl(Context context) {
//		support = new DaoSupport(context, DaoSupport.DATABASE_NAME);
//		db = support.getWritableDatabase();
        support = DaoSupport.getInstance(context);
	}
	synchronized public static ConfigDao getInstance(Context context) {
		if(instance == null) {
			instance = new ConfigDaoImpl(context);
		}
		return instance;
	}
	
	@Override
	public long addConfig(Config config) {
		db = support.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put("configName", config.getConfigName());
		value.put("configType", config.getConfigType());
		value.put("configResult", config.getConfigResult());
		long row_id = db.insert("t_config", null, value);
		db.close();
		return row_id;
	}
	
	@Override
	public int modConfig(Config config, String configType) {
		db = support.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put("configName", config.getConfigName());
		value.put("configType", config.getConfigType());
		value.put("configResult", config.getConfigResult());
		int row_id = db.update("t_config", value, "configType=?", new String[]{configType});
		db.close();
		return row_id;
	}

	@Override
	public Config getConfigWithConfigType(String type) {
		Config conf = null;
		db = support.getReadableDatabase();
		Cursor cursor = db.query("t_config", null, "configType=?", new String[]{type}, null, null, null);
		if (cursor != null) {
			if(cursor.getCount() > 0) {
				conf = new Config();
				while(cursor.moveToNext()) {
					int configId = cursor.getInt(cursor.getColumnIndex("configId"));
					String configName = cursor.getString(cursor.getColumnIndex("configName"));
					int configType = cursor.getInt(cursor.getColumnIndex("configType"));
					int configResult = cursor.getInt(cursor.getColumnIndex("configResult"));
					conf.setConfigId(configId);
					conf.setConfigName(configName);
					conf.setConfigType(configType);
					conf.setConfigResult(configResult);
				}
			}
		}
		cursor.close();
		db.close();
		return conf;
	}
}