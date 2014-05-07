package com.sdhz.crpandroid.group;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.group.adapter.AddListViewAdapter;
import com.sdhz.domain.group.UserInfo;
import com.sdhz.view.XListView;
import com.sdhz.view.XListView.IXListViewListener;

/**群组的右上角的添加
 * 添加好友或群组
 * 
 */
public class AddActivity extends Activity implements OnClickListener,IXListViewListener
{
	private TextView					title_bar;//标题
	private Button						searchbtn;//搜索按钮
	private XListView					addListView;//显示搜索结果
	private AddListViewAdapter			addListViewAdapter;//装配数据在listview上显示
	private EditText etdiscover;
	private  LinkedList<UserInfo> mListItems =new LinkedList<UserInfo>();
	private RequestParams params ;
	private int pageIndex=0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//没有titlebar
		setContentView(R.layout.crp_add);
		title_bar = (TextView) findViewById(R.id.title_bar);
		title_bar.setText("添加");
		title_bar.setTextColor(Color.WHITE);
		etdiscover=(EditText)findViewById(R.id.etdiscover);
		searchbtn = (Button) findViewById(R.id.searchbtn);
		addListView = (XListView) findViewById(R.id.addlist_view);
		addListViewAdapter = new AddListViewAdapter(this, mListItems);
		addListView.setAdapter(addListViewAdapter);
		addListView.setPullLoadEnable(true);
		addListView.setXListViewListener(this);
		bindOnClick();
		params=new RequestParams();
		initdata(null);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
	}
	//绑定发送消息
	private void bindOnClick()
	{
		searchbtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				mListItems.clear();
				params.put("operator_id", etdiscover.getText().toString());
				params.put("pageIndex","0");
				initdata(params);
			}
		});
	}
	//初始化显示数据
	private void initdata(RequestParams params )
	{
		HttpClient.get(Constants.FINDALLFRIENDORGROUP, params, new AsyncHttpResponseHandler()
		{
			@Override
			public void onSuccess(String result)
			{
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
					addListViewAdapter.notifyDataSetChanged();
				}
				catch (JSONException e)
				{
				}
			}
		});
	}

	@Override
	public void onRefresh()
	{
		onLoad();
	}

	@Override
	public void onLoadMore()
	{
		params.put("pageIndex",String.valueOf( ++pageIndex));
		initdata(params);
		onLoad();
	}
	private void onLoad() 
	{
		addListView.stopRefresh();
		addListView.stopLoadMore();
		addListView.setRefreshTime("刚刚");
	}
}
