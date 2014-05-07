package com.sdhz.crpandroid.config;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpUtils;
import com.sdhz.crpandroid.BaseActivity;
import com.sdhz.crpandroid.R;
/**
 * 意见反馈
 *
 */
public class SuggestActivity extends BaseActivity
{
	private Button leftButton,rightButton;
	private Button btn_save;
	private TextView tvtitle;
	private EditText et_suggest;
	MyHander myHander=new MyHander();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.crp_suggest);
		initView();//初始化view
		//设置监听
		leftButton.setOnClickListener(onClickListener);
		bindOnClick();
	}
	//初始化view
	private void initView()
	{
		tvtitle=(TextView)findViewById(R.id.title_bar);
		tvtitle.setText("意见反馈");
		tvtitle.setTextColor(Color.WHITE);
		
		leftButton=(Button) findViewById(R.id.left_button);
		rightButton=(Button) findViewById(R.id.right_button);
		rightButton.setVisibility(View.GONE);
		btn_save=(Button) findViewById(R.id.btn_save);
		et_suggest=(EditText)findViewById(R.id.et_suggest);//意见反馈内容
	}
	
	//绑定发送消息
	private void bindOnClick()
	{
		btn_save.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Thread t=new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						// TODO Auto-generated method stub
						Map map=new HashMap<String, String>();
						map.put("suggest", et_suggest.getText().toString());
						String resultt=HttpUtils.postRequest(Constants.SUGGEST, map);
						Log.e("error", resultt);
						Message msg = new Message();
						msg.what = 1;
						myHander.sendMessage(msg);
					}
				});
				t.start();
			}
		});
	}
	private OnClickListener onClickListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			Button btn=(Button)v;
			switch (btn.getId())
			{
			case R.id.left_button:
				SuggestActivity.this.finish();
				break;
			default:
				break;
			}
		}
	};
	
	class MyHander extends Handler 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			// TODO Auto-generated method stub
			Log.e("error", msg.what + "");
			Toast.makeText(SuggestActivity.this, "提交成功，感谢您的反馈", Toast.LENGTH_SHORT).show();
			SuggestActivity.this.finish();
		}
	}	
}
