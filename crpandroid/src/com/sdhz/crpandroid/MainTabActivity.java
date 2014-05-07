package com.sdhz.crpandroid;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.AlertDialog.Builder;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushNotificationBuilder;
import com.baidu.android.pushservice.PushSettings;
import com.sdhz.crpandroid.config.ConfigMainActivity;
import com.sdhz.crpandroid.group.GroupActivity;
import com.sdhz.crpandroid.person.PersonMainActivity;
import com.sdhz.crpandroid.search.SearchMainActivity;
import com.sdhz.crpandroid.task.TaskMainListView;
/** Called when the activity is first created. */
/* (non-Javadoc)tab页app客户端下方展示的tab分类显示分别为首页，群组，我，搜索，更多五个部分
 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
 */
@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity implements
		OnCheckedChangeListener {

	private TabHost mTabHost;//主tab
	private Intent mAIntent;
	private Intent groupIntent;
	private Intent moreIntent;
	private Intent mCIntent;
	private Intent searchIntent;
	private boolean  flag=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintabs);
		this.mAIntent = new Intent(this, TaskMainListView.class);
		this.groupIntent = new Intent(this, GroupActivity.class);
		this.mCIntent = new Intent(this, PersonMainActivity.class);
		this.searchIntent=new Intent(this, SearchMainActivity.class);
		this.moreIntent = new Intent(this, ConfigMainActivity.class);
		this.mTabHost = getTabHost();

		((RadioButton) findViewById(R.id.main_honme))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.main_message_center))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.main_discover))
		.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.main_more))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.main_profile))
				.setOnCheckedChangeListener(this);
		

		mTabHost.addTab(buildTabSpec("A_TAB", R.string.main_home,
				R.drawable.tabbar_menu_home, this.mAIntent));
		mTabHost.addTab(buildTabSpec("B_TAB", R.string.main_message_center,
				R.drawable.tabbar_menu_message_center, this.groupIntent));
		mTabHost.addTab(buildTabSpec("C_TAB", R.string.main_profile,
				R.drawable.tabbar_menu_profile, this.mCIntent));
		mTabHost.addTab(buildTabSpec("D_TAB", R.string.main_discover,
				R.drawable.tabbar_menu_discover, this.searchIntent));
		mTabHost.addTab(buildTabSpec("E_TAB", R.string.main_more,
				R.drawable.tabbar_menu_more, this.moreIntent));
		
		registerBroadcast();
		initBindBaidu();
	}

	private void initBindBaidu() {
		if (!Utils.hasBind(this)) {
			Log.d("YYY", "before start work at " + Calendar.getInstance().getTimeInMillis());
			PushManager.startWork(this,
					PushConstants.LOGIN_TYPE_API_KEY, 
					Utils.getMetaValue(this, "api_key"));
			// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
			PushManager.enableLbs(this);
			Log.d("YYY", "after enableLbs at " + Calendar.getInstance().getTimeInMillis());
			Resources  resource=this.getResources();
		      CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
		        		getApplicationContext(),
		        		resource.getIdentifier("notification_custom_builder", "layout", "sss"), 
		        		resource.getIdentifier("notification_icon", "id", "sss"), 
		        		resource.getIdentifier("notification_title", "id", "sss"), 
		        		resource.getIdentifier("notification_text", "id", "sss"));
		        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
		        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
		        cBuilder.setLayoutDrawable(resource.getIdentifier("simple_notification_icon", "drawable", "sss"));
		       
				PushManager.setNotificationBuilder(this, 1, cBuilder);
				PushSettings.enableDebugMode(this, true);
		}
		List list=new ArrayList<String>();
		list.add("基础建设部");
		PushManager.setTags(getApplicationContext(),list);
	}

	private void registerBroadcast() {
			IntentFilter intentFilter=new IntentFilter();
			intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
			registerReceiver(reBroadcastReceiver, intentFilter);
	}
	
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(reBroadcastReceiver);
		super.onDestroy();
	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.main_honme:
				this.mTabHost.setCurrentTabByTag("A_TAB");
				break;
			case R.id.main_message_center:
				this.mTabHost.setCurrentTabByTag("B_TAB");
				break;
			case R.id.main_profile:
				this.mTabHost.setCurrentTabByTag("C_TAB");
				break;
			case R.id.main_discover:
				this.mTabHost.setCurrentTabByTag("D_TAB");
				break;
			case R.id.main_more:
				this.mTabHost.setCurrentTabByTag("E_TAB");
				break;
			}
		}

	}
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(getString(resLabel),getResources().getDrawable(resIcon)).setContent(content);
	}
	protected void dialog() { 
        AlertDialog.Builder builder = new Builder(MainTabActivity.this); 
        builder.setMessage("确定要退出吗?"); 
        builder.setTitle("提示"); 
        builder.setPositiveButton("确认", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                        MainTabActivity.this.finish(); 
                    } 
                }); 
        builder.setNegativeButton("取消", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                    } 
                }); 
        builder.create().show(); 
    }

	
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				// 响应事件的具体代码
				dialog(); 
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
	private BroadcastReceiver reBroadcastReceiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager manager=(ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetInfo=manager.getActiveNetworkInfo();
			if(activeNetInfo!=null)
			{
				if(flag){
					Toast.makeText(context, activeNetInfo.getTypeName()+"：网络已连接", Toast.LENGTH_SHORT).show();
					flag=false;
				}
			}
			else
			{
				Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show();
				flag=true;
			}
		}
	};
	
	
}