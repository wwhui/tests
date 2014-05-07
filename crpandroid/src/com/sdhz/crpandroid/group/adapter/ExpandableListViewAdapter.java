package com.sdhz.crpandroid.group.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdhz.crpandroid.R;
import com.sdhz.domain.group.GroupUser;
import com.sdhz.domain.group.UserInfo;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter
{
	private List<com.sdhz.domain.group.GroupUser>	mGroupData;
	LayoutInflater									mInflater;
	private LinkedList<UserInfo>					selectedListUser;

	// 用来控制CheckBox的选中状况
	public ExpandableListViewAdapter(LayoutInflater inflater, Context mcontext,
			List<GroupUser> mGroup)
	{
		mInflater = inflater;
		mGroupData = mGroup;
		selectedListUser = new LinkedList<UserInfo>();
	}

	public class ViewTag
	{
		public TextView		option_name;
		public CheckBox		option_checkbox;
		public ImageView	group_icon;
		public UserInfo		userInfo;
	}

	public List<UserInfo> getSelectedUserInfo()
	{
		return selectedListUser;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return mGroupData.get(groupPosition).getUserInfoList()
				.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewTag mVobj = new ViewTag();
		View v;
		if (convertView != null)
		{
			v = convertView;
		}
		else
		{
			v = mInflater.inflate(R.layout.crp_person_item_isselect, null);
		}
		mVobj.option_name = (TextView) v.findViewById(R.id.option_name);
		mVobj.option_checkbox = (CheckBox) v.findViewById(R.id.option_checkbox);
		mVobj.option_name.setText(mGroupData.get(groupPosition)
				.getUserInfoList().get(childPosition).getName());
		v.setTag(mVobj);
		v.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ViewTag child = (ViewTag) v.getTag();
				if (selectedListUser.contains(child.userInfo))
				{
					selectedListUser.remove(child.userInfo);
				}
				else
				{
					selectedListUser.add(child.userInfo);
				}
				child.option_checkbox.toggle();
			}
		});
		return v;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		// TODO Auto-generated method stub
		return mGroupData.get(groupPosition).getUserInfoList().size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		// TODO Auto-generated method stub
		return mGroupData.get(groupPosition);
	}

	@Override
	public int getGroupCount()
	{
		// TODO Auto-generated method stub
		return mGroupData.size();
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewTag mVobj = new ViewTag();
		View v;

		if (convertView != null)
		{
			v = convertView;
		}
		else
		{
			v = mInflater.inflate(R.layout.crp_person_group, null);
		}
		// /此处的option_name 只是个变量 代指 group_person_name
		mVobj.option_name = (TextView) v.findViewById(R.id.group_person_name);
		mVobj.option_name.setText(mGroupData.get(groupPosition).getGroupName());
		mVobj.group_icon = (ImageView) v.findViewById(R.id.group_icon);

		v.setTag(mVobj);
		// expandlist_up
		if (isExpanded)
		{
			mVobj.group_icon.setImageResource(R.drawable.rowdown);
		}
		else
		{
			mVobj.group_icon.setImageResource(R.drawable.rowleft);
		}
		return v;
	}

	@Override
	public boolean hasStableIds()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return true;
	}
}
