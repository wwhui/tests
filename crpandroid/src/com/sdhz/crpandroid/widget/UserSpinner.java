package com.sdhz.crpandroid.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.sdhz.crpandroid.R;
import com.sdhz.dao.AccountDao;
import com.sdhz.dao.impl.AccountDaoImpl;
import com.sdhz.domain.Account;
public class UserSpinner {
	private PopupWindow popWinodw;
	private ListView  listView;
	private Context mContext;
	private List <Account> dataList=new ArrayList<Account>();
	private View  view;
	private UserSpinnerAdapter dataAdapter;
	public UserSpinner(Context context,
			OnClickListener onClickListener) {
		super();
		this.mContext = context;
		view=LayoutInflater.from(mContext).inflate(R.layout.pop_user_spinner, null);
		AccountDao accountDao=AccountDaoImpl.getInstance(mContext);
		dataList=accountDao.getAccountList();
		dataAdapter=new  UserSpinnerAdapter(dataList, mContext,onClickListener);
		listView=(ListView) view.findViewById(R.id.listView);
		listView.setAdapter(dataAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "arg2"+arg2, Toast.LENGTH_SHORT).show();
			}
		});
	}
	public void show(View v) {
		if(popWinodw==null){
			popWinodw = new PopupWindow(view, v.getWidth(),LayoutParams.WRAP_CONTENT);
			popWinodw.setBackgroundDrawable(new BitmapDrawable());
			popWinodw.setFocusable(true);
		}
		popWinodw.showAsDropDown(v,0,		// 保证尺寸是根据屏幕像素密度来的
				v.getLayoutParams().width);
		// 使其聚集
		popWinodw.setFocusable(true);
		// 设置允许在外点击消失
		popWinodw.setOutsideTouchable(true);
		// 刷新状态
		popWinodw.update();
	}
	public void dimiss(){
		if(popWinodw!=null&&popWinodw.isShowing()){
			popWinodw.dismiss();
		}
	}
}
class  UserSpinnerAdapter extends BaseAdapter{
	private List<Account> dataList;
	private Context mContext;
	private OnClickListener oncClickListener;
 	public UserSpinnerAdapter(List<Account> dataList, Context mContext,OnClickListener oncClickListener) {
		super();
		this.dataList = dataList;
		this.mContext = mContext;
		this.oncClickListener=oncClickListener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return dataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View converView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		holder = new ViewHolder();
		converView = LayoutInflater.from(mContext).inflate(
				R.layout.pop_user_spinner_item, null);
		holder.accountTextView = (TextView) converView
				.findViewById(R.id.accountTextView);
		holder.deleteButton = (ImageView) converView.findViewById(R.id.delete);
		Account account = dataList.get(position);
		holder.acco = dataList.get(position);
		holder.accountTextView.setText(account.getAccount());
		holder.deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dataList.remove(position);
				UserSpinnerAdapter.this.notifyDataSetChanged();
			}
		});
		converView.setTag(holder.acco);
		converView.setOnClickListener(oncClickListener);
		return converView;
	}
	class  ViewHolder{
			public TextView accountTextView;
			public ImageView  deleteButton;
			public Account acco;
	}
	
	
}