package com.sdhz.crpandroid.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.sdhz.crpandroid.R;

public class PopMenu {
	private List itemList;
	private Context context;
	private PopupWindow popupWindow;
	private ListView listView;

	// private OnItemClickListener listener;

	public PopMenu(Context context, List  itemList,
			OnItemClickListener listener) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.itemList = itemList;
		View view = LayoutInflater.from(context)
				.inflate(R.layout.popmenu, null);
		listView = (ListView) view.findViewById(R.id.listView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				R.layout.pomenu_item, R.id.textView,this.itemList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(listener);
		// popupWindow = new PopupWindow(view, 100, LayoutParams.WRAP_CONTENT);
//		popupWindow = new PopupWindow(view, context.getResources()
//				.getDimensionPixelSize(R.dimen.popmenu_width),
//				LayoutParams.WRAP_CONTENT);
		popupWindow = new PopupWindow(view, 350,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);

	}

	// 下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {
		popupWindow.setFocusable(true);
		popupWindow.showAtLocation(parent, Gravity.TOP, 5, 105);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}

	// 隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();
	}

}
