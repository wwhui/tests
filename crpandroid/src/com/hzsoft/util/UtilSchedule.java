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
 *�ӷ������ϻ�ȡ��Ϣ 
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
        System.out.println("ִ��timer");
    }

    public void run_task()
    {
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                //�������ȡ��Ϣ�������뱾�����ݿ�
                task_1();
                //�ӱ������ݿ��ȡ��Ϣ������������
                task_2();
            }
        });
        t.start();
    }

    // �ͻ���ͬ����������Ϣ�������·���������Ϣ״̬Ϊ��ͬ��
    public void task_1()
    {
        //û����������
        if (!HttpUtils.checkNetworkIsAvailable(this.context))
        {
            return;
        }
		SharedPreferences share =context. getSharedPreferences("crp_account", 0);
		String account = share.getString("account", "");
		String pwd = share.getString("password", "");
		boolean isRemember = share.getBoolean("isRemember", false);
        //�޷���ȡ�ʺ���Ϣ
        String phone = getAccount();
        Log.d("jiangqi", "phone:" + phone);
        if (account == null || "".equals(phone)||isRemember)
        {
            return;
        }
        
        //û�л�ȡ���κ���Ϣ
      HttpClient.get("", null, new JsonHttpResponseHandler(){

		@Override
		public void onSuccess(JSONArray jsonArray) {
			
		}
		
      });

      
    }

    /**
     * push��Ϣ
     */
    public void task_2()
    {
        for (int k = 1; k < 4; k++)
        {
            List<Map<String, Object>> datas = getNoticeCount(String.valueOf(k));
            for (int i = 0; i < datas.size(); i++)
            {
                Map<String, Object> map = datas.get(i);
                // count1 ��Ҫ������count2 ��������
                int count1 = 0;
                int count2 = 0;
                String title = "";
                String head_title = "";
                int notificationId_tmp = 0;
                if ("1".equals(String.valueOf(k)))
                {
                    head_title = "��  ��";
                    notificationId_tmp = notificationId1;
                    title = map.get("title").toString();
                } else if ("2".equals(String.valueOf(k)))
                {
                    head_title = "��  ��";
                    notificationId_tmp = notificationId2;
                    title = map.get("title").toString();
                } else if ("3".equals(String.valueOf(k)))
                {
                    count1 = Integer.valueOf(map.get("count1").toString());
                    count2 = Integer.valueOf(map.get("count2").toString());
                    if (((count1 + count2) > 0))
                    {
                        head_title = "��  ��";
                        notificationId_tmp = notificationId3;
                        String count1_str = (Integer.valueOf(count1) > 0) ? count1 + "����Ҫ��Ϣ" : "";
                        String count2_str = (Integer.valueOf(count2) > 0) ? count2 + "��������Ϣ" : "";
                        title = "�յ�" + count1_str + " " + count2_str;
                    } else
                    {
                        break;
                    }
                } else
                {
                    head_title = "��  ��";
                }

                showNotification(count2);
                Intent intent = new Intent();
                intent.putExtra("typeId", String.valueOf(k));
                intent.putExtra("title", head_title);
          //      intent.setClass(context, MoreMessageActivity.class);
                
                //PendingIntent.FLAG_UPDATE_CURRENT  ��ʾ�����������PendingIntent�Ѵ��ڣ���ı��Ѵ��ڵ�PendingIntent��Extra����Ϊ�µ�PendingIntent��Extra���ݡ�
                PendingIntent pendingIntent = PendingIntent.getActivity(context,
                        notificationId_tmp, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setLatestEventInfo(context, head_title, title, pendingIntent);
                manager.notify(notificationId_tmp, notification);
            }
        }
        // ÿ��������Ϣ��send_num���ʹ���-1
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
        //�ñ�־��ʾ���û���� Clear ֮���ܹ������֪ͨ��
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.tickerText = "����Ϣ";
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
            // shake_flag>0 ��ʾ�н�������Ϣ����Ҫ������
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
