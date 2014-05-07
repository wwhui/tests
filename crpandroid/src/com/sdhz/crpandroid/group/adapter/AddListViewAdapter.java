package com.sdhz.crpandroid.group.adapter;

import java.util.List;

import android.content.Context;
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
import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.CrpaApplication;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.task.adapter.CommentSecondListViewAdapter;
import com.sdhz.crpandroid.widget.PopInput;
import com.sdhz.dao.UserInfoDao;
import com.sdhz.dao.impl.UserDaoImpl;
import com.sdhz.domain.group.UserInfo;
import com.sdhz.view.ChildListView;

public class AddListViewAdapter extends BaseAdapter 
{
	private Context						mContext;
	private LayoutInflater				listContainer;
	private List<UserInfo>	mListItems;
	private AsyncBitmapLoader			asyncBitmapLoader;
	private PopInput					popInput;
	ViewAdd	holder;
	public static String text[]=new String[]{"0","1","2","3","4"};
	public AddListViewAdapter(Context context,
			List<UserInfo> listItems)
	{
		asyncBitmapLoader = new AsyncBitmapLoader();

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
		ViewAdd	holder=null;
		/** 1.初始化 Item 中的view */
		if (convertView == null)
		{
			holder = new ViewAdd();
			convertView = listContainer.inflate(
					R.layout.add_list_item, null);
			holder.comment_title = (TextView) convertView
					.findViewById(R.id.comment_title);
			holder.comment_person = (TextView) convertView
					.findViewById(R.id.comment_person);
			holder.addbtn = (Button) convertView
					.findViewById(R.id.addbtn);
		}
		else
		{
			holder = (ViewAdd) convertView.getTag();
		}
		
		holder.addbtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Button btn=(Button) v;
				UserInfo  userInfo=(UserInfo) v.getTag();
				Toast.makeText(mContext,"",Toast.LENGTH_SHORT).show();
				UserInfoDao	userInfoDao=UserDaoImpl.getInstance(mContext);
				String u_id= ( (CrpaApplication)mContext.getApplicationContext()).getAccountString();
				String operator_id=userInfo.getOperator_id();
				userInfoDao.addUserInfo(userInfo, operator_id);
				RequestParams params=new RequestParams();
				params.put("u_id", u_id);
				params.put("operator_id", operator_id);
				intiData(params);
				 btn.setBackgroundResource(R.drawable.button_add_color);
				 btn.setTextColor(Color.BLUE);
				 btn.setClickable(false);
				
			}
		});
		/** 2. Item 中的view每个组件赋值 */
		UserInfo userInfo = mListItems.get(position);
		holder.userInfo=userInfo;
		if (userInfo != null)
		{
			holder.comment_title.setText(userInfo.getOperator_id());
			holder.comment_person.setText(userInfo.getName());
		}
		holder.addbtn.setTag(userInfo);
		convertView.setTag(holder);
		return convertView;
	}

	public void addImageItem(UserInfo userInfo)
	{
		mListItems.add(userInfo);
	}
	
	public void intiData(RequestParams params)
	{
		HttpClient.get(Constants.ADDFRIEND, params, new AsyncHttpResponseHandler()
		{

			@Override
			public void onSuccess(String result)
			{
				// TODO Auto-generated method stub
				super.onSuccess(result);
			}
			
		});
	}
}
// 自定义控件集合
class ViewAdd
{
	public TextView						comment_title;
	public TextView						comment_time;
	public TextView						comment_person;
	public Button						moreButton;
	public ChildListView				xListView;
	public Button						addbtn;
	public CommentSecondListViewAdapter	secondListAdapter;
	public UserInfo userInfo;
}
