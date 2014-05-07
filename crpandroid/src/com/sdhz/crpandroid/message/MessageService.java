package com.sdhz.crpandroid.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.UtilSchedule;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.task.CommentDetailViewActivity;
import com.sdhz.dao.AccountDao;
import com.sdhz.dao.MessageDao;
import com.sdhz.dao.impl.AccountDaoImpl;
import com.sdhz.dao.impl.MessageDaoImpl;
import com.sdhz.domain.Account;
import com.sdhz.domain.Message;
import com.sdhz.domain.blog.Blog;
//import org.apache.log4j.Logger;

public class MessageService extends Service {
	private int notificationId1= 1000,notificationId2= 1001,notificationId3= 1002;

	private MessageDao msgDao;
	private AccountDao accountDao;

	private Notification notification;
	private NotificationManager manager;
	public Timer t;
	public UtilSchedule utilSchedule;
	boolean isPush1 = true;
	boolean isPush2 = true;
	boolean isPush3 = true;
	boolean isSound = false;
	boolean isShake = true;
	private  List<Blog> list =new ArrayList();
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.e("start", "create  sertvice");
		manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//thread = new SocketThread(this, "socketThread");
		t =new Timer();
		//每5分钟 需延迟1分钟后执行 随机1-5分钟 
//	utilSchedule=new UtilSchedule(MessageService.this, manager);
		t.schedule(new GetTimeTask(),
				  120*1000 + Long.valueOf((Math.round(Math.random()*5*60*1000))),
				  5*60*1000);
	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。随后系统会尝试重新创建service，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null。
		//调用oncreate,onStartCommand    startId = 2 intent = null
		Log.e("start", "start sertvice");
		return START_STICKY;
		//不启动
		//return START_NOT_STICKY;
		//只调用了oncreate，没有调用 onStartCommand
		//return START_STICKY_COMPATIBILITY;
		//调用 onCreate,onStartCommand    startId = 1 intent != null
		//return START_REDELIVER_INTENT;
	}

	private void showNotification(int shake_flag) {
		notification = new Notification();
		notification.icon = R.drawable.ic_launcher222;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.tickerText = "新消息";
		isSound = false;
		if (isSound) {
			if (isShake) {
				notification.defaults = Notification.DEFAULT_ALL;
			} else {
				notification.defaults |= Notification.DEFAULT_SOUND; 
				notification.vibrate=null; 
			}
		} else {
			//shake_flag>0 表示有紧急的消息，需要开启震动
			if(shake_flag >0) {
				if (isShake) { 
					notification.defaults |= Notification.DEFAULT_VIBRATE; 
					notification.sound=null;
				} else {
					notification.sound = null; 
					notification.vibrate = null;
				}
			}
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what==1) {
					for (int i=0;i<list.size()&&i<5;i++) {
						Blog blog = list.get(i); 
						//count1 重要条数，count2 紧急条数
						showNotification(1);
						int count1 = 0;
						String title="";
						String head_title =""; 
						int notificationId_tmp = 0;
						head_title = "新消息，有人@到你";
								notificationId_tmp = notificationId1;
						title =blog.getSend_content();
						Intent intent = new Intent();
						intent.putExtra("blogInfo", blog);
						intent.setClass(MessageService.this, CommentDetailViewActivity.class);
						PendingIntent pendingIntent = PendingIntent.getActivity(MessageService.this, notificationId_tmp, intent, PendingIntent.FLAG_UPDATE_CURRENT);
						notification.defaults=Notification.DEFAULT_SOUND;
						notification.setLatestEventInfo(MessageService.this,  head_title  ,  title  , pendingIntent);
						manager.notify(notificationId_tmp, notification);  
					}
				}
		};
	};

	public long addMessage(Message message) {
		msgDao = MessageDaoImpl.getInstance(this);
		return msgDao.addMsg(message);
	}
	/** 获取需要推送的用户 */
	public Account getAccount() {
		accountDao = AccountDaoImpl.getInstance(this);
		return accountDao.getAccountList().get(0);
	}
	public List<Map<String, Object>> getNoticeCount(String typeId){
		msgDao = MessageDaoImpl.getInstance(this); 
		return msgDao.getNoticeCount(accountDao.getAccountList().get(0).getAccount().toString(),typeId);
	}
	public void update_message_send_num() {
		msgDao = MessageDaoImpl.getInstance(this); 
		msgDao.update_message_send_num();
		return;
	}
	//获取进程中是否存在推送线程
//	private boolean isExistsSocketThread() {
//		thread = new SocketThread(this,"socketThread");
//		ThreadGroup group = thread.getThreadGroup();
////		String name = group.getName();
//		Thread[] thrds = new Thread[group.activeCount()];
//		group.enumerate(thrds);
//		for (Thread t : thrds) {
//			if("socketThread".equals(t.getName())) {
//				Log.d("socket", "socket线程已存在");
//				thread = (SocketThread) t;
//				return true;
//			}
//		}
//		return false;
//	}
	@Override
	public void onDestroy() {
		 Log.i("MessageService","服务已关闭");
			 if(t!=null){
			  if(utilSchedule!=null){
				utilSchedule.cancel();
				 Log.i("MessageService","utilSchedule已关闭");
			  }
			  t.cancel();
	        }
		}
	class GetTimeTask extends TimerTask{
		@Override
		public void run() {
			Log.e("service", "get  datat    ");
			  HttpClient.get(Constants.Route_Blog_List, null, new AsyncHttpResponseHandler(){

					@Override
					public void onSuccess(String result) {
						Log.e("service", "get  datat    "+result);
										JSONObject jo;
										try {
											JSONArray	jsonArray = new JSONArray(result);
											for (int i = 0; i < jsonArray.length(); i++) {
													jo = jsonArray.getJSONObject(i);
													Blog blog = new Blog();
													blog.setBlog_id(jo.getString("BLOG_ID"));
													blog.setSend_operator(jo.getString("SEND_OPERATOR"));
													blog.setSend_username(jo.getString("SEND_USERNAME"));
													blog.setFlow_id(jo.getString("FLOW_ID"));
													blog.setRoute_id(jo.optString("ROUTE_ID", ""));
													blog.setSend_content(jo.getString("BLOG_CONTENT"));
													blog.setIs_redict(jo.getInt("IS_REDICT"));
													blog.setType(jo.getInt("TYPE"));
													blog.setSend_date(jo.getString("SEND_DATE"));
													list.add(blog);
											}
											handler.sendEmptyMessage(1);
										} catch (JSONException e) {
									
								}
							}
			      });
			
		}
	}
}