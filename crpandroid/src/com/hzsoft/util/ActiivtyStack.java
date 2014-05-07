package com.hzsoft.util;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActiivtyStack {
	public static List<Activity> mActivityStack;
	private static ActiivtyStack instance;

	private ActiivtyStack() {
	}
	public static ActiivtyStack getScreenManager() {
		if (instance == null) {
			instance = new ActiivtyStack();
		}
		return instance;
	}

	// ����ǰActivity����ջ��
	public void pushActivity(Activity activity) {
		if (mActivityStack == null) {
			mActivityStack = new ArrayList<Activity>();
		}
		mActivityStack.add(activity);
	}
}
