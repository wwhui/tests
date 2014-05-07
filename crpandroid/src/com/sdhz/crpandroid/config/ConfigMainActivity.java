package com.sdhz.crpandroid.config;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sdhz.crpandroid.CrpaApplication;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.UpdateManager;
import com.sdhz.crpandroid.person.PersonPswActivity;
import com.sdhz.dao.AccountDao;

public class ConfigMainActivity extends Activity
{
	private TextView	push_conf;				//推送
	private TextView	check_version;			// 检查更新
	private TextView	person_detail;			// 个人信息
	private TextView	password_conf;			// 修改密码
	private TextView	suggest;				// 意见反馈
	private TextView	about;
	private TextView	clear;
	private TextView	admin_manager;
	private AccountDao	accountDao;
	private AboutDialog	dialog;
	private Button leftBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// showCustomTitle("设置");
		setContentView(R.layout.activity_config_main);
		person_detail = (TextView) findViewById(R.id.person_detail);
		password_conf = (TextView) findViewById(R.id.password_conf);
		suggest = (TextView) findViewById(R.id.suggest);
		about = (TextView) findViewById(R.id.about);
		// dialog_button_cancel=(Button)findViewById(R.id.dialog_button_cancel);
		leftBtn=(Button)findViewById(R.id.leftBtn);
		leftBtn.setVisibility(View.INVISIBLE);
		password_conf.setOnClickListener(onClickListener);
		suggest.setOnClickListener(onClickListener);
		person_detail.setOnClickListener(onClickListener);
		about.setOnClickListener(onClickListener);

		// dialog_button_cancel.setOnClickListener(onClickListener);
	//	push_conf = (TextView) findViewById(R.id.push_conf);
		check_version = (TextView) findViewById(R.id.check_version);
		// password_conf = (TextView) findViewById(R.id.password_conf);
		// about = (TextView) findViewById(R.id.about);
		 clear=(TextView) findViewById(R.id.clear);
		 clear.setOnClickListener(onClickListener);
		// admin_manager = (TextView) findViewById(R.id.admin_manager);
		// accountDao=AccountDaoImpl.getInstance(this);
		// String
		// phone=accountDao.getAccountList().get(0).getAccount().toString();
		// if("13616199738".endsWith(phone)){
		// admin_manager.setVisibility(View.VISIBLE);
		// }
		//
//		push_conf.setVisibility(View.GONE);
		// about.setVisibility(View.GONE);
		//推送
//		push_conf.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				intent = new Intent(ConfigMainActivity.this,
//						PushConfigActivity.class);
//				startActivity(intent);
//			}
//		});
		check_version.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new UpdateManager(ConfigMainActivity.this)
						.checkUpdate(UpdateManager.SHOW_CHECK_UPDATE_NOTICE_YES);

			}
		});
		// password_conf.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// intent = new Intent(ConfigMainActivity.this, AccountActivity.class);
		// startActivity(intent);
		// }
		// });
		// about.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// intent = new Intent(ConfigMainActivity.this, AboutActivity.class);
		// startActivity(intent);
		// }
		// });
		// clear.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// int i=clearCacheFolder(ConfigMainActivity.this.getCacheDir(),
		// System.currentTimeMillis());
		// if(i>0){
		// Toast.makeText(ConfigMainActivity.this,
		// "缓存已清空",Toast.LENGTH_LONG).show();
		// }else{
		// Toast.makeText(ConfigMainActivity.this,
		// "暂无缓存",Toast.LENGTH_LONG).show();
		// }
		// }
		// });
		// admin_manager.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// intent = new Intent(ConfigMainActivity.this,
		// AdminManagerViewActivity.class);
		// intent.putExtra("mediaURL", Constants.ADMIN_MANAGER_URL);
		// startActivity(intent);
		// }
		// });
	}

	private OnClickListener	onClickListener	= new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated
			// method stub
			// AboutDialog dialog=new
			// AboutDialog(this,
			// R.style.DialogAbout, new
			// About);
			// dialog.setContentView(R.layout.crp_dialog);
			Intent intent = new Intent();
			switch (v.getId())
			{
			case R.id.person_detail:
//				intent.setClass(
//						ConfigMainActivity.this,
//						PersonInfoActivity.class);
//				startActivity(intent);
				break;
			case R.id.password_conf:
				intent.putExtra("account",( (CrpaApplication)getApplication()).getAccountString());
				intent.setClass(
						ConfigMainActivity.this,
						PersonPswActivity.class);
				startActivity(intent);
				break;
			case R.id.suggest:
				intent.setClass(
						ConfigMainActivity.this,
						SuggestActivity.class);
				startActivity(intent);
				break;
			case R.id.about:
				dialog = new AboutDialog(
						ConfigMainActivity.this,
						R.style.DialogAbout,
						new AboutDialog.AboutDialogListener()
						{
			
							@Override
							public void onClick(
									View v)
							{
							
							}
						});
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
				break;
			case R.id.clear:
				 int i=clearCacheFolder(ConfigMainActivity.this.getCacheDir(),
				 System.currentTimeMillis());
				 if(i>0){
				 Toast.makeText(ConfigMainActivity.this,
				 "缓存已清空",Toast.LENGTH_LONG).show();
				 }else{
				 Toast.makeText(ConfigMainActivity.this,
				 "暂无缓存",Toast.LENGTH_LONG).show();
				 }
				 break;
			default:
				break;
			}
		}
	};

	// /** 提示新版本对话框 */
	// public void showUpdateBuilder(final int remoteVersion) {
	// Builder builder = new Builder(this);
	// builder.setIcon(android.R.drawable.ic_dialog_info);
	// String version = String.valueOf(remoteVersion/10f);
	// builder.setTitle("目前有新版本,是否升级?");
	// builder.setMessage("最新版本(Ver."+version+")");
	// builder.setPositiveButton("确定", new
	// android.content.DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// progressDialogShow(ConfigMainActivity.this);
	// new ConfigDownApk().execute(remoteVersion);
	// }
	// });
	// builder.setNegativeButton("取消", new
	// android.content.DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// }
	// });
	// builder.create().show();
	// }
	//
	// class ConfigDownApk extends AsyncTask<Integer, Integer, String> {
	// @Override
	// protected String doInBackground(Integer... params) {
	// DownloadUtil downloader = new DownloadUtil(ConfigMainActivity.this, 2);
	// downloader.downFile("download/", "vod.apk");
	// // modVersionIsNew(params[0]);
	// return null;
	// }
	// @Override
	// protected void onPostExecute(String result) {
	// progressDialogClose();
	// installApk("download/vod.apk");
	// }
	// }

	/** 更新版本号为最新 */
	// private void modVersionIsNew(int remoteVersion) {
	// versionDao = VersionDaoImpl.getInstance(this);
	// versionDao.modVersionIsNew(remoteVersion);
	// }
	private int clearCacheFolder(File dir, long numDays)
	{
		int deletedFiles = 0;

		if (dir != null && dir.isDirectory())
		{
			try
			{
				for (File child : dir.listFiles())
				{
					if (child.isDirectory())
					{
						deletedFiles += clearCacheFolder(child, numDays);
					}
					if (child.lastModified() < numDays)
					{
						if (child.delete())
						{
							deletedFiles++;
						}
					}
					ConfigMainActivity.this.deleteDatabase("webview.db");
					ConfigMainActivity.this.deleteDatabase("webviewCache.db");
					ConfigMainActivity.this.deleteFile( Environment.getExternalStorageDirectory() + "/crp");
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}
}
