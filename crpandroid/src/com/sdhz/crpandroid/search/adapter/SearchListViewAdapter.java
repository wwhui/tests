package com.sdhz.crpandroid.search.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.task.CommentDetailViewActivity;
import com.sdhz.crpandroid.task.PointMainListActivity;
import com.sdhz.crpandroid.task.adapter.CommentSecondListViewAdapter;
import com.sdhz.domain.blog.Blog;
import com.sdhz.domain.task.TaskInfo;
import com.sdhz.view.ChildListView;

public class SearchListViewAdapter extends BaseAdapter implements
		OnClickListener {
	private Context mContext;
	private LayoutInflater listContainer;
	private List mListItems;// 装配的数据可能是TasKInfo 也可能是BLog
	ViewHolder holder;

	public SearchListViewAdapter(Context context, List listItems) {
		this.mContext = context;
		this.mListItems = listItems;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
	}

	public void addItem(List listItems) {
		this.mListItems.addAll(listItems);
	}

	public void setData(List listItems) {
		this.mListItems = listItems;
	}

	@Override
	public int getCount() {
		return mListItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mListItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			Object object=mListItems.get(position);
			ViewHolder holder = null;
			if(convertView!=null){
				holder = (ViewHolder) convertView.getTag();
			}else{
					convertView = listContainer
							.inflate(R.layout.search_list_item_task, null);
					holder.item_type = (TextView) convertView
							.findViewById(R.id.item_type);
					holder.main_person_name = (TextView) convertView
							.findViewById(R.id.main_person_name);
					holder.main_time = (TextView) convertView
							.findViewById(R.id.main_time);
					holder.main_content = (TextView) convertView
							.findViewById(R.id.main_content);
			}
			if(object instanceof Blog){
				Blog  blog=(Blog) object;
				holder.item_type.setText("博文");
				holder.main_person_name.setText(blog.getSend_username());
				holder.main_time.setText(blog.getSend_date());
				holder.main_content.setText(blog.getSend_content());
				holder.blog=blog;
			}else if(object instanceof TaskInfo){
				TaskInfo info=(TaskInfo) object;
				holder.taskInfo=info;
				holder.item_type.setText("流程");
				holder.main_person_name.setText(info.getName());
				holder.main_time.setText(info.getBegin_date());
				holder.main_content.setText(info.getFlow_id());
			}
			convertView.setOnClickListener(this);
			convertView.setTag(holder);
		return convertView;
	}

	public void addItem(Object object) {
		mListItems.add(object);
	}

	// 自定义控件集合
	class ViewHolder {
		public TextView item_type;//类型 可能是 BLog或者是TaskInfo
		public TextView main_person_name;//发起人 
		public TextView main_time;//发起时间
		public TextView main_content;//主要内容
		public TaskInfo taskInfo;
		public Blog blog;
	}

	@Override
	public void onClick(View v) {
		ViewHolder  ho=(ViewHolder) v.getTag();
		Intent intent=new Intent();
		if(ho.taskInfo!=null){
			intent.putExtra("taskInfo", ho.taskInfo);
			intent.setClass(mContext, PointMainListActivity.class);
			mContext.startActivity(intent);
		}else if(ho.blog!=null){
			intent.setClass(mContext, CommentDetailViewActivity.class);
			intent.putExtra("blogInfo", ho.blog);
			mContext.startActivity(intent);
		}
	}
}
