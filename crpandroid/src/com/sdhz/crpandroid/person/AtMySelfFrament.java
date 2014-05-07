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
import com.sdhz.crpandroid.person.adapter.AtMySelfListViewAdapter;
import com.sdhz.crpandroid.widget.PopupDialog;
import com.sdhz.domain.blog.Reply;
import com.sdhz.view.XListView;
import com.sdhz.view.XListView.IXListViewListener;

/***
 * �ҷ���Ĳ���
 * 
 * */
public class AtMySelfFrament extends Fragment implements IXListViewListener
{
	private XListView					xlist_View;
	private Context						mContext;
	private LinkedList<Reply>	mListItems	;
	private AtMySelfListViewAdapter atMySelfListViewAdapter;
	private 	RequestParams params;
	private int pageIndex=0;
	private PopupDialog				popupDialog;			// ����popupwindow
	private Handler					mHandler=new Handler();				// ���ڼ��activity�ĳ�ʼ�����û�У�popupDialog���ֱ�ӳ�ʼ�����ᱨ��ָ��
	private int						detachtime	= 5;		// ÿ5����һ��
	private View view;		//�������ʼ��Ϊ��������ؽ���popupdialog		
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.task_atmy_list_fragment,
				container, false);
		mContext=inflater.getContext();
		mListItems=new LinkedList<Reply>();
		xlist_View = (XListView) view.findViewById(R.id.xlist_view);
		atMySelfListViewAdapter=new AtMySelfListViewAdapter(getActivity(), mListItems);
		xlist_View.setAdapter(atMySelfListViewAdapter);
		xlist_View.setPullLoadEnable(true);
		xlist_View.setPullRefreshEnable(true);
		xlist_View.setXListViewListener(this);
		//��ȡ��½�û�
		String operator_id= ( (CrpaApplication)mContext.getApplicationContext()).getAccountString();
		 params=new RequestParams();
		params.put("operator_id", operator_id);
		params.put("pageIndex", "0");
		initdata(params);
		return view;
	}
	// ����ѭ�����activity�Ƿ��ʼ����ϣ�һ����ʼ����Ͼ��ٳ�ʼ��popupwindow
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
//					view  =  view.findViewById(R.id.layout_main);// activity�и�Ԫ��
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
		// ��ʼ���
		mHandler.post(runnable);
	}
	
	//��ʼ����ʾ����
	private void initdata(RequestParams params)
	{
		showPopupDialog();
		HttpClient.get(Constants.ATMYSELF, params, new AsyncHttpResponseHandler()
		{

			@Override
			public void onSuccess(String result)
			{
				// TODO Auto-generated method stub
				JSONArray jsonArray;
				try
				{
					jsonArray=new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++)
					{
						JSONObject jsonObject=jsonArray.getJSONObject(i);
						Reply reply=new Reply();
						reply.setReply_content(jsonObject.getString("REPLY_CONTENT"));
						reply.setSoure_operator(jsonObject.getString("SOURE_OPERATOR"));
						reply.setReply_date(jsonObject.getString("REPLY_DATE"));
						reply.setBlog_id(jsonObject.getString("BLOG_ID"));
						mListItems.add(reply);
					}
					atMySelfListViewAdapter.setData(mListItems);
					atMySelfListViewAdapter.notifyDataSetChanged();
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
	public void onRefresh() {
		pageIndex=0;
		params.put("pageIndex", String.valueOf(pageIndex));
		mListItems.clear();
		initdata(params);
		onLoad();
	}
	@Override
	public void onLoadMore() {
		pageIndex++;
		params.put("pageIndex", String.valueOf(pageIndex));
		initdata(params);
		onLoad();
	}
	private void onLoad() 
	{
		xlist_View.stopRefresh();
		xlist_View.stopLoadMore();
		xlist_View.setRefreshTime("�ո�");
	}
}
