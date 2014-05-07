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
 * �޸�����
 *
 */
public class PersonPswActivity extends BaseActivity
{
	private Button leftButton;//��߷��ذ�ť
	private Button rightButton;//����
	private Button saveBtn;//���水ť
	private TextView title_bar;//����
	private TextView old_pwd;
	private TextView new_pwd;
	private TextView new_pwd_repeart;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.crp_person_psw);
		initView();//��ʼ��view
		//��Ӽ���
		leftButton.setOnClickListener(onClickListener);
		saveBtn.setOnClickListener(onClickListener);
	}
	//��ʼ��view
	private void initView()
	{

		title_bar=(TextView)findViewById(R.id.title_bar);
		title_bar.setText("�޸�����");
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
	//����
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
			Toast.makeText(PersonPswActivity.this, "�������������벻��ȷ������������", Toast.LENGTH_SHORT).show();
		}else{
			if(oldPwd!=null&&!"".equals(oldPwd)){
				if(newPwd!=null&&!"".equals(newPwd)&&newPwd.equals(newPwdResert)){
					params.put("newPwd", newPwd);
					getData(params);
					return ;
				}else{
					Toast.makeText(PersonPswActivity.this, "�������������벻��ȷ������������", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(PersonPswActivity.this, "�����벻��Ϊ��", Toast.LENGTH_SHORT).show();
			}
		}
	}
	public void getData(RequestParams params){
		HttpClient.post(Constants.ResertPwd, params, new AsyncHttpResponseHandler(){
			@Override
			@Deprecated
			public void onSuccess(String content) {
				if("1".equals(content)){
					Toast.makeText(PersonPswActivity.this, "��������ɹ�", Toast.LENGTH_LONG).show();
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
