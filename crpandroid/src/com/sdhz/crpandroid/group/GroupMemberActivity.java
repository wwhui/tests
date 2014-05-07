package com.sdhz.crpandroid.group;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.BaseActivity;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.group.adapter.GroupMemberListAdapter;
import com.sdhz.crpandroid.widget.PopupDialog;
import com.sdhz.domain.group.UserInfo;
import com.sdhz.view.XListView;
import com.sdhz.view.XListView.IXListViewListener;

/**
 * 查看群成员
 * 
 */
public class GroupMemberActivity extends BaseActivity implements IXListViewListener
{
	private TextView				title_bar;
	private Button					left_button;
	private Button					right_button;
	private XListView				xListView;
	private LinkedList<UserInfo>	mListItems;	// 数据集合
	private GroupMemberListAdapter	mAdapter;
	private int						page		= 0;
	private RequestParams			params=new RequestParams();
	private Intent 					intent;
	private PopupDialog				popupDialog;			// 加载popupwindow
	private Handler					mHandler=new Handler();				// 用于检测activity的初始化完毕没有，popupDialog如果直接初始化，会报空指针
	private int						detachtime	= 5;		// 每5秒检测一次
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.groupmember_list_view);
		initView();
		intent=getIntent();
		String g_id=intent.getStringExtra("g_id");
		params.put("g_id", g_id);
		initData();
	}

	// 初始化view
	private void initView()
	{
		// TODO Auto-generated method stub
		title_bar = (TextView) findViewById(R.id.title_bar);
		title_bar.setText("查看群成员");
		left_button = (Button) findViewById(R.id.left_button);
		left_button.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				GroupMemberActivity.this.finish();
			}
		});
		right_button = (Button) findViewById(R.id.right_button);
		right_button.setVisibility(View.GONE);
		xListView = (XListView) findViewById(R.id.xlist_view);
		mListItems = new LinkedList<UserInfo>();
		mAdapter = new GroupMemberListAdapter(this, mListItems);
		xListView.setAdapter(mAdapter);
		xListView.setPullLoadEnable(true);// 注意 是否分页
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);// 添加监听
	}

	// 用来循环检测activity是否初始化完毕，一旦初始化完毕就再初始化popupwindow
	private void showPopupDialog()
	{
		// TODO Auto-generated method stub
		popupDialog = new PopupDialog(GroupMemberActivity.this);

		Runnable runnable = new Runnable()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				View view=getWindow().getDecorView();
//				View view = findViewById(R.id.layout_main);// activity中根元素
				if (view != null && view.getWidth() > 0 && view.getHeight() > 0)
				{
					popupDialog.show(view);
					mHandler.removeCallbacks(this);
				}
				else
				{
					mHandler.postDelayed(this, detachtime);
				}
			}
		};
		// 开始检测
		mHandler.post(runnable);
	}
	
	// 初始化显示数据
	private void initData()
	{
		// TODO Auto-generated method stub
		showPopupDialog();
		HttpClient.get(Constants.GROUPMEMBER, params, new AsyncHttpResponseHandler()
		{
			@Override
			public void onSuccess(String result)
			{
				// TODO Auto-generated method stub
				super.onSuccess(result);
				JSONArray jsonArray;
				try
				{
					jsonArray=new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++)
					{
						JSONObject jsonObject=jsonArray.getJSONObject(i);
						UserInfo userInfo=new UserInfo();
						userInfo.setOperator_id(jsonObject.getString("OPERATOR_ID"));
						userInfo.setName(jsonObject.getString("NAME"));
						userInfo.setLong_phone(jsonObject.getString("LONGPHONE"));
						mListItems.add(userInfo);
					}
					mAdapter.setData(mListItems);
					mAdapter.notifyDataSetChanged();
					popupDialog.dismiss();
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onRefresh()
	{
		// TODO Auto-generated method stub
		if(mListItems!=null && mListItems.size()>0)
		{
			mListItems.clear();
		}
		showPopupDialog();
		params.put("pageIndex", String.valueOf(0));
		page=0;
		initData();
		onLoad();
	}

	@Override
	public void onLoadMore()
	{
		// TODO Auto-generated method stub
		page++;
		showPopupDialog();
		params.put("pageIndex", String.valueOf(page));
		initData();
		onLoad();
	}

	private void onLoad()
	{
		// TODO Auto-generated method stub
		xListView.stopRefresh();
		xListView.stopLoadMore();
		xListView.setRefreshTime("刚刚");
	}
	
}
