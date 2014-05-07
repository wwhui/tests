package com.sdhz.dao.impl;

//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.hzsoft.dao.VersionDao;
//import com.hzsoft.dao.support.DaoSupport;
//import com.hzsoft.domain.Version;
//
//public class VersionDaoImpl implements VersionDao {
//	private DaoSupport support;
//	private SQLiteDatabase db;
//	private static VersionDaoImpl instance = null;
//	
//	public VersionDaoImpl(Context context) {
//		support = new DaoSupport(context, DaoSupport.DATABASE_NAME);
//		db = support.getWritableDatabase();
//	}
//	synchronized public static VersionDao getInstance(Context context) {
//		if(instance == null) {
//			instance = new VersionDaoImpl(context);
//		}
//		return instance;
//	}
//
//	@Override
//	public void addVersion(Version ver) {
//		db = support.getWritableDatabase();
//		db.delete("t_version", null, null);
//		ContentValues value = new ContentValues();
//		value.put("localVersion", ver.getLocalVersion());
//		value.put("remoteVersion", ver.getRemoteVersion());
//		value.put("isNew", ver.getIsNew());
//		db.insert("t_version", null, value);
//		db.close();
//	}
//	
//	@Override
//	public void modVersionIsNew(Version ver, String versionId) {
//		db = support.getWritableDatabase();
//		ContentValues value = new ContentValues();
//		value.put("versionNum", ver.getRemoteVersion());
//		value.put("remoteVersion", ver.getRemoteVersion());
//		value.put("isNew", true);
//		db.update("t_version", value, "versionId=?", new String[]{versionId});
//	}
//
//	@Override
//	public void modVersionIsNew(int remoteVersion) {
//		db = support.getWritableDatabase();
//		ContentValues value = new ContentValues();
//		value.put("localVersion", remoteVersion);
//		value.put("remoteVersion", remoteVersion);
//		value.put("isNew", 1);
//		db.update("t_version", value, null, null);
//	}
//	
//	@Override
//	public void modVersion(Version ver, String versionId) {
//		db = support.getWritableDatabase();
//		ContentValues value = new ContentValues();
//		value.put("versionNum", ver.getLocalVersion());
//		value.put("remoteVersion", ver.getRemoteVersion());
//		value.put("isNew", ver.getIsNew());
//		db.update("t_version", value, "versionId=?", new String[]{versionId});
//	}
//
//	@Override
//	public Version getVersion() {
//		Version ver = new Version();
//		db = support.getReadableDatabase();
//		Cursor cursor = db.query("t_version", null, null, null, null, null, null);
//		if(cursor != null) {
//			if(cursor.getCount() > 0) {
//				while(cursor.moveToNext()) {
//					int versionId = cursor.getInt(cursor.getColumnIndex("versionId"));
//					int localVersion = cursor.getInt(cursor.getColumnIndex("localVersion"));
//					int remoteVersion = cursor.getInt(cursor.getColumnIndex("remoteVersion"));
//					int isNew = cursor.getInt(cursor.getColumnIndex("isNew"));
//					ver.setVersionId(versionId);
//					ver.setLocalVersion(localVersion);
//					ver.setRemoteVersion(remoteVersion);
//					ver.setIsNew(isNew);
//					//return ver;
//				}
//			}
//		}
//		cursor.close();
//		db.close();
//		return ver;
//	}
//	
//	@Override
//	public String getLocalVersion() {
//		String versionNum = null;
//		db = support.getReadableDatabase();
//		Cursor cursor = db.query("t_version", new String[]{"localVersion"}, null, null, null, null, null);
//		if(cursor != null) {
//			if(cursor.getCount() > 0) {
//				while(cursor.moveToNext()){
//					versionNum = cursor.getString(cursor.getColumnIndex("localVersion"));
//					//return versionNum;
//				}
//			}
//		}
//		cursor.close();
//		db.close();
//		return versionNum;
//	}
//
//}
