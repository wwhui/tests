package com.sdhz.crpandroid.widget;

import com.sdhz.crpandroid.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * ����ʧ��dialog
 *
 */
public class PopupFailDialog
{
	private Context mContext;
	private TextView tv_faildialog;
	private PopupWindow popupWindow;
	
	public PopupFailDialog(Context mContext)
	{
		super();
		this.mContext = mContext;
		View view=LayoutInflater.from(mContext).inflate(R.layout.popupfaildialog, null);
		tv_faildialog=(TextView)view.findViewById(R.id.tv_faildialog);
		tv_faildialog.getBackground().setAlpha(150);//����͸����
		popupWindow=new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
	}
	//��ʾ
	public void show(View parent)
	{
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 120, 120);
		
	}
	//��ʾ
	public void showCenter(View parent)
	{
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 120, 338);
		
	}
	//���ؽ�������ʧ
	public void dismiss()
	{
		if(popupWindow!=null && popupWindow.isShowing())
		{
			popupWindow.dismiss();
			popupWindow.update();
		}
	}
}
