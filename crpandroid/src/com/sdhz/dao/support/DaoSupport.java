package com.sdhz.dao.support;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DaoSupport extends SQLiteOpenHelper
{
  //  public static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/crp/" + "crp.db";
  public static final String DATABASE_NAME = "crp.db";
    private static final int VERSION = 21;

    private static DaoSupport mInstance;
    
//    public DaoSupport(Context context, String name, CursorFactory factory, int version)
//    {
//        super(context, name, factory, version);
//    }
//
//    public DaoSupport(Context context, String name, int version)
//    {
//        this(context, name, null, version);
//    }

    private DaoSupport(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public synchronized static DaoSupport getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new DaoSupport(context);
        }
        return mInstance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        System.out.println("========create a Database========");
        Log.e("sss", " DatabaseDatabaseDatabaseDatabase");
        String sql_image = "create table if not exists t_image(imageId integer primary key, categoryId integer, postdate datetime, imageName varchar(20), "
                + "imageType varchar(10), imageResource varchar(100), synopsis varchar(200), type int)";
        db.execSQL(sql_image);
        String sql_tak_type = "create table  if not exists t_task_type(id integer primary key autoincrement, typename varchar(20) )";
        db.execSQL(sql_tak_type);
        
        String sql_user_info= "create table if not exists  t_user_info(operator_id varchar(20)   primary key , name varchar(20),long_phone  varchar(20) , userid  varchar(20)  )";
        db.execSQL(sql_user_info);
        
        String sql_talk_info= "create table if not exists  t_talk_info(id integer primary key autoincrement, name varchar(20),content  varchar(2000) , group_id  varchar(20) ,content_date date )";
        db.execSQL(sql_talk_info);
        
        String sql_account = "create table if not exists  t_account( account varchar(20)  primary key  , "
                + "password varchar(20), remember int, lastLoginTime date)";
        db.execSQL(sql_account);
        
        String sql_version = "create table if not exists  t_version(versionId integer primary key autoincrement, localVersion int, "
                + "remoteVersion int, isNew int, content varchar(500))";
        db.execSQL(sql_version);

        String sql_message = "create table if not exists  t_message(messageId integer primary key , vid int, status int, title varchar(100), content varchar(1000), time varchar(100), type int, phone varchar(11),important int,urgency int,send_num int DEFAULT 1,user_options_1 varchar(50),user_options_2 varchar(50),user_options_3 varchar(50),user_checkbox_4 int,reply varchar(500))";
        db.execSQL(sql_message);
        
        String sql_config = "create table if not exists  t_config(configId integer primary key autoincrement, configName varchar(100), "
                + "configType int, configResult int)";
        db.execSQL(sql_config);
        
        String sql_answer = "create  table  if not exists  lz_subject(s_id int(6),s_subject_name varchar(256)  ,"
                + "s_is_publis int(6) DEFAULT '1',s_status int(6) DEFAULT '1',s_create_u_phone int(11) DEFAULT NULL,"
                + "s_create_u_name varchar(256) DEFAULT NULL,s_create_time datetime DEFAULT NULL,s_answer_num int(6),s_order int(6) DEFAULT NULL,"
                + "PRIMARY KEY (s_id))";
        db.execSQL(sql_answer);
        
        
        String sql_option = "create table lz_option_answer(oa_id  int(11),oa_q_id  int(11) not null,"
                + "oa_key   varchar(256) default  null,oa_value  varchar(256) default  null,"
                + "is_answer int(6) default '1',PRIMARY KEY (oa_id))";
        db.execSQL(sql_option);
        
        String sql_score = "create table t_score(id integer primary key autoincrement,s_id varchar(10),user_phone varchar(20),use_time varchar(20),score varchar(20),rignum varchar(10),misnum varchar(10),answertime varchar(30),isup varchar(2))";
        db.execSQL(sql_score);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d("jiangqi", "SQLiteOpenHelper.onUpgrade: " + oldVersion + " -> " + newVersion);
        
        if(newVersion == 21)
        {
            dropTable(db, "t_image");
            
            String sql_image = "create table t_image(imageId integer primary key, categoryId integer, postdate datetime, imageName varchar(20), "
                    + "imageType varchar(10), imageResource varchar(100), synopsis varchar(200), type int)";
            db.execSQL(sql_image);
        }

    }
    
    private void dropTable(SQLiteDatabase db, String tablename)
    {
        db.execSQL("DROP TABLE IF EXISTS " + tablename);
    }

}
