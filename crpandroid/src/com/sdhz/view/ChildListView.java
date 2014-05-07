package com.sdhz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ChildListView extends ListView {

	public ChildListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChildListView(Context context) {
		super(context);
	}

	public ChildListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	/***
	 * 
	 * 重写此方法 是为了 修改高度
	 * 
	 * **/
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
