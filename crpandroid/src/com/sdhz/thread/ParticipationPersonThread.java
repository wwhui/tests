package com.sdhz.thread;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import com.hzsoft.util.JsonUtil;
import com.sdhz.web.RemoteService;

public class ParticipationPersonThread extends Thread {
	private String calPhone;
	private String calId;
	private Context context;
	private RemoteService remoteService;

	public ParticipationPersonThread(Context context, String calPhone, String calId) {
		this.context = context;
		this.calPhone = calPhone;
		this.calId = calId;
	}
	@Override
	public void run() {
		remoteService = RemoteService.getInstance();
		Message msg = Message.obtain();
		String personJson = remoteService.getParticipationPerson(calPhone, calId);
		if(null!=personJson && !"".equals(personJson)) {
			List<Map<String, Object>> list = JsonUtil.getParticipationPersonFromJson(personJson);
			msg.obj = list;
		} else {
			msg.obj = null;
		}
		personThreadHandler.sendMessage(msg);
	}
	
	public Handler personThreadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			List<Map<String, Object>> list = (List)msg.obj;
			
			if(null==list) {
				Toast toast = Toast.makeText(context, "连接服务器失败,请先检查网络！", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER|Gravity.BOTTOM, 0, 180);
				toast.show();
			} else {
//				WebcalAddActivity w = (WebcalAddActivity) context;
//				w.setView(list);
//				w.flag = false;
			}
		}
	};
}
