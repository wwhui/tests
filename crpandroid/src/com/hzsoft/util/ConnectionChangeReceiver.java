package com.hzsoft.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionChangeReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO Auto-generated method stub
		ConnectivityManager manager=(ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo=manager.getActiveNetworkInfo();
//		NetworkInfo mobNewInfo=manager.getNetworkInfo(manager.TYPE_MOBILE);
		if(activeNetInfo!=null)
		{
			Toast.makeText(context, activeNetInfo.getTypeName()+"：网络已连接", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show();
		}
//		if(mobNewInfo!=null)
//		{
//			Toast.makeText(context, mobNewInfo.getTypeName()+"网络已断开", Toast.LENGTH_SHORT).show();
//		}
		
	}

}
