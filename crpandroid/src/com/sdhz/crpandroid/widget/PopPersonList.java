package com.sdhz.crpandroid.widget;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.widget.adpter.PopPersonAdapter;
import com.sdhz.dao.UserInfoDao;
import com.sdhz.dao.impl.UserDaoImpl;
import com.sdhz.domain.group.UserInfo;

public class PopPersonList
{
	private ListView			info_main_listview;
	private PopupWindow			popWinodw;
	private List<UserInfo>		selectUserList;
	private Context				mContext;
	private PopPersonAdapter	adapter;
	private Button				btnSave;
	private View				view;

	public PopPersonList(Context mContext)
	{
		super();
		this.mContext = mContext;
		view = LayoutInflater.from(mContext).inflate(
				R.layout.pop_winow_select_user, null);
		popWinodw = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, true);
		popWinodw.setBackgroundDrawable(new BitmapDrawable());
		popWinodw.setFocusable(true);
		popWinodw.setOutsideTouchable(true);
		UserInfoDao dao = UserDaoImpl.getInstance(mContext);
		selectUserList = new LinkedList<UserInfo>();
		adapter = new PopPersonAdapter(selectUserList, mContext);
		info_main_listview = (ListView) view.findViewById(R.id.personList);
		info_main_listview.setAdapter(adapter);
		init();
		initData();
	}

	public PopPersonList(Context context, OnClickListener onClickListener)
	{
		this(context);
		btnSave = (Button) view.findViewById(R.id.right_button);
		btnSave.setOnClickListener(onClickListener);
	}

	public void init()
	{
		((TextView) view.findViewById(R.id.title_bar)).setText("ÎÒµÄºÃÓÑ");
		((Button) view.findViewById(R.id.left_button))
				.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						dimiss();
					}
				});
	}

	private void initData()
	{
		HttpClient.get(Constants.FINDATUSER, null,
				new AsyncHttpResponseHandler()
				{
					@Override
					public void onSuccess(String result)
					{
						// TODO Auto-generated method stub
						super.onSuccess(result);
						JSONArray jsonArray;
						try
						{
							jsonArray = new JSONArray(result);
							for (int i = 0; i < jsonArray.length(); i++)
							{
								JSONObject jsonObject = jsonArray
										.getJSONObject(i);
								UserInfo userInfo = new UserInfo();
								userInfo.setOperator_id(jsonObject
										.getString("OPERATOR_ID"));
								userInfo.setName(jsonObject.getString("NAME"));
								userInfo.setLong_phone(jsonObject
										.getString("LONGPHONE"));
								selectUserList.add(userInfo);
							}
							adapter.setDataList(selectUserList);
							adapter.notifyDataSetChanged();
						}
						catch (JSONException e)
						{
						}
					}
				});
	}

	public List<UserInfo> getUserInfo()
	{
		return adapter.getSelectedUser();
	}

	public void show(View v)
	{
		popWinodw.showAtLocation(v.getRootView(), Gravity.FILL, 0, 0);
	}

	public void dimiss()
	{
		if (popWinodw != null && popWinodw.isShowing())
		{
			popWinodw.dismiss();
		}
	}

}
