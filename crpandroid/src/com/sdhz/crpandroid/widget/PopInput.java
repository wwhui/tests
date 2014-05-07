package com.sdhz.crpandroid.widget;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.hzsoft.util.EditTextUtil;
import com.sdhz.crpandroid.R;
import com.sdhz.domain.group.UserInfo;

public class PopInput  implements OnKeyListener,OnTouchListener{
	private Context context;
	private PopupWindow popupWindow;
	private Button sendButton;
	private EditText content;
	private Object object;
	private 	InputMethodManager m;
	private OnClickListener onClickListener;
	private ImageButton imageButton;
	private View  parentView;
	private PopPersonList pop;
	private List<UserInfo> userInfoList;
	public PopInput(Context context, OnClickListener onClickListener) {
		super();
		this.context = context;
		View	view = LayoutInflater.from(context).inflate(R.layout.popupwinow,
				null);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		sendButton = (Button) view.findViewById(R.id.save);
		content = (EditText) view.findViewById(R.id.content);
		sendButton.setOnClickListener(onClickListener);
		popupWindow.setOutsideTouchable(true);
		view.setOnKeyListener(this);
		view.setOnTouchListener(this);
		content.setOnKeyListener(this);
		imageButton=(ImageButton) view.findViewById(R.id.btn_at);
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 pop=new PopPersonList(PopInput.this.context,new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						userInfoList=pop.getUserInfo();
						String co="";
						if(userInfoList!=null&&userInfoList.size()>0){
							for (UserInfo info : userInfoList) {
								co+="@"+info.getName()+" ";
							}
						}
						//content.setText(co);
						new EditTextUtil(content).addString(co);
						pop.dimiss();
					}
				});
				pop.show(parentView);
			}
		});
	}
	public PopInput(Context mContext, Object  object,
			OnClickListener onClickListener2) {
		// TODO Auto-generated constructor stub
		this(mContext, onClickListener2);
		sendButton.setTag(object);
		this.object=object;
	}
	public Object getObject(){
		return object;
	}

	public List<UserInfo> getUserInfoList() {
		return userInfoList;
	}
	public void setUserInfoList(List<UserInfo> userInfoList) {
		this.userInfoList = userInfoList;
	}
	public EditText getContent() {
		return content;
	}

	public void show(View v) {
		parentView=v;
		content.setFocusable(true);
		content.setFocusableInTouchMode(true);
		 new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
			 m= (InputMethodManager) context
						.getSystemService(context.INPUT_METHOD_SERVICE);
				m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 100);
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public OnClickListener getOnClickListener() {
		return onClickListener;
	}

	public void setContent(EditText content) {
		this.content = content;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		sendButton.setOnClickListener(onClickListener);
		this.onClickListener = onClickListener;
	}

	public void dimiss() {
		popupWindow.dismiss();
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
			dimiss();
			return true;
		}
		return false;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(popupWindow!=null&&popupWindow.isShowing()){
			dimiss();
			return true;
		}
		return false;
	}

}
