package com.sdhz.crpandroid;

import com.sdhz.domain.Account;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

/**
 * ��activity �����˳��õķ���
 */
public class BaseActivity extends Activity implements OnTouchListener,OnGestureListener
{
	 GestureDetector mGestureDetector;
     private static final int FLING_MIN_DISTANCE = 50;
     private static final int FLING_MIN_VELOCITY = 0;
     
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		checkNetworkIsAvailable();
		isNet();
		 mGestureDetector = new GestureDetector(this);
	}
	public Account getAccount(){
		CrpaApplication  crp=(CrpaApplication) getApplication();
		return 	crp.getAccount();
	}
	public String AccountString(){
		CrpaApplication  crp=(CrpaApplication) getApplication();
		return crp.getAccountString();
	}
	
	/**
	 * ��ʾ�Ի���
	 * 
	 * builder.setPositiveButton(text, listener)//ȷ����
	 * builder.setNegativeButton(text, listener)//�񶨵�
	 * builder.setNeutralButton(text, listener)//������
	 * 
	 * */
	public void showBuilder(String title, String message, String[] params)
	{
		Builder builder = new Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("ȷ��",
				new android.content.DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
		builder.setNegativeButton("ȡ��",
				new android.content.DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/**
	 * ��ʾ��ʾ��Ϣ
	 * 
	 * @param content
	 */
	public void showToast(String content)
	{
		Toast toast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 180);
		toast.show();
	}

	/**
	 * ��ʾ��ʾ��Ϣ
	 * 
	 * @param context
	 * @param resId
	 *            R.string.***
	 * @param duration
	 *            Toast.LENGTH_SHORT : 0, Toast.LENGTH_LONG : 1
	 */
	public static void showToast(Context context, int resId, int duration)
	{
		Toast toast = new Toast(context);
		toast.setText(resId);
		toast.setDuration(duration);
		toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 180);
		toast.show();
	}

	public boolean isNet()
	{
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable())
		{
//			new AlertDialog.Builder(this).setMessage("û�п���ʹ�õ�����")
//					.setPositiveButton("Ok", null).show();
			showToast("û�п���ʹ�õ�����,���飡");
			return false;
		}
		// new
		// AlertDialog.Builder(this).setMessage("������������ʹ��").setPositiveButton("Ok",
		// null).show();
		return true;
	}

	/** �ж������Ƿ����� */
	public void checkNetworkIsAvailable()
	{
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();

		// new
		// AlertDialog.Builder(this).setMessage(mobile.toString()).setPositiveButton("������ʹ��3G����",
		// null).show();//��ʾ3G��������״̬
		// new
		// AlertDialog.Builder(this).setMessage(wifi.toString()).setPositiveButton("������ʹ��WIFI����",
		// null).show();//��ʾwifi��������״̬
		// NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		// �ж�wifi�Ƿ�����
		// boolean wifi =
		// manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		// if (networkinfo == null || !networkinfo.isAvailable())
		// {
		// return false;
		// }
		// return true;
	}
	
	
//	@Override
//	protected void onPostResume() {
//		// TODO Auto-generated method stub
//		super.onPostResume();
//		getWindow().getDecorView().setOnTouchListener(this);
//		getWindow().getDecorView().setLongClickable(true);
//	}


	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		   // TODO Auto-generated method stub
        if (e1.getX()-e2.getX() > FLING_MIN_DISTANCE 
               && Math.abs(velocityX) > FLING_MIN_VELOCITY) { 
        	flidingLeft();
           //Toast.makeText(this, "��������", Toast.LENGTH_SHORT).show(); 
       } else if (e2.getX()-e1.getX() > FLING_MIN_DISTANCE 
               && Math.abs(velocityX) > FLING_MIN_VELOCITY) { 
    	   flidingRight();
           //Toast.makeText(this, "��������", Toast.LENGTH_SHORT).show(); 
       } 
       return false; 
	}
	public void flidingLeft(){
	}
	public void flidingRight(){
		this.finish();
	}
	
	
	@Override
	public void onLongPress(MotionEvent e) {
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		 if (mGestureDetector.onTouchEvent(event))
		 return true;
		 else
		 return false;
		 }
}
