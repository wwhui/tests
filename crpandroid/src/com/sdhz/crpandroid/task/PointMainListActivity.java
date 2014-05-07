package com.sdhz.crpandroid.task;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hzsoft.util.Constants;
import com.hzsoft.util.DensityUtil;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.BaseActivity;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.task.adapter.TaskPointListViewAdapter;
import com.sdhz.crpandroid.widget.PopupDialog;
import com.sdhz.domain.task.TaskInfo;
import com.sdhz.domain.task.TaskRoute;
/**
 * 
 * route  路由信息
 * 由TaskMainListView的Item 单击事件 跳转到
 * 需要获取的对象 ： TaskInfo     通过TaskInfo  来查询路由信息
 * 对应的布局文件 ：R.layout.task_point_list_view
 * 单条的布局文件：R.layout.task_point_list_item
 * 
 * */
public class PointMainListActivity extends BaseActivity {
	private ListView listView;//布局listView
	private LinkedList<TaskRoute> pointsList;//数据集合
	private TaskPointListViewAdapter adapter;
	private ProgressBar progressBar_loading;
	private TaskInfo info;
	private Intent intent;
	private PopupDialog				popupDialog;			// 加载popupwindow
	private Handler					mHandler=new Handler();				// 用于检测activity的初始化完毕没有，popupDialog如果直接初始化，会报空指针
	private int						detachtime	= 5;		// 每5秒检测一次
	private PopupWindow popupWindow;
	private String content="";
	private TextView  titleName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//将titleBar隐藏掉
		setContentView(R.layout.task_point_list_view);
		pointsList = new LinkedList<TaskRoute>();
		initTitleBar();
		initListView();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO Auto-generated method stub
		popupDialog.dismiss();
		return super.onTouchEvent(event);
	}
	private void initTitleBar() {
		intent=getIntent();
		String name=intent.getStringExtra("name");
			 titleName= (TextView) findViewById(R.id.title_bar);
			titleName.setText(name);
			titleName.requestFocus();
			titleName.setTextColor(Color.WHITE);
			titleName.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if("".equals(content)){
						 TaskInfo info= (TaskInfo) getIntent().getSerializableExtra("taskInfo");
						 if(info!=null){
							 RequestParams params=new RequestParams();
							 params.put("flow_id", info.getFlow_id());
							 getTaskDetail(params);
						 }
					}else{
						popWindow();
					}
				}
			});
		((Button) findViewById(R.id.left_button))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						PointMainListActivity.this.finish();//后退事件
					}
				});
		((Button) findViewById(R.id.right_button))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(PointMainListActivity.this,
								CreateNewComentActivity.class);//创建一个关于工单的评论
						intent.putExtra(GlobalParams.Data, info);//将taskInfo 传递给CreateNewComentActivity
						startActivity(intent);
					}
				});
		progressBar_loading = (ProgressBar) findViewById(R.id.progressBar_loading);
		progressBar_loading.setVisibility(View.VISIBLE);
	}
	// 用来循环检测activity是否初始化完毕，一旦初始化完毕就再初始化popupwindow
	private void showPopupDialog()
	{
		// TODO Auto-generated method stub
		popupDialog = new PopupDialog(PointMainListActivity.this);

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
	private void initListView() {
		listView = (ListView) findViewById(R.id.xlist_view);
		adapter = new TaskPointListViewAdapter(this, pointsList);
		listView.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		pointsList.clear();
		adapter.clear();
		adapter.notifyDataSetChanged();
		info = (TaskInfo) getIntent().getSerializableExtra("taskInfo");
		RequestParams params = new RequestParams();
		params.put("flow_id", info.getFlow_id());
		getData(params);//查询数据
	}
	private Handler myHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1){
				popWindow();
			}
		}
	};
	
	
	private  void popWindow(){
		View view = LayoutInflater.from(this)
				.inflate(R.layout.task_content_detail, null);
		TextView  textView=(TextView) view.findViewById(R.id.content);
		textView.setText(content);
		if(popupWindow==null){
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			popupWindow.setFocusable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
		}
		int offzie=DensityUtil.px2dip(this,getWindow().getDecorView().getWidth()/2-titleName.getWidth()/2);
		popupWindow.showAsDropDown(titleName,-offzie , 0);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
	}
	// 获取数据
	private void getData(RequestParams params) {
		showPopupDialog();
		HttpClient.get(Constants.Route_Point_List, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String result) {
						JSONArray json_question;
						try {
							json_question = new JSONArray(result);
							for (int i = 0; i < json_question.length(); i++) {
								JSONObject jo = json_question.getJSONObject(i);
								TaskRoute route = new TaskRoute();
								route.setFlow_id(jo.getString("FLOW_ID"));
								route.setRoute_id(jo.getString("ROUTE_ID"));
								route.setReceive_date(jo.getString("RECEIVE_DATE"));
								route.setCur_task(jo.getString("CUR_TASK"));
								route.setCur_task_state(jo.getString("CUR_TASK_STATE"));//处理状态
								route.setCur_role(jo.getString("CUR_ROLE"));
								route.setCur_code(jo.getString("CUR_CODE"));
								route.setCommit_date(jo.getString("COMMIT_DATE"));
								route.setCur_name(jo.getString("NAME"));
								route.setAudit_content(jo.getString("AUDIT_CONTENT"));
								route.setZfNum(jo.getString("ZFNUM"));
								route.setPlNum((jo.getString("PLNUM")));
								pointsList.add(route);
							}
							//setData  将数据集合赋给adpater 中数据集合
						//	adapter.setData(pointsList);
							adapter.notifyDataSetChanged();
							progressBar_loading.setVisibility(View.GONE);
							popupDialog.dismiss();
						} catch (JSONException e) {
						}
						super.onSuccess(result);
					}
				});
	}

	private void getTaskDetail(RequestParams params){
		showPopupDialog();
		HttpClient.get(Constants.Task_Detail, params, new AsyncHttpResponseHandler(){
			@Override
			@Deprecated
			public void onSuccess(String content) {
				PointMainListActivity.this.content=content;
				myHandler.sendEmptyMessage(1);
				popupDialog.dismiss();
			}
		});
	}
	
}
