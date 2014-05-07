/*package com.sdhz.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sdhz.web.RemoteService;

*//** ��������-��ȡ����� *//*
public class RandomThread extends Thread {
	private RemoteService remoteService;
	private String account;
	private LoginActivity m;

	public RandomThread(String account, LoginActivity m) {
		this.account = account;
		this.m = m;
	}
	@Override
	public void run() {
		remoteService = RemoteService.getInstance();
		Message msg = Message.obtain();
		String result = remoteService.getPasswordSMS(account);
		Log.d("jiangqi", "RandomThread result = " + result);
		if(result.equals("unexists")) {
			msg.what = 4;
		} else if(result.equals("overdue")) {
			msg.what = 3;
		} else if(result.equals("fail")) {
			msg.what = 2;
		} else if(result.equals("success")) {
			msg.what = 1;
		}
		this.randomHandler.sendEmptyMessage(msg.what);
	}
	
    public Handler randomHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
            case 1:
                m.showToast("����뷢���У�");
                break;
            case 2:
                m.showToast("�����������ʧ�ܣ�");
                break;
            case 3:
                m.showToast("���������ڣ�������һ�Σ�");
                break;
            case 4:
                m.showToast("�ʺŲ����ڣ�����ϵ������Ա��ӣ�");
                break;
            default:
                m.showToast("�����������ʧ�ܣ�");
                break;
            }
        };
    };

}
*/