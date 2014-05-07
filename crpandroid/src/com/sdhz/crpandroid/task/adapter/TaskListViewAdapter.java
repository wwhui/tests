package com.sdhz.crpandroid.task.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.hzsoft.util.AsyncBitmapLoader;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.task.CommentListViewActivity;
import com.sdhz.crpandroid.task.GlobalParams;
import com.sdhz.crpandroid.task.PointMainListActivity;
import com.sdhz.domain.blog.Blog;
import com.sdhz.domain.task.TaskInfo;

/**
 * 工单信息 adapter
 * 
 * @author TOSHIBA 单条的布局文件 R.layout. task_main_activity_list_item 存在的事件 (1) 转发
 *         (2)评论 (3)Item 的OnClick时间
 */
public class TaskListViewAdapter extends BaseAdapter implements OnClickListener
{
	// public int count = 10;
	private Context					mContext;
	private LayoutInflater			listContainer;
	private List<TaskInfo>			mListItems;
	private PopupWindow				mPopupWindow;
	private CommentListViewActivity	comm;

	// 自定义控件集合
	public final class ViewHolder
	{
		public TextView		main_person_name;
		public TextView		main_time;
		public TextView		main_content;
		public TextView		main_title;
		public TextView		flow_name;	//流程类型
		public TaskInfo		info;
		private ImageView	person_image;
		public Button		btn_comment;	// 评论
		public Button		btn_redirect;		// /转发
	}

	public TaskListViewAdapter(Context context, List<TaskInfo> listItems)
	{
		this.mContext = context;
		this.mListItems = listItems;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
	}

	public void add(List<TaskInfo> listItems)
	{
		this.mListItems.addAll(listItems);
	}

	public void setData(List<TaskInfo> listItems)
	{
		this.mListItems = listItems;
	}

	@Override
	public int getCount()
	{
		return mListItems.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mListItems.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		// 此处使用if else 判断 是用来是用ListView的缓存的
		// view 存在 只需重置下Tag() 属性即可，不存在 则重新创建
		if (convertView == null)
		{
			holder = new ViewHolder();
			// 加载布局文件
			convertView = listContainer.inflate(
					R.layout.task_main_activity_list_item, null);
			holder.main_person_name = (TextView) convertView
					.findViewById(R.id.main_person_name);
			holder.main_time = (TextView) convertView
					.findViewById(R.id.main_time);
			holder.main_content = (TextView) convertView
					.findViewById(R.id.main_content);
			holder.main_title = (TextView) convertView
					.findViewById(R.id.main_title);
			holder.flow_name = (TextView) convertView
					.findViewById(R.id.flow_name);
			holder.btn_comment = (Button) convertView
					.findViewById(R.id.btn_comment);
			holder.btn_redirect = (Button) convertView
					.findViewById(R.id.btn_redirect);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		TaskInfo info = mListItems.get(position);// postion即位置
		holder.info = info;
		if (info != null)
		{
			holder.main_person_name.setText(info.getName());
			holder.main_time.setText(info.getBegin_date());
			holder.main_content.setText("流程ID：" + info.getFlow_id());
			holder.main_title.setText("流程名称：" + info.getItem_name());
			holder.flow_name.setText("流程类型：" + info.getFlow_name());
			//添加下划线
			holder.flow_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		}
		// 转发
		holder.btn_redirect.setTag(info);
		holder.btn_redirect.setText("转发(" + info.getZfnum() + ")");
		holder.btn_redirect.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				TaskInfo info = (TaskInfo) v.getTag();
				Intent intent2 = new Intent(mContext,
						CommentListViewActivity.class);// CommentListViewActivity
														// 评论列表 根据传递值的不同 来决定
				// ，comment的显示的不同的数据
				intent2.putExtra(GlobalParams.Data, info);
				intent2.putExtra(GlobalParams.IS_Direct, "1");
				mContext.startActivity(intent2);
			}
		});

		// 评论传值
		holder.btn_comment.setTag(info);
		holder.btn_comment.setText("评论(" + info.getPlnum() + ")");
		// 评论
		holder.btn_comment.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				TaskInfo info = (TaskInfo) v.getTag();
				Intent intent2 = new Intent(mContext,
						CommentListViewActivity.class);// CommentListViewActivity
														// 评论列表 根据传递值的不同 来决定
				// ，comment的显示的不同的数据
				intent2.putExtra(GlobalParams.Data, info);
				intent2.putExtra(GlobalParams.IS_Direct, "0");
				mContext.startActivity(intent2);
			}
		});
		// 设置控件集到convertView
		convertView.setTag(holder);// 方便下面的监听时间获取到 所用的的对象
		convertView.setOnClickListener(this);// 给当前的Item (ListView 中的单条) 添加监听事件
		return convertView;
	}

	public void addItem(TaskInfo info)
	{
		mListItems.add(info);
	}

	// 点击item
	@Override
	public void onClick(View v)
	{
		ViewHolder holder = (ViewHolder) v.getTag();
		Intent intent = new Intent(mContext, PointMainListActivity.class);
		// 将taskInfo 传递给路由PointMainListActivity，注意传递的对象必须实现序列化接口
		intent.putExtra("taskInfo", holder.info);
		intent.putExtra("name", holder.info.getItem_name());
		mContext.startActivity(intent);
	}

}
