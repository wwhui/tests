package com.sdhz.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sdhz.dao.AnswerDao;
import com.sdhz.dao.support.DaoSupport;
import com.sdhz.domain.AnswerResult;
import com.sdhz.domain.OptionAnswer;
import com.sdhz.domain.Question;
import com.sdhz.domain.Subject;

public  class AnswerDaoImpl implements AnswerDao {
	private DaoSupport support;
	private SQLiteDatabase db;
	private static AnswerDaoImpl instance = null;
	
	public AnswerDaoImpl(Context context) {
//		support = new DaoSupport(context, DaoSupport.DATABASE_NAME);
//		db = support.getWritableDatabase();
	    support = DaoSupport.getInstance(context);
	}
	synchronized public static AnswerDao getInstance(Context context) {
		if(instance == null) {
			instance = new AnswerDaoImpl(context);
		}
		return instance;
	}
	
	
	@Override
	public void addFreeQuestion() {
		db = support.getWritableDatabase();
		Subject subject=new Subject();
		subject.setS_id("1");
		subject.setS_subject_name("网络部3月份答题");
		subject.setS_status("1");
		subject.setS_is_publis("1");
		subject.setS_create_u_name("boss");
		subject.setS_create_u_phone("12345678901");
		subject.setS_create_time("2013-02-09");
		subject.setS_answer_num("1");
		subject.setS_order("1");
		addsubject(subject);
		
		Question question=new Question();
		question.setQ_id("1");
		question.setQ_s_id("1");
		question.setQ_question("最近你最喜欢干什么？");
		question.setQ_option("1");
		question.setQ_level_1("1");
		question.setQ_level_2("1");
		question.setQ_answer_time("20");
		addQeustion(question);
		
		OptionAnswer optionAnswer=new OptionAnswer();
		optionAnswer.setOa_id("1");
		optionAnswer.setOa_q_id("1");
		optionAnswer.setOa_key("A");
		optionAnswer.setOa_value("打球");
		optionAnswer.setIs_answer("1");
		addOptionAnswer(optionAnswer);
		
		OptionAnswer optionAnswer1=new OptionAnswer();
		optionAnswer1.setOa_id("2");
		optionAnswer1.setOa_q_id("1");
		optionAnswer1.setOa_key("B");
		optionAnswer1.setOa_value("看电视");
		optionAnswer1.setIs_answer("1");
		addOptionAnswer(optionAnswer1);
		
		OptionAnswer optionAnswer2=new OptionAnswer();
		optionAnswer2.setOa_id("3");
		optionAnswer2.setOa_q_id("1");
		optionAnswer2.setOa_key("C");
		optionAnswer2.setOa_value("Dota");
		optionAnswer2.setIs_answer("1");
		addOptionAnswer(optionAnswer2);
		
		OptionAnswer optionAnswer3=new OptionAnswer();
		optionAnswer3.setOa_id("4");
		optionAnswer3.setOa_q_id("1");
		optionAnswer3.setOa_key("C");
		optionAnswer3.setOa_value("Dota");
		optionAnswer3.setIs_answer("2");
		addOptionAnswer(optionAnswer3);
	}
	@Override
	public long addQeustion(Question question) {
		db = support.getWritableDatabase();
		db=support.getReadableDatabase();
		Cursor cursor=db.rawQuery("select q_id  from lz_question where q_id=?", new String[]{question.getQ_id()});
		long res = 0;
		if(cursor.getCount()==0){
		ContentValues value = new ContentValues();
		value.put("q_id",question.getQ_id());
		value.put("q_s_id",question.getQ_s_id());
		value.put("q_question",question.getQ_question());
		value.put("q_level_1",question.getQ_level_1());
		value.put("q_level_2",question.getQ_level_2());
		value.put("q_answer_time",question.getQ_answer_time());
		value.put("q_option",question.getQ_option());
		res= db.insert("lz_question", null, value);
		}
		db.close();
		cursor.close();
		return res;
		
	}
	@Override
	public long addOptionAnswer(OptionAnswer optionAnswer) {
		db = support.getWritableDatabase();
		db=support.getReadableDatabase();
		Cursor cursor=db.rawQuery("select oa_id  from lz_option_answer where oa_id=? ", new String[]{optionAnswer.getOa_id()});
		long res = 0;
		if(cursor.getCount()==0){
		ContentValues value = new ContentValues();
		value.put("oa_id",optionAnswer.getOa_id());
		value.put("oa_q_id",optionAnswer.getOa_q_id());
		value.put("oa_key",optionAnswer.getOa_key());
		value.put("oa_value",optionAnswer.getOa_value());
		value.put("is_answer",optionAnswer.getIs_answer());
		res= db.insert("lz_option_answer", null, value);
		}
		db.close();
		cursor.close();
		return res;
	}
	@Override
	public long addsubject(Subject subject) {
		db = support.getWritableDatabase();
		db=support.getReadableDatabase();
		Cursor cursor=db.rawQuery("select s_id from lz_subject where s_id=?", new String[]{subject.getS_id()});
		long res = 0;
		if(cursor.getCount()==0){
		ContentValues value = new ContentValues();
		value.put("s_id",subject.getS_id());
		value.put("s_subject_name",subject.getS_subject_name());
		value.put("s_is_publis",subject.getS_is_publis());
		value.put("s_status",subject.getS_status());
		value.put("s_create_u_phone",subject.getS_create_u_phone());
		value.put("s_create_u_name",subject.getS_create_u_name());
		value.put("s_create_time",subject.getS_create_time());
		value.put("s_answer_num",subject.getS_answer_num());
		value.put("s_order",subject.getS_order());
		res= db.insert("lz_subject", null, value);
		}
		db.close();
		cursor.close();
		return res;
		
	}
	@Override
	public List<Map<String, Object>> getRandomQuestion(String id,String num) {
	    List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
	    db=support.getReadableDatabase();
	    Cursor cursor=db.rawQuery("select a.q_id,a.q_question,a.q_answer_time,a.q_option from lz_question a where a.q_s_id =? order by random()*random() desc  limit  ?",new String[]{id,num});
	    if(cursor != null) {
			if(cursor.getCount() > 0) {
				while(cursor.moveToNext()){
					Map<String, Object> map=new HashMap<String, Object>();
					String q_id = String.valueOf(cursor.getInt(cursor.getColumnIndex("q_id")));	
					String q_question = cursor.getString(cursor.getColumnIndex("q_question"));
					String q_option = String.valueOf(cursor.getInt(cursor.getColumnIndex("q_option")));
					String q_answer_time = String.valueOf(cursor.getInt(cursor.getColumnIndex("q_answer_time")));
					map.put("question", q_question);
					map.put("isDx", q_option);
					map.put("time", q_answer_time);
					int i=1;
					int j=1;
					Cursor cursor1=db.rawQuery("select oa_id,oa_key,oa_value,is_answer from lz_option_answer where oa_q_id = ? order by oa_key",new String[]{q_id});
					 if(cursor1 != null) {
							if(cursor1.getCount() > 0) {
								while(cursor1.moveToNext()){
									String oa_id=String.valueOf(cursor1.getInt(cursor1.getColumnIndex("oa_id")));
									String oa_key = cursor1.getString(cursor1.getColumnIndex("oa_key"));
									String oa_value = cursor1.getString(cursor1.getColumnIndex("oa_value"));
									String is_answer=String.valueOf(cursor1.getInt(cursor1.getColumnIndex("is_answer")));
									if("1".equals(is_answer)){
									map.put("answer"+i, oa_key+"."+oa_value);
									i++;
									}else{
										if("1".equals(q_option)){
											map.put("result", oa_key); 	
										}else{
											 map.put("result"+j, oa_key); 
											 j++;	
										}
								  
									}
									}
								}
							}
					 cursor1.close();
					 lists.add(map);
				}
				}
				}
	    cursor.close();
	    db.close();
	    return lists;
	}
	@Override
	public List<Map<String, Object>> getSubject() {
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		db=support.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from lz_subject a where s_status =1 and exists(select 1 from lz_question b where  b.q_s_id=a.s_id) order by s_order",new String[]{});
		if(cursor != null) {
			if(cursor.getCount() > 0) {
				while(cursor.moveToNext()){
					String s_id = String.valueOf(cursor.getInt(cursor.getColumnIndex("s_id")));
					String s_name = cursor.getString(cursor.getColumnIndex("s_subject_name"));
					String s_answer_num = String.valueOf(cursor.getInt(cursor.getColumnIndex("s_answer_num")));
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("id", s_id);
					map.put("name", s_name);
					map.put("num", s_answer_num);
					lists.add(map);
				}
				}
				}
		db.close();
		return lists;
	}
	@Override
	public long addScore(AnswerResult aResult,String isup) {
		db = support.getWritableDatabase();
		long res = 0;
		ContentValues value = new ContentValues();
		value.put("s_id",aResult.getS_id());
		value.put("user_phone",aResult.getUser_phone());
		value.put("use_time",aResult.getUse_time());
		value.put("score",aResult.getScore());
		value.put("rignum",aResult.getRignum());
		value.put("misnum",aResult.getMisnum());
		value.put("answertime",aResult.getAnswertime());
		value.put("isup",isup);
		res= db.insert("t_score", null, value);
		db.close();
		return res;
	}
	@Override
	public List<Map<String, Object>> getHistoryResult() {
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		db=support.getReadableDatabase();
		Cursor cursor=db.rawQuery("select id,b.s_subject_name,user_phone,use_time,score,misnum,rignum,answertime from t_score,lz_subject b where t_score.s_id=b.s_id order by id desc",new String[]{});
		if(cursor != null) {
			if(cursor.getCount() > 0) {
				while(cursor.moveToNext()){
					String id = String.valueOf(cursor.getInt(cursor.getColumnIndex("id")));
					String s_name = cursor.getString(cursor.getColumnIndex("s_subject_name"));
					String user_phone = cursor.getString(cursor.getColumnIndex("user_phone"));
					String use_time = cursor.getString(cursor.getColumnIndex("use_time"));
					String score = cursor.getString(cursor.getColumnIndex("score"));
					String misnum = cursor.getString(cursor.getColumnIndex("misnum"));
					String rignum = cursor.getString(cursor.getColumnIndex("rignum"));
					String answertime = cursor.getString(cursor.getColumnIndex("answertime"));
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("id",id);
					map.put("s_name", s_name);
					map.put("user_phone", user_phone);
					map.put("use_time", use_time);
					map.put("score", score);
					map.put("misnum", misnum);
					map.put("rignum", rignum);
					map.put("answertime", answertime);
					lists.add(map);
				}
				}
				}
		db.close();
		return lists;
	}
	@Override
	public void deleteAll() {
		db = support.getWritableDatabase(); 
		db.execSQL("delete from  lz_subject");
		db.execSQL("delete from lz_question");
		db.execSQL("delete from lz_option_answer");
		db.close();
	}
	@Override
	public void deleteScore() {
		db = support.getWritableDatabase(); 
		db.execSQL("delete from t_score where isup=0");
		db.close();
		
	}
	@Override
	public void deleteOneScore(String id) {
		db = support.getWritableDatabase(); 
		db.execSQL("delete from  t_score where id=?",new String[]{id});
		db.close();
		
	}	
}
