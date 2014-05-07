package com.sdhz.crpandroid.group.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sdhz.crpandroid.R;
import com.sdhz.domain.group.UserInfo;

public class GroupMemberListAdapter extends BaseAdapter implements
		OnClickListener
{
	private Context			mContext;
	private LayoutInflater	listContainer;
	private List<UserInfo>	mListItems;

	public GroupMemberListAdapter(Context context, List<UserInfo> listItems)
	{
		this.mContext = context;
		this.mListItems = listItems;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
	}

	public void addItem(List<UserInfo> listItems)
	{
		this.mListItems.addAll(listItems);
	}

	public void setData(List<UserInfo> listItems)
	{
		this.mListItems = listItems;
		this.notifyDataSetChanged();
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
		
		/** 1.初始化 Item 中的view */
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = listContainer.inflate(R.layout.groupmember_list_item,
					null);
			holder.tv_account = (TextView) convertView
					.findViewById(R.id.tv_account);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_longphone = (TextView) convertView
					.findViewById(R.id.tv_longphone);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		/** 2. Item 中的view每个组件赋值 */
		UserInfo userInfo = mListItems.get(position);
		holder.userInfo = userInfo;
		if(userInfo!=null)
		{
			holder.tv_account.setText("账号：" + userInfo.getOperator_id());
			holder.tv_name.setText("名字：" + userInfo.getName());
			holder.tv_longphone.setText("号码：" + userInfo.getLong_phone());
		}
		convertView.setOnClickListener(this);
		return convertView;
	}

	public void addItem(UserInfo userInfo)
	{
		mListItems.add(userInfo);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		ViewHolder holder = (ViewHolder) v.getTag();
		String phone=holder.userInfo.getLong_phone();
		if(phone==null||"".equals(phone.trim()))
		{
			Toast.makeText(mContext, "没有电话号码", Toast.LENGTH_SHORT).show();
		}
		Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
		mContext.startActivity(intent);
	}
	// 自定义控件集合
	class ViewHolder
	{
		public TextView	tv_account;	// 账号
		public TextView	tv_name;		// 名字
		public TextView	tv_longphone;	// 号码
		public UserInfo	userInfo;
	}


}
