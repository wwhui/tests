package com.sdhz.crpandroid.person;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.CrpaApplication;
import com.sdhz.crpandroid.R;



/**
 * 个人信息
 *
 */
public class PersonInfoActivity extends Activity
{
	private Button btn_save;
	private EditText et1_operator_id,et2_name,et3_longphone,et4_shortphone,et5_tongyi,et6_describe;
	private Message msg;
	MyHander myHander=new MyHander();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.crp_person_info);
		btn_save=(Button)findViewById(R.id.btn_save);
		et1_operator_id=(EditText)findViewById(R.id.et1_operator_id);
		et2_name=(EditText)findViewById(R.id.et2_name);
		et3_longphone=(EditText)findViewById(R.id.et3_longphone);
		et4_shortphone=(EditText)findViewById(R.id.et4_shortphone);
		et5_tongyi=(EditText)findViewById(R.id.et5_tongyi);
		et6_describe=(EditText)findViewById(R.id.et6_describe);
		String operator_id= ( (CrpaApplication)getApplication()).getAccountString();
		RequestParams params=new RequestParams();
		params.put("operator_id", operator_id);
		intiData(params);
		DetailAsyncTask detailAsyncTask=new DetailAsyncTask();
		detailAsyncTask.execute();
	}
	//绑定发送消息
	private void bindOnClick()
	{
		btn_save.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				Thread t=new Thread(new Runnable()
				{
					
					@Override
					public void run()
					{
						// TODO Auto-generated method stub
						String userid= ( (CrpaApplication)getApplication()).getAccountString();
						Map<String, String> map=new HashMap<String, String>();
						map.put("userid", userid);
						if(!"".equals(et1_operator_id.getText().toString())||!"".equals(et2_name.getText().toString())
								||!"".equals(et3_longphone.getText().toString()))
						{
							map.put("operator_id", et1_operator_id.getText().toString());
							map.put("name", et2_name.getText().toString());
							map.put("longphone", et3_longphone.getText().toString());
							map.put("shortphone", et4_shortphone.getText().toString());
							map.put("tongyi", et5_tongyi.getText().toString());
							map.put("describe", et6_describe.getText().toString());
							String result=HttpUtils.postRequest(Constants.UPDATEDETAIL, map);
							Log.e("error", result);
							msg=new Message();
							msg.what=1;
							myHander.sendMessage(msg);
						}
						else
						{
							msg=new Message();
							msg.what=2;
							myHander.sendMessage(msg);
						}
					}
				});
				t.start();
			}
		});
	}
	
	private void intiData(RequestParams params)
	{
		HttpClient.get(Constants.FINDDETAIL, params, new AsyncHttpResponseHandler(){

			@Override
			public void onSuccess(String result)
			{
				// TODO Auto-generated method stub
				try
				{
					JSONObject jsonObjects=new JSONObject(result);
					String operator_id=jsonObjects.optString("OPERATOR_ID");
					et1_operator_id.setHint(operator_id);
					String name=jsonObjects.optString("NAME");
					et2_name.setHint(name);
					String longphone=jsonObjects.optString("LONGPHONE");
					et3_longphone.setHint(longphone);
					String shortphone=jsonObjects.optString("SHORTPHONE");
					et4_shortphone.setHint(shortphone);
					String tongyi=jsonObjects.optString("PORTALOPERATORID");
					et5_tongyi.setHint(tongyi);
					String describe=jsonObjects.optString("DESCRIPTION");
					et6_describe.setHint(describe);
					
				}
				catch (JSONException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				super.onSuccess(result);
			}
			
		});
	}
	
	class MyHander extends Handler 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			// TODO Auto-generated method stub
			switch (msg.what)
			{
			case 1:
				Log.e("error", msg.what + "");
				Toast.makeText(PersonInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
				PersonInfoActivity.this.finish();
				break;
			case 2:
				Toast.makeText(PersonInfoActivity.this, "没有任何修改", Toast.LENGTH_SHORT).show();
				PersonInfoActivity.this.finish();
				break;
			default:
				break;
			}
			
		}
	}	
	public class DetailAsyncTask extends AsyncTask<Void, Void, String>
	{

		@Override
		protected String doInBackground(Void... arg0)
		{
			// TODO Auto-generated method stub
			bindOnClick();//绑定发送消息
			return null;
		}
	}
}
