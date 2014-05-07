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
 * route  ·����Ϣ
 * ��TaskMainListView��Item �����¼� ��ת��
 * ��Ҫ��ȡ�Ķ��� �� TaskInfo     ͨ��TaskInfo  ����ѯ·����Ϣ
 * ��Ӧ�Ĳ����ļ� ��R.layout.task_point_list_view
 * �����Ĳ����ļ���R.layout.task_point_list_item
 * 
 * */
public class PointMainListActivity extends BaseActivity {
	private ListView listView;//����listView
	private LinkedList<TaskRoute> pointsList;//���ݼ���
	private TaskPointListViewAdapter adapter;
	private ProgressBar progressBar_loading;
	private TaskInfo info;
	private Intent intent;
	private PopupDialog				popupDialog;			// ����popupwindow
	private Handler					mHandler=new Handler();				// ���ڼ��activity�ĳ�ʼ�����û�У�popupDialog���ֱ�ӳ�ʼ�����ᱨ��ָ��
	private int						detachtime	= 5;		// ÿ5����һ��
	private PopupWindow popupWindow;
	private String content="";
	private TextView  titleName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//��titleBar���ص�
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
						PointMainListActivity.this.finish();//�����¼�
					}
				});
		((Button) findViewById(R.id.right_button))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(PointMainListActivity.this,
								CreateNewComentActivity.class);//����һ�����ڹ���������
						intent.putExtra(GlobalParams.Data, info);//��taskInfo ���ݸ�CreateNewComentActivity
						startActivity(intent);
					}
				});
		progressBar_loading = (ProgressBar) findViewById(R.id.progressBar_loading);
		progressBar_loading.setVisibility(View.VISIBLE);
	}
	// ����ѭ�����activity�Ƿ��ʼ����ϣ�һ����ʼ����Ͼ��ٳ�ʼ��popupwindow
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
//				View view = findViewById(R.id.layout_main);// activity�и�Ԫ��
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
		getData(params);//��ѯ����
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
		// ʹ��ۼ�
		popupWindow.setFocusable(true);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
	}
	// ��ȡ����
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
								route.setCur_task_state(jo.getString("CUR_TASK_STATE"));//����״̬
								route.setCur_role(jo.getString("CUR_ROLE"));
								route.setCur_code(jo.getString("CUR_CODE"));
								route.setCommit_date(jo.getString("COMMIT_DATE"));
								route.setCur_name(jo.getString("NAME"));
								route.setAudit_content(jo.getString("AUDIT_CONTENT"));
								route.setZfNum(jo.getString("ZFNUM"));
								route.setPlNum((jo.getString("PLNUM")));
								pointsList.add(route);
							}
							//setData  �����ݼ��ϸ���adpater �����ݼ���
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
