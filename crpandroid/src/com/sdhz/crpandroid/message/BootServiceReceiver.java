package com.sdhz.crpandroid.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootServiceReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		intent.setClass(context, MessageService.class);
		context.startService(intent);
	}
}