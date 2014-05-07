package com.sdhz.crpandroid.search;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.search.adapter.SearchListViewAdapter;
import com.sdhz.crpandroid.task.CommentListViewActivity;
import com.sdhz.crpandroid.task.TaskMainListView;
import com.sdhz.crpandroid.task.adapter.CommentListViewAdapter;
import com.sdhz.crpandroid.task.adapter.TaskListViewAdapter;
import com.sdhz.crpandroid.widget.PopupDialog;
import com.sdhz.domain.blog.Blog;
import com.sdhz.domain.blog.Reply;
import com.sdhz.domain.task.TaskInfo;
import com.sdhz.view.XListView;
import com.sdhz.view.XListView.IXListViewListener;

public class SearchMainActivity extends Activity implements IXListViewListener
{
	private XListView				search_info_listview;
	private EditText				etdiscover;
	private Button searchbtn;
	private int  pageIndex=0;
	private 	RequestParams  params;
	private Spinner  select_option;
	private ArrayAdapter<String>  spinnerAdpeter;
	private TaskListViewAdapter		mAdapter;				// 适配器 流程适配器
	private CommentListViewAdapter commentListViewAdapter;//博文适配器
	private LinkedList mListItems	;//数据存储集合
	private String[]  options=new String[]{"流程","博文"};
	private int  selectPosition=0;
	private PopupDialog				popupDialog;			// 加载popupwindow
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.crp_main_discover_activity);
		mListItems= new LinkedList();
		search_info_listview = (XListView) findViewById(R.id.search_info_listview);
		search_info_listview.setPullLoadEnable(true);
		search_info_listview.setPullRefreshEnable(true);
		search_info_listview.setXListViewListener(this);
		mAdapter=new TaskListViewAdapter(this, mListItems);
		commentListViewAdapter=new CommentListViewAdapter(this, mListItems);
		etdiscover = (EditText) findViewById(R.id.etdiscover);
		initSpinner();
		params=new RequestParams();
		bindOnClick();
	}
	
	private void initSpinner() {
		select_option=(Spinner) findViewById(R.id.select_option);
		spinnerAdpeter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);  
        //设置下拉列表的风格  
		spinnerAdpeter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        //将adapter 添加到spinner中  
		select_option.setAdapter(spinnerAdpeter);  
        //添加事件Spinner事件监听    
		select_option.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int postion, long arg3) {
				if(postion==0){
					pageIndex=0;
					params=new RequestParams();
					selectPosition=postion;
				}else if(postion==1){
					pageIndex=0;
					params=new RequestParams();
					selectPosition=postion;
				}
				mListItems.clear();
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});  
        //设置默认值  
		select_option.setVisibility(View.VISIBLE);  
	}

		//绑定发送消息 触发查询过程
		private void bindOnClick()
		{
			searchbtn=(Button) findViewById(R.id.searchbtn);
			searchbtn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
						if(popupDialog==null){
							popupDialog=new PopupDialog(SearchMainActivity.this);
						}
					popupDialog.show(v);
					String content=etdiscover.getText().toString();
					params.put("content", content);
					
					switch (selectPosition) {
					case 0:
						search_info_listview.setAdapter(mAdapter);
						mListItems.clear();
						getTaskData();
						break;
					case 1:
						search_info_listview.setAdapter(commentListViewAdapter);
						mListItems.clear();
						getBlogData();
						break;
					}
				}
			});
		}
	
	@Override
	public void onRefresh()
	{
		pageIndex = 0;
		params.put("pageIndex", "0");
		switch (selectPosition) {
		case 1://获取博文信息
			getBlogData();
			break;
		case 0://获取l路由信息
			getTaskData();
			break;
		}
		onLoad();
	}

	@Override
	public void onLoadMore()
	{
		pageIndex++;
		params.put("pageIndex", String.valueOf(pageIndex));
		switch (selectPosition) {
		case 1://获取博文信息
			getBlogData();
			break;
		case 0://获取l路由信息
			getTaskData();
			break;
		}
		onLoad();
	}
	private void onLoad() {
		search_info_listview.stopRefresh();
		search_info_listview.stopLoadMore();
		search_info_listview.setRefreshTime("刚刚");
	}
	/**
	 * 加载  TasKListInfo 数据
	 * 
	 * */

	private void getTaskData()
	{
		///content 为参数名
	//showPopupDialog();
		HttpClient.get(Constants.Task_List, params,
				new AsyncHttpResponseHandler()
				{

					@Override
					public void onSuccess(String result)
					{
						JSONArray json_question;
						try
						{
							json_question = new JSONArray(result);
							for (int i = 0; i < json_question.length(); i++)
							{
								JSONObject jo = json_question.getJSONObject(i);
								TaskInfo task = new TaskInfo();
								task.setProject_name(jo
										.getString("PROJECT_NAME"));
								task.setBegin_code(jo.getString("BEGIN_CODE"));
								task.setFlow_name(jo.getString("FLOW_NAME"));
								task.setFlow_id(jo.getString("FLOW_ID"));
								task.setBegin_code(jo.getString("BEGIN_CODE"));
								task.setName(jo.getString("NAME"));
								task.setBegin_date(jo.getString("BEGIN_DATE"));
								task.setItem_name(jo.getString("ITEM_NAME"));
								task.setState(jo.getString("STATE"));
								task.setZfnum(jo.getString("ZFNUM"));
								task.setPlnum(jo.getString("PLNUM"));
								task.setRr(jo.getString("RR"));
								mListItems.add(task);// 获取数据 赋给mlistems
							}
							// 此处注释掉原因，在构建Adpater是将mlistItemszhi地址传进去啦。只需调用notifyDataSetChanged方法即可
						mAdapter.setData(mListItems);
							mAdapter.notifyDataSetChanged();
						}
						catch (JSONException e)
						{
						}
						popupDialog.dismiss();
					}
					@Override
					public void onFailure(Throwable arg0)
					{
						popupDialog.dismiss();
						Toast.makeText(SearchMainActivity.this, "加载数据失败",
								Toast.LENGTH_SHORT).show();
					}
				});
	}
	// 获取博文数据数据
	private void getBlogData() {
		HttpClient.get(Constants.Route_Blog_List, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String result) {
							JSONArray json_question;
							try {
								json_question = new JSONArray(result);
								for (int i = 0; i < json_question.length(); i++) {
									JSONObject jo = json_question.getJSONObject(i);
									Blog blog = new Blog();
									blog.setBlog_id(jo.getString("BLOG_ID"));
									blog.setSend_operator(jo.getString("SEND_OPERATOR"));
									blog.setSend_username(jo.getString("SEND_USERNAME"));
									blog.setFlow_id(jo.getString("FLOW_ID"));
									blog.setRoute_id(jo.optString("ROUTE_ID", ""));
									blog.setSend_content(jo.getString("BLOG_CONTENT"));
									blog.setIs_redict(jo.getInt("IS_REDICT"));
									blog.setType(jo.getInt("TYPE"));
									blog.setSend_date(jo.getString("SEND_DATE"));
									blog.setFileName(jo.getString("FILE_NAME"));
									List replyList=new ArrayList();
									JSONArray json_replyArray=jo.getJSONArray("replylist"); 
									//回复的前5条信息
									if(json_replyArray!=null&&json_replyArray.length()>0){
										for(int j=0;j<json_replyArray.length();j++){
											JSONObject jo2 = json_replyArray.getJSONObject(j);
											Reply reply=new Reply();
											reply.setReply_id(jo2.getString("REPLY_ID"));
											reply.setReply_operator(jo2.getString("REPLY_OPERATOR"));
											reply.setReply_username(jo2.getString("REPLY_USERNAME"));
											reply.setReply_content(jo2.getString("REPLY_CONTENT"));
											reply.setSoure_operator(jo2.getString("SOURE_OPERATOR"));
											reply.setBlog_id(jo2.getString("BLOG_ID"));
											reply.setReply_date(jo2.getString("REPLY_DATE"));
											replyList.add(reply);
										}
									}
									blog.setReplyList(replyList);
								mListItems.add(blog);
								}
								commentListViewAdapter.setData(mListItems);
								commentListViewAdapter.notifyDataSetChanged();
							} catch (JSONException e) {
								Toast.makeText(SearchMainActivity.this, "暂时无数据", Toast.LENGTH_SHORT).show();
							}
							popupDialog.dismiss();
						}
					});
		}
}
