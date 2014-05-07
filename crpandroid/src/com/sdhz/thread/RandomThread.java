/*package com.sdhz.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sdhz.web.RemoteService;

*//** 忘记密码-获取随机码 *//*
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
                m.showToast("随机码发送中！");
                break;
            case 2:
                m.showToast("生成随机密码失败！");
                break;
            case 3:
                m.showToast("随机密码过期，请再试一次！");
                break;
            case 4:
                m.showToast("帐号不存在，请联系管理人员添加！");
                break;
            default:
                m.showToast("生成随机密码失败！");
                break;
            }
        };
    };

}
*/