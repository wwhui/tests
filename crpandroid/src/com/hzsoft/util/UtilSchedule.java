package com.hzsoft.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.json.JSONArray;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sdhz.crpandroid.R;
import com.sdhz.dao.AccountDao;
import com.sdhz.dao.MessageDao;
import com.sdhz.dao.impl.AccountDaoImpl;
import com.sdhz.dao.impl.MessageDaoImpl;
import com.sdhz.domain.Account;
import com.sdhz.domain.Message;

/*
 *从服务器上获取消息 
 * */

public class UtilSchedule extends TimerTask
{
    private int notificationId1 = 1000, notificationId2 = 1001, notificationId3 = 1002;
    private MessageDao msgDao;
    private AccountDao accountDao;
    private Context context;
    private Notification notification;
    private NotificationManager manager;
    boolean isPush1 = true;
    boolean isPush2 = true;
    boolean isPush3 = true;
    boolean isSound = false;
    boolean isShake = true;

    public UtilSchedule(Context context, NotificationManager manager)
    {
        this.context = context;
        this.manager = manager;
    }

    @Override
    public void run()
    {
        run_task();
        System.out.println("执行timer");
    }

    public void run_task()
    {
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                //从网络获取消息，并插入本地数据库
                task_1();
                //从本地数据库读取消息，并进行提醒
                task_2();
            }
        });
        t.start();
    }

    // 客户端同步服务器消息，并更新服务器中消息状态为已同步
    public void task_1()
    {
        //没有网络连接
        if (!HttpUtils.checkNetworkIsAvailable(this.context))
        {
            return;
        }
		SharedPreferences share =context. getSharedPreferences("crp_account", 0);
		String account = share.getString("account", "");
		String pwd = share.getString("password", "");
		boolean isRemember = share.getBoolean("isRemember", false);
        //无法获取帐号信息
        String phone = getAccount();
        Log.d("jiangqi", "phone:" + phone);
        if (account == null || "".equals(phone)||isRemember)
        {
            return;
        }
        
        //没有获取到任何消息
      HttpClient.get("", null, new JsonHttpResponseHandler(){

		@Override
		public void onSuccess(JSONArray jsonArray) {
			
		}
		
      });

      
    }

    /**
     * push消息
     */
    public void task_2()
    {
        for (int k = 1; k < 4; k++)
        {
            List<Map<String, Object>> datas = getNoticeCount(String.valueOf(k));
            for (int i = 0; i < datas.size(); i++)
            {
                Map<String, Object> map = datas.get(i);
                // count1 重要条数，count2 紧急条数
                int count1 = 0;
                int count2 = 0;
                String title = "";
                String head_title = "";
                int notificationId_tmp = 0;
                if ("1".equals(String.valueOf(k)))
                {
                    head_title = "新  闻";
                    notificationId_tmp = notificationId1;
                    title = map.get("title").toString();
                } else if ("2".equals(String.valueOf(k)))
                {
                    head_title = "公  告";
                    notificationId_tmp = notificationId2;
                    title = map.get("title").toString();
                } else if ("3".equals(String.valueOf(k)))
                {
                    count1 = Integer.valueOf(map.get("count1").toString());
                    count2 = Integer.valueOf(map.get("count2").toString());
                    if (((count1 + count2) > 0))
                    {
                        head_title = "日  程";
                        notificationId_tmp = notificationId3;
                        String count1_str = (Integer.valueOf(count1) > 0) ? count1 + "条重要消息" : "";
                        String count2_str = (Integer.valueOf(count2) > 0) ? count2 + "条紧急消息" : "";
                        title = "收到" + count1_str + " " + count2_str;
                    } else
                    {
                        break;
                    }
                } else
                {
                    head_title = "其  他";
                }

                showNotification(count2);
                Intent intent = new Intent();
                intent.putExtra("typeId", String.valueOf(k));
                intent.putExtra("title", head_title);
          //      intent.setClass(context, MoreMessageActivity.class);
                
                //PendingIntent.FLAG_UPDATE_CURRENT  表示如果该描述的PendingIntent已存在，则改变已存在的PendingIntent的Extra数据为新的PendingIntent的Extra数据。
                PendingIntent pendingIntent = PendingIntent.getActivity(context,
                        notificationId_tmp, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setLatestEventInfo(context, head_title, title, pendingIntent);
                manager.notify(notificationId_tmp, notification);
            }
        }
        // 每次推送消息后，send_num推送次数-1
        update_message_send_num();
    }

    public List<Map<String, Object>> getNoticeCount(String typeId)
    {
        msgDao = MessageDaoImpl.getInstance(context);
        String account = getAccount();
        System.out.println("account" + account);
        if (account != null && !"".equals(account))
        {
            return msgDao.getNoticeCount(getAccount(), typeId);
        } else
        {
            return new ArrayList<Map<String, Object>>();
        }
    }

    public void update_message_send_num()
    {
        msgDao = MessageDaoImpl.getInstance(context);
        msgDao.update_message_send_num();
        return;
    }

    private void showNotification(int shake_flag)
    {
        notification = new Notification();
        notification.icon = R.drawable.ic_launcher222;
        //该标志表示当用户点击 Clear 之后，能够清除该通知。
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.tickerText = "新消息";
        isSound = false;
        if (isSound)
        {
            if (isShake)
            {
                notification.defaults = Notification.DEFAULT_ALL;
            } else
            {
                notification.defaults |= Notification.DEFAULT_SOUND;
                notification.vibrate = null;
            }
        } else
        {
            // shake_flag>0 表示有紧急的消息，需要开启震动
            if (shake_flag > 0)
            {
                if (isShake)
                {
                    notification.defaults |= Notification.DEFAULT_VIBRATE;
                    notification.sound = null;
                } else
                {
                    notification.sound = null;
                    notification.vibrate = null;
                }
            }
        }
    }

    public long addMessage(Message message)
    {
        msgDao = MessageDaoImpl.getInstance(context);
        return msgDao.addMsg(message);
    }

    public String getAccount()
    {
        accountDao = AccountDaoImpl.getInstance(context);
        List<Account> list = accountDao.getAccountList();
        String account = null;
        if (list.size() > 0)
        {
            account = list.get(0).getAccount().toString();
        }
        return account;
    }
}
