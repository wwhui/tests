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
	/** �ж������Ƿ����� */
	private boolean checkNetworkIsAvailable() {
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		//�ж�wifi�Ƿ�����
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
	
	
	
	//����6000�˿ڣ�׼�����շ�������������
	public void monitorMsg(android.os.Message msg,MessageService m)
	{
		int insertData =0;
		try {
			Thread.sleep(10*1000);
			sys_out("����6000�˿ڣ�׼�����շ�������������");
			socket = new Socket(Constants.HOST, Constants.SOCKET_PORT);
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "gb2312"));
			//��½������ϵͳ
			int i=0;
			while (flag) {
				//����������ʱ��,����Ϊ3����һ����.
				Thread.sleep(1*60*1000);
				sys_out("�����Ƿ�����"+ checkNetworkIsAvailable());
				if(insertData ==0){
					if(i==0)
					{
						sendPantToService("Login", msg, m , socket );
					}else{
						sendPantToService("Heart", msg, m , socket);
					}
				}
				
				//���10���û�����ݣ���ʾ����û�з��ء�
				sys_out("br.ready()" + br.ready());
				if(br.ready()) {
					String out = br.readLine();
					sys_out("out��" + out );
					if(null!=out && !"".equals(out) && !"null".equals(out)) {
						if( !(out.startsWith("Online") || out.startsWith("Login") 
								  || out.startsWith("Success") || out.startsWith("Fail"))) 
						{
							 //�ͻ��˽������ݣ�Ȼ�󱣴������ݿ⣬
							 //ע�⣺�ֻ�������жϷ��ڷ���˴���
								Message mess = JsonUtil.getRemoteMessageFromJSON(out);
								long res = m.addMessage(mess);
								if(res ==-1)
								{
									sys_out( mess.getRu_id() + "����ʧ��");
								}else{
									sys_out(  mess.getRu_id()+ "����ɹ� ");
								}
								insertData =1;
								sendPantToService("Result,"+mess.getRu_id()+","+res, msg, m , socket );
						 }else{
							 //�˴�Ϊ����˷��صĽ�����Ϣ������insertdata����Ϊ1����ʾ�ٴη�������
							 insertData= 0;
							 sys_out("���������ѻظ���" + out); 
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
	 * pw_type : Login:��¼��Logout�ǳ���Online����
	 * */
	
	public void sendPantToService(String type, android.os.Message msg, MessageService m, Socket socket ) 
	{
		if(checkNetworkIsAvailable())
		{
			//������������ ,����true
			PrintWriter pw =null;
			try {
				 pw = new PrintWriter(socket.getOutputStream()); 
				 pw.println(type + ","+ this.getPhone() );
			     pw.flush();
			     
			     sys_out("�ѷ�������ָ�"+type + ","+ this.getPhone()+"�� ��ȴ�10�룬���������أ�10��ʱ���Ƿ���������ʱ��");
				 Thread.sleep(20*1000);
					 
			} catch (Exception e1) {
				//�����쳣 
				sys_out("�����쳣����1���Ӻ�ȴ��ٴ�����");
				//������һѭ��
				monitorMsg(msg,m);
			}finally{ 
			}
		}else{
			//������һѭ��
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
