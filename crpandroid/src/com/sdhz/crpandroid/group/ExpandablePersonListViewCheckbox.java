package com.sdhz.crpandroid.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.group.adapter.ExpandableListViewAdapter;
import com.sdhz.domain.group.GroupUser;
import com.sdhz.domain.group.UserInfo;

public class ExpandablePersonListViewCheckbox extends Activity implements
		OnClickListener
{

	private static final String			TAG					= "ExpandableListViewCheckboxActivity";
	private Button						mBtn;
	private ExpandableListView			mListView;
	private ExpandableListViewAdapter	mListViewAdapter	= null;
	private List<GroupUser>				mGroupData			= new ArrayList<GroupUser>();
	// control select button status(SelectedAll or UnselectAll)
	private Button						confirmButton;
	private Button						cancelButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.crp_expand_person_list_view);
		mBtn = (Button) findViewById(R.id.select_btn);
		confirmButton = (Button) findViewById(R.id.right_button);
		cancelButton = (Button) findViewById(R.id.left_button);
		confirmButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		mBtn.setOnClickListener(this);
		mListView = (ExpandableListView) findViewById(R.id.expandableListView);
		mListView.setGroupIndicator(null);
		InitData();

		mListViewAdapter = new ExpandableListViewAdapter(getLayoutInflater(),
				this, mGroupData);
		mListView.setAdapter(mListViewAdapter);
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

	private void InitData()
	{
		for (int i = 0; i < 2; i++)
		{
			UserInfo info;
			List list = new ArrayList();
			for (int j = 0; j < 4; j++)
			{
				info = new UserInfo();
				info.setName("ÍõXX" + j);
				info.setLong_phone("ss" + j);
				list.add(info);
			}
			GroupUser group = new GroupUser("group" + i, list);
			mGroupData.add(group);
		}
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.left_button:
			this.finish();
			break;
		case R.id.right_button:
			if (mListViewAdapter.getSelectedUserInfo() != null)
			{
				Intent inten = new Intent();
				Bundle bundle = new Bundle();
				inten.putExtra("selectUserInfoList",
						(Serializable) mListViewAdapter.getSelectedUserInfo());
				setResult(RESULT_OK, inten);
			}
			this.finish();
			break;
		default:
			break;
		}
	}
}
