package com.sdhz.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.hzsoft.util.Constants;
import com.hzsoft.util.JsonUtil;
import com.sdhz.crpandroid.message.MessageService;
import com.sdhz.domain.Message;

public class SocketThread extends Thread {
//	public final Logger log = Logger.getLogger(this.getClass());
	private Socket socket;
	private Context context;
	public BufferedReader br;
	public PrintWriter pw;
	private boolean flag = true;
	private String phone;

	public SocketThread(Context context, String threadName) {
		this.context = context;
		this.setName(threadName);
	}
	/** 判断网络是否连接 */
	private boolean checkNetworkIsAvailable() {
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		//判断wifi是否连接
		//boolean wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		if(networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}
	@Override
	public void run() {
		android.os.Message msg = android.os.Message.obtain();
		MessageService m = (MessageService) context;
		monitorMsg(msg,m);
	}
	
	
	
	//监听6000端口，准备接收服务器推送数据
	public void monitorMsg(android.os.Message msg,MessageService m)
	{
		int insertData =0;
		try {
			Thread.sleep(10*1000);
			sys_out("监听6000端口，准备接收服务器推送数据");
			socket = new Socket(Constants.HOST, Constants.SOCKET_PORT);
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "gb2312"));
			//登陆服务器系统
			int i=0;
			while (flag) {
				//监听心跳包时间,设置为3分钟一周期.
				Thread.sleep(1*60*1000);
				sys_out("网络是否连接"+ checkNetworkIsAvailable());
				if(insertData ==0){
					if(i==0)
					{
						sendPantToService("Login", msg, m , socket );
					}else{
						sendPantToService("Heart", msg, m , socket);
					}
				}
				
				//如果10秒后没有数据，表示心跳没有返回。
				sys_out("br.ready()" + br.ready());
				if(br.ready()) {
					String out = br.readLine();
					sys_out("out是" + out );
					if(null!=out && !"".equals(out) && !"null".equals(out)) {
						if( !(out.startsWith("Online") || out.startsWith("Login") 
								  || out.startsWith("Success") || out.startsWith("Fail"))) 
						{
							 //客户端接受数据，然后保存至数据库，
							 //注意：手机号码的判断放在服务端处理。
								Message mess = JsonUtil.getRemoteMessageFromJSON(out);
								long res = m.addMessage(mess);
								if(res ==-1)
								{
									sys_out( mess.getRu_id() + "插入失败");
								}else{
									sys_out(  mess.getRu_id()+ "插入成功 ");
								}
								insertData =1;
								sendPantToService("Result,"+mess.getRu_id()+","+res, msg, m , socket );
						 }else{
							 //此处为服务端返回的接受信息，并将insertdata设置为1，表示再次发送心跳
							 insertData= 0;
							 sys_out("本次心跳已回复：" + out); 
							 msg.what = 1;
							 m.handler.sendMessage(msg);
						 }
					}else{
						monitorMsg(msg,m);
					}
				}else{
					monitorMsg(msg,m);
				}
				i++;
			}
		} catch (IOException e) {
			sys_out("IOException");
			monitorMsg(msg,m);
		} catch (InterruptedException e) {
			sys_out("InterruptedException");
			monitorMsg(msg,m);
		}catch (Exception e) {
			sys_out("Exception");
			e.printStackTrace();
			monitorMsg(msg,m);
		}
	}
	
	public void sys_out(String str) {
		Log.d("SocketThread", str);
	}

	/**
	 * pw_type : Login:登录，Logout登出，Online在线
	 * */
	
	public void sendPantToService(String type, android.os.Message msg, MessageService m, Socket socket ) 
	{
		if(checkNetworkIsAvailable())
		{
			//网络连接正常 ,返回true
			PrintWriter pw =null;
			try {
				 pw = new PrintWriter(socket.getOutputStream()); 
				 pw.println(type + ","+ this.getPhone() );
			     pw.flush();
			     
			     sys_out("已发送心跳指令："+type + ","+ this.getPhone()+"。 请等待10秒，等心跳返回，10秒时间是服务器处理时间");
				 Thread.sleep(20*1000);
					 
			} catch (Exception e1) {
				//连接异常 
				sys_out("连接异常啦，1分钟后等待再次连接");
				//进入下一循环
				monitorMsg(msg,m);
			}finally{ 
			}
		}else{
			//进入下一循环
			monitorMsg(msg,m);
		}
		
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public void login(String account) {
		if(null != socket) {
			try {
				pw = new PrintWriter(socket.getOutputStream());
				pw.println("login:" + account);
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void exit(String account) {
		if(null != socket) {
			try {
				pw = new PrintWriter(socket.getOutputStream());
				pw.println("exit:" + account);
				pw.flush();
				Thread.sleep(5000);
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
