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
 * 群聊天界面
 * 
 */
public class TalkActivity extends BaseActivity implements IXListViewListener

{
	private TextView				title_bar;
	private XListView				xlist_view;
	private LinkedList<TalkInfo>	mListItems;								// 数据集合
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
		initView();// 初始化view
		// 取得带过来的参数
		intent = getIntent();
		g_id = intent.getStringExtra("g_id");
		params = new RequestParams();
		params.put("g_id", g_id);
		initData(params, 1);
		bindOnClick();
	}

	// 初始化view
	private void initView()
	{
		// TODO Auto-generated method stub
		left_button=(Button)findViewById(R.id.left_button);
		left_button.setText("返回");
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
		right_button.setText("查看");
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
		intent = getIntent();//获得点击时带过来的名字
		groupname=intent.getStringExtra("groupname");
		title_bar.setText(groupname+"群组聊天");
		
		title_bar.setTextColor(Color.WHITE);

		xlist_view = (XListView) findViewById(R.id.xlist_view);
		xlist_view.setFocusable(false);
		mListItems = new LinkedList<TalkInfo>();
		talkListAdapter = new TalkListAdapter(TalkActivity.this, mListItems);
		xlist_view.setXListViewListener(this);
		xlist_view.setAdapter(talkListAdapter);
		xlist_view.setPullRefreshEnable(true);
		xlist_view.setPullLoadEnable(false);
		// 获取用户发表内容，同时应该获得用户id和群组id插入聊天内容表
		content = (EditText) findViewById(R.id.content);
		save = (Button) findViewById(R.id.save);
		save.setFocusable(true);
	}
	//开始定时器
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
	//停止定时器，不能单独停timer，会报错，可以单独停timertask
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

	// 绑定发送消息
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
		 // 1. 设置developer平台的ApiKey/SecretKey
        String apiKey = "WYP01COxOjHllr5E9sWbaUL8";
        String secretKey = "KIUGLRhMChUpMvlYqHbCOEH3uMv1wFWq";
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);
        // 2. 创建BaiduChannelClient对象实例
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);
        // 3. 若要了解交互细节，请注册YunLogHandler类
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {

            // 4. 创建请求类对象
            PushTagMessageRequest request = new PushTagMessageRequest();
            request.setTagName("基础建设部");
            request.setDeviceType(3); // device_type => 1: web 2: pc 3:android
                                      // 4:ios 5:wp
            request.setMessage(content);
            // 若要通知，
             request.setMessageType(1);
             //request.setMessage("{\"title\":\"Notify_title_danbo\",\"description\":\"Notify_description_content\"}");
             JsonObject object=new JsonObject();
             object.addProperty("title", "提示");
             object.addProperty("description", content);
             request.setMessage(object.toString());
            // 5. 调用pushMessage接口
            PushTagMessageResponse response = channelClient
                    .pushTagMessage(request);

            // 6. 认证推送成功
            System.out.println("push amount : " + response.getSuccessAmount());

        } catch (ChannelClientException e) {
            // 处理客户端错误异常
            e.printStackTrace();
        } catch (ChannelServerException e) {
            // 处理服务端错误异常
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

	// 初始化显示数据
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
		xlist_view.setRefreshTime("刚刚");
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
				Toast.makeText(TalkActivity.this, "发送成功",
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
