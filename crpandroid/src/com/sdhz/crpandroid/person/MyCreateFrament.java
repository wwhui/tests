package com.sdhz.crpandroid.person;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.CrpaApplication;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.person.adapter.MyCreateListViewAdapter;
import com.sdhz.crpandroid.widget.PopupDialog;
import com.sdhz.domain.blog.Blog;
import com.sdhz.view.XListView;
import com.sdhz.view.XListView.IXListViewListener;
public class MyCreateFrament extends Fragment implements IXListViewListener{

	private Context mContext;
	private XListView		xlist_view;
	private MyCreateListViewAdapter		myCreateListViewAdapter;
	private LinkedList<Blog>	mListItems	;
	private int   pageIndex=0;
	private 	RequestParams params;
	private PopupDialog				popupDialog;			// 加载popupwindow
	private Handler					mHandler=new Handler();				// 用于检测activity的初始化完毕没有，popupDialog如果直接初始化，会报空指针
	private int						detachtime	= 5;		// 每5秒检测一次
	private View view;		//在这里初始化为了下面加载进度popupdialog			
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.task_mycreate_list_fragment,
				container, false);
		mContext = inflater.getContext();
		xlist_view = (XListView) view.findViewById(R.id.xlist_view);
		mListItems=new LinkedList<Blog>();
		myCreateListViewAdapter=new MyCreateListViewAdapter(getActivity(), mListItems);
		xlist_view.setAdapter(myCreateListViewAdapter);
		xlist_view.setPullLoadEnable(true);
		xlist_view.setPullRefreshEnable(true);
		xlist_view.setXListViewListener(this);
		String operator_id= ( (CrpaApplication)mContext.getApplicationContext()).getAccountString();
		params=new RequestParams();
		params.put("operator_id", operator_id);
		initdata(params);
		return view;
	}

	// 用来循环检测activity是否初始化完毕，一旦初始化完毕就再初始化popupwindow
	private void showPopupDialog()
	{
		// TODO Auto-generated method stub
		popupDialog = new PopupDialog(mContext);

		Runnable runnable = new Runnable()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
//				view  =  view.findViewById(R.id.layout_main);// activity中根元素
				if (view != null && view.getWidth() > 0 && view.getHeight() > 0)
				{
					popupDialog.showCenter(view);
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
		
	//初始化显示数据
	private void initdata(RequestParams params)
	{
		showPopupDialog();
		HttpClient.get(Constants.MYCREATE, params, new AsyncHttpResponseHandler()
		{

			@Override
			public void onSuccess(String result)
			{
				super.onSuccess(result);
				JSONArray jsonArray;
				try
				{
					jsonArray=new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++)
					{
						JSONObject jsonObject=jsonArray.getJSONObject(i);
						Blog blog=new Blog();
						blog.setSend_content(jsonObject.getString("BLOG_CONTENT"));
						blog.setFlow_id(jsonObject.getString("FLOW_ID"));
						blog.setSend_date(jsonObject.getString("SEND_DATE"));
						blog.setBlog_id(jsonObject.getString("BLOG_ID"));
						blog.setFileName(jsonObject.getString("FILE_NAME"));
						mListItems.add(blog);
					}
					myCreateListViewAdapter.notifyDataSetChanged();
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
		pageIndex=0;
		params.put("pageIndex","0");
		mListItems.clear();
		initdata(params);
		onLoad();
	}


	@Override
	public void onLoadMore()
	{
		pageIndex++;
		params.put("pageIndex", String.valueOf(pageIndex));
		initdata(params);
		onLoad();
	}
	private void onLoad() {
		xlist_view.stopRefresh();
		xlist_view.stopLoadMore();
		xlist_view.setRefreshTime("刚刚");
	}
}
