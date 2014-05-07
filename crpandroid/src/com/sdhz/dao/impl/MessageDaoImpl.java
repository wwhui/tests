package com.sdhz.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sdhz.dao.MessageDao;
import com.sdhz.dao.support.DaoSupport;
import com.sdhz.domain.Message;

public class MessageDaoImpl implements MessageDao
{
    private DaoSupport support;
    private SQLiteDatabase db;
    private static MessageDaoImpl instance = null;

    public MessageDaoImpl(Context context)
    {
        // support = new DaoSupport(context, DaoSupport.DATABASE_NAME);
        // db = support.getWritableDatabase();
        support = DaoSupport.getInstance(context);
    }

    synchronized public static MessageDao getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new MessageDaoImpl(context);
        }
        return instance;
    }

    // 添加消息
    @Override
    public long addMsg(Message msg)
    {
        db = support.getWritableDatabase();
        db = support.getReadableDatabase();
        Cursor cursor = db.rawQuery("select messageId from t_message where messageId=?",
                new String[] { String.valueOf(msg.getMessageId()) });
        long res = 0;
        if (cursor.getCount() == 0)
        {
            ContentValues value = new ContentValues();
            value.put("messageId", msg.getMessageId());
            value.put("vid", msg.getVid());
            value.put("status", msg.getStatus());
            value.put("title", msg.getTitle());
            value.put("content", msg.getContent());
            value.put("time", msg.getTime());
            value.put("type", msg.getType());
            value.put("phone", msg.getPhone());
            value.put("important", msg.getImportant());
            value.put("urgency", msg.getUrgency());
            value.put("user_options_1", msg.getUser_options_1());
            value.put("user_options_2", msg.getUser_options_2());
            value.put("user_options_3", msg.getUser_options_3());
            value.put("user_checkbox_4", msg.getUser_checkbox_4());
            value.put("reply", "");
            int send_num = 0;
            if (msg.getImportant() == 0)
            {
                send_num = 0; // 普通不提示
            } else if (msg.getImportant() == 1)
            {
                send_num = 1; // 重要 提示一次
            } else if (msg.getImportant() == 2)
            {
                send_num = 3; // 紧急 提示3次，并震动
            }
            value.put("send_num", send_num);
            res = db.insert("t_message", null, value);
        }
        db.close();
        cursor.close();
        return res;
    }

    // 获取全部信息
    @Override
    public List<Map<String, Object>> getMessages(String typeId, String phone)
    {
        List<Map<String, Object>> messageList = new ArrayList<Map<String, Object>>();
        db = support.getReadableDatabase();
        Cursor cursor = db.query("t_message", new String[] { "messageId", "vid", "title",
                "content", "time", "status" }, "type=? and phone=? ",
                new String[] { typeId, phone }, null, null, "status,messageId desc");
        if (cursor != null)
        {
            if (cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    String messageId = String.valueOf(cursor.getInt(cursor
                            .getColumnIndex("messageId")));
                    String vid = String.valueOf(cursor.getInt(cursor.getColumnIndex("vid")));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String content = cursor.getString(cursor.getColumnIndex("content"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String status = String.valueOf(cursor.getInt(cursor.getColumnIndex("status")));
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("messageId", messageId);
                    map.put("title", title);
                    map.put("vid", vid);
                    map.put("content", content);
                    map.put("time", convertTime(time));
                    map.put("status", status);
                    messageList.add(map);
                }
            }
        }
        cursor.close();
        db.close();
        return messageList;
    }

    // 获取通知栏未读数据
    @Override
    public List<Map<String, Object>> getNoticeCount(String phone, String typeId)
    {
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        db = support.getReadableDatabase();
        // important 分三档次，0普通，1重要，2紧急
        // 推送通知仅important=1hu
        if ("1".equals(typeId) || "2".equals(typeId))
        {
            Cursor cursor = db
                    .rawQuery(
                            " select * from t_message where messageId =(select max(messageId)  from t_message where phone=?  and type=? and status=0 and important>0 and  exists(select 1 from t_message where send_num>0) )",
                            new String[] { phone, typeId });
            while (cursor.moveToNext())
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("typeId", cursor.getInt(cursor.getColumnIndex("type")));
                map.put("title", cursor.getString(cursor.getColumnIndex("title")));
                datas.add(map);
            }
            cursor.close();
        } else if ("3".equals(typeId))
        {
            Cursor cursor = db
                    .rawQuery(
                            "select type,  sum(case when important =1 then 1 else 0 end)   ct1 , sum(case when important = 2 then 1 else 0 end) ct2   from t_message where phone=? and type=?  and status=0 and important>0 and  exists(select 1 from t_message where send_num>0) group by type",
                            new String[] { phone, typeId });
            while (cursor.moveToNext())
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("typeId", cursor.getInt(cursor.getColumnIndex("type")));
                map.put("count1", cursor.getInt(cursor.getColumnIndex("ct1")));
                map.put("count2", cursor.getInt(cursor.getColumnIndex("ct2")));
                datas.add(map);
            }
            cursor.close();
        }

        db.close();
        return datas;
    }

    // 获取未读数
    @Override
    public List<Map<String, Object>> getCount(String phone)
    {
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        db = support.getReadableDatabase();

        // important 分三档次，0普通，1重要，2紧急
        // 推送通知仅important=1hu
        Cursor cursor = db
                .rawQuery(
                        "select type,count(1) sum1   from t_message where phone=? and status=0  group by type",
                        new String[] { phone });

        while (cursor.moveToNext())
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("typeId", cursor.getInt(cursor.getColumnIndex("type")));
            map.put("count", cursor.getInt(cursor.getColumnIndex("sum1")));
            datas.add(map);
        }
        cursor.close();
        db.close();
        return datas;
    }

    // 获取最多5条数据
    @Override
    public List<Map<String, Object>> getMessagesLimitFive(String phone, String typeId)
    {
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        db = support.getReadableDatabase();
        Cursor cursor = db
                .rawQuery(
                        "select * from(select messageId,type,title,time,content,vid from t_message where type=? and phone= ? and status=0 order by messageId desc) limit 0,5",
                        new String[] { typeId, phone });
        while (cursor.moveToNext())
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("messageId", cursor.getInt(cursor.getColumnIndex("messageId")));
            map.put("typeId", cursor.getInt(cursor.getColumnIndex("type")));
            map.put("title", cursor.getString(cursor.getColumnIndex("title")));
            map.put("content", cursor.getString(cursor.getColumnIndex("content")));
            map.put("time", cursor.getString(cursor.getColumnIndex("time")));
            map.put("vid", cursor.getInt(cursor.getColumnIndex("vid")));
            datas.add(map);
        }
        cursor.close();
        db.close();
        return datas;
    }

    public String convertTime(String time)
    {
        String result = null; // yyyyMMddHHmmss
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try
        {
            date = format.parse(time);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        result = new SimpleDateFormat("MM-dd HH:mm").format(date).toString();
        return result;
    }

    // 修改读的状态
    @Override
    public void updateStatus(String messageId)
    {
        db = support.getWritableDatabase();
        db.execSQL("update t_message set status =1 where messageId=?", new String[] { messageId });
        db.close();

    }

    @Override
    public List<Map<String, String>> getMessageList()
    {
        List<Map<String, String>> messageList = new ArrayList<Map<String, String>>();
        db = support.getReadableDatabase();
        Cursor cursor = db.query("t_message", new String[] { "messageId", "title", "content" },
                "type=?", new String[] { "2" }, null, null, "messageId desc");
        if (cursor != null)
        {
            if (cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    String messageId = String.valueOf(cursor.getInt(cursor
                            .getColumnIndex("messageId")));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String content = cursor.getString(cursor.getColumnIndex("content"));
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("messageId", messageId);
                    map.put("title", title);
                    map.put("content", content);
                    messageList.add(map);
                }
            }
        }
        cursor.close();
        db.close();
        return messageList;
    }

    @Override
    public void addMessage(Message message)
    {
        db = support.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("vid", message.getVid());
        value.put("status", message.getStatus());
        value.put("title", message.getTitle());
        value.put("content", message.getContent());
        value.put("type", message.getType());
        db.insert("t_message", null, value);
        db.close();
    }

    @Override
    public void update_message_send_num()
    {
        db = support.getWritableDatabase();
        db.execSQL("update  t_message set send_num = send_num-1 where send_num>0");
        db.close();
    }

    @Override
    public void deleteOneMessage(String messageId)
    {
        db = support.getWritableDatabase();
        db.execSQL("delete from t_message where messageId=?", new String[] { messageId });
        db.close();

    }

    @Override
    public void deleteAllMessageByTypeLeftThirty(String typeId, String phone)
    {
        db = support.getWritableDatabase();
        db.execSQL(
                "delete from t_message where messageId in (select messageId from t_message where type=? and phone=? order by status,messageId desc limit 30,10000 ) and status=1",
                new String[] { typeId, phone });
        db.close();

    }

    public void deleteAllMessageByType(String typeId, String phone)
    {
        db = support.getWritableDatabase();
        db.execSQL("delete from t_message where type=? and phone =?",
                new String[] { typeId, phone });
        db.close();

    }

    @Override
    public Map<String, Object> getOneGongGao(String messageid)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        db = support.getReadableDatabase();
        Cursor cursor = db.query("t_message", new String[] { "messageId", "title", "time",
                "content", "user_options_1", "user_options_2", "user_options_3", "user_checkbox_4",
                "reply" }, "messageId=?", new String[] { messageid }, null, null, null);
        if (cursor != null)
        {
            if (cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    String messageId = String.valueOf(cursor.getInt(cursor
                            .getColumnIndex("messageId")));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String content = cursor.getString(cursor.getColumnIndex("content"));
                    String user_options_1 = cursor.getString(cursor
                            .getColumnIndex("user_options_1"));
                    String user_options_2 = cursor.getString(cursor
                            .getColumnIndex("user_options_2"));
                    String user_options_3 = cursor.getString(cursor
                            .getColumnIndex("user_options_3"));
                    String user_checkbox_4 = String.valueOf(cursor.getInt(cursor
                            .getColumnIndex("user_checkbox_4")));
                    String reply = cursor.getString(cursor.getColumnIndex("reply"));
                    map.put("messageId", messageId);
                    map.put("title", title);
                    map.put("time", time);
                    map.put("content", content);
                    map.put("user_options_1", user_options_1);
                    map.put("user_options_2", user_options_2);
                    map.put("user_options_3", user_options_3);
                    map.put("user_checkbox_4", user_checkbox_4);
                    map.put("reply", reply);
                }
            }
        }
        cursor.close();
        db.close();
        return map;
    }

    @Override
    public void updateReply(String messageId, String reply)
    {
        db = support.getWritableDatabase();
        db.execSQL("update t_message set reply=? where messageId=?", new String[] { reply,
                messageId });
        db.close();

    }
}
