package com.sdhz.crpandroid;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.person.PersonPswActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPwdActivity extends BaseActivity {
	private Button sendBtn;
	private EditText phoneEdit;
	private EditText accountEdit;
	private EditText  smsEdit;
	private Button smsBtn;
	private Button  leftBtn;
	String account="";
	private TextView title_bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forget_pwd_activity);
		sendBtn = (Button) findViewById(R.id.sendBtn);
		phoneEdit = (EditText) findViewById(R.id.phone);
		accountEdit = (EditText) findViewById(R.id.accountEditText);
		smsEdit=(EditText) findViewById(R.id.sms_code);
		smsBtn=(Button) findViewById(R.id.smsBtn);
		leftBtn=(Button) findViewById(R.id.left_button);
		((Button) findViewById(R.id.right_button)).setVisibility(View.GONE);
		title_bar=(TextView) findViewById(R.id.title_bar);
		title_bar.setText("忘记密码");
	 account =getIntent().getStringExtra("account");
		if (account != null && !"".equals(account)) {
			accountEdit.setText(account);
		}
		initBtn();
		initSmsBtn();
		initLeftBtn();
	}

	private void initLeftBtn() {
		leftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					ForgetPwdActivity.this.finish();
			}
		});
	}

	private void initSmsBtn() {
			smsBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 account = accountEdit.getText().toString();
					String phone = phoneEdit.getText().toString();
					if (account == null || "".equals(account.trim())
							|| phone == null || "".equals(phone)) {
						Toast.makeText(ForgetPwdActivity.this, "账户和电话号码不能为空....",
								Toast.LENGTH_SHORT).show();
						return;
					}
					RequestParams params=new RequestParams();
					params.put("account", account);
					params.put("phone", phone);
					getSmsCode(params);
				}
			});
	}

	private void initBtn() {
		
		
		
		
		
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 account = accountEdit.getText().toString();
				String phone = phoneEdit.getText().toString();
				String  sms_code=smsEdit.getText().toString();
				if (account == null || "".equals(account.trim())
						|| phone == null || "".equals(phone)||sms_code==null||"".equals(sms_code.trim())) {
					Toast.makeText(ForgetPwdActivity.this, "账户和电话号码不能为空....",
							Toast.LENGTH_SHORT).show();
					return;
				}
			RequestParams params=new RequestParams();
			params.put("account", account);
			params.put("phone", phone);
			params.put("sms_code", sms_code);
			getData(params);
			}
		});
	}
	public void getSmsCode(RequestParams params){
		HttpClient.post(Constants.Forget_Pwd, params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String string) {
				if("0".equals(string)){
					Toast.makeText(ForgetPwdActivity.this, "短信已经发送,请等待", Toast.LENGTH_SHORT).show();
				}else if("-1".equals(string)){
					Toast.makeText(ForgetPwdActivity.this, "账户与号码绑定不对应", Toast.LENGTH_SHORT).show();
				}else if("1".equals(string)){
					Toast.makeText(ForgetPwdActivity.this, "短信已经发送过，5分钟之后请重试!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	
	
	public void getData(RequestParams params){
		HttpClient.post(Constants.CheckSMSCode, params, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(Throwable arg0) {
				Toast.makeText(ForgetPwdActivity.this, "连接服务器失败，请重新提交", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(String string) {
				if("1".equals(string)){
					 new  MyHandler().sendEmptyMessage(1);
				}else if("0".equals(string)){
					Toast.makeText(ForgetPwdActivity.this, "验证码错误或者已经失效", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
  class MyHandler extends Handler{

	@Override
	public void handleMessage(Message msg) {
			if(msg.what==1){
				Intent intent=new Intent(ForgetPwdActivity.this, PersonPswActivity.class);
				intent.putExtra("resert", "1");
				intent.putExtra("account", account);
				ForgetPwdActivity.this.startActivity(intent);
				ForgetPwdActivity.this.finish();
			}
		
		
	}
	  
  }
}
