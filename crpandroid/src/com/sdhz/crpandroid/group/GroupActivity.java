package com.sdhz.crpandroid.group;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sdhz.crpandroid.R;

/**
 * Ⱥ��
 * 
 */
public class GroupActivity extends FragmentActivity implements OnClickListener

{
	private FragmentManager fragmentManager;
	private Button groupbtn,friendbtn,addgroup;
	Intent intent;
	Fragment fragment = new Fragment();
	private TextView title_bar;
	@Override
	protected void onCreate(Bundle arg0)
	{
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.crp_fragment);
		initView();//��ʼ��view
		
		//��ʼ��fragment������
		fragmentManager=getSupportFragmentManager();
	}
	//��ʼ��view
	private void initView()
	{
		// TODO Auto-generated method stub
		title_bar=(TextView)findViewById(R.id.title_bar);
		title_bar.setText("Ⱥ��");
		//��ʼ��view
//		groupbtn=(Button)findViewById(R.id.groupbtn);
//		groupbtn.setVisibility(View.GONE);
//		friendbtn=(Button)findViewById(R.id.friendbtn);
//		friendbtn.setVisibility(View.GONE);
//		addgroup=(Button)findViewById(R.id.addgroup);
		//��ӵ������
//		groupbtn.setOnClickListener(this);
//		friendbtn.setOnClickListener(this);
//		addgroup.setOnClickListener(this);
//		addgroup.setVisibility(View.GONE);
	}
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		Button btn=(Button)v;
//		switch (btn.getId())
//		{
//		case R.id.groupbtn:
//			groupButton();
//			fragment=new GroupFrament();
//			break;
//		case R.id.friendbtn:
//			friendButton();
//			fragment=new FriendFrament();
//			break;
//		case R.id.addgroup:
//			intent=new Intent();
//			intent.setClass(this, AddActivity.class);
//			startActivity(intent);
//			break;
//		default:
//			break;
//		}
		fragmentConfig();
	}
	/**
	 * Ⱥ�鰴ť�����ʱ�ı�״̬
	 */
	private void groupButton()
	{
		friendbtn.setBackgroundResource(R.drawable.tabbutton_right_color);
		groupbtn.setBackgroundResource(R.drawable.tabbutton_left_color);
		friendbtn.setTextColor(Color.BLACK);
		groupbtn.setTextColor(Color.WHITE);
	}
	/**
	 * ���Ѱ�ť
	 */
	private void friendButton()
	{
		groupbtn.setBackgroundResource(R.drawable.tabbutton_right_color);
		friendbtn.setBackgroundResource(R.drawable.tabbutton_left_color);
		groupbtn.setTextColor(Color.BLACK);
		friendbtn.setTextColor(Color.WHITE);
	}
	/**
	 * fragment����ύ
	 */
	private void fragmentConfig()
	{
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();  
        beginTransaction.replace(R.id.fragment1, fragment);  
        beginTransaction.commit();
	}
}
