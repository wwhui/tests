package com.sdhz.crpandroid.task;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.blog.MainBlogListActivity;
import com.sdhz.crpandroid.task.adapter.TaskListViewAdapter;
import com.sdhz.crpandroid.widget.PopMenu;
import com.sdhz.crpandroid.widget.PopupDialog;
import com.sdhz.crpandroid.widget.PopupFailDialog;
import com.sdhz.dao.TaskTypeDao;
import com.sdhz.dao.impl.TasktypeDaoImpl;
import com.sdhz.domain.task.TaskInfo;
import com.sdhz.view.XListView;
import com.sdhz.view.XListView.IXListViewListener;

/**
 * 主页显示之一 对应第一个
 * 
 * 工单列表信息 布局文件 R.layout.task_main_activity_list_view 单条布局文件
 * ：R.layout.task_main_activity_list_item
 * 
 * */
public class TaskMainListView extends Activity implements IXListViewListener
{
	private XListView				info_main_listview;
	private LinkedList<TaskInfo>	mListItems;				// 数据集合
	private TaskListViewAdapter		mAdapter;				// 适配器
	private TextView				title_bar_text;			// 标题
	private PopupWindow				rightPopMenu;
	private PopMenu					popMenu;
	private int						page		= 0;
	private ProgressBar				progressBar_loading;	// 加载条。。在网络请求时显示出来
	private RequestParams			params;
	private PopupDialog				popupDialog;			// 加载popupwindow
	private Handler					mHandler;				// 用于检测activity的初始化完毕没有，popupDialog如果直接初始化，会报空指针
	private int						detachtime	= 5;		// 每5秒检测一次
	private PopupFailDialog			popupFailDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_main_activity_list_view);
		initLeftButton();// 初始化左侧按钮 添加监听
		initRightButton();// 初始化右侧按钮 添加监听
		initTitleBar();// title 监听 添加监听
		progressBar_loading = (ProgressBar) findViewById(R.id.progressBar_loading);
		progressBar_loading.setVisibility(View.VISIBLE);
		info_main_listview = (XListView) findViewById(R.id.xlist_view);
		// 首先new 一个是先让ListView加载出来然后，防止卡死在页面
		mListItems = new LinkedList<TaskInfo>();
		mAdapter = new TaskListViewAdapter(TaskMainListView.this, mListItems);
		info_main_listview.setAdapter(mAdapter);
		params = new RequestParams();
		mHandler = new Handler();
		info_main_listview.setPullLoadEnable(true);// 注意 是否分页
		info_main_listview.setPullRefreshEnable(true);
		info_main_listview.setXListViewListener(this);// 添加监听
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		mListItems.clear();
		mAdapter.notifyDataSetChanged();
		getData();
	}


	// 用来循环检测activity是否初始化完毕，一旦初始化完毕就再初始化popupwindow
	private void showPopupDialog()
	{
		popupDialog = new PopupDialog(TaskMainListView.this);

		Runnable runnable = new Runnable()
		{

			@Override
			public void run()
			{
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
	// 用来循环检测activity是否初始化完毕，一旦初始化完毕就再初始化popupwindow
	private void showPopupFailDialog()
	{
		popupFailDialog = new PopupFailDialog(TaskMainListView.this);
		
		Runnable runnable = new Runnable()
		{
			
			@Override
			public void run()
			{
				View view=getWindow().getDecorView();
//				View view = findViewById(R.id.layout_main);// activity中根元素
				if (view != null && view.getWidth() > 0 && view.getHeight() > 0)
				{
					popupFailDialog.show(view);
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

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		popupDialog.dismiss();
		return super.onTouchEvent(event);
	}
	
	private void initTitleBar()
	{
		title_bar_text = (TextView) findViewById(R.id.btn_title_popmenu);
		title_bar_text.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				popMenu.showAsDropDown(v);
			}
		});
		TaskTypeDao dao = TasktypeDaoImpl.getInstance(this);
		final List<String> items = dao.getTypeInfos();
//		items.add("全部");
		popMenu = new PopMenu(TaskMainListView.this, items,
				new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3)
					{
						title_bar_text.setText(items.get(arg2).toString());
						mListItems.clear();
						params.put("pageIndex", "0");
						params.put("flowName", items.get(arg2).toString());
						progressBar_loading.setVisibility(View.VISIBLE);
						getData();// 将查询条件传入到服务器端，同时将page,mListItems 重置
						popMenu.dismiss();
					}
				});
	}

	// /添加左侧按钮事件
	private void initLeftButton()
	{
		((Button) findViewById(R.id.left_button))
				.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Intent intent = new Intent(TaskMainListView.this,
								CreateNewComentActivity.class);
						startActivity(intent);
					}
				});

	}

	/**
	 * 右侧 按钮时间
	 * 
	 * */
	private void initRightButton()
	{
		((Button) findViewById(R.id.right_button))
				.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						showPopWindow(v);
					}
				});
	}

	private void showPopWindow(View parent)
	{

		View view = LayoutInflater.from(TaskMainListView.this).inflate(
				R.layout.activity_popmenu, null);
		rightPopMenu = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// popupWindow = new PopupWindow(view,
		// TabHostActivity.this.getResources()
		// .getDimensionPixelSize(R.dimen.popmenu_width),
		// LayoutParams.WRAP_CONTENT);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		// popupWindow.setBackgroundDrawable(new BitmapDrawable());
		rightPopMenu.showAsDropDown(parent,
				10,
				// 保证尺寸是根据屏幕像素密度来的
				TaskMainListView.this.getResources().getDimensionPixelSize(
						R.dimen.popmenu_yoff));

		// 使其聚集
		rightPopMenu.setFocusable(true);
		rightPopMenu.getContentView().setOnTouchListener(
				new View.OnTouchListener()
				{

					@Override
					public boolean onTouch(View arg0, MotionEvent arg1)
					{
						rightPopMenu.setFocusable(false);// 失去焦点
						rightPopMenu.dismiss();// 消除pw
						return true;
					}
				});
		// 设置允许在外点击消失
		rightPopMenu.setOutsideTouchable(true);
		// 刷新状态
		rightPopMenu.update();

		// 刷新
		TextView tvRefresh = (TextView) view
				.findViewById(R.id.popmenu_item_refresh);
		tvRefresh.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				rightPopMenu.dismiss();
				Intent intent2=new Intent(TaskMainListView.this, MainBlogListActivity.class);
				startActivity(intent2);

			}
		});

	}

	private void getData()
	{
		showPopupDialog();
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
							// mAdapter.add(mListItems);
							mAdapter.notifyDataSetChanged();
							progressBar_loading.setVisibility(View.GONE);
						}
						catch (JSONException e)
						{
							popupDialog.dismiss();
						}
						popupDialog.dismiss();
					}
					@Override
					public void onFailure(Throwable arg0)
					{
						progressBar_loading.setVisibility(View.GONE);
						popupDialog.dismiss();
						showPopupFailDialog();
						Toast.makeText(TaskMainListView.this, "加载数据失败",
								Toast.LENGTH_SHORT).show();
						new MyHandler().sendEmptyMessageDelayed(0, 5000);
						
					}
				});
	}
	class MyHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			popupFailDialog.dismiss();
		}
	}
	@Override
	public void onRefresh()
	{
		if (mListItems != null && mListItems.size() > 0)
		{
			mListItems.clear();
		}
		progressBar_loading.setVisibility(View.VISIBLE);
		params.put("pageIndex", String.valueOf(0));
		page=0;
		getData();
		onLoad();
	}

	@Override
	public void onLoadMore()
	{
		page++;
		progressBar_loading.setVisibility(View.VISIBLE);
		params.put("pageIndex", String.valueOf(page));
		getData();
		onLoad();
	}

	private void onLoad()
	{
		info_main_listview.stopRefresh();
		info_main_listview.stopLoadMore();
		info_main_listview.setRefreshTime("刚刚");
	}
}
