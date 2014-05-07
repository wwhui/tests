package com.sdhz.crpandroid.person;

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
 * 广场页面
 *
 */
public class PersonMainActivity extends FragmentActivity
{
	private FragmentManager	fragmentManager;
	private Button			atMyButton;			//与我相关
	private Button			myCreateBtn;		//我发起的
	private Button		left_button,attention;		// 关注的对象

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crp_person_main_activity);
		initView();//	初始化view
		initAttention(); // 关注博客
		fragmentManager = getSupportFragmentManager();//初始化fragment管理器
		show(0);//	初始化显示fragment
	}
//	初始化view
	private void initView()
	{
		((TextView) findViewById(R.id.title_bar)).setText("广场");
		((TextView) findViewById(R.id.title_bar)).setTextColor(Color.WHITE);
		atMyButton = (Button) findViewById(R.id.atmy);
		atMyButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				show(1);
				atMyButton
						.setBackgroundResource(R.drawable.tabbutton_left_color);
				myCreateBtn
						.setBackgroundResource(R.drawable.tabbutton_right_color);
				atMyButton.setTextColor(Color.WHITE);
				myCreateBtn.setTextColor(Color.BLACK);
			}
		});
		myCreateBtn = (Button) findViewById(R.id.mycreate);
		myCreateBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				show(0);
				atMyButton
						.setBackgroundResource(R.drawable.tabbutton_right_color);
				myCreateBtn
						.setBackgroundResource(R.drawable.tabbutton_left_color);
				atMyButton.setTextColor(Color.BLACK);
				myCreateBtn.setTextColor(Color.WHITE);
			}
		});
	}
//	初始化关注
	private void initAttention()
	{
		left_button=(Button)findViewById(R.id.left_button);
		left_button.setVisibility(View.GONE);
		attention = (Button) findViewById(R.id.right_button);
		attention.setText("关注");
		attention.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(PersonMainActivity.this,
						AttentionActivity.class);
				startActivity(intent);
			}
		});
	}
//	初始化显示fragment
	private void show(int i)
	{
		// TODO Auto-generated method stub
		Fragment fragment = null;
		switch (i)
		{
		case 0:
			fragment = new MyCreateFrament();
			break;
		case 1:
			fragment = new AtMySelfFrament();
			break;
		default:
			break;
		}
		//开始事物，提交
		FragmentTransaction beginTransaction = fragmentManager
				.beginTransaction();
		beginTransaction.replace(R.id.fragment1, fragment);
		beginTransaction.commit();
	}

}
