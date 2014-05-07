package com.sdhz.crpandroid.group;

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
import com.hzsoft.util.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.group.adapter.GroupListAdapter;
import com.sdhz.crpandroid.task.TaskMainListView;
import com.sdhz.crpandroid.widget.PopupDialog;
import com.sdhz.domain.group.GroupInfo;
import com.sdhz.domain.task.TaskInfo;
import com.sdhz.view.XListView;
import com.sdhz.view.XListView.IXListViewListener;

/**
 * 显示群组
 * 
 */
public class GroupFrament extends Fragment implements IXListViewListener
{
	private XListView			xlist_view;
	private GroupListAdapter	mGroupListAdapter;
	private LinkedList<GroupInfo>		mListItems	;
	private Context				mContext;
	private LayoutInflater		inflater;
	private PopupDialog				popupDialog;			// 加载popupwindow
	private Handler					mHandler=new Handler();				// 用于检测activity的初始化完毕没有，popupDialog如果直接初始化，会报空指针
	private int						detachtime	= 5;		// 每5秒检测一次
	private View view;		//在这里初始化为了下面加载进度popupdialog							
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater = inflater;
		view = inflater.inflate(R.layout.crp_groupfragment, container,
				false);
		mContext = inflater.getContext();
		mListItems=new LinkedList<GroupInfo>();
		xlist_view = (XListView) view.findViewById(R.id.xlist_view);
		xlist_view.setPullLoadEnable(false);
		mGroupListAdapter=new GroupListAdapter(getActivity(), mListItems);
		xlist_view.setAdapter(mGroupListAdapter);
		initData();
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
	//初始化显示数据
	private void initData()
	{
		showPopupDialog();
		HttpClient.get(Constants.GROUPINFO, null, new AsyncHttpResponseHandler()
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
						GroupInfo groupInfo=new GroupInfo();
						groupInfo.setG_id(jsonObject.getString("G_ID"));
						groupInfo.setGroupname(jsonObject.getString("GROUPNAME"));
						groupInfo.setLable(jsonObject.getString("LABEL"));
						mListItems.add(groupInfo);
					}
					mGroupListAdapter.setData(mListItems);
					mGroupListAdapter.notifyDataSetChanged();
					
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					popupDialog.dismiss();
				}
				popupDialog.dismiss();
			}
			
		});
	
	}

	@Override
	public void onRefresh()
	{
		// TODO Auto-generated method stub
		if (mListItems != null && mListItems.size() > 0)
		{
			mListItems.clear();
		}
		initData();
		onLoad();
	}

	@Override
	public void onLoadMore()
	{
		// TODO Auto-generated method stub
		initData();
		onLoad();
	}

	private void onLoad()
	{
		xlist_view.stopRefresh();
		xlist_view.stopLoadMore();
		xlist_view.setRefreshTime("刚刚");
	}
}
