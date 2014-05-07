package com.sdhz.crpandroid.person;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.BaseActivity;
import com.sdhz.crpandroid.R;



/**
 * 修改密码
 *
 */
public class PersonPswActivity extends BaseActivity
{
	private Button leftButton;//左边返回按钮
	private Button rightButton;//隐藏
	private Button saveBtn;//保存按钮
	private TextView title_bar;//标题
	private TextView old_pwd;
	private TextView new_pwd;
	private TextView new_pwd_repeart;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.crp_person_psw);
		initView();//初始化view
		//添加监听
		leftButton.setOnClickListener(onClickListener);
		saveBtn.setOnClickListener(onClickListener);
	}
	//初始化view
	private void initView()
	{

		title_bar=(TextView)findViewById(R.id.title_bar);
		title_bar.setText("修改密码");
		title_bar.setTextColor(Color.WHITE);
		leftButton=(Button) findViewById(R.id.left_button);
		rightButton=(Button) findViewById(R.id.right_button);
		rightButton.setVisibility(View.GONE);
		saveBtn=(Button)findViewById(R.id.saveBtn);
		old_pwd=(TextView) findViewById(R.id.old_pwd);
		new_pwd=(TextView) findViewById(R.id.new_pwd);
		new_pwd_repeart=(TextView) findViewById(R.id.new_pwd_repeat);
		String resert=getIntent().getStringExtra("resert");
		if("1".equals(resert)){
			old_pwd.setVisibility(View.GONE);
		}
	}
	//监听
	private OnClickListener onClickListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			Button btn=(Button)v;
			switch (btn.getId())
			{
			case R.id.left_button:
				PersonPswActivity.this.finish();
				break;
			case R.id.saveBtn:
					resertPwd();
				break;
			default:
				break;
			}
		}
	};
	private void resertPwd() {
		String resert=getIntent().getStringExtra("resert");
		String account=getIntent().getStringExtra("account");
		String oldPwd=old_pwd.getText().toString();
		String newPwd=new_pwd.getText().toString();
		String newPwdResert=new_pwd_repeart.getText().toString();
		RequestParams params=new RequestParams();
		params.put("account", account);
		if("1".equals(resert)){
			if(newPwd!=null&&!"".equals(newPwd)&&newPwd.equals(newPwdResert)){
				params.put("newPwd", newPwd);
				getData(params);
				return ;
			}
			Toast.makeText(PersonPswActivity.this, "新密码两次输入不正确，请重新输入", Toast.LENGTH_SHORT).show();
		}else{
			if(oldPwd!=null&&!"".equals(oldPwd)){
				if(newPwd!=null&&!"".equals(newPwd)&&newPwd.equals(newPwdResert)){
					params.put("newPwd", newPwd);
					getData(params);
					return ;
				}else{
					Toast.makeText(PersonPswActivity.this, "新密码两次输入不正确，请重新输入", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(PersonPswActivity.this, "旧密码不能为空", Toast.LENGTH_SHORT).show();
			}
		}
	}
	public void getData(RequestParams params){
		HttpClient.post(Constants.ResertPwd, params, new AsyncHttpResponseHandler(){
			@Override
			@Deprecated
			public void onSuccess(String content) {
				if("1".equals(content)){
					Toast.makeText(PersonPswActivity.this, "重置密码成功", Toast.LENGTH_LONG).show();
					new MyHandler().sendEmptyMessage(1);
				}
				
			}
		});
	}
	class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1){
				PersonPswActivity.this.finish();
			}
		}
		
	}
	
	
}
