package com.sdhz.crpandroid.group.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hzsoft.util.AsyncBitmapLoader;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.group.TalkActivity;
import com.sdhz.crpandroid.task.adapter.CommentSecondListViewAdapter;
import com.sdhz.crpandroid.widget.PopInput;
import com.sdhz.domain.group.GroupInfo;
import com.sdhz.domain.task.TaskInfo;
import com.sdhz.view.ChildListView;

public class GroupListAdapter extends BaseAdapter implements OnClickListener
{
	private Context						mContext;
	private LayoutInflater				listContainer;
	private List<GroupInfo>	mListItems;
	private PopInput					popInput;
	ViewHolder	holder;
	GroupInfo groupInfo;
	public GroupListAdapter(Context context,
			List<GroupInfo> listItems)
	{
		this.mContext = context;
		this.mListItems = listItems;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
	}

	public void addItem(List<GroupInfo> listItems)
	{
		this.mListItems.addAll(listItems);
	}
	public void setData(List<GroupInfo> listItems)
	{
		this.mListItems=listItems;
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
		ViewHolder	holder=null;
		/** 1.初始化 Item 中的view */
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = listContainer.inflate(
					R.layout.group_list_item, null);
			holder.comment_title = (TextView) convertView
					.findViewById(R.id.comment_title);
			holder.tv_projectname = (TextView) convertView
					.findViewById(R.id.tv_projectname);
			holder.tv_groupintro=(TextView) convertView.findViewById(R.id.tv_groupintro);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		/** 2. Item 中的view每个组件赋值 */
		groupInfo = mListItems.get(position);
		holder.groupInfo=groupInfo;
		if (groupInfo != null)
		{
//			holder.comment_title.setText(groupInfo.getG_id());
			holder.comment_title.setVisibility(View.GONE);
//			holder.tv_groupintro.setVisibility(View.INVISIBLE);
			holder.tv_groupintro.setText(groupInfo.getLable());
			holder.tv_projectname.setText(groupInfo.getGroupname());
		}
		convertView.setTag(holder);
		convertView.setOnClickListener(this);
		return convertView;
	}

	public void addImageItem(GroupInfo groupInfo)
	{
		mListItems.add(groupInfo);
	}
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		ViewHolder holder = (ViewHolder) v.getTag();
		Intent intent = new Intent(mContext, TalkActivity.class);
		intent.putExtra("groupInfo", holder.groupInfo);
		intent.putExtra("groupname", holder.groupInfo.getGroupname());
		intent.putExtra("g_id", holder.groupInfo.getG_id());
		mContext.startActivity(intent);
	}
// 自定义控件集合
	class ViewHolder
	{
		public TextView						comment_title;
		public TextView						comment_time;
		public TextView						tv_projectname;
		public TextView						tv_groupintro;
		public GroupInfo groupInfo;
	}

}
