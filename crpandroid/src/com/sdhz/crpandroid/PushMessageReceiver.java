package com.sdhz.crpandroid;

import com.sdhz.crpandroid.blog.MainBlogListActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PushMessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intent2= new Intent();
		intent2.setClass(context, MainBlogListActivity.class);
		intent2.
	       addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
  //  intent.addFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
	       context.startActivity(intent2);
	}

}
