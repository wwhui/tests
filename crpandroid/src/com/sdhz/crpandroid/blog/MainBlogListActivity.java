package com.sdhz.crpandroid.blog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.task.CreateNewComentActivity;
import com.sdhz.crpandroid.task.GlobalParams;
import com.sdhz.crpandroid.task.adapter.CommentListViewAdapter;
import com.sdhz.crpandroid.widget.PopupDialog;
import com.sdhz.domain.blog.Blog;
import com.sdhz.domain.blog.Reply;
import com.sdhz.view.XListView;
import com.sdhz.view.XListView.IXListViewListener;

public class MainBlogListActivity extends Activity implements IXListViewListener{

	private XListView commentListView;
	private CommentListViewAdapter commentListViewAdapter;
	private List dataList;
	private int  page=0;
	private Intent  intent;//全据Intent 用来页面的跳转
	private ProgressBar progressBar_loading;
	private RequestParams params;//全局参数的传递
	private PopupDialog				popupDialog;			// 加载popupwindow
	private Handler					mHandler=new Handler();				// 用于检测activity的初始化完毕没有，popupDialog如果直接初始化，会报空指针
	private int						detachtime	= 5;		// 每5秒检测一次
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.task_comment_list_view);
		intent=new Intent();
		params = new RequestParams();
		dataList = new LinkedList();
		progressBar_loading=(ProgressBar) findViewById(R.id.progressBar_loading);
		//初始化titleBar
		initTitleBar();
		initListData();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		popupDialog.dismiss();
		return super.onTouchEvent(event);
	}
	
	// 用来循环检测activity是否初始化完毕，一旦初始化完毕就再初始化popupwindow
	private void showPopupDialog()
	{
		popupDialog = new PopupDialog(MainBlogListActivity.this);

		Runnable runnable = new Runnable()
		{

			@Override
			public void run()
			{
				View view=getWindow().getDecorView();// activity中根元素
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
	private void initTitleBar() {
		//初始化 title
		((TextView) findViewById(R.id.title_bar)).setTextColor(Color.WHITE);
		((Button) findViewById(R.id.left_button))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MainBlogListActivity.this.finish();
					}
				});
		
		((Button) findViewById(R.id.right_button))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 发起一个新的评论 针对此 路由信息
						intent.setClass(
								MainBlogListActivity.this,
								CreateNewComentActivity.class);
						startActivity(intent);
					}
				});
	}

	private void initListData() {
		commentListView = (XListView) findViewById(R.id.xlist_view);
		commentListViewAdapter = new CommentListViewAdapter(this, dataList);
		commentListView.setAdapter(commentListViewAdapter);
		commentListView.setPullLoadEnable(true);//注意 是否分页
		commentListView.setPullRefreshEnable(true);
		commentListView.setXListViewListener(this);
	}
	public void  initData(){
		dataList.clear();
		//intent.putExtra("data", route);//统一的由data 向CreateNewComment传递数据
		Object object =  getIntent().getSerializableExtra(GlobalParams.Data);//data 可能是TaksInfo 可能是routeInfo
		TextView title= (TextView) findViewById(R.id.title_bar);
		title.setText("贴子列表");
		getData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		dataList.clear();
		page=0;
		initData();
	}
		// 获取数据
	private void getData() {
		showPopupDialog();
			progressBar_loading.setVisibility(View.VISIBLE);
			params.put("pageIndex", String.valueOf(page));
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
									blog.setFileName(jo.getString("FILE_NAME"));
									blog.setSend_date(jo.getString("SEND_DATE"));
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
									dataList.add(blog);
								}
							//	commentListViewAdapter.setData(dataList);
								progressBar_loading.setVisibility(View.GONE);
								commentListViewAdapter.notifyDataSetChanged();
							} catch (JSONException e) {
								progressBar_loading.setVisibility(View.GONE);
								Toast.makeText(MainBlogListActivity.this, "暂时无数据", Toast.LENGTH_SHORT).show();
							}
							popupDialog.dismiss();
							super.onSuccess(result);
						}
					});
		}
	//刷新
	@Override
	public void onRefresh() {
		page=0;
		dataList.clear();
		getData();
		onLoad();
	}
	//上啦更多
	@Override
	public void onLoadMore() {
		page++;
		getData();
		onLoad();
	}
	private void onLoad() {
		commentListView.stopRefresh();
		commentListView.stopLoadMore();
		commentListView.setRefreshTime("刚刚");
	}
}
