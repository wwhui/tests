package com.sdhz.crpandroid.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpClient;
import com.hzsoft.util.PinyingUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.group.SideBar.OnTouchingLetterChangedListener;
import com.sdhz.crpandroid.group.adapter.SelectUserAdapter;
import com.sdhz.domain.group.UserInfo;
import com.sdhz.view.ClearEditText;

public class SelectUserActivity extends Activity implements OnClickListener {
	private ListView personListView;
	private SelectUserAdapter selectUserAdapter;
	private LinkedList<UserInfo> mListItems ;
	private SideBar sideBar;
	private TextView dialog;
	private ClearEditText mClearEditText;
	private Button confirmButton;
	private Button cancelButton;
	private ProgressBar  progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_person_activity);
		personListView=(ListView) findViewById(R.id.personList);
		progressBar=(ProgressBar) findViewById(R.id.progressBar_loading);
		  progressBar.setVisibility(View.VISIBLE);
//		userInfoList=dao.getUserInfoList(Cache.getInstance().getAccount().getAccount());
		mListItems=new LinkedList<UserInfo>();
		selectUserAdapter = new SelectUserAdapter(mListItems, this);
		personListView.setAdapter(selectUserAdapter);
		confirmButton = (Button) findViewById(R.id.right_button);
		cancelButton = (Button) findViewById(R.id.left_button);
		confirmButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		initCleartext();
		initSideBar();
		initData();
	//	getUserInfo();
	}
	private void initCleartext() {
		// TODO Auto-generated method stub
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	protected void filterData(String string) {
		// TODO Auto-generated method stub
		List list = new ArrayList();
		if (string != null && !"".equals(string.trim())) {
			string = string.trim();
			for (UserInfo info : mListItems) {
				if (info.getName().indexOf(string) != -1
						|| PinyingUtil.stringArrayToString(
								PinyingUtil.getHeadByString(info.getName()))
								.startsWith(string.toUpperCase())) {
					list.add(info);
				}
			}
			selectUserAdapter.setDataList(list);
			selectUserAdapter.notifyDataSetChanged();
		} else {
			selectUserAdapter.setDataList(mListItems);
			selectUserAdapter.notifyDataSetChanged();
		}
	}

	private void initData()
	{
		HttpClient.get(Constants.FINDATUSER, null, new AsyncHttpResponseHandler()
		{
			@Override
			public void onSuccess(String result)
			{
				// TODO Auto-generated method stub
				super.onSuccess(result);
				JSONArray jsonArray;
				try
				{
					jsonArray=new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++)
					{
						JSONObject jsonObject=jsonArray.getJSONObject(i);
						UserInfo userInfo=new UserInfo();
						userInfo.setOperator_id(jsonObject.getString("OPERATOR_ID"));
						userInfo.setName(jsonObject.getString("NAME"));
						userInfo.setLong_phone(jsonObject.getString("LONGPHONE"));
						mListItems.add(userInfo);
					}
					selectUserAdapter.setDataList(mListItems);
					selectUserAdapter.notifyDataSetChanged();
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				progressBar.setVisibility(View.GONE);
			}
		});
	}
	
	private void initSideBar() {
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				// TODO Auto-generated method stub
				int position = selectUserAdapter.getAlphaI(s);
				if (position != -1) {
					personListView.setSelection(position);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_button:
			this.finish();
			break;
		case R.id.right_button:
			if (selectUserAdapter.getSelectedUser() != null) {
				Intent inten = new Intent();
				Bundle bundle = new Bundle();
				inten.putExtra("selectUserInfoList",
						(Serializable) selectUserAdapter.getSelectedUser());
				setResult(RESULT_OK, inten);
			}
			this.finish();
			break;
		default:
			break;
		}
	}

}
