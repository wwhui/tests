package com.sdhz.crpandroid.person;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.CrpaApplication;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.person.adapter.AttentionListViewAdapter;
import com.sdhz.crpandroid.widget.PopupDialog;
import com.sdhz.domain.blog.Blog;
import com.sdhz.view.XListView;
import com.sdhz.view.XListView.IXListViewListener;

/**
 * ��ע
 * 
 */
public class AttentionActivity extends Activity implements IXListViewListener
{
	private XListView					xlistview;
	private TextView					title_bar_text;	// ����
	private AttentionListViewAdapter	mAdapter;
	private LinkedList<Blog>			mListItems;
	private Button						left_button, right_button;
	private RequestParams				params;
	private int							pageIndex	= 0;
	private PopupDialog					popupDialog;				// ����popupwindow
	private Handler						mHandler=new Handler();					// ���ڼ��activity�ĳ�ʼ�����û�У�popupDialog���ֱ�ӳ�ʼ�����ᱨ��ָ��
	private int							detachtime	= 5;			// ÿ5����һ��

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.attention_list_view);
		initView();// ��ʼ��view
		params = new RequestParams();
		String operator_id = ( (CrpaApplication)getApplication()).getAccountString();
		params.put("pageIndex", String.valueOf(pageIndex));
		params.put("type", "0");
		params.put("operator_id", operator_id);
		initData(params);// ��ʼ����ʾ����
	}

	// ��ʼ��view
	private void initView()
	{
		xlistview = (XListView) findViewById(R.id.xlist_view);
		title_bar_text = (TextView) findViewById(R.id.title_bar);
		title_bar_text.setText("��ע");
		title_bar_text.setTextColor(Color.WHITE);
		mListItems = new LinkedList<Blog>();
		mAdapter = new AttentionListViewAdapter(this, mListItems);
		xlistview.setAdapter(mAdapter);
		left_button = (Button) findViewById(R.id.left_button);
		right_button = (Button) findViewById(R.id.right_button);
		right_button.setVisibility(View.GONE);
		bindLefBtn();
		xlistview.setPullRefreshEnable(true);
		xlistview.setPullLoadEnable(true);
		xlistview.setXListViewListener(this);
		
	}

	// ����ѭ�����activity�Ƿ��ʼ����ϣ�һ����ʼ����Ͼ��ٳ�ʼ��popupwindow
	private void showPopupDialog()
	{
		// TODO Auto-generated method stub
		popupDialog = new PopupDialog(AttentionActivity.this);

		Runnable runnable = new Runnable()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				View view = findViewById(R.id.layout_main);// activity�и�Ԫ��
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
		// ��ʼ���
		mHandler.post(runnable);
	}

	// ���ؼ�
	private void bindLefBtn()
	{
		left_button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AttentionActivity.this.finish();
			}
		});
	}

	// ��ʼ����ʾ����
	private void initData(RequestParams params)
	{
		showPopupDialog();
		HttpClient.get(Constants.FIND_Blog_Attention, params,
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
								Blog blog = new Blog();
								blog.setBlog_id(jsonObject.getString("BLOG_ID"));
								blog.setSend_username(jsonObject
										.getString("NAME"));
								blog.setSend_date(jsonObject
										.getString("SEND_DATE"));
								blog.setSend_content(jsonObject
										.getString("BLOG_CONTENT"));
								blog.setFileName(jsonObject.getString("FILE_NAME"));
								mListItems.add(blog);
							}
							mAdapter.setData(mListItems);
							mAdapter.notifyDataSetChanged();
							
						}
						catch (JSONException e)
						{
							popupDialog.dismiss();
						}
						popupDialog.dismiss();
						
					}
				});
	}

	@Override
	public void onRefresh()
	{
		pageIndex = 0;
		mListItems.clear();
		params.put("pageIndex", String.valueOf(pageIndex));
		initData(params);
		onLoad();
	}

	@Override
	public void onLoadMore()
	{
		pageIndex++;
		params.put("pageIndex", String.valueOf(pageIndex));
		initData(params);
		onLoad();
	}

	private void onLoad()
	{
		xlistview.stopRefresh();
		xlistview.stopLoadMore();
		xlistview.setRefreshTime("�ո�");
	}
}
