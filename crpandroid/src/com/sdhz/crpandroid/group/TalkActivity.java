package com.sdhz.crpandroid.group;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushTagMessageRequest;
import com.baidu.yun.channel.model.PushTagMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.google.gson.JsonObject;
import com.hp.hpl.sparta.Sparta.Cache;
import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.BaseActivity;
import com.sdhz.crpandroid.CrpaApplication;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.group.adapter.TalkListAdapter;
import com.sdhz.domain.group.TalkInfo;
import com.sdhz.view.XListView;
import com.sdhz.view.XListView.IXListViewListener;

/**
 * Ⱥ�������
 * 
 */
public class TalkActivity extends BaseActivity implements IXListViewListener

{
	private TextView				title_bar;
	private XListView				xlist_view;
	private LinkedList<TalkInfo>	mListItems;								// ���ݼ���
	private TalkListAdapter			talkListAdapter;
	private EditText				content;
	private Button					save;
	private Button					left_button;
	private Button					right_button;
	private ImageButton				btn_at;
	private String					g_id		= null;
	private String					groupname		= null;
	private Intent					intent;
	private Timer					timer		= null;
	private TimerTask				timerTask	= null;
	private int						i			= 0;
	private RequestParams			params;
	private SimpleDateFormat		sdf			= new SimpleDateFormat(
														"yyyy-MM-dd HH:mm:ss");
	private MyHandler myHandler=new MyHandler();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.talk);
		initView();// ��ʼ��view
		// ȡ�ô������Ĳ���
		intent = getIntent();
		g_id = intent.getStringExtra("g_id");
		params = new RequestParams();
		params.put("g_id", g_id);
		initData(params, 1);
		bindOnClick();
	}

	// ��ʼ��view
	private void initView()
	{
		// TODO Auto-generated method stub
		left_button=(Button)findViewById(R.id.left_button);
		left_button.setText("����");
//		left_button.setBackgroundResource(R.drawable.left_btn_back_list);
		left_button.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				TalkActivity.this.finish();
			}
		});
//		left_button.setVisibility(View.GONE);
		right_button=(Button)findViewById(R.id.right_button);
		right_button.setText("�鿴");
		right_button.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				intent=new Intent();
				intent.setClass(TalkActivity.this, GroupMemberActivity.class);
				intent.putExtra("g_id", g_id);
				startActivity(intent);
			}
		});
		btn_at = (ImageButton) findViewById(R.id.btn_at);
		btn_at.setVisibility(View.GONE);

		title_bar = (TextView) findViewById(R.id.title_bar);
		intent = getIntent();//��õ��ʱ������������
		groupname=intent.getStringExtra("groupname");
		title_bar.setText(groupname+"Ⱥ������");
		
		title_bar.setTextColor(Color.WHITE);

		xlist_view = (XListView) findViewById(R.id.xlist_view);
		xlist_view.setFocusable(false);
		mListItems = new LinkedList<TalkInfo>();
		talkListAdapter = new TalkListAdapter(TalkActivity.this, mListItems);
		xlist_view.setXListViewListener(this);
		xlist_view.setAdapter(talkListAdapter);
		xlist_view.setPullRefreshEnable(true);
		xlist_view.setPullLoadEnable(false);
		// ��ȡ�û��������ݣ�ͬʱӦ�û���û�id��Ⱥ��id�����������ݱ�
		content = (EditText) findViewById(R.id.content);
		save = (Button) findViewById(R.id.save);
		save.setFocusable(true);
	}
	//��ʼ��ʱ��
	private void startTimer()
	{
		if(timer==null)
		{
			timer=new Timer();
		}
		if(timerTask==null)
		{
			timerTask=new TimerTask()
			{
				
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					if (mListItems != null && mListItems.size() > 0)
					{
						TalkInfo info = mListItems.getLast();
						params.put("last_t_id", info.getT_id());
					}
					params.put("u_id",  ( (CrpaApplication)getApplication()).getAccountString());
					initData(params, 0);
				}
			};
		}
		if(timer!=null && timerTask!=null)
		{
			timer.schedule(timerTask, 0, 7000);
		}
	}
	//ֹͣ��ʱ�������ܵ���ͣtimer���ᱨ�����Ե���ͣtimertask
	private void stopTimer()
	{
		if(timer!=null)
		{
			timer.cancel();
			timer=null;
		}
		if(timerTask!=null)
		{
			timerTask.cancel();
			timerTask=null;
		}
	}
	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		stopTimer();
	}

	// �󶨷�����Ϣ
	private void bindOnClick()
	{
		save.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				RequestParams newParams = new RequestParams();
				if (mListItems != null && mListItems.size() > 0)
				{
					TalkInfo info = mListItems.getLast();
					newParams.put("last_t_id", info.getT_id());
				}
				newParams.put("talk", content.getText().toString());
				newParams.put("u_id", ( (CrpaApplication)getApplication()).getAccountString());
				newParams.put("g_id", g_id);
				sendData(content.getText().toString());
				TalkInfo info = new TalkInfo();
				if (mListItems != null && mListItems.size() > 0)
				{
					info.setT_id(mListItems.getLast().getT_id());
					info.setName(( (CrpaApplication)getApplication()).getAccount().getName());
					info.setTalk_content(content.getText().toString());
					info.setU_id(( (CrpaApplication)getApplication()).getAccountString());
					info.setCreate_date(sdf.format(new Date()));
					info.setG_id(g_id);
					mListItems.add(info);
					new MyHandler().sendEmptyMessage(1);
				}
				if (!"".equals(content.getText().toString()))
				{
					content.setText("");
				}
			}
		});
	}

	public void sendData(String content)
	{
		 // 1. ����developerƽ̨��ApiKey/SecretKey
        String apiKey = "WYP01COxOjHllr5E9sWbaUL8";
        String secretKey = "KIUGLRhMChUpMvlYqHbCOEH3uMv1wFWq";
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);
        // 2. ����BaiduChannelClient����ʵ��
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);
        // 3. ��Ҫ�˽⽻��ϸ�ڣ���ע��YunLogHandler��
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {

            // 4. �������������
            PushTagMessageRequest request = new PushTagMessageRequest();
            request.setTagName("�������貿");
            request.setDeviceType(3); // device_type => 1: web 2: pc 3:android
                                      // 4:ios 5:wp
            request.setMessage(content);
            // ��Ҫ֪ͨ��
             request.setMessageType(1);
             //request.setMessage("{\"title\":\"Notify_title_danbo\",\"description\":\"Notify_description_content\"}");
             JsonObject object=new JsonObject();
             object.addProperty("title", "��ʾ");
             object.addProperty("description", content);
             request.setMessage(object.toString());
            // 5. ����pushMessage�ӿ�
            PushTagMessageResponse response = channelClient
                    .pushTagMessage(request);

            // 6. ��֤���ͳɹ�
            System.out.println("push amount : " + response.getSuccessAmount());

        } catch (ChannelClientException e) {
            // ����ͻ��˴����쳣
            e.printStackTrace();
        } catch (ChannelServerException e) {
            // �������˴����쳣
            System.out.println(String.format(
                    "request_id: %d, error_code: %d, error_message: %s",
                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));

    }
		
		
		
		
//		HttpClient.post(Constants.ADDTALK, params, new AsyncHttpResponseHandler()
//		{
//
//			@Override
//			public void onSuccess(String arg0)
//			{
//				// TODO Auto-generated method stub
//				if (arg0.equals("1"))
//				{
//					myHandler.sendEmptyMessage(3);
//				}
//			}
//		});
	}

	// ��ʼ����ʾ����
	private void initData(RequestParams params, final int m)
	{
		HttpClient.get(Constants.FINDTALK, params,
				new AsyncHttpResponseHandler()
				{

					@Override
					public void onSuccess(String result)
					{
						JSONArray jsonArray;
						try
						{
							jsonArray = new JSONArray(result);
							for (int i = 0; i < jsonArray.length(); i++)
							{
								JSONObject jsonObject = jsonArray
										.getJSONObject(i);
								TalkInfo talkInfo = new TalkInfo();
								talkInfo.setTalk_content(jsonObject
										.getString("TALK_CONTENT"));
								talkInfo.setName(jsonObject.getString("NAME"));
								talkInfo.setG_id(jsonObject.getString("G_ID"));
								talkInfo.setT_id(jsonObject.getString("T_ID"));
								talkInfo.setU_id(jsonObject.getString("U_ID"));
								talkInfo.setCreate_date(jsonObject
										.getString("CREATE_DATE"));
								switch (m)
								{
								case 0:
									mListItems.addLast(talkInfo);
									break;
								case 1:
									mListItems.addFirst(talkInfo);
									break;
								default:
									mListItems.add(talkInfo);
									break;
								}
							}
							if (i == 0)
							{
								myHandler.sendEmptyMessage(2);
								i++;
							}
							else
							{
								myHandler.sendEmptyMessage(1);
							}

						}
						catch (JSONException e)
						{
						}
						super.onSuccess(result);
					}

				});

	}

	@Override
	public void onRefresh()
	{
		RequestParams newparams = new RequestParams();
		if (mListItems != null && mListItems.size() > 0)
		{
			TalkInfo info = mListItems.getFirst();
			newparams.put("first_t_id", info.getT_id());
			newparams.put("g_id", g_id);
			initData(newparams, 1);
		}
		onLoad();
	}

	@Override
	public void onLoadMore()
	{

	}

	private void onLoad()
	{
		xlist_view.stopRefresh();
		// xlist_view.stopLoadMore();
		xlist_view.setRefreshTime("�ո�");
	}

	class MyHandler extends Handler
	{

		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == 2)
			{
				//startTimer();
			}else if(msg.what==3){
				Toast.makeText(TalkActivity.this, "���ͳɹ�",
						Toast.LENGTH_SHORT).show();
			}
			talkListAdapter.setData(mListItems);
			talkListAdapter.notifyDataSetChanged();
			xlist_view.setSelection(talkListAdapter.getCount() - 1);
			xlist_view.setFocusable(true);
			xlist_view.setFocusableInTouchMode(true);
			xlist_view.setPullRefreshEnable(true);
		}

	}
}
