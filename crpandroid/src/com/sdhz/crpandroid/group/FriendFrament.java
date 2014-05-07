package com.sdhz.crpandroid.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hp.hpl.sparta.Sparta.Cache;
import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.PinyingUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.CrpaApplication;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.group.SideBar.OnTouchingLetterChangedListener;
import com.sdhz.crpandroid.group.adapter.FriendListAdpater;
import com.sdhz.crpandroid.widget.PopupDialog;
import com.sdhz.dao.UserInfoDao;
import com.sdhz.dao.impl.UserDaoImpl;
import com.sdhz.domain.group.UserInfo;
import com.sdhz.view.ClearEditText;

/**
 * 显示好友
 * 
 */
public class FriendFrament extends Fragment
{
	private Intent				intent;
	private ListView			personListView;
	private FriendListAdpater	friendAdpater;
	private List<UserInfo>		userInfoList	= new ArrayList<UserInfo>();
	private SideBar				sideBar;
	private TextView			dialog;
	private Context				mContext;
	private ClearEditText		mClearEditText;
	private LayoutInflater		inflater;
	private PopupDialog				popupDialog;			// 加载popupwindow
	private Handler					mHandler=new Handler();				// 用于检测activity的初始化完毕没有，popupDialog如果直接初始化，会报空指针
	private int						detachtime	= 5;		// 每5秒检测一次
	private View view;		//在这里初始化为了下面加载进度popupdialog		

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater = inflater;
		view = inflater.inflate(R.layout.crp_friendfragment, container, false);
		mContext = inflater.getContext();
		personListView = (ListView) view.findViewById(R.id.personList);
		String u_id=  ( (CrpaApplication)mContext.getApplicationContext()).getAccountString();
		RequestParams params=new RequestParams();
		params.put("u_id", u_id);
		InitData(params);
		friendAdpater = new FriendListAdpater(userInfoList, mContext);
		personListView.setAdapter(friendAdpater);
		initSideBar();
		initCleartext();
		return view;
	}
	
	// 用来循环检测activity是否初始化完毕，一旦初始化完毕就再初始化popupwindow
	private void showPopupDialog()
	{
		// TODO Auto-generated method stub
		popupDialog = new PopupDialog(mContext);

		Runnable runnable = new Runnable()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
//				view  =  view.findViewById(R.id.layout_main);// activity中根元素
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

	private void initCleartext()
	{
		// TODO Auto-generated method stub
		mClearEditText = (ClearEditText) view.findViewById(R.id.filter_edit);
		mClearEditText.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{
				// TODO Auto-generated method stub
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				// TODO Auto-generated method stub

			}
		});
	}

	protected void filterData(String string)
	{
		List list = new ArrayList();
		if (string != null && !"".equals(string.trim()))
		{
			string = string.trim();
			for (UserInfo info : userInfoList)
			{
				if (info.getName().indexOf(string) != -1
						|| PinyingUtil.stringArrayToString(
								PinyingUtil.getHeadByString(info.getName()))
								.startsWith(string.toUpperCase()))
				{
					list.add(info);
				}
			}
			Log.e("sss", list.size() + ".................");
			friendAdpater.setDataList(list);
			friendAdpater.notifyDataSetChanged();
		}
		else
		{
			friendAdpater.setDataList(userInfoList);
			friendAdpater.notifyDataSetChanged();
		}
	}

	private void initSideBar()
	{
		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		dialog = (TextView) view.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener()
		{

			@Override
			public void onTouchingLetterChanged(String s)
			{
				// TODO Auto-generated method stub
				int position = friendAdpater.getAlphaI(s);
				if (position != -1)
				{
					personListView.setSelection(position);
				}
			}
		});

	}

	private HashMap<Integer, String> setHashMap(int size, String mstring)
	{
		HashMap<Integer, String> temp = new HashMap<Integer, String>();
		for (int i = 0; i < size; ++i)
		{
			temp.put(i, mstring + i);
		}
		return temp;
	}

	private void InitData(RequestParams params)
	{
		showPopupDialog();
		HttpClient.get(Constants.FINDALLFRIEND, params,
				new AsyncHttpResponseHandler()
				{

					@Override
					public void onSuccess(String result)
					{
						// TODO Auto-generated method stub
						Log.e("ee", result);
						JSONArray json_question;
						try
						{
							json_question = new JSONArray(result);
							String userid= ( (CrpaApplication)mContext.getApplicationContext()).getAccountString();
							for (int i = 0; i < json_question.length(); i++)
							{
								JSONObject jo = json_question.getJSONObject(i);
								UserInfo user = new UserInfo();
								user.setOperator_id(jo.getString("OPERATOR_ID"));
								user.setName(jo.getString("NAME"));
								user.setLong_phone(jo.getString("LONGPHONE"));
								userInfoList.add(user);
							}
							friendAdpater.setDataList(userInfoList);
							friendAdpater.notifyDataSetChanged();
							popupDialog.dismiss();
							UserInfoDao dao = UserDaoImpl.getInstance(mContext);
							dao.addUserInfo(userInfoList,userid);
						}
						catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(result);
					}
				});
	}

}
