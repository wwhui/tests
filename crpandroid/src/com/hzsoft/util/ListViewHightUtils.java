package com.hzsoft.util;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListViewHightUtils {
	private static void setListViewHeightBasedOnChildren(ListView listView) {
	android.widget.ListAdapter listAdapter = listView.getAdapter();
	if (listAdapter == null) {
		return;
	}

	int totalHeight = 0;
	int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
	for (int i = 0; i <listAdapter.getCount(); i++) {
		View listItem = listAdapter.getView(i, null, listView);
//		if (listItem instanceof ViewGroup)
//            listItem.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		listItem.measure(0, 0);
		totalHeight += listItem.getMeasuredHeight();
	}

	ViewGroup.LayoutParams params = listView.getLayoutParams();
	params.height = totalHeight
			+ (listView.getDividerHeight() * (listAdapter.getCount() -1));
	listView.setLayoutParams(params);
}
}
