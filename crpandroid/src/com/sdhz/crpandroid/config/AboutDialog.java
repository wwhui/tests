package com.sdhz.crpandroid.config;

import com.sdhz.crpandroid.R;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 *自定义dialog
 *
 */
public class AboutDialog extends Dialog implements android.view.View.OnClickListener
{
	private Button dialog_button_cancel,dialog_button_ok;
	private TextView versioncode,versionowner;
	private AboutDialogListener listener;
	Context context;
	
	public interface AboutDialogListener
	{  
        public void onClick(View v);  
    } 
	

	public AboutDialog(Context context,int theme,AboutDialogListener listener)
	{
		super(context,theme);
		this.context=context;
		this.listener=listener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crp_dialog);
		intiViews();
	}
	//初始化自定义dialog内部的view
	private void intiViews()
	{
		versioncode=(TextView) findViewById(R.id.versioncode);
		versionowner=(TextView)findViewById(R.id.versionowner);
		PackageManager packageManager=context.getPackageManager();
		PackageInfo packageInfo;
		try
		{
			packageInfo=packageManager.getPackageInfo(context.getPackageName(), 0);
			String version="版本号："+packageInfo.versionName;
			String owner="版权所有 中国移动通信集团江苏有限公司 无锡分公司";
			versioncode.setText(version);
			versionowner.setText(owner);
		}
		catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		versioncode.setText("123");
//		dialog_button_cancel=(Button)findViewById(R.id.dialog_button_cancel);
//		dialog_button_ok=(Button)findViewById(R.id.dialog_button_ok);
		
//		dialog_button_cancel.setOnClickListener(this);
//		dialog_button_ok.setOnClickListener(this);
	}
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		listener.onClick(v);
	}
	
}
